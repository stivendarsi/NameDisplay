package me.stivendarsi.nameDisplay;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import me.stivendarsi.nameDisplay.commands.CommandHandler;
import me.stivendarsi.nameDisplay.packetlisteners.ShowDisplayHandler;
import me.stivendarsi.nameDisplay.packetlisteners.HideDisplayHandler;
import me.stivendarsi.nameDisplay.packetlisteners.DisplayExistenceHandler;
import me.stivendarsi.nameDisplay.packetlisteners.SneakPacketHandler;
import me.stivendarsi.nameDisplay.shakeit.HitBoxInteractionEventHandler;
import me.stivendarsi.nameDisplay.shakeit.ShakeDisplayEvent;
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
        events.registerListener(new DisplayExistenceHandler(), PacketListenerPriority.NORMAL);
        events.registerListener(new HideDisplayHandler(), PacketListenerPriority.NORMAL);
        events.registerListener(new ShowDisplayHandler(), PacketListenerPriority.NORMAL);
       // events.registerListener(new SneakPacketHandler(), PacketListenerPriority.NORMAL);

        getServer().getPluginManager().registerEvents(new HitBoxInteractionEventHandler(), this);
        getServer().getPluginManager().registerEvents(new SneakPacketHandler(), this);
        getServer().getPluginManager().registerEvents(new ShakeDisplayEvent(), this);

        CommandHandler.registerCommands(this.getLifecycleManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
