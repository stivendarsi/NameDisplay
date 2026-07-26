package me.stivendarsi.nameDisplay.packetlisteners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.papermc.paper.event.player.PlayerTrackEntityEvent;
import io.papermc.paper.event.player.PlayerUntrackEntityEvent;
import me.stivendarsi.nameDisplay.NameDisplay;
import me.stivendarsi.nameDisplay.utility.TextDisplayEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class EventHandlers implements Listener {

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

//
//    @EventHandler
//    public void changeDimension(PlayerChangedWorldEvent event) {
//        Player owner = event.getPlayer();
//
//        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
//        if (textDisplayEntity == null) {
//            textDisplayEntity = new TextDisplayEntity(owner.getUniqueId());
//            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), textDisplayEntity);
//        }
//        textDisplayEntity.changeEntityId(event.getPlayer().getWorld());
//        textDisplayEntity.startTextUpdating();
//        textDisplayEntity.showFor(event.getPlayer(), mainHandler().configurationHandler().showForOwners());
//    }


//    @EventHandler
//    public void onPlayerTeleport(PlayerMoveEvent event) {
//        Player owner = event.getPlayer();
//        owner.sendRichMessage("שוגר");
//
//        System.out.println("AAHAHHAHA");
//
//        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
//        if (textDisplayEntity == null) return;
//        owner.getScheduler().runDelayed(NameDisplay.nameDisplay(), task -> {
//            textDisplayEntity.showFor(owner, mainHandler().configurationHandler().showForOwners());
//        }, null, 1L);
//
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
