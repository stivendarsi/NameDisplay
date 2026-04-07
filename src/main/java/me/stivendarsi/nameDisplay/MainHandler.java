package me.stivendarsi.nameDisplay;

import me.stivendarsi.nameDisplay.handlers.ConfigurationHandler;

public class MainHandler {
    private ConfigurationHandler configurationHandler;


    public void load(){
        this.configurationHandler = new ConfigurationHandler();
    }

    public void reload(){
        this.configurationHandler.load();
    }

    public ConfigurationHandler configurationHandler() {
        return configurationHandler;
    }
}
