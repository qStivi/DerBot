package qStivi.commands.rpg;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import qStivi.ICommand;
import qStivi.db.DB;

import java.sql.SQLException;

public class SkillsCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
        var db = new DB();
        var id = event.getAuthor().getIdLong();
        db.insertSkillTreeIfNotExists(id);
        var spentSkillPoints = db.getSpentSkillPoints(id);
        long totalSkillPoints = db.getXP(id) / 800;
        var availableSkillPoints = totalSkillPoints - spentSkillPoints;
        var channel = event.getChannel();

        if (args.length == 1) {
            var embed = new EmbedBuilder();
            embed.setFooter("Available skill points: " + availableSkillPoints);
            embed.setAuthor(event.getMember().getEffectiveName(), null, event.getAuthor().getAvatarUrl());
            embed.addField("Efficient worker :chart_with_upwards_trend:", "You do your work more efficiently which means you get more done. Increases your wage. (1%)", false);
            embed.addField("Skill Points: " + db.getSkillPointsWorkMoney(id), "", false);
            embed.addField("Attentive worker :office_worker_tone2:", "You pay more attention to what you are doing. Increases XP from working. (4%)", false);
            embed.addField("Skill Points: " + db.getSkillPointsWorkXP(id), "", false);
            embed.addField("Attentive gambler :slot_machine:", "You pay more attention to what you are doing. Increases XP from gambling. (10%)", false);
            embed.addField("Skill Points: " + db.getSkillPointsGambleXP(id), "", false);
            embed.addField("Socializer :microphone2:", "You socialize more than others. Increases XP from voice chat, chat messages, relations, and other non-gambling/RPG commands. (1%)", false);
            embed.addField("Skill Points: " + db.getSkillPointsSocialXP(id), "", false);

            channel.sendMessage(embed.build()).queue();
        } else if (args.length == 2) {
            if (args[1].equalsIgnoreCase("reset")) {
                var money = db.getMoney(id);
                if (money > 999999) {
                    db.resetSkillTree(id);
                }
            }
        } else if (args.length == 3) {
            var amount = Integer.parseInt(args[2]);
            if (amount > availableSkillPoints) return;
            boolean successful = false;
            switch (Integer.parseInt(args[1])) {
                case 1:
                    db.incrementSkillWorkMoney(id, amount);
                    successful = true;
                    break;
                case 2:
                    db.incrementSkillWorkXP(id, amount);
                    successful = true;
                    break;
                case 3:
                    db.incrementSkillGambleXP(id, amount);
                    successful = true;
                    break;
                case 4:
                    db.incrementSkillSocialXP(id, amount);
                    successful = true;
                    break;
            }
            if (successful) {
                db.decrementSkillPoints(id, amount);
            }
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "skills";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "todo";
    }

    @Override
    public long getXp() {
        return 0;
    }
}
