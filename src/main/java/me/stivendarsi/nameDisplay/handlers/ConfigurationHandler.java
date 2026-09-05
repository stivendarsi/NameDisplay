package me.stivendarsi.nameDisplay.handlers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.stivendarsi.nameDisplay.utility.TextDisplayData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.nameDisplay;

public class ConfigurationHandler {
    private long updateIntervalInTicks;
    private String text;
    private boolean showForOwners;
    private boolean debug;

    private TextDisplayData data;
    private TextDisplayData sneakData;

    private boolean sneakEnabled;


    public void load() {
        ConfigurationSection defaultSection = nameDisplay().getConfig().getConfigurationSection("properties");
        if (defaultSection == null) throw new RuntimeException("No default properties section");
        this.data = new TextDisplayData(defaultSection);


        ConfigurationSection sneakSection = nameDisplay().getConfig().getConfigurationSection("sneak-properties");
        if (sneakSection == null) throw new RuntimeException("No sneak properties section");
        this.sneakData = new TextDisplayData(sneakSection);
        this.sneakEnabled = sneakSection.getBoolean("enabled");

        List<String> textInLines = nameDisplay().getConfig().getStringList("text");
        this.text = String.join("<newline>", textInLines);

        ConfigurationSection miscSection = nameDisplay().getConfig().getConfigurationSection("misc");
        if (miscSection == null) throw new RuntimeException("No misc properties section");

        this.showForOwners = miscSection.getBoolean("show-for-owners");
        this.debug = miscSection.getBoolean("debug");
        double updateIntervalInSeconds = miscSection.getDouble("update-interval", -1);
        this.updateIntervalInTicks = (long) (updateIntervalInSeconds * 20);
    }

    public long updateIntervalInTicks() {
        return updateIntervalInTicks;
    }


    public boolean visibleForOwners() {
        return showForOwners;
    }

    public boolean debug() {
        return debug;
    }

    public boolean sneakEnabled() {
        return sneakEnabled;
    }

    public TextDisplayData properties() {
        return data;
    }

    public TextDisplayData sneakProperties() {
        return sneakData;
    }

    public String getTextWithSetPlaceholders(@Nullable Player player) {
        return mainHandler().placeholderApiEnabled() && player != null ? PlaceholderAPI.setPlaceholders(player, this.text) : this.text;
    }
}
