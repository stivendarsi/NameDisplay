package me.stivendarsi.nameDisplay.handlers;

import java.util.List;

import static me.stivendarsi.nameDisplay.NameDisplay.plugin;

public class ConfigurationHandler {
    private long updateIntervalInTicks;
    private String text;

    public void load() {
        double updateIntervalInSeconds = plugin().getConfig().getDouble("update-interval", -1);
        this.updateIntervalInTicks = (long) (updateIntervalInSeconds * 1000);

        List<String> textInLines = plugin().getConfig().getStringList("text");
        this.text = String.join("<newline>", textInLines);
    }

    public long updateIntervalInTicks() {
        return updateIntervalInTicks;
    }

    public String text() {
        return text;
    }
}
