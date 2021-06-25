package de.qStivi;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class Config {

    private static final Logger logger = getLogger(Config.class);

    public static String get(String key) {
        String result = "";

        File f = new File(System.getProperty("user.dir") + "\\.env");

        Dotenv DOTENV;

        if (f.exists() && !f.isDirectory()) {
            DOTENV = Dotenv.load();
            result = DOTENV.get(key);
        } else {
            boolean fileCreated;
            try {
                fileCreated = f.createNewFile();
                if (fileCreated) {
                    FileWriter fw = new FileWriter(f);
                    fw.write("# Discord Stuff");
                    fw.write(System.lineSeparator());
                    fw.write("TOKEN=Your bot token goes here");
                    fw.write(System.lineSeparator());
                    fw.write("DEV_TOKEN=Your bot token goes here");
                    fw.write(System.lineSeparator());
                    fw.write("PREFIX=Your command prefix goes here");
                    fw.write(System.lineSeparator());
                    fw.write("GUILD_ID=Your Guild ID");
                    fw.write(System.lineSeparator());
                    fw.write("CATEGORY_ID=The bot channel category");
                    fw.write(System.lineSeparator());
                    fw.write("CHANNEL_ID=Bot channel ID goes here");
                    fw.write(System.lineSeparator());
                    fw.write("DEV_CHANNEL_ID=DevBot channel ID goes here");
                    fw.write(System.lineSeparator());
                    fw.write("DEV_VOICE_CHANNEL_ID=DevBot channel ID goes here");
                    fw.write(System.lineSeparator());
                    fw.write("DEV_MODE=false");
                    fw.write(System.lineSeparator());
                    fw.write(System.lineSeparator());
                    fw.write("# Spotify Stuff");
                    fw.write(System.lineSeparator());
                    fw.write("SPOTIFY_ID=Spotify ID goes here");
                    fw.write(System.lineSeparator());
                    fw.write("SPOTIFY_SECRET=Spotify secret goes here");
                    fw.write(System.lineSeparator());
                    fw.write(System.lineSeparator());
                    fw.write("# Reddit Stuff");
                    fw.write(System.lineSeparator());
                    fw.write("REDDIT_ID=Reddit ID goes here");
                    fw.write(System.lineSeparator());
                    fw.write("REDDIT_SECRET=Reddit secret goes here");
                    fw.write(System.lineSeparator());
                    fw.write("REDDIT_PASSWORD=Reddit password goes here");
                    fw.write(System.lineSeparator());
                    fw.write(System.lineSeparator());
                    fw.write("# Youtube Stuff");
                    fw.write(System.lineSeparator());
                    fw.write("YOUTUBE_KEY=Youtube API key goes here");
                    fw.write(System.lineSeparator());
                    fw.close();
                    logger.error("Please configure your data!");
                    System.exit(0);
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        return result;
    }

}
