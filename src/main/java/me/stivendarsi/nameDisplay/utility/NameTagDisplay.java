package me.stivendarsi.nameDisplay.utility;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.plugin;

public class NameTagDisplay {
    private final int entityID;
    private final UUID entityUUID;

    public NameTagDisplay() {
        this.entityUUID = UUID.randomUUID();
        this.entityID = SpigotReflectionUtil.generateEntityId();
    }

    public void spawn(Player player) {
        Location location = SpigotConversionUtil.fromBukkitLocation(player.getLocation().add(0, 2, 0));

        WrapperPlayServerSpawnEntity serverSpawnEntity = new WrapperPlayServerSpawnEntity(this.entityID, this.entityUUID, EntityTypes.TEXT_DISPLAY, location, location.getYaw(), 0, null);
        WrapperPlayServerSetPassengers serverSetPassengers = new WrapperPlayServerSetPassengers(player.getEntityId(), new int[]{this.entityID});

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        user.sendPacket(serverSpawnEntity);
        user.sendPacket(serverSetPassengers);

        user.sendPacket(getDisplayDefaultMetaDataPacket());

        long interval = mainHandler().configurationHandler().updateIntervalInTicks();

        // If interval is enabled, update at fixed rate.
        if (0 < interval) {
            player.getScheduler().runAtFixedRate(plugin(), task -> {
                if (player.isValid()) user.sendPacket(getUpdatedTextPacket(player));
                else task.cancel();
            }, null, 1, mainHandler().configurationHandler().updateIntervalInTicks());
        }
        else user.sendPacket(getUpdatedTextPacket(player));
    }


    private WrapperPlayServerEntityMetadata getDisplayDefaultMetaDataPacket() {
        List<EntityData<?>> metadata = new ArrayList<>();
        EntityData<?> billboard = new EntityData<>(15, EntityDataTypes.BYTE, (byte) 3);
        metadata.add(billboard);

        EntityData<?> translation = new EntityData<>(11, EntityDataTypes.VECTOR3F, new Vector3f(0f, 0.25f, 0f));
        metadata.add(translation);

        return new WrapperPlayServerEntityMetadata(this.entityID, metadata);
    }

    public WrapperPlayServerEntityMetadata getUpdatedTextPacket(Player player) {
        List<EntityData<?>> metadata = new ArrayList<>();

        EntityData<?> textData = new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, MiniMessage.miniMessage().deserialize(mainHandler().configurationHandler().getWithSetPlaceholders(player)));

        metadata.add(textData);
        return new WrapperPlayServerEntityMetadata(this.entityID, metadata);
    }

    public int entityID() {
        return entityID;
    }

    public UUID entityUUID() {
        return entityUUID;
    }
}
