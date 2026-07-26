package me.stivendarsi.nameDisplay.packetlisteners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import me.stivendarsi.nameDisplay.utility.TextDisplayEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;
import static me.stivendarsi.nameDisplay.NameDisplay.nameDisplay;

public class ShowDisplayHandler implements PacketListener {
//    @Override
//    public void onPacketSend(PacketSendEvent event) {
//        if (event.getPacketType() != PacketType.Play.Server.SET_PASSENGERS) return;
//        if (event.isCancelled()) return;
//
//        WrapperPlayServerSetPassengers wrapperPlayServerSetPassengers = new WrapperPlayServerSetPassengers(event);
//        Player player = event.getPlayer();
//        player.sendRichMessage("<red>Passengers Got");
//    }
//    @Override
//    public void onPacketSend(PacketSendEvent event) {
//        if (event.getPacketType() != PacketType.Play.Server.SPAWN_ENTITY) return;
//        if (event.isCancelled()) return;
//
//        WrapperPlayServerSpawnEntity serverSpawnEntity = new WrapperPlayServerSpawnEntity(event);
//
//        Player viewer = event.getPlayer();
//        Entity entity = SpigotConversionUtil.getEntityById(viewer.getWorld(), serverSpawnEntity.getEntityId());
//
//        if (serverSpawnEntity.getEntityType() != EntityTypes.PLAYER) return;
//        if (!(entity instanceof Player owner)) return;
//
//        viewer.sendRichMessage("הופיע שחקן: " + owner.getName());
//
//        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
//        if (textDisplayEntity == null) throw new RuntimeException("Null owner display");
//
//        textDisplayEntity.showFor(viewer, mainHandler().configurationHandler().showForOwners(), true);
//        viewer.sendRichMessage("היה אמור להופיע...");
//    }
}