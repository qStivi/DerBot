package main.java.qStivi.commands.rpg.slots;

public record Symbol(float weight, String emote, double multiplier) {

    public float getWeight() {
        return weight;
    }

    public String getEmote() {
        return emote;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
