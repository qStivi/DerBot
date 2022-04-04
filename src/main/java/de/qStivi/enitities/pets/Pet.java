package de.qStivi.enitities.pets;

import de.qStivi.enitities.Entity;

import java.util.ArrayList;
import java.util.Date;

public class Pet extends Entity {

    private final PetType[] types;
    private final Date age;
    private final Stats baseStats;
    private final Stats trainingStats;
    private final ArrayList<StatusEffect> statusEffects;
    private final ArrayList<Ability> abilities;
    private double xp;
    private double currentHealth;

    public Pet(String displayName, long id, PetType[] types, Date age, double xp, Stats baseStats, Stats trainingStats, ArrayList<StatusEffect> statusEffects, ArrayList<Ability> abilities) {
        super(displayName, id);
        this.types = types;
        this.age = age;
        this.xp = xp;
        this.baseStats = baseStats;
        this.trainingStats = trainingStats;
        this.statusEffects = statusEffects;
        this.abilities = abilities;
        this.currentHealth = getMaxHealth();
    }

    public double getMaxHealth() {
        return 0;
    }

    public PetType[] getTypes() {
        return types;
    }

    public Date getAge() {
        return age;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
    }

    public Stats getBaseStats() {
        return baseStats;
    }

    public Stats getTrainingStats() {
        return trainingStats;
    }

    public ArrayList<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
    }
}
