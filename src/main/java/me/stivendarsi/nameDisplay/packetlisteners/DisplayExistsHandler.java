package me.stivendarsi.nameDisplay.packetlisteners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import me.stivendarsi.nameDisplay.utility.NameTagDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class DisplayExistsHandler implements PacketListener {
//    @Override
//    public void onPacketReceive(PacketReceiveEvent event) {
//        if (event.getPacketType() != PacketType.Play.Client.PLAYER_LOADED) return;
//        Player owner = event.getPlayer();
//
//        NameTagDisplay nameTagDisplay = mainHandler().displayHandler().getPlayerNameTagDisplay(owner.getUniqueId());
//        if (nameTagDisplay == null) {
//            nameTagDisplay = new NameTagDisplay(owner.getUniqueId());
//            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), nameTagDisplay);
//        }
//        nameTagDisplay.startIfNeed();
//        if (mainHandler().configurationHandler().showForOwners()) nameTagDisplay.showFor(event.getPlayer(), true);
//    }

    @Override
    public void onUserLogin(UserLoginEvent event) {
        Player owner = event.getPlayer();

        NameTagDisplay nameTagDisplay = mainHandler().displayHandler().getPlayerNameTagDisplay(owner.getUniqueId());
        if (nameTagDisplay == null) {
            nameTagDisplay = new NameTagDisplay(owner.getUniqueId());
            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), nameTagDisplay);
        }
        nameTagDisplay.startTextUpdating();
        if (mainHandler().configurationHandler().showForOwners()) nameTagDisplay.showFor(event.getPlayer(), true);
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        UUID uuid = event.getUser().getUUID();
        NameTagDisplay nameTagDisplay = mainHandler().displayHandler().getPlayerNameTagDisplay(uuid);
        if (nameTagDisplay == null) return;
      //  nameTagDisplay.disableTextUpdate();
        mainHandler().displayHandler().unRegisterDisplay(uuid);
    }
}
