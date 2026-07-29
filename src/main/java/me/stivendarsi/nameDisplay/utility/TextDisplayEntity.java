package me.stivendarsi.nameDisplay.utility;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.miniplaceholders.api.MiniPlaceholders;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import me.stivendarsi.nameDisplay.handlers.ConfigurationHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.nameDisplay;

public class TextDisplayEntity {
    private int entityID;
    private final UUID entityUUID;
    private final UUID ownerUUID;
    private boolean textUpdating;
    private final List<UUID> viewers;
    private boolean isSneaking;
    private World currentWorld = null;

    private final WrapperPlayServerDestroyEntities removePacket;

    public TextDisplayEntity(UUID ownerUUID) {
        this.viewers = new ArrayList<>();
        this.entityUUID = UUID.randomUUID();
        this.ownerUUID = ownerUUID;
        this.textUpdating = false;

        Player owner = Bukkit.getPlayer(this.ownerUUID);
        if (owner == null) throw new RuntimeException("Null owner");

        handleWorldChange(owner.getWorld());

        //   this.serverSetPassengers = new WrapperPlayServerSetPassengers(owner.getEntityId(), new int[]{this.entityID});
        this.removePacket = new WrapperPlayServerDestroyEntities(this.entityID);
    }

    // Use only in dimension switching

    public void handleWorldChange(World world) {
        if (currentWorld != world) {
            changeEntityId(world);
            this.currentWorld = world;
        }
    }

    private void changeEntityId(World world) {
        if (entityID != 0) disableAndHideForViewers();
        else this.entityID = SpigotReflectionUtil.generateEntityId(world);
    }

    private WrapperPlayServerSetPassengers getServerSetPassengers(Player owner) {
        return new WrapperPlayServerSetPassengers(owner.getEntityId(), new int[]{this.entityID});
    }

    public void sneak() {
        this.isSneaking = true;
        sendPacketsToViewers(getModePacket());
    }

    public void unSneak() {
        this.isSneaking = false;
        sendPacketsToViewers(getModePacket());
    }

    private WrapperPlayServerEntityMetadata getModePacket() {
        ConfigurationHandler config = mainHandler().configurationHandler();

        if (config.sneakEnabled() && this.isSneaking) return getDisplayMeta(config.sneakProperties());
        else return getDisplayMeta(config.properties());
    }

    private void sendPacketsToViewers(PacketWrapper<?> packet) {
        List<UUID> viewersSnapshot = new ArrayList<>(this.viewers);
        nameDisplay().getServer().getAsyncScheduler().runNow(nameDisplay(), _ -> {
            for (UUID viewerUUID : viewersSnapshot) {
                Player viewer = Bukkit.getPlayer(viewerUUID);
                if (viewer == null) continue;
                User user = PacketEvents.getAPI().getPlayerManager().getUser(viewer);
                if (user == null) {
                    nameDisplay().getLogger().warning("Null user");
                    return;
                }
                user.sendPacket(packet);
            }
        });
    }

    public void showForAsync(@NotNull Player viewer, boolean visibleToTheOwner){
        nameDisplay().getServer().getAsyncScheduler().runNow(nameDisplay(), scheduledTask -> {
            showDisplayForUser(viewer, visibleToTheOwner);
        });
    }

    private void showDisplayForUser(@NotNull Player viewer, boolean visibleToTheOwner) {
        if (!visibleToTheOwner && viewer.getUniqueId().equals(this.ownerUUID)) {
            viewer.sendRichMessage("בוטל כי: uuid: " + viewer.getUniqueId().equals(this.ownerUUID));
            viewer.sendRichMessage("בוטל כי: visible: " + false);
            return;
        }
        Player owner = Bukkit.getPlayer(this.ownerUUID);
        if (owner == null) {
            throw new RuntimeException("Null owner");
        }

        User user = PacketEvents.getAPI().getPlayerManager().getUser(viewer);
        if (user == null) throw new RuntimeException("Null packet user");

        Location loc = owner.getLocation();

        WrapperPlayServerSpawnEntity serverSpawnEntity = new WrapperPlayServerSpawnEntity(this.entityID,
                this.entityUUID,
                EntityTypes.TEXT_DISPLAY,
                SpigotConversionUtil.fromBukkitLocation(loc.add(0, 2f, 0)),
                0, 0, null
        );

        user.sendPacket(serverSpawnEntity); // Spawn the display.

        if (mainHandler().configurationHandler().debug()) {
            TagResolver.Single namePlaceHolder = Placeholder.parsed("name", "<#ffbdec>" + owner.getName() + "</#ffbdec>");
            if (ownerUUID.equals(viewer.getUniqueId()))
                viewer.sendRichMessage("<yellow>Debug <gray>|</gray> Self Visible</yellow><#7dffe5> - <name>'s display was shown to you", namePlaceHolder);
            else
                viewer.sendRichMessage("<yellow>Debug <gray>|</gray></yellow><#7dffe5> <name>'s display was shown to you", namePlaceHolder);
        }
        user.sendPacket(getServerSetPassengers(owner)); // Set display as a passenger of the player.

        user.sendPacket(getModePacket()); // Send default display properties.
        user.sendPacket(getUpdatedTextPacket());

        viewer.getUniqueId();
        this.viewers.add(viewer.getUniqueId());
    }

