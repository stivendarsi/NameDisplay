package me.stivendarsi.nameDisplay;

import me.stivendarsi.nameDisplay.handlers.ConfigurationHandler;
import me.stivendarsi.nameDisplay.handlers.DisplayHandler;
import me.stivendarsi.nameDisplay.shakeit.ShakeHandler;
import org.bukkit.Bukkit;

public class MainHandler {
    private final ConfigurationHandler configurationHandler;
    private final DisplayHandler displayHandler;
    private final ShakeHandler shakeHandler;


    public MainHandler() {
        this.configurationHandler = new ConfigurationHandler();
        this.displayHandler = new DisplayHandler();
        this.shakeHandler = new ShakeHandler();
    }

    public void load() {
        this.configurationHandler.load();
        this.displayHandler.load();
    }

    public ShakeHandler shakeHandler() {
        return shakeHandler;
    }

    /**Getters*/

    public ConfigurationHandler configurationHandler() {
        return configurationHandler;
    }

    public DisplayHandler displayHandler() {
        return displayHandler;
    }


    public boolean placeholderApiEnabled(){
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }
}
