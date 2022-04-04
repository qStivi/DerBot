package de.qStivi.commands.slash.pets;

import com.oblac.nomen.Nomen;
import de.qStivi.DB;
import de.qStivi.commands.slash.ISlashCommand;
import de.qStivi.enitities.pets.Pet;
import de.qStivi.enitities.pets.PetType;
import de.qStivi.enitities.pets.Stats;
import de.qStivi.enitities.pets.StatusEffect;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class StarterCommand implements ISlashCommand {

    @Override
    public void handle(SlashCommandInteractionEvent event) throws Exception {
        var id = DB.getSmallestFreeID("Pets");
        var fireBaseStats = new Stats(10, 20, 20, 5, 10, 5);
        var waterBaseStats = new Stats(15, 10, 10, 10, 20, 10);
        var earthBaseStats = new Stats(20, 5, 10, 20, 5, 20);
        var fire = new Pet(Nomen.est().color().adjective().superhero().get(), id, new PetType[]{PetType.FIRE}, new Date(), 0, fireBaseStats, fireTrainingStats, new StatusEffect(0, new Stats(0, 0, 0, 0, 0, 0)), fireAbilities);
    }

    @Override
    public @NotNull CommandData getCommand() {
        return Commands.slash(getName(), getDescription());
    }

    @Override
    public @NotNull String getName() {
        return "starter";
    }

    @Override
    public @NotNull String getDescription() {
        return "Get yourself a starter Pet.";
    }
}