    public void disableAndHideForViewers() {
        stopTextUpdate();
        List<UUID> uuids = this.viewers.stream()
                .filter(Objects::nonNull)
                .toList();

        for (UUID viewer : uuids) {
            Player player = Bukkit.getPlayer(viewer);
            if (player != null) hideFor(player);
        }
    }

    public void hideFor(Player viewer) {
        Player owner = Bukkit.getPlayer(ownerUUID);
        if (owner == null) return;

        User user = PacketEvents.getAPI().getPlayerManager().getUser(viewer);
        if (user == null) {
           // nameDisplay().getLogger().warning("Null user");
            return;
        }
        user.sendPacket(this.removePacket);

        if (mainHandler().configurationHandler().debug())
            owner.sendRichMessage("<yellow>Debug <gray>|</gray></yellow><#7dffe5> Hiding your display for %s".formatted(viewer.getName()));

        viewer.getUniqueId();
        this.viewers.remove(viewer.getUniqueId());
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
            owner.getScheduler().runAtFixedRate(nameDisplay(), task -> {
                if (textUpdating) updateTextForAll();
                else task.cancel();
            }, null, 1, interval);
        } else {
            stopTextUpdate();
        }
    }

    private void updateTextForAll() {
        WrapperPlayServerEntityMetadata updatedTextPacket = getUpdatedTextPacket();

        sendPacketsToViewers(updatedTextPacket); // Send default display properties.
    }

    private WrapperPlayServerEntityMetadata getDisplayMeta(TextDisplayData textDisplayData) {
        List<EntityData<?>> metadata = new ArrayList<>();

        EntityData<?> renderWidth = new EntityData<>(20, EntityDataTypes.FLOAT, textDisplayData.renderWidth());
        metadata.add(renderWidth);

        EntityData<?> renderHeight = new EntityData<>(21, EntityDataTypes.FLOAT, textDisplayData.renderHeight());
        metadata.add(renderHeight);

        EntityData<?> billboard = new EntityData<>(15, EntityDataTypes.BYTE, textDisplayData.billboard()); // Display.Billboard.CENTER
        metadata.add(billboard);

        EntityData<?> textWidth = new EntityData<>(24, EntityDataTypes.INT, textDisplayData.maxLineWidth());
        metadata.add(textWidth);

        EntityData<?> translation = new EntityData<>(11, EntityDataTypes.VECTOR3F, textDisplayData.translation()); // Translate the display 0.25 above the player
        metadata.add(translation);

        EntityData<?> textOpacityData = new EntityData<>(26, EntityDataTypes.BYTE, textDisplayData.textOpacity());
        metadata.add(textOpacityData);

        EntityData<?> backgroundOpacityData = new EntityData<>(25, EntityDataTypes.INT, textDisplayData.backgroundOpacity());
        metadata.add(backgroundOpacityData);

        return new WrapperPlayServerEntityMetadata(this.entityID, metadata);
    }

    public WrapperPlayServerEntityMetadata getUpdatedTextPacket() {
        Player owner = Bukkit.getPlayer(ownerUUID);
        if (owner == null) throw new RuntimeException("Null Owner.");
        List<EntityData<?>> metadata = new ArrayList<>();

        Component text = MiniMessage.miniMessage().deserialize(mainHandler().configurationHandler().getTextWithSetPlaceholders(owner), owner, MiniPlaceholders.audienceGlobalPlaceholders());

        EntityData<?> textData = new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, text);

        metadata.add(textData);
        return new WrapperPlayServerEntityMetadata(this.entityID, metadata);
    }

    public UUID entityUUID() {
        return entityUUID;
    }

    public UUID ownerUUID() {
        return ownerUUID;
    }
}
