package de.qStivi.commands.slash;

import de.qStivi.Util;
import de.qStivi.enitities.player.Players;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WorkCommand implements ISlashCommand {

    public final static long BASE_SALARY = 10;
    public final static long BASE_DELAY_MINUTES = 1440;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(SlashCommandInteractionEvent event) throws Exception {
        var hook = event.getHook();
        var member = event.getMember();
        if (member == null) throw new NullPointerException("Error while getting member!");
        var player = Players.getPlayer(member.getIdLong());
        var skills = player.getSkills();

        var delayInMinutes = BASE_DELAY_MINUTES - (BASE_DELAY_MINUTES * (skills.getWorkSpeed()) / 100);
        var restDelay = delayInMinutes - Util.getDateDiff(player.lastWork, new Date(), TimeUnit.MINUTES);
        if (restDelay > 0) {
            hook.editOriginal("You can't work now. You can work again in " + restDelay + " minutes.").queue();
        } else {
            player.lastWork = new Date();

            var baseSalary = BASE_SALARY + player.getLevel();
            var salary = baseSalary + (baseSalary * (skills.getWorkEfficiency() / 100));

            player.setMoney(player.getMoney() + salary);
            player.setXp(player.getXp() + 1);
            hook.editOriginal("+" + salary + ":gem:").queue();
        }
    }

    @Override
    public @NotNull CommandData getCommand() {
        return Commands.slash(getName(), getDescription());
    }

    @Override
    public @NotNull String getName() {
        return "work";
    }

    @Override
    public @NotNull String getDescription() {
        return "You can work once a day to earn some money.";
    }
}
