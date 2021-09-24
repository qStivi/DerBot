package de.qStivi.commands.rpg;

import de.qStivi.DB;
import de.qStivi.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

public class TradeCommand implements ICommand {
    private static final Logger logger = getLogger(TradeCommand.class);
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var user = event.getAuthor();

        if (args.length == 2){
            User tradePartner = null;
            try {
                tradePartner = event.getJDA().getUserById(args[1]);
            }catch (Exception ignored){}
            if (tradePartner != null) {
                startTrade(user, tradePartner, reply, db);
            }
        }
    }

    private void startTrade(User user, User tradePartner, Message reply, DB db) throws SQLException {
        reply.editMessage("Trading...").queue();

        var userInv = getInventoryAsEmbed(user, db);
        var tradePartnerInv = getInventoryAsEmbed(tradePartner, db);
        reply.editMessageEmbeds(userInv, tradePartnerInv).queue();
    }

    private MessageEmbed getInventoryAsEmbed(User user, DB db) throws SQLException {
        var items = db.getItems(user.getIdLong());
        var inv = new EmbedBuilder();
        inv.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        if (items.isEmpty()) {
            inv.addField("Empty!", "", false);
        } else {
            for (long itemID : items){
                var item = db.getItem(itemID);
                inv.addField(item.getDisplayName() + " (" + itemID + ")", String.valueOf(item.getPrice()), true);
            }
        }
        return inv.build();
    }

    @NotNull
    @Override
    public String getName() {
        return "trade";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "TODO";
    }

    @Override
    public long getXp() {
        return 0;
    }
}
