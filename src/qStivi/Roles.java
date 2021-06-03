package qStivi;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;

public class Roles {
    public static final long LOL = 843108657079779359L;
    public static final long MINECRAFT = 755490059976966254L;
    public static final long AMONGUS = 750014132220330016L;
    public static final long WARZONE = 846009044610580500L;
    public static final long APEX = 846010998728425472L;
    public static final long PLAYSTATION = 846784335804760079L;
    public static final long BOT = 846784745073672252L;
    public static final long GMOD = 846785053819666502L;
    public static final long SWITCH = 846785268694777896L;
    public static final long CIV = 846785388076335137L;
    public static final long XBOX = 846785641474818059L;
    public static final long ARK = 846785894525567007L;
    public static final long GTA = 846786036750614560L;
    public static final long HEARTHSTONE = 846786187653677077L;
    public static final long VR = 846786480381624410L;
    public static final long ROCKETLEAGUE = 846786687484821545L;
    JDA jda;

    public Roles(JDA jda) {
        this.jda = jda;
    }

    public Role getRole(long roleId) {
        return jda.getRoleById(roleId);
    }
}
