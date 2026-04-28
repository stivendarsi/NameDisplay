package me.stivendarsi.nameDisplay.commands;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static io.papermc.paper.command.brigadier.argument.ArgumentTypes.entity;
import static io.papermc.paper.command.brigadier.argument.ArgumentTypes.player;

public class CommandHandler {
    public static void registerCommands(LifecycleEventManager<Plugin> manager) {
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
//            commands.register(Commands.literal("shakedisplay")
//                    .then(Commands.literal("create")
//                            .then(Commands.argument("rows", integer(0))
//                                    .then(Commands.argument("columns", integer(0)).executes(ShakeCommands::create))))
//                    .then(Commands.literal("shake")
//                            .then(Commands.argument("seconds", doubleArg(0))
//                                    .then(Commands.argument("angle", doubleArg()).executes(ShakeCommands::shake))
//                            )
//                    )
//                    .build(), List.of("sd"));
            commands.register(Commands.literal("namedisplay")
                    .then(Commands.literal("reload").executes(UtilityCommands::reloadCommand))
                    .then(Commands.literal("show-all-displays").then(Commands.argument("player", player()).executes(UtilityCommands::showAllDisplaysCommand)))
                    .then(Commands.literal("debug-current-displays").executes(UtilityCommands::debug))
                    .then(Commands.literal("camera-as").then(Commands.argument("target", entity()).executes(UtilityCommands::cameraAs)))
                    .build(), List.of("nd"));
        });
    }
}