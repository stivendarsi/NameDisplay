package me.stivendarsi.nameDisplay.events;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import me.stivendarsi.nameDisplay.utility.NameTagDisplay;
import org.bukkit.entity.Player;

public class UserLoginPacketHandler implements PacketListener {
    @Override
    public void onUserLogin(UserLoginEvent event) {
         Player player = event.getPlayer();
         NameTagDisplay nameTagDisplay = new NameTagDisplay();
         nameTagDisplay.spawn(player);
    }

}
