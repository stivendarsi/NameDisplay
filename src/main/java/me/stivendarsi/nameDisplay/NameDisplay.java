package me.stivendarsi.nameDisplay;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import me.stivendarsi.nameDisplay.events.UserLoginPacketHandler;
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
        events.registerListener(new UserLoginPacketHandler(), PacketListenerPriority.NORMAL);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
