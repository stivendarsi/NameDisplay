package me.stivendarsi.nameDisplay.packetlisteners;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeConstant;
import com.github.retrooper.packetevents.wrapper.PacketTypeData;
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
        textDisplayEntity.showFor(event.getPlayer(), mainHandler().configurationHandler().showForOwners());
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        UUID uuid = event.getUser().getUUID();
        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(uuid);
        if (textDisplayEntity == null) return;
        mainHandler().displayHandler().unRegisterDisplay(uuid);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.TELEPORT_CONFIRM) return;
        Player owner = event.getPlayer();
        owner.sendRichMessage("שוגר");

        System.out.println("AAHAHHAHA");

        TextDisplayEntity textDisplayEntity = mainHandler().displayHandler().getPlayerTextDisplay(owner.getUniqueId());
        if (textDisplayEntity == null) return;
        owner.getScheduler().runDelayed(NameDisplay.nameDisplay(), task -> {
            textDisplayEntity.showFor(owner, mainHandler().configurationHandler().showForOwners());
        }, null, 1L);
    }
}