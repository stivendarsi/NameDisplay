package me.stivendarsi.nameDisplay.commands;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.stivendarsi.foliaSurvivalTools.proxychannelsystem.messagetypes.redis.RedisChannel;
import me.stivendarsi.foliaSurvivalTools.userdata.interfaced.RedisUtil;
import org.bukkit.entity.Player;

public class UtilityCommands {
    public static int reloadCommand(CommandContext<CommandSourceStack> context) {

        RedisUtil.redis().publish(RedisChannel.RELOAD_PLUGIN.identifier(), "");

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
