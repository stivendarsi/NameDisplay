package me.stivendarsi.nameDisplay.shakeit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class ShakeDisplaySet {
    private ShakedDisplay[][] shakedDisplays;


    public void spawn(Location leftTop, int rows, int columns) {
        shakedDisplays = new ShakedDisplay[rows][columns];
        for (int row = 0; row < this.shakedDisplays.length; row++) {
            for (int colunm = 0; colunm < this.shakedDisplays[row].length; colunm++) {
                ShakedDisplay shakedDisplay = this.shakedDisplays[row][colunm];
                if (shakedDisplay != null) {
                    if (!(Bukkit.getEntity(shakedDisplay.displayUUID()) instanceof ItemDisplay itemDisplay)) return;
                    if (!(Bukkit.getEntity(shakedDisplay.interactionUUID()) instanceof Interaction interaction)) return;
                    itemDisplay.remove();
                    interaction.remove();
                }
                this.shakedDisplays[row][colunm] = new ShakedDisplay(leftTop);
                leftTop.add(1.5,0,0);
            }
            leftTop.subtract(row * -1.5, 0,0);
            leftTop.subtract(0,1.5,0);
        }
    }


    public void forEachDisplay(Consumer<@Nullable ShakedDisplay> displayConsumer){
        if (this.shakedDisplays == null) return;
        for (ShakedDisplay[] display : this.shakedDisplays) {
            for (ShakedDisplay shakedDisplay : display) {
                displayConsumer.accept(shakedDisplay);
            }
        }
    }

    @Nullable
    public ShakedDisplay[][] shakedDisplays() {
        return shakedDisplays;
    }
}
