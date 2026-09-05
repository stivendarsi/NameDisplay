package me.stivendarsi.nameDisplay;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import me.stivendarsi.nameDisplay.commands.CommandHandler;
import me.stivendarsi.nameDisplay.packetlisteners.RegisterDisplayHandlerHandler;

import me.stivendarsi.nameDisplay.packetlisteners.BukitEventHandlers;
import org.bukkit.plugin.java.JavaPlugin;

public final class NameDisplay extends JavaPlugin {

    private static NameDisplay nameDisplay;
    public static NameDisplay nameDisplay(){
        return nameDisplay;
    }

    private static MainHandler mainHandler;
    public static MainHandler mainHandler(){
        return mainHandler;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        nameDisplay = this;
        mainHandler = new MainHandler();

        saveDefaultConfig();
        reloadConfig();

        mainHandler().load();

        PacketEvents.getAPI().load();
        EventManager events = PacketEvents.getAPI().getEventManager();
        events.registerListener(new RegisterDisplayHandlerHandler(), PacketListenerPriority.NORMAL);
//        events.registerListener(new HideDisplayHandler(), PacketListenerPriority.NORMAL);
//       // events.registerListener(new ShowDisplayHandler(), PacketListenerPriority.NORMAL);
//       // events.registerListener(new SneakPacketHandler(), PacketListenerPriority.NORMAL);
        getServer().getPluginManager().registerEvents(new BukitEventHandlers(), this);

        // Shake display
       // getServer().getPluginManager().registerEvents(new HitBoxInteractionEventHandler(), this);
      //  getServer().getPluginManager().registerEvents(new ShakeDisplayEvent(), this);

        CommandHandler.registerCommands(this.getLifecycleManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
