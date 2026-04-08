package me.stivendarsi.nameDisplay.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.plugin;

public class UtilityCommands {
    public static int reloadCommand(CommandContext<CommandSourceStack> context) {

        mainHandler().displayHandler().unRegisterDisplays();
        plugin().reloadConfig();
        mainHandler().load();
        context.getSource().getSender().sendRichMessage("<#aeff00>נטען מחדש!");
        return 1;
    }

    public static int showAllDisplaysCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final PlayerSelectorArgumentResolver targetResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
        final Player target = targetResolver.resolve(context.getSource()).getFirst();

        mainHandler().displayHandler().showAllExistingDisplays(target);

        return 1;
    }


    public static int debug(CommandContext<CommandSourceStack> context) {
        Map<UUID, UUID> map = mainHandler().displayHandler().getUUID();
        map.forEach((uuid, uuid2) -> {
            TagResolver resolver = TagResolver.builder()
                    .tag("uuid1", Tag.preProcessParsed(String.valueOf(uuid)))
                    .tag("uuid2", Tag.preProcessParsed(String.valueOf(uuid2)))
                    .tag("equal", Tag.preProcessParsed(String.valueOf(uuid == uuid2)))
                    .build();
            Component msg = MiniMessage.miniMessage().deserialize("<uuid1>, <uuid2> | <equal>", resolver);
            context.getSource().getSender().sendMessage(msg);
        });

        return 1;
    }

    public static int testComponentCommand(CommandContext<CommandSourceStack> context) {
        String string = context.getArgument("text", String.class);
        context.getSource().getSender().sendRichMessage(string);

        return 1;
    }

    public static int ping(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getExecutor() instanceof Player player)) return 0;
        player.sendRichMessage("זמן תגובה: %s".formatted(player.getPing()));
        return 1;
    }
}
