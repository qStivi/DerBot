package de.qStivi.enitities.pets;

public class StatusEffect {

    public final double globalDamageModifier;
    public final Stats mofier;

    public StatusEffect(double globalDamageModifier, Stats mofier) {
        this.globalDamageModifier = globalDamageModifier;
        this.mofier = mofier;
    }
}
