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

    private Pet(String displayName, long id, PetType[] types, Date age, double xp, Stats baseStats, Stats trainingStats, ArrayList<StatusEffect> statusEffects, ArrayList<Ability> abilities) {
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

    private double getMaxHealth() {
        return 0;
    }

    private PetType[] getTypes() {
        return types;
    }

    private Date getAge() {
        return age;
    }

    private double getXp() {
        return xp;
    }

    private void setXp(double xp) {
        this.xp = xp;
    }

    private double getCurrentHealth() {
        return currentHealth;
    }

    private void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
    }

    private Stats getBaseStats() {
        return baseStats;
    }

    private Stats getTrainingStats() {
        return trainingStats;
    }

    private ArrayList<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    private ArrayList<Ability> getAbilities() {
        return abilities;
    }
}
