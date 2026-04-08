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
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.plugin;

public class NameTagDisplay {
    private final int entityID;
    private final UUID entityUUID;
    private final UUID ownerUUID;
    private boolean ticking;
    private final Set<UUID> viewers;

    private final WrapperPlayServerSpawnEntity serverSpawnEntity;
    private final WrapperPlayServerSetPassengers serverSetPassengers;
    private final WrapperPlayServerDestroyEntities removePacket;
    private final WrapperPlayServerEntityMetadata defaultMetaData;

    public NameTagDisplay(UUID ownerUUID) {
        this.viewers = new HashSet<>();
        this.entityUUID = UUID.randomUUID();
        this.ownerUUID = ownerUUID;
        this.entityID = SpigotReflectionUtil.generateEntityId();
        this.ticking = false;

        Player owner = Bukkit.getPlayer(this.ownerUUID);
        if (owner == null) throw new RuntimeException("Null owner");

        Location location = new Location(0,0,0,0,0);

        this.serverSpawnEntity = new WrapperPlayServerSpawnEntity(this.entityID, this.entityUUID, EntityTypes.TEXT_DISPLAY, location, location.getYaw(), 0, null);
        this.serverSetPassengers = new WrapperPlayServerSetPassengers(owner.getEntityId(), new int[]{this.entityID});

        this.removePacket = new WrapperPlayServerDestroyEntities(this.entityID);
        this.defaultMetaData = getDisplayDefaultMetaDataPacket();

    }

    public void hideForViewers() {
        if (this.viewers == null || this.viewers.isEmpty()) return;
        for (UUID viewer : this.viewers) {
            Player player = Bukkit.getPlayer(viewer);
            if (player == null) return;
            hideFor(player);
        }

        this.viewers.clear();
    }

    public void hideFor(Player player) {
        Player owner = Bukkit.getPlayer(ownerUUID);
        if (owner == null) return;



        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        user.sendPacket(this.removePacket);

        owner.sendRichMessage("Hiding your display for %s".formatted(player.getName()));
        this.viewers.remove(player.getUniqueId());
    }

    public void disableTextUpdate() {
        this.ticking = false;
    }


    public void startIfNeed() {
        if (!this.ticking) startTheUpdateAtFixedRate();
    }

    private void startTheUpdateAtFixedRate() {
        Player owner = Bukkit.getPlayer(ownerUUID);
        if (owner == null) return;

        long interval = mainHandler().configurationHandler().updateIntervalInTicks();

        // If interval is enabled, update at fixed rate.
        if (0 < interval) {
            this.ticking = true;
            owner.getScheduler().runAtFixedRate(plugin(), task -> {
                if (ticking) updateTextForAll();
                else task.cancel();
            }, null, 1, interval);
        } else {
            //   updateTextForAll();
            this.ticking = false;
        }
    }


    public void showFor(Player player, boolean visibleToTheOwner) {
        Player owner = Bukkit.getPlayer(this.ownerUUID);
        if (owner == null) return;

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        // Location location = SpigotConversionUtil.fromBukkitLocation(owner.getLocation());
     //   Location location = new Location(0, 0, 0, 0, 0);

        //  WrapperPlayServerSpawnEntity serverSpawnEntity = new WrapperPlayServerSpawnEntity(this.entityID, this.entityUUID, EntityTypes.TEXT_DISPLAY, location, location.getYaw(), 0, null);
        //  WrapperPlayServerSetPassengers serverSetPassengers = new WrapperPlayServerSetPassengers(owner.getEntityId(), new int[]{this.entityID});

        user.sendPacket(this.serverSpawnEntity); // Spawn the display.
        if (visibleToTheOwner)
            player.sendRichMessage("<yellow>Self Visible</yellow> - %s's display was shown to you".formatted(owner.getName()));
        else player.sendRichMessage("%s's display was shown to you".formatted(owner.getName()));
        user.sendPacket(this.serverSetPassengers); // Set display as a passenger of the player.
        user.sendPacket(this.defaultMetaData); // Send default display properties.
        user.sendPacket(getUpdatedTextPacket());

        this.viewers.add(player.getUniqueId());
    }

    private void updateTextForAll() {
        WrapperPlayServerEntityMetadata updatedTextPacket = getUpdatedTextPacket();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Player player = onlinePlayer.getPlayer();
            if (player == null) return;
            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
            user.sendPacket(updatedTextPacket); // Send default display properties.
        }
    }

    private WrapperPlayServerEntityMetadata getDisplayDefaultMetaDataPacket() {
        List<EntityData<?>> metadata = new ArrayList<>();
        EntityData<?> billboard = new EntityData<>(15, EntityDataTypes.BYTE, (byte) 3);
        metadata.add(billboard);

        EntityData<?> translation = new EntityData<>(11, EntityDataTypes.VECTOR3F, new Vector3f(0f, 0.25f, 0f));
        metadata.add(translation);

        return new WrapperPlayServerEntityMetadata(this.entityID, metadata);
    }

    public WrapperPlayServerEntityMetadata getUpdatedTextPacket() {
        Player owner = Bukkit.getPlayer(ownerUUID);
        List<EntityData<?>> metadata = new ArrayList<>();

        EntityData<?> textData = new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, MiniMessage.miniMessage().deserialize(mainHandler().configurationHandler().getWithSetPlaceholders(owner)));

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
