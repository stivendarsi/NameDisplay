package me.stivendarsi.nameDisplay.packetlisteners;

import me.stivendarsi.nameDisplay.utility.NameTagDisplay;
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

        NameTagDisplay nameTagDisplay = mainHandler().displayHandler().getPlayerNameTagDisplay(owner.getUniqueId());
        if (nameTagDisplay == null) return;

        if (event.isSneaking()) nameTagDisplay.sneak();
        else nameTagDisplay.unSneak();
    }
}
