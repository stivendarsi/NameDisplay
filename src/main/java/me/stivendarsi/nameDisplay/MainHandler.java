package me.stivendarsi.nameDisplay;

import me.stivendarsi.nameDisplay.handlers.ConfigurationHandler;
import me.stivendarsi.nameDisplay.handlers.DisplayHandler;
import org.bukkit.Bukkit;

public class MainHandler {
    private final ConfigurationHandler configurationHandler;
    private final DisplayHandler displayHandler;


    public MainHandler() {
        this.configurationHandler = new ConfigurationHandler();
        this.displayHandler = new DisplayHandler();
    }

    public void load() {
        this.configurationHandler.load();
        this.displayHandler.load();
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
