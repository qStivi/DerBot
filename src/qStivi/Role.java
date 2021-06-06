package qStivi;

public class Role {
    private final long roleID;
    private final String emoteID;

    public Role(long roleID, String emoteID) {
        this.roleID = roleID;
        this.emoteID = emoteID;
    }

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
