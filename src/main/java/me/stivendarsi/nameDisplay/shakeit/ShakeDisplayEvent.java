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
        Entity entity = player.getTargetEntity(10);
        Interaction interaction = (entity instanceof Interaction) ? (Interaction) entity : null;

        mainHandler().shakeHandler().shakeDisplaySet().forEachDisplay(shakedDisplay -> {
            if (shakedDisplay == null) return;
            Interaction mainInter = (Interaction) Bukkit.getEntity(shakedDisplay.interactionUUID());
            if (mainInter == null) return;

            if (interaction == null) {
                if (!shakedDisplay.looking()) return;
                shakedDisplay.setLooking(false);
                shakedDisplay.removeViewer(player.getUniqueId());
                shakedDisplay.setDurationSecond(shakedDisplay.originalDuration());
                shakedDisplay.startShake();
                player.sendRichMessage("Canceling cause no entity");
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

        });
    }
}
