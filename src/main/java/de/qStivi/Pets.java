package de.qStivi;

public class Pets {
    private int ID;
    private String name;
    private int level;
    private Sex sex;
    private Type type;

    private int maxHealth;
    private int maxMana;
    private int maxStamina;
    private int maxFood;
    private int maxHappiness;

    private int health;
    private int mana;
    private int stamina;
    private int food;
    private int happiness;

    public Pets(String name, int level, Sex sex, Type type, int maxHealth, int maxMana, int maxStamina, int maxFood, int maxHappiness, int health, int mana, int stamina, int food, int happiness) {
        this.name = name;
        this.level = level;
        this.sex = sex;
        this.type = type;
        this.maxHealth = maxHealth;
        this.maxMana = maxMana;
        this.maxStamina = maxStamina;
        this.maxFood = maxFood;
        this.maxHappiness = maxHappiness;
        this.health = health;
        this.mana = mana;
        this.stamina = stamina;
        this.food = food;
        this.happiness = happiness;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public void setMaxStamina(int maxStamina) {
        this.maxStamina = maxStamina;
    }

    public int getMaxFood() {
        return maxFood;
    }

    public void setMaxFood(int maxFood) {
        this.maxFood = maxFood;
    }

    public int getMaxHappiness() {
        return maxHappiness;
    }

    public void setMaxHappiness(int maxHappiness) {
        this.maxHappiness = maxHappiness;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }
}
