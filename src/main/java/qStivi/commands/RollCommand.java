package qStivi.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import qStivi.ICommand;
import qStivi.commands.rpg.SkillsCommand;
import qStivi.DB;

import javax.annotation.Nonnull;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RollCommand implements ICommand {

    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        var rollInput = args[1];
        xp = 0;

        if (rollInput.equals("stats")) {
            reply.editMessage(statsRoll()).queue();
        } else {
            try {
                var result = normalRoll(rollInput);
                if (result == null) {
                    reply.editMessage("This is not a valid roll!\nExample: `6d8`").queue();
                    return;
                }
                reply.editMessage(result).queue();
                reply.editMessage("Roll").queue();
            }catch (OutOfMemoryError e){
                reply.editMessage("Your roll is too heavy!").queue();
            }
        }

        xp = 15 + (long) (15 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
    }


    MessageEmbed normalRoll(String query) throws OutOfMemoryError{
        String[] input = query.split("d");
        long numOfDice;
        int numOfSides;
        try {
            numOfDice = Integer.parseInt(input[0]);
            numOfSides = Integer.parseInt(input[1]);
        } catch (Exception e) {
            return null;
        }

        List<Long> rolls = new ArrayList<>();
        long sum = 0;

        for (int i = 0; i < numOfDice; i++) {
            long rand = ThreadLocalRandom.current().nextInt(1, numOfSides + 1);
            rolls.add(rand);
            sum += rand;
        }

        float mean = (float) sum / numOfDice;

        EmbedBuilder embed = new EmbedBuilder().setDescription("∑=" + sum + " | Ø=" + mean);

        if (rolls.stream().filter(integer -> integer == 1).count() > rolls.stream().filter(integer -> integer == numOfSides).count()) {
            embed.setColor(Color.red);
        } else if (rolls.stream().filter(integer -> integer == 1).count() < rolls.stream().filter(integer -> integer == numOfSides).count()) {
            embed.setColor(Color.green);
        } else if (rolls.containsAll(List.of(1L, (long) numOfSides)) && rolls.stream().filter(integer -> integer == 1).count() == rolls.stream().filter(integer -> integer == numOfSides).count()) {
            embed.setColor(Color.yellow);
        }

        if (numOfDice > 25) {
            embed.setFooter("Notice: Not all Dice could be displayed!");
        }

        for (long roll : rolls) {
            embed.addField("", String.valueOf(roll), true); // Embeds can hold a max. of 25 Fields so this will just stop after 25 Fields for now.
            if (embed.getFields().size() > 25) {
                break;
            }
        }

        return embed.build();
    }

    MessageEmbed statsRoll() {

        // 6*(2d6+6)

        List<Long> rolls = new ArrayList<>();
        long sum = 0;

        for (long i = 0; i < 6; i++) {
            long rand = ThreadLocalRandom.current().nextInt(1, 7);
            long rand2 = ThreadLocalRandom.current().nextInt(1, 7);
            rand += rand2;
            rand += 6;
            rolls.add(rand);
            sum += rand;
        }

        EmbedBuilder embed = new EmbedBuilder().setDescription("∑=" + sum);

        for (long roll : rolls) {
            embed.addField("", String.valueOf(roll), true);
        }

        return embed.build();

    }

    @Override
    public @Nonnull
    String getName() {
        return "roll";
    }

    @Override
    public @Nonnull
    String getDescription() {
        return "Clickety-Clackety, I roll to attackety";
    }

    @Override
    public long getXp() {
        return xp;
    }
}
