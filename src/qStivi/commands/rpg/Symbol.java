package qStivi.commands.rpg;

public class Symbol {
    private final float weight;
    private final String emote;
    private final double multiplier;

    public Symbol(float weight, String emote, double multiplier) {
        this.weight = weight;
        this.emote = emote;
        this.multiplier = multiplier;
    }

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
