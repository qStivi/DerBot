//package de.qStivi;
//
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//
//import javax.annotation.Nonnull;
//import java.sql.SQLException;
//
//public interface ICommand {
//    void handle(MessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException;
//
//    @Nonnull
//    String getName();
//
//    @Nonnull
//    String getDescription();
//
//    long getXp();
//}
