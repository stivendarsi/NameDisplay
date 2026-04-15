package me.stivendarsi.nameDisplay.shakeit;


import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemType;
import org.joml.Matrix4f;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static me.stivendarsi.nameDisplay.NameDisplay.plugin;

public class ShakedDisplay {
    private final UUID displayUUID;
    private ScheduledTask scheduledTask;
    private final Matrix4f translate = new Matrix4f().translate(0,-0.5f,0);

    private final UUID interactionUUID;

    public ShakedDisplay(Location location) {
        location.setPitch(0);
        float yaw = location.getYaw();
        yaw = ((yaw + 180 + 360) % 360) - 180;
        location.setYaw(yaw);
        ItemDisplay itemDisplay = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        this.displayUUID = itemDisplay.getUniqueId();
        itemDisplay.setItemStack(ItemType.EMERALD.createItemStack());
        itemDisplay.setTransformationMatrix(this.translate);

        Interaction interaction = (Interaction) location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        this.interactionUUID = interaction.getUniqueId();

        interaction.addPassenger(itemDisplay);
    }

    public void stop(){
        if (this.scheduledTask != null) this.scheduledTask.cancel();
    }

    public void startShake(int durationTick, double angle) {
        stop();

        ItemDisplay display = (ItemDisplay) Bukkit.getEntity(displayUUID);
        if (display == null) return;

        Matrix4f matrix4d = new Matrix4f(this.translate);
        display.setTransformationMatrix(matrix4d); // Reset

        matrix4d.rotateZ((float) Math.toRadians(angle / 2));

        AtomicReference<Float> way = new AtomicReference<>((float) -1);

        this.scheduledTask = display.getScheduler().runAtFixedRate(plugin(), task -> {
            if (!display.isValid() || this.scheduledTask != task) {
                task.cancel();
                return;
            }
            matrix4d.rotateZ((float) (way.get() * Math.toRadians(angle)));
            way.set(way.get() * -1);

            display.setTransformationMatrix(matrix4d);
            display.setInterpolationDuration(durationTick);
            display.setInterpolationDelay(0);

        }, null, 1, durationTick);

    }

    public UUID interactionUUID() {
        return interactionUUID;
    }

    public UUID displayUUID() {
        return displayUUID;
    }
}
