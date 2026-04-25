package me.stivendarsi.nameDisplay.shakeit;

public class ShakeHandler {
    private final ShakeDisplaySet shakeDisplaySet;

    public ShakeHandler() {
        this.shakeDisplaySet = new ShakeDisplaySet();
    }

    public ShakeDisplaySet shakeDisplaySet() {
        return shakeDisplaySet;
    }
}
