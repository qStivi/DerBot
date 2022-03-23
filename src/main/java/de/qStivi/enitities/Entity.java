package de.qStivi.enitities;

public abstract class Entity {

    private final String displayName;
    private final long id;

    public Entity(String displayName, long id) {
        this.displayName = displayName;
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getId() {
        return id;
    }
}
