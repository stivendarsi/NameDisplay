package me.stivendarsi.nameDisplay.shakeit;


import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static me.stivendarsi.nameDisplay.NameDisplay.nameDisplay;

public class ShakedDisplay {
    private final UUID displayUUID;
    private ScheduledTask scheduledTask;
    private final Matrix4f translate = new Matrix4f().translate(0, 0.5f, 0);
    private boolean looking;

    private double durationSecond;
    private double originalDuration;
    private double angle;

    private final UUID interactionUUID;

    private List<UUID> viewers;

    public ShakedDisplay(Location location) {
        this.viewers = new ArrayList<>();
        location.setPitch(0);
        float yaw = location.getYaw();
        yaw = ((yaw + 180 + 360) % 360) - 180;
        location.setYaw(yaw);
        ItemDisplay itemDisplay = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        this.displayUUID = itemDisplay.getUniqueId();

        ItemStack box = ItemType.EMERALD.createItemStack();
        box.setData(DataComponentTypes.ITEM_MODEL, Key.key("shake:box"));

        itemDisplay.setItemStack(box);
        itemDisplay.setTransformationMatrix(this.translate);
        itemDisplay.setPersistent(false);

        Interaction interaction = (Interaction) location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        this.interactionUUID = interaction.getUniqueId();

        interaction.addPassenger(itemDisplay);
        interaction.setPersistent(false);

        this.angle = 0;
        this.durationSecond = 1;
    }

    public void stop() {
        if (this.scheduledTask != null) this.scheduledTask.cancel();
    }

    public void setDurationSecond(double durationSecond) {
        this.durationSecond = Math.max(0, durationSecond);
    }

    public void setOriginalDurationSecond(double originalDurationSecond) {
        setDurationSecond(originalDurationSecond);
        this.originalDuration = Math.max(0, originalDurationSecond);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void addViewer(UUID uuid) {
        this.viewers.add(uuid);
    }

    public void removeViewer(UUID uuid) {
        this.viewers.remove(uuid);
    }

    public void startShake() {
        stop();

        ItemDisplay display = (ItemDisplay) Bukkit.getEntity(displayUUID);
        if (display == null) return;

        Matrix4f matrix4d = new Matrix4f(this.translate);
        display.setTransformationMatrix(matrix4d); // Reset

        matrix4d.rotateZ((float) Math.toRadians(angle / 2));

        AtomicReference<Float> way = new AtomicReference<>((float) -1);

        int durationTick = Math.max((int) (this.durationSecond * 20), 1);
        Sound sound = Sound.sound(Key.key("minecraft:item.bundle.drop_contents"), Sound.Source.UI, 1, 1);

        this.scheduledTask = display.getScheduler().runAtFixedRate(nameDisplay(), task -> {
            if (!display.isValid() || this.scheduledTask != task) {
                task.cancel();
                return;
            }

            matrix4d.rotateZ((float) (way.get() * Math.toRadians(angle)));
            way.set(way.get() * -1);

            display.setTransformationMatrix(matrix4d);
            display.setInterpolationDuration(durationTick);
            display.setInterpolationDelay(0);

            if (!this.looking) return;
            for (UUID viewer : this.viewers) {
                Player player = Bukkit.getPlayer(viewer);
                if (player == null) continue;
                player.playSound(sound);
            }

        }, null, 1, durationTick);

    }

    public UUID interactionUUID() {
        return interactionUUID;
    }

    public double durationSecond() {
        return durationSecond;
    }

    public double angle() {
        return angle;
    }

    public boolean looking() {
        return looking;
    }

    public void setLooking(boolean looking) {
        this.looking = looking;
    }

    public double originalDuration() {
        return originalDuration;
    }

    public UUID displayUUID() {
        return displayUUID;
    }
}
