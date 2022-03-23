package de.qStivi.enitities.pets;

import net.dv8tion.jda.api.audit.TargetType;

public class Ability {

    public final AbilityType type;
    public final double baseDamage;
    public final double speed;
    public final long delay;
    public final PetType petType;
    public final TargetType targetType;

    private Ability(AbilityType type, double baseDamage, double speed, long delay, PetType petType, TargetType targetType) {
        this.type = type;
        this.baseDamage = baseDamage;
        this.speed = speed;
        this.delay = delay;
        this.petType = petType;
        this.targetType = targetType;
    }
}
