package me.stivendarsi.nameDisplay.handlers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.stivendarsi.nameDisplay.utility.DisplayProperties;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.plugin;

public class ConfigurationHandler {
    private long updateIntervalInTicks;
    private String text;
    private boolean showForOwners;
    private boolean debug;

    private DisplayProperties properties;
    private DisplayProperties sneakProperties;

    private boolean sneakEnabled;


    public void load() {
        ConfigurationSection defaultSection = plugin().getConfig().getConfigurationSection("properties");
        if (defaultSection == null) throw new RuntimeException("No default properties section");
        this.properties = new DisplayProperties(defaultSection);


        ConfigurationSection sneakSection = plugin().getConfig().getConfigurationSection("sneak-properties");
        if (sneakSection == null) throw new RuntimeException("No sneak properties section");
        this.sneakProperties = new DisplayProperties(sneakSection);
        this.sneakEnabled = sneakSection.getBoolean("enabled");

        List<String> textInLines = plugin().getConfig().getStringList("text");
        this.text = String.join("<newline>", textInLines);

        ConfigurationSection miscSection = plugin().getConfig().getConfigurationSection("misc");
        if (miscSection == null) throw new RuntimeException("No misc properties section");

        this.showForOwners = miscSection.getBoolean("show-for-owners");
        this.debug = miscSection.getBoolean("debug");
        double updateIntervalInSeconds = miscSection.getDouble("update-interval", -1);
        this.updateIntervalInTicks = (long) (updateIntervalInSeconds * 20);

    }

    public long updateIntervalInTicks() {
        return updateIntervalInTicks;
    }


    public boolean showForOwners() {
        return showForOwners;
    }

    public boolean debug() {
        return debug;
    }

    public boolean sneakEnabled() {
        return sneakEnabled;
    }

    public DisplayProperties properties() {
        return properties;
    }

    public DisplayProperties sneakProperties() {
        return sneakProperties;
    }

    public String getWithSetPlaceholders(@Nullable Player player) {
        return mainHandler().placeholderApiEnabled() && player != null ? PlaceholderAPI.setPlaceholders(player, this.text) : this.text;
    }
}
