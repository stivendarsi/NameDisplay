package me.stivendarsi.nameDisplay.commands;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class UtilityCommands {
    public static int reloadCommand(CommandContext<CommandSourceStack> context) {
        mainHandler().load();
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
