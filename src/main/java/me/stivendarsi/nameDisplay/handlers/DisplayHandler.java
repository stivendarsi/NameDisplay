package me.stivendarsi.nameDisplay.handlers;

import me.stivendarsi.nameDisplay.utility.TextDisplayEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.stivendarsi.nameDisplay.NameDisplay.mainHandler;

public class DisplayHandler {
    Map<UUID, TextDisplayEntity> playerNameDisplays; // Player UUID, display

    public void load() {
        this.playerNameDisplays = new HashMap<>();
    }

    public void registerDisplay(UUID playerUUID, TextDisplayEntity display) {
        this.playerNameDisplays.put(playerUUID, display);
    }

    public void unRegisterDisplay(UUID playerUUID) {
        TextDisplayEntity textDisplayEntity = getPlayerTextDisplay(playerUUID);
        if (textDisplayEntity != null) textDisplayEntity.disableAndHideForViewers();
        this.playerNameDisplays.remove(playerUUID);
    }

    public void unRegisterDisplays() {
        for (TextDisplayEntity value : this.playerNameDisplays.values()) {
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
    public TextDisplayEntity getPlayerTextDisplay(@Nullable UUID uuid){
        if (uuid == null) return null;
        return this.playerNameDisplays.getOrDefault(uuid, null);
    }


    public void showAllExistingDisplays(Player playerToShow){
        for (TextDisplayEntity textDisplayEntity : this.playerNameDisplays.values()) {
                textDisplayEntity.showFor(playerToShow, mainHandler().configurationHandler().showForOwners());
        }
    }
}
