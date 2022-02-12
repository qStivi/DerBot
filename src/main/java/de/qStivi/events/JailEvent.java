//package de.qStivi.events;
//
//import de.qStivi.DB;
//import de.qStivi.items.GetOutOfJailFreeCardItem;
//import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
//
//import java.sql.SQLException;
//import java.util.Date;
//
//public class JailEvent implements IEvent {
//    @Override
//    public void execute(GuildMessageReceivedEvent event, DB db, User author) throws SQLException {
//        if (db.getUniqueItemIDs(author.getIdLong()).isEmpty()) {
//            event.getMessage().reply("Go to jail. Go *directly* to jail. Do not pass go. Do not collect any money.").queue();
//            db.setLastJail(new Date().getTime(), event.getAuthor().getIdLong());
//        }
//        db.getUniqueItemIDs(author.getIdLong()).forEach(aLong -> {
//            try {
//                if (db.getItem(aLong).getStaticItemName().equals(new GetOutOfJailFreeCardItem().getStaticItemName())) {
//                    db.removeItem(author.getIdLong(), aLong);
//                    db.setLastJail(0, author.getIdLong());
//                    event.getMessage().reply("You have been freed.").queue();
//                } else {
//                    event.getMessage().reply("Go to jail. Go *directly* to jail. Do not pass go. Do not collect any money.").queue();
//                    db.setLastJail(new Date().getTime(), event.getAuthor().getIdLong());
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//}
