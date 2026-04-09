package me.stivendarsi.nameDisplay.handlers;

import me.stivendarsi.nameDisplay.utility.NameTagDisplay;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DisplayHandler {
    Map<UUID, NameTagDisplay> playerNameDisplays; // Player UUID, display

    public void load() {
        this.playerNameDisplays = new HashMap<>();
    }

    public void registerDisplay(UUID playerUUID, NameTagDisplay display) {
        this.playerNameDisplays.put(playerUUID, display);
    }

    public void unRegisterDisplay(UUID playerUUID) {
        NameTagDisplay nameTagDisplay = getPlayerNameTagDisplay(playerUUID);
        if (nameTagDisplay != null) nameTagDisplay.disableAndHideForViewers();
        this.playerNameDisplays.remove(playerUUID);
    }

    public void unRegisterDisplays() {
        for (NameTagDisplay value : this.playerNameDisplays.values()) {
            value.disableAndHideForViewers();
        }
        this.playerNameDisplays.clear();
    }

    public Map<UUID, UUID> getUUID(){
        Map<UUID, UUID> map = new HashMap<>();
        this.playerNameDisplays.forEach((uuid, nameTagDisplay) -> {
            map.put(uuid, nameTagDisplay.ownerUUID());
        });
        return map;
    }

    @Nullable
    public NameTagDisplay getPlayerNameTagDisplay(@Nullable UUID uuid){
        if (uuid == null) return null;
        return this.playerNameDisplays.getOrDefault(uuid, null);
    }


    public void showAllExistingDisplays(Player playerToShow){
        for (NameTagDisplay nameTagDisplay : this.playerNameDisplays.values()) {
                nameTagDisplay.showFor(playerToShow, false);
        }
    }
}
