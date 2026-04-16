package me.stivendarsi.nameDisplay.shakeit;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.UUID;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class HitBoxInteractionEventHandler implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractAtEntityEvent event){
        if (!(event.getRightClicked() instanceof Interaction interaction)) return;
        UUID uuid = interaction.getUniqueId();
        if (!mainHandler().shakeHandler().shakedDisplay().interactionUUID().equals(uuid)) return;
        Player player = event.getPlayer();
        player.sendRichMessage("Right Clicked");
    }

    @EventHandler
    public void onLeftClick(PrePlayerAttackEntityEvent event){
        if (!(event.getAttacked() instanceof Interaction interaction)) return;
        UUID uuid = interaction.getUniqueId();
        if (!mainHandler().shakeHandler().shakedDisplay().interactionUUID().equals(uuid)) return;

        Player player = event.getPlayer();
        player.sendRichMessage("Left Clicked");
    }
}
