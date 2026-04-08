package me.stivendarsi.nameDisplay.packetlisteners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import me.stivendarsi.nameDisplay.utility.NameTagDisplay;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class RemoveDisplayHandler implements PacketListener {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.DESTROY_ENTITIES) return;
        Player viewer = event.getPlayer();
        WrapperPlayServerDestroyEntities destroyEntities = new WrapperPlayServerDestroyEntities(event);
        for (int entityId : destroyEntities.getEntityIds()) {
            Entity entity = SpigotConversionUtil.getEntityById(viewer.getWorld(), entityId);
            if (!(entity instanceof Player owner)) continue;
            NameTagDisplay nameTagDisplay = mainHandler().displayHandler().getPlayerNameTagDisplay(owner.getUniqueId());
            if (nameTagDisplay == null) continue;
            nameTagDisplay.hideFor(viewer);
        }
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        UUID uuid = event.getUser().getUUID();
        NameTagDisplay nameTagDisplay = mainHandler().displayHandler().getPlayerNameTagDisplay(uuid);
        if (nameTagDisplay == null) return;
        nameTagDisplay.disableTextUpdate();
        mainHandler().displayHandler().unRegisterDisplay(uuid);
    }
}
