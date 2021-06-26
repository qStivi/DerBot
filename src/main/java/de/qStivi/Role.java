package de.qStivi;

public record Role(long roleID, String emoteID) {

    public long getRoleID() {
        return roleID;
    }

    public String getEmoteID() {
        return emoteID;
    }

    public long getEmoteIDLong() {
        return Emotes.getEmoteIDLong(emoteID);
    }
}
