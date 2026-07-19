package me.stivendarsi.nameDisplay.packetlisteners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import me.stivendarsi.nameDisplay.utility.TextDisplayEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.player.PlayerHideEntityEvent;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class HideDisplayHandler implements PacketListener {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.DESTROY_ENTITIES) return;
        if (event.isCancelled()) return;
        Player viewer = event.getPlayer();
        WrapperPlayServerDestroyEntities destroyEntities = new WrapperPlayServerDestroyEntities(event);
        viewer.sendRichMessage("נמחקו ישויות");

        for (int entityId : destroyEntities.getEntityIds()) {

            Entity entity = SpigotConversionUtil.getEntityById(viewer.getWorld(), entityId);
            if (!(entity instanceof Player owner)) continue;

            TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
            if (textDisplayEntity == null) continue;


            textDisplayEntity.hideFor(viewer);
        }
    }
}