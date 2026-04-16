package me.stivendarsi.nameDisplay.shakeit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class ShakeDisplayEvent implements Listener {
    @EventHandler
    public void advanceShake(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ShakedDisplay shakedDisplay = mainHandler().shakeHandler().shakedDisplay();
        if (shakedDisplay == null) return;

        Interaction mainInter = (Interaction) Bukkit.getEntity(mainHandler().shakeHandler().shakedDisplay().interactionUUID());
        if (mainInter == null) return;

        Entity entity = player.getTargetEntity(10);

        if (entity == null) {
            if (!shakedDisplay.looking()) return;
            shakedDisplay.setLooking(false);
            shakedDisplay.removeViewer(player.getUniqueId());
            shakedDisplay.setDurationSecond(shakedDisplay.originalDuration());
            shakedDisplay.startShake();
            player.sendRichMessage("Canceling cause no entity");
            return;
        }
        if (!(entity instanceof Interaction interaction)) {
            player.sendRichMessage("Canceling cause not interaction");
            return;
        }

        if (!interaction.getUniqueId().equals(shakedDisplay.interactionUUID())) {
            player.sendRichMessage("Canceling not the same UUID");
            return;
        }
        if (shakedDisplay.looking()) return;

        shakedDisplay.setLooking(true);
        shakedDisplay.addViewer(player.getUniqueId());


        player.sendRichMessage("Speeding");

        shakedDisplay.setDurationSecond(0.2);
        shakedDisplay.startShake();
    }
}
