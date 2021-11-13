package de.qStivi.commands.rpg.pets;

import com.oblac.nomen.Nomen;
import de.qStivi.DB;
import de.qStivi.ICommand;
import de.qStivi.Pet;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Random;

public class FindPetCommand implements ICommand {
    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException, InterruptedException {
        var r = new Random();
        var sex = Pet.Sex.FEMALE;
        var type = Pet.Type.AIR;
        if (r.nextInt(2) == 1) sex = Pet.Sex.MALE;
        switch (r.nextInt(10)) {
            case 1 -> type = Pet.Type.CRYSTAL;
            case 2 -> type = Pet.Type.FIRE;
            case 3 -> type = Pet.Type.EARTH;
            case 4 -> type = Pet.Type.ELECTRIC;
            case 5 -> type = Pet.Type.GHOST;
            case 6 -> type = Pet.Type.GRASS;
            case 7 -> type = Pet.Type.ICE;
            case 8 -> type = Pet.Type.PSYCHIC;
            case 9 -> type = Pet.Type.WATER;
        }
        var pet = new Pet(Nomen.randomName(), r.nextInt(50), sex, type, r.nextInt(50), r.nextInt(50), r.nextInt(50), r.nextInt(50), r.nextInt(50));
        reply.editMessage("Pet").queue();
        var embed = new EmbedBuilder();
        embed.setTitle(pet.getName());
        embed.addField("Level", String.valueOf(pet.getLevel()), false);
        embed.addField("Health", String.valueOf(pet.getHealth()), false);
        embed.addField("Mana", String.valueOf(pet.getMana()), false);
        embed.addField("Stamina", String.valueOf(pet.getStamina()), false);
        embed.addField("Food", String.valueOf(pet.getFood()), false);
        embed.addField("Happiness", String.valueOf(pet.getHappiness()), false);
        embed.setFooter("Sex: " + pet.getSex() + " | Type: " + pet.getType());
        reply.editMessageEmbeds(embed.build())
                .setActionRow(
                        Button.primary("catchPet " + pet.getID(), "Catch")
                ).queue();
    }

    @NotNull
    @Override
    public String getName() {
        return "fp";
    }

    @NotNull
    @Override
    public String getDescription() {
        return ":notes: I play PokemonGO every day :notes:";
    }

    @Override
    public long getXp() {
        return 0;
    }
}
