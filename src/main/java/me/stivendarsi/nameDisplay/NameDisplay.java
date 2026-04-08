package me.stivendarsi.nameDisplay;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import me.stivendarsi.nameDisplay.commands.CommandHandler;
import me.stivendarsi.nameDisplay.packetlisteners.RidingHandler;
import me.stivendarsi.nameDisplay.packetlisteners.RemoveDisplayHandler;
import me.stivendarsi.nameDisplay.packetlisteners.SpawnDisplayHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class NameDisplay extends JavaPlugin {

    private static NameDisplay plugin;
    public static NameDisplay plugin(){
        return plugin;
    }

    private static MainHandler mainHandler;
    public static MainHandler mainHandler(){
        return mainHandler;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        mainHandler = new MainHandler();

        saveDefaultConfig();
        reloadConfig();

        mainHandler().load();

        PacketEvents.getAPI().load();
        EventManager events = PacketEvents.getAPI().getEventManager();
        events.registerListener(new SpawnDisplayHandler(), PacketListenerPriority.NORMAL);
        events.registerListener(new RemoveDisplayHandler(), PacketListenerPriority.NORMAL);
        events.registerListener(new RidingHandler(), PacketListenerPriority.NORMAL);

        CommandHandler.registerCommands(this.getLifecycleManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
