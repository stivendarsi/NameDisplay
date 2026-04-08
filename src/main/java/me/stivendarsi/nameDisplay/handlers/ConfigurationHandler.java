package me.stivendarsi.nameDisplay.handlers;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.plugin;

public class ConfigurationHandler {
    private long updateIntervalInTicks;
    private String text;
    private boolean showForOwners;

    public void load() {
        double updateIntervalInSeconds = plugin().getConfig().getDouble("update-interval", -1);
        this.updateIntervalInTicks = (long) (updateIntervalInSeconds * 20);

        List<String> textInLines = plugin().getConfig().getStringList("text");
        this.text = String.join("<newline>", textInLines);

        this.showForOwners = plugin().getConfig().getBoolean("show-for-owners");
    }

    public long updateIntervalInTicks() {
        return updateIntervalInTicks;
    }

    public String text() {
        return text;
    }

    public boolean showForOwners() {
        return showForOwners;
    }

    public String getWithSetPlaceholders(@Nullable Player player){
        return mainHandler().placeholderApiEnabled() && player != null ? PlaceholderAPI.setPlaceholders(player, this.text) : this.text;
    }
}
