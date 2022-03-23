//package de.qStivi.items;
//
//import de.qStivi.Bot;
//import de.qStivi.Category;
//import de.qStivi.DB;
//import de.qStivi.Rarity;
//import de.qStivi.commands.rpg.SkillsCommand;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
//
//import java.sql.SQLException;
//import java.util.concurrent.ThreadLocalRandom;
//
//public class XPPotionItem implements IItem {
//    @Override
//    public void use(MessageReceivedEvent event, String[] args, DB db, Message reply, User author) throws SQLException, ClassNotFoundException {
//        var randomNumber = ThreadLocalRandom.current().nextInt(50);
//        var xp = (randomNumber + (randomNumber + SkillsCommand.getGambleXPMultiplier(event.getAuthor().getIdLong())))* Bot.happyHour;
//        db.incrementXP((long) xp, event.getAuthor().getIdLong());
//        event.getChannel().sendMessage("You gained " + xp + "XP").queue();
//    }
//
//    @Override
//    public String getStaticItemName() {
//        return "XP_POTION_ITEM";
//    }
//
//    @Override
//    public String getDisplayName() {
//        return "Bottle o' Enchanting";
//    }
//
//    @Override
//    public Category getCategory() {
//        return Category.CATEGORY2;
//    }
//
//    @Override
//    public Rarity getRarity() {
//        return Rarity.UNCOMMON;
//    }
//
//    @Override
//    public long getPrice() {
//        return 100;
//    }
//
//    @Override
//    public String getDescription() {
//        return "TODO";
//    }
//}
