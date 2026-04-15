package me.stivendarsi.nameDisplay.shakeit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.TextDisplay;

public class ShakeHandler {
    private ShakedDisplay shakedDisplay;

    public void spawn(Location location) {
        if (this.shakedDisplay != null) {
            if (!(Bukkit.getEntity(shakedDisplay.displayUUID()) instanceof TextDisplay textDisplay)) return;
            if (!(Bukkit.getEntity(shakedDisplay.interactionUUID()) instanceof Interaction interaction)) return;
            textDisplay.remove();
            interaction.remove();
        }
        this.shakedDisplay = new ShakedDisplay(location);
    }

    public ShakedDisplay shakedDisplay() {
        return shakedDisplay;
    }
}
