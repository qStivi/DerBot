//package de.qStivi.commands.rpg;
//
//import de.qStivi.DB;
//import de.qStivi.ICommand;
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.SQLException;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//public class SkillsCommand implements ICommand {
//    private long xp;
//
//    public static float getWorkMoneyMultiplier(long id) throws SQLException, ClassNotFoundException {
//        var skillPoints = DB.getInstance().getSkillPointsWorkMoney(id);
//        return (float) skillPoints / 100;
//    }
//
//    public static float getWorkXPMultiplier(long id) throws SQLException, ClassNotFoundException {
//        var skillPoints = DB.getInstance().getSkillPointsWorkXP(id);
//        return (float) skillPoints / 20;
//    }
//
//    public static float getGambleXPMultiplier(long id) throws SQLException, ClassNotFoundException {
//        var skillPoints = DB.getInstance().getSkillPointsGambleXP(id);
//        return (float) skillPoints / 10;
//    }
//
//    public static float getSocialXPPMultiplier(long id) throws SQLException, ClassNotFoundException {
//        var skillPoints = DB.getInstance().getSkillPointsSocialXP(id);
//        return (float) skillPoints / 100;
//    }
//
//    @Override
//    public void handle(MessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
//        var id = event.getAuthor().getIdLong();
//        long totalSkillPoints = db.getLevel(id);
//        db.insertSkillTreeIfNotExists(id, totalSkillPoints);
//        var spentSkillPoints = db.getSpentSkillPoints(id);
//        var availableSkillPoints = totalSkillPoints - spentSkillPoints;
//        xp = 0;
//
//        if (args.length == 1) {
//            sendStatBlock(event, totalSkillPoints, db, id, reply);
//        } else if (args.length == 2) {
//            if (args[1].equalsIgnoreCase("reset")) {
//                var money = db.getMoney(id);
//                if (money > 999999 && new Date().getTime() - db.getSkillLastReset(id) > TimeUnit.DAYS.toMillis(3)) {
//                    db.resetSkillTree(id);
//                    db.decrementMoney(1000000, id);
//                    db.setSkillLastReset(id, new Date().getTime());
//                    sendStatBlock(event, totalSkillPoints, db, id, reply);
//                } else {
//                    reply.editMessage("Nope!").queue();
//                }
//            }
//        } else if (args.length == 3) {
//            var amount = Integer.parseInt(args[2]);
//            if (amount < 0) return;
//            if (amount > availableSkillPoints) return;
//            boolean successful = false;
//            switch (Integer.parseInt(args[1])) {
//                case 1 -> {
//                    db.incrementSkillWorkMoney(id, amount);
//                    successful = true;
//                }
//                case 2 -> {
//                    db.incrementSkillWorkXP(id, amount);
//                    successful = true;
//                }
//                case 3 -> {
//                    db.incrementSkillGambleXP(id, amount);
//                    successful = true;
//                }
//                case 4 -> {
//                    db.incrementSkillSocialXP(id, amount);
//                    successful = true;
//                }
//            }
//            if (successful) {
//                db.decrementSkillPoints(id, amount);
//                sendStatBlock(event, totalSkillPoints, db, id, reply);
//            }
//        }
//
//        xp = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
//    }
//
//    private void sendStatBlock(GuildMessageReceivedEvent event, long totalSkillPoints, DB db, long id, Message reply) throws SQLException {
//        var spentSkillPoints = db.getSpentSkillPoints(id);
//        var availableSkillPoints = totalSkillPoints - spentSkillPoints;
//        var embed = new EmbedBuilder();
//        embed.setFooter("Available skill points: " + availableSkillPoints);
//        var member = event.getMember();
//        if (member == null) return;
//        embed.setAuthor(member.getEffectiveName(), null, event.getAuthor().getAvatarUrl());
//        embed.addField("Efficient worker :chart_with_upwards_trend:", "You do your work more efficiently which means you get more done. Increases your wage. (1%)", false);
//        embed.addField("Skill Points: " + db.getSkillPointsWorkMoney(id), "", false);
//        embed.addField("Attentive worker :office_worker_tone2:", "You pay more attention to what you are doing. Increases XP from working. (5%)", false);
//        embed.addField("Skill Points: " + db.getSkillPointsWorkXP(id), "", false);
//        embed.addField("Attentive gambler :slot_machine:", "You pay more attention to what you are doing. Increases XP from gambling. (10%)", false);
//        embed.addField("Skill Points: " + db.getSkillPointsGambleXP(id), "", false);
//        embed.addField("Socializer :microphone2:", "You socialize more than others. Increases XP from voice chat, chat messages, relations, and other non-gambling/RPG commands. (1%)", false);
//        embed.addField("Skill Points: " + db.getSkillPointsSocialXP(id), "", false);
//
//        reply.editMessage(embed.build()).queue();
//    }
//
//    @NotNull
//    @Override
//    public String getName() {
//        return "skills";
//    }
//
//    @NotNull
//    @Override
//    public String getDescription() {
//        return "todo";
//    }
//
//    @Override
//    public long getXp() {
//        return xp;
//    }
//}
