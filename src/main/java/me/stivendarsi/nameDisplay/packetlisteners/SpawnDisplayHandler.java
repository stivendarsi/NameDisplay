package me.stivendarsi.nameDisplay.packetlisteners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import me.stivendarsi.nameDisplay.utility.NameTagDisplay;
import org.bukkit.entity.Player;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class SpawnDisplayHandler implements PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.PLAYER_LOADED) return;
        Player owner = event.getPlayer();

        NameTagDisplay nameTagDisplay = mainHandler().displayHandler().getPlayerNameTagDisplay(owner.getUniqueId());
        if (nameTagDisplay == null) {
            nameTagDisplay = new NameTagDisplay(owner.getUniqueId());
            mainHandler().displayHandler().registerDisplay(owner.getUniqueId(), nameTagDisplay);
        }
        nameTagDisplay.startIfNeed();
        if (mainHandler().configurationHandler().showForOwners()) nameTagDisplay.showFor(event.getPlayer(), false);
    }
}
