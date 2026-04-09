package me.stivendarsi.nameDisplay.utility;

import com.github.retrooper.packetevents.util.Vector3f;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class DisplayProperties {
    private final int backgroundOpacity;
    private final byte textOpacity;
    private final Vector3f translation;
    private final byte billboard;


    public DisplayProperties(ConfigurationSection section) {
        int opacity = section.getInt("opacity", 100);
        this.textOpacity = (byte) (opacity > 127 ? opacity - 256 : opacity);
        this.backgroundOpacity = Color.fromARGB(section.getInt("background-opacity", 63), 0, 0, 0).asARGB();

        this.billboard = (byte) section.getInt("billboard", 3);

        List<Float> f = section.getFloatList("translation");
        if (f.isEmpty()) f = List.of(0f,0.25f,0f);
        this.translation = new Vector3f(f.getFirst(), f.get(1), f.get(2));
    }

    public int backgroundOpacity() {
        return backgroundOpacity;
    }

    public byte textOpacity() {
        return textOpacity;
    }

    public Vector3f translation() {
        return translation;
    }

    public byte billboard() {
        return billboard;
    }
}
