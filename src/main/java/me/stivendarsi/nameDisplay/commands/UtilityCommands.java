package me.stivendarsi.nameDisplay.commands;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCamera;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.stivendarsi.nameDisplay.utility.TextDisplayEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.nameDisplay;

public class UtilityCommands {
    public static int reloadCommand(CommandContext<CommandSourceStack> context) {

        mainHandler().displayHandler().unRegisterDisplays();
        nameDisplay().reloadConfig();
        mainHandler().load();
        context.getSource().getSender().sendRichMessage("<#aeff00>Name Display Reloaded!</#aeff00>");

        // Load displays for online players.
        for (Player owner : Bukkit.getOnlinePlayers()) {
            TextDisplayEntity textDisplayEntity = new TextDisplayEntity(owner.getUniqueId());
            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), textDisplayEntity);

            textDisplayEntity.startTextUpdating();

            // add viewers
            for (Player player : owner.getTrackedBy()) {
                textDisplayEntity.showFor(player, mainHandler().configurationHandler().showForOwners());
            }
            textDisplayEntity.showFor(owner, mainHandler().configurationHandler().showForOwners());
        }

        return 1;
    }

    public static int showAllDisplaysCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final PlayerSelectorArgumentResolver targetResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
        final Player target = targetResolver.resolve(context.getSource()).getFirst();

        mainHandler().displayHandler().showAllExistingDisplays(target);

        return 1;
    }

    public static int cameraAs(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final EntitySelectorArgumentResolver entitySelectorArgumentResolver = context.getArgument("target", EntitySelectorArgumentResolver.class);
        final Entity entity = entitySelectorArgumentResolver.resolve(context.getSource()).getFirst();

        if (!(context.getSource().getExecutor() instanceof Player sender)) return 0;
        User user = PacketEvents.getAPI().getPlayerManager().getUser(sender);
        WrapperPlayServerCamera cameraPacket = new WrapperPlayServerCamera(entity.getEntityId());
        user.sendPacket(cameraPacket);
        return 1;
    }


    public static int debug(CommandContext<CommandSourceStack> context) {
        Map<UUID, UUID> map = mainHandler().displayHandler().getUUID();
        map.forEach((uuid1, uuid2) -> {

            TagResolver resolver = TagResolver.builder()
                    .tag("uuid1", Tag.preProcessParsed(String.valueOf(uuid1)))
                    .tag("uuid2", Tag.preProcessParsed(String.valueOf(uuid2)))
                    .tag("equal", Tag.preProcessParsed(String.valueOf(uuid1 == uuid2)))
                    .build();
            Component msg = MiniMessage.miniMessage().deserialize("<uuid1>, <uuid2> | <equal>", resolver);
            context.getSource().getSender().sendMessage(msg);
        });

        return 1;
    }




}
