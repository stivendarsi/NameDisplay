package me.stivendarsi.nameDisplay.shakeit;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class ShakeCommands {

    public static int create(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getExecutor() instanceof Player player)) return 0;

        int rows = context.getArgument("rows", Integer.class);
        int columns = context.getArgument("columns", Integer.class);

        mainHandler().shakeHandler().shakeDisplaySet().spawn(player.getLocation(), rows, columns);

        return 1;
    }

    public static int shake(CommandContext<CommandSourceStack> context) {
        if (!(context.getSource().getExecutor() instanceof Player player)) return 0;
        ShakedDisplay[][] displays = mainHandler().shakeHandler().shakeDisplaySet().shakedDisplays();

        double duration = context.getArgument("seconds", Double.class);
        double angle = context.getArgument("angle", Double.class);

        for (ShakedDisplay[] display : displays) {
            for (ShakedDisplay shakedDisplay : display) {
                if (shakedDisplay == null) return 0;
                shakedDisplay.setOriginalDurationSecond(duration);
                shakedDisplay.setAngle(angle);

                shakedDisplay.startShake();
            }
        }

        return 1;
    }
}
