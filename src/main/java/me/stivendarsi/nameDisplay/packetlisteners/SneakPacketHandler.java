package me.stivendarsi.nameDisplay.packetlisteners;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import me.stivendarsi.nameDisplay.utility.TextDisplayEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class SneakPacketHandler implements Listener {
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!mainHandler().configurationHandler().sneakEnabled()) return;
        Player owner = event.getPlayer();

        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) return;

        if (event.isSneaking()) textDisplayEntity.sneak();
        else textDisplayEntity.unSneak();
    }

    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event){
        Player owner = event.getPlayer();
        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) return;

        textDisplayEntity.showFor(owner, mainHandler().configurationHandler().showForOwners());
    }
}
