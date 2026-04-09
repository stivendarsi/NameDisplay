package me.stivendarsi.nameDisplay.utility;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;

import java.util.*;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.plugin;

public class NameTagDisplay {
    private final int entityID;
    private final UUID entityUUID;
    private final UUID ownerUUID;
    private boolean textUpdating;
    private final List<UUID> viewers;

    private final WrapperPlayServerSpawnEntity serverSpawnEntity;
    private final WrapperPlayServerSetPassengers serverSetPassengers;
    private final WrapperPlayServerDestroyEntities removePacket;
    private final WrapperPlayServerEntityMetadata defaultMetaData;

    public NameTagDisplay(UUID ownerUUID) {
        this.viewers = new ArrayList<>();
        this.entityUUID = UUID.randomUUID();
        this.ownerUUID = ownerUUID;
        this.entityID = SpigotReflectionUtil.generateEntityId();
        this.textUpdating = false;

        Player owner = Bukkit.getPlayer(this.ownerUUID);
        if (owner == null) throw new RuntimeException("Null owner");

        Location location = new Location(0, 0, 0, 0, 0);

        this.serverSpawnEntity = new WrapperPlayServerSpawnEntity(this.entityID, this.entityUUID, EntityTypes.TEXT_DISPLAY, location, location.getYaw(), 0, null);
        this.serverSetPassengers = new WrapperPlayServerSetPassengers(owner.getEntityId(), new int[]{this.entityID});

        this.removePacket = new WrapperPlayServerDestroyEntities(this.entityID);
        this.defaultMetaData = getDisplayDefaultMetaDataPacket();

    }

    public void showFor(Player player, boolean visibleToTheOwner) {
        Player owner = Bukkit.getPlayer(this.ownerUUID);
        if (owner == null) return;

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);

        user.sendPacket(this.serverSpawnEntity); // Spawn the display.
        if (visibleToTheOwner)
            player.sendRichMessage("<yellow>Debug <gray>|</gray> Self Visible</yellow> - %s's display was shown to you".formatted(owner.getName()));
        else player.sendRichMessage("<yellow>Debug <gray>|</gray></yellow><#7dffe5> %s's display was shown to you".formatted(owner.getName()));
        user.sendPacket(this.serverSetPassengers); // Set display as a passenger of the player.
        user.sendPacket(this.defaultMetaData); // Send default display properties.
        user.sendPacket(getUpdatedTextPacket());

        this.viewers.add(player.getUniqueId());
    }

    public void disableAndHideForViewers() {
        stopTextUpdate();
        System.out.println(this.viewers);
        List<UUID> uuids = List.copyOf(this.viewers);
        for (UUID viewer : uuids) {
            Player player = Bukkit.getPlayer(viewer);
            if (player != null) hideFor(player);
        }
        System.out.println(this.viewers);
    }

    public void hideFor(Player player) {
        Player owner = Bukkit.getPlayer(ownerUUID);
        if (owner == null) return;

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        user.sendPacket(this.removePacket);

        owner.sendRichMessage("<yellow>Debug <gray>|</gray></yellow><#7dffe5> Hiding your display for %s".formatted(player.getName()));
        this.viewers.remove(player.getUniqueId());
    }

    private void stopTextUpdate() {
        this.textUpdating = false;
    }

    public void startTextUpdating() {
        if (!this.textUpdating) startTextUpdatingTask();
    }

    private void startTextUpdatingTask() {
        Player owner = Bukkit.getPlayer(ownerUUID);
        if (owner == null) return;

        long interval = mainHandler().configurationHandler().updateIntervalInTicks();

        // If interval is enabled, update at fixed rate.
        if (0 < interval) {
            this.textUpdating = true;
            owner.getScheduler().runAtFixedRate(plugin(), task -> {
                if (textUpdating) updateTextForAll();
                else task.cancel();
            }, null, 1, interval);
        } else {
            stopTextUpdate();
        }
    }

    private void updateTextForAll() {
        WrapperPlayServerEntityMetadata updatedTextPacket = getUpdatedTextPacket();

        for (UUID viewerUUID : this.viewers) {
            Player viewer = Bukkit.getPlayer(viewerUUID);
            if (viewer == null) return;

            User user = PacketEvents.getAPI().getPlayerManager().getUser(viewer);
            user.sendPacket(updatedTextPacket); // Send default display properties.
        }
    }

    private WrapperPlayServerEntityMetadata getDisplayDefaultMetaDataPacket() {
        List<EntityData<?>> metadata = new ArrayList<>();
        EntityData<?> billboard = new EntityData<>(15, EntityDataTypes.BYTE, (byte) 3); // Display.Billboard.CENTER
        metadata.add(billboard);

        EntityData<?> translation = new EntityData<>(11, EntityDataTypes.VECTOR3F, new Vector3f(0f, 0.25f, 0f)); // Translate the display 0.25 above the player
        metadata.add(translation);

        return new WrapperPlayServerEntityMetadata(this.entityID, metadata);
    }

    public WrapperPlayServerEntityMetadata getUpdatedTextPacket() {
        Player owner = Bukkit.getPlayer(ownerUUID);
        List<EntityData<?>> metadata = new ArrayList<>();

        Component text =  MiniMessage.miniMessage().deserialize(mainHandler().configurationHandler().getWithSetPlaceholders(owner));

        EntityData<?> textData = new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, text);

        metadata.add(textData);
        return new WrapperPlayServerEntityMetadata(this.entityID, metadata);
    }

    public int entityID() {
        return entityID;
    }

    public UUID entityUUID() {
        return entityUUID;
    }

    public UUID ownerUUID() {
        return ownerUUID;
    }
}
