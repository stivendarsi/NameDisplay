package me.stivendarsi.nameDisplay.commands;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.attribute.Attribute;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.DataPalette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.Palette;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.PaletteType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChunkBatchAck;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkBatchBegin;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkBiomes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.stivendarsi.nameDisplay.shakeit.ShakeCommands;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static io.papermc.paper.command.brigadier.argument.ArgumentTypes.entity;
import static io.papermc.paper.command.brigadier.argument.ArgumentTypes.player;

public class CommandHandler {
    public static void registerCommands(LifecycleEventManager<Plugin> manager) {
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands commands = event.registrar();
            commands.register(Commands.literal("shakedisplay")
                    .then(Commands.literal("create")
                            .then(Commands.argument("rows", integer(0))
                                    .then(Commands.argument("columns", integer(0)).executes(ShakeCommands::create))))
                    .then(Commands.literal("shake")
                            .then(Commands.argument("seconds", doubleArg(0))
                                    .then(Commands.argument("angle", doubleArg()).executes(ShakeCommands::shake))
                            )
                    )
                    .build(), List.of("sd"));
            commands.register(Commands.literal("namedisplay")
                    .then(Commands.literal("reload").executes(UtilityCommands::reloadCommand))
                    .then(Commands.literal("show-all-displays").then(Commands.argument("player", player()).executes(UtilityCommands::showAllDisplaysCommand)))
                    .then(Commands.literal("debug-current-displays").executes(UtilityCommands::debug))
                    .then(Commands.literal("camera-as").then(Commands.argument("target", entity()).executes(UtilityCommands::cameraAs)))
                    .build(), List.of("nd"));
        });
    }
}