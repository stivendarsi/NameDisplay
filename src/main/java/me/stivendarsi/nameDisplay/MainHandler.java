package me.stivendarsi.nameDisplay;

import me.stivendarsi.nameDisplay.handlers.ConfigurationHandler;
import me.stivendarsi.nameDisplay.handlers.DisplayHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MainHandler {
    private final ConfigurationHandler configurationHandler;
    private final DisplayHandler displayHandler;


    public MainHandler() {
        this.configurationHandler = new ConfigurationHandler();
        this.displayHandler = new DisplayHandler();
    }

    public void load() {
        this.displayHandler.load();
        this.configurationHandler.load();
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
