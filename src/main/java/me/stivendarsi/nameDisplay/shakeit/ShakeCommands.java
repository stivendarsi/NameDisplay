package me.stivendarsi.nameDisplay.shakeit;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class ShakeCommands {

    public static int create(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getExecutor() instanceof Player player)) return 0;

        mainHandler().shakeHandler().spawn(player.getLocation());


        return 1;
    }

    public static int shake(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getExecutor() instanceof Player player)) return 0;
        if (mainHandler().shakeHandler().shakedDisplay() == null) return 0;

        double duration = context.getArgument("seconds", Double.class);
        double angle = context.getArgument("angle", Double.class);

        mainHandler().shakeHandler().shakedDisplay().startShake((int) (duration * 20), angle);

        return 1;
    }
}
