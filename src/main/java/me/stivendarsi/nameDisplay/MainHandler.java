package me.stivendarsi.nameDisplay;

import me.stivendarsi.nameDisplay.handlers.ConfigurationHandler;
import org.bukkit.Bukkit;

public class MainHandler {
    private final ConfigurationHandler configurationHandler;


    public MainHandler() {
        this.configurationHandler = new ConfigurationHandler();
    }

    public void load() {
        this.configurationHandler.load();
    }

    /**Getters*/

    public ConfigurationHandler configurationHandler() {
        return configurationHandler;
    }

    public boolean placeholderApiEnabled(){
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }
}
