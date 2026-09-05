package me.stivendarsi.nameDisplay.packetlisteners;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import me.stivendarsi.nameDisplay.NameDisplay;
import me.stivendarsi.nameDisplay.utility.TextDisplayEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class RegisterDisplayHandlerHandler implements PacketListener {

    @Override
    public void onUserLogin(UserLoginEvent event) {
        Player owner = event.getPlayer();

        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) {
            textDisplayEntity = new TextDisplayEntity(owner.getUniqueId());
            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), textDisplayEntity);
        }
        textDisplayEntity.startTextUpdating();
     //   textDisplayEntity.showForAsync(event.getPlayer(), mainHandler().configurationHandler().showForOwners());
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        UUID uuid = event.getUser().getUUID();
        mainHandler().displayHandler().unRegisterDisplay(uuid);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.TELEPORT_CONFIRM) return;
        Player owner = event.getPlayer();

        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) return;
        owner.getScheduler().runDelayed(NameDisplay.nameDisplay(), task -> {
            textDisplayEntity.showForAsync(owner, mainHandler().configurationHandler().visibleForOwners());
        }, null, 1L);
    }
}