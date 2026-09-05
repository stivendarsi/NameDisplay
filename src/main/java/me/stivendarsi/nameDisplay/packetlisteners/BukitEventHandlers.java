package me.stivendarsi.nameDisplay.packetlisteners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import io.papermc.paper.event.player.PlayerTrackEntityEvent;
import io.papermc.paper.event.player.PlayerUntrackEntityEvent;
import me.stivendarsi.nameDisplay.NameDisplay;
import me.stivendarsi.nameDisplay.utility.TextDisplayEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class BukitEventHandlers implements Listener {

    @EventHandler
    public void onTrack(PlayerTrackEntityEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getEntity() instanceof Player owner)) return;
        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) {
            textDisplayEntity = new TextDisplayEntity(owner.getUniqueId());
            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), textDisplayEntity);
        }

        Player viewer = event.getPlayer();

        TextDisplayEntity finalTextDisplayEntity = textDisplayEntity;
        viewer.getScheduler().runDelayed(NameDisplay.nameDisplay(), task -> {
            finalTextDisplayEntity.showForAsync(viewer, mainHandler().configurationHandler().visibleForOwners());
        }, null, 1L);

    }

    @EventHandler
    public void onInvisible(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player owner)) return;

        if (event.getModifiedType() != PotionEffectType.INVISIBILITY) return;

        EntityPotionEffectEvent.Action action = event.getAction();

        TextDisplayEntity displayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (displayEntity == null) return;
        switch (action) {
            case ADDED, CHANGED -> displayEntity.disableAndHideForViewers();
            case REMOVED, CLEARED -> {
                for (Player viewer : owner.getTrackedBy()) {
                    viewer.getScheduler().runDelayed(NameDisplay.nameDisplay(), task -> {
                        displayEntity.showForAsync(viewer, mainHandler().configurationHandler().visibleForOwners());
                    }, null, 1L);
                }

                displayEntity.showForAsync(owner, mainHandler().configurationHandler().visibleForOwners());
            }
        }
    }

    @EventHandler
    public void onUnTrack(PlayerUntrackEntityEvent event) {
        if (!(event.getEntity() instanceof Player owner)) return;
        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) return;
        textDisplayEntity.hideFor(event.getPlayer());
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        Player owner = event.getPlayer();
        TextDisplayEntity displayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (displayEntity == null) return;
        displayEntity.disableAndHideForViewers();
    }

    @EventHandler
    public void playerRespawnEvent(PlayerPostRespawnEvent event) {
        Player owner = event.getPlayer();

        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) {
            textDisplayEntity = new TextDisplayEntity(owner.getUniqueId());
            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), textDisplayEntity);
        }
        textDisplayEntity.startTextUpdating();
        textDisplayEntity.showForAsync(event.getPlayer(), mainHandler().configurationHandler().visibleForOwners());
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!mainHandler().configurationHandler().sneakEnabled()) return;
        Player owner = event.getPlayer();

        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) return;

        if (event.isSneaking()) textDisplayEntity.sneak();
        else textDisplayEntity.unSneak();
    }
}
