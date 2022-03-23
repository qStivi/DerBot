//package de.qStivi.items;
//
//import de.qStivi.Category;
//import de.qStivi.DB;
//import de.qStivi.Rarity;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//
//import java.sql.SQLException;
//
//public interface IItem {
//    void use(MessageReceivedEvent event, String[] args, DB db, Message reply, User author) throws SQLException, ClassNotFoundException;
//
//    String getStaticItemName();
//
//    String getDisplayName();
//
//    Category getCategory();
//
//    Rarity getRarity();
//
//    long getPrice();
//
//    String getDescription();
//}
