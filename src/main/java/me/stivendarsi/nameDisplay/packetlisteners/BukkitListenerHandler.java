package me.stivendarsi.nameDisplay.packetlisteners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import io.papermc.paper.event.player.PlayerTrackEntityEvent;
import io.papermc.paper.event.player.PlayerUntrackEntityEvent;
import me.stivendarsi.nameDisplay.NameDisplay;
import me.stivendarsi.nameDisplay.utility.TextDisplayEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class BukkitListenerHandler implements Listener {

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
            finalTextDisplayEntity.showFor(viewer, mainHandler().configurationHandler().showForOwners());
        }, null, 1L);

    }

    @EventHandler
    public void onUnTrack(PlayerUntrackEntityEvent event) {
        if (!(event.getEntity() instanceof Player owner)) return;
        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) return;
        textDisplayEntity.hideFor(event.getPlayer());
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event){
        Player owner = event.getPlayer();
        TextDisplayEntity displayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (displayEntity == null) return;
        displayEntity.disableAndHideForViewers();
    }

    @EventHandler
    public void playerRespawnEvent(PlayerPostRespawnEvent event){
        Player owner = event.getPlayer();

        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) {
            textDisplayEntity = new TextDisplayEntity(owner.getUniqueId());
            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), textDisplayEntity);
        }
        textDisplayEntity.startTextUpdating();
        textDisplayEntity.showFor(event.getPlayer(), mainHandler().configurationHandler().showForOwners());
    }


    @EventHandler
    public void changeDimension(PlayerChangedWorldEvent event){
        Player owner = event.getPlayer();

        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) {
            textDisplayEntity = new TextDisplayEntity(owner.getUniqueId());
            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), textDisplayEntity);
        }
        textDisplayEntity.changeEntityId(event.getPlayer().getWorld());
        textDisplayEntity.startTextUpdating();
        textDisplayEntity.showFor(event.getPlayer(), mainHandler().configurationHandler().showForOwners());
    }


//    @EventHandler
//    public void onPlayerTeleport(PlayerTeleportEvent event) {
//        Player viewer = event.getPlayer();
//        int distance = viewer.getViewDistance();
//        viewer.getNearbyEntities(distance, distance, distance).forEach(entity -> {
//            if (!(entity instanceof Player owner)) return;
//            TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
//            if (textDisplayEntity == null) return;
//            textDisplayEntity.showFor(viewer, mainHandler().configurationHandler().showForOwners());
//        });
//    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!mainHandler().configurationHandler().sneakEnabled()) return;
        Player owner = event.getPlayer();

        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) return;

        if (event.isSneaking()) textDisplayEntity.sneak();
        else textDisplayEntity.unSneak();
    }
//
//    @EventHandler
//    public void onKill(PlayerDeathEvent event) {
//        Player owner = event.getPlayer();
//        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
//        if (textDisplayEntity == null) return;
//
//        textDisplayEntity.hideFor(owner);
//    }
//
//    @EventHandler
//    public void onRespawn(PlayerRespawnEvent event) {
//        Player owner = event.getPlayer();
//        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
//        if (textDisplayEntity == null) return;
//
//        textDisplayEntity.showFor(owner, mainHandler().configurationHandler().showForOwners());
//    }
}
