package qStivi.commands.music;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.hc.core5.http.ParseException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import qStivi.DB;
import qStivi.ICommand;
import qStivi.apis.Spotify;
import qStivi.apis.YouTube;
import qStivi.audioManagers.PlayerManager;
import qStivi.commands.rpg.SkillsCommand;
import qStivi.listeners.ControlsManager;

import java.io.IOException;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static qStivi.Util.isValidLink;
import static qStivi.commands.JoinCommand.join;

public class PlayCommand implements ICommand {

    private static final Logger logger = getLogger(PlayCommand.class);
    long xp = 0;

    public static String cleanForURL(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFKD);
        str = str.replaceAll("[^a-z0-9A-Z +-]", ""); // Remove all non valid chars
        str = str.replaceAll(" {2}", " ").trim(); // convert multiple spaces into one space
        str = str.replaceAll(" ", "+"); // //Replace spaces by dashes
        return str;
    }

    public String playSong(String[] args, boolean shuffle, TextChannel channel, Guild guild, Message reply) throws ParseException, SpotifyWebApiException, IOException {


        StringBuilder query = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            query.append(arg).append(" ");
        }
        var song = query.toString().trim();

        RequestType requestType = getRequestType(song);

        if (requestType == RequestType.YOUTUBE) {
            YoutubeType youtubeType = getYouTubeType(song);
            if (youtubeType != null) {
                switch (youtubeType) {
                    case TRACK -> song = playYoutubeTrack(song, guild);
                    case PLAYLIST -> song = playYoutubePlaylist(song, shuffle, channel);
                }
            }
        } else if (requestType == RequestType.SPOTIFY) {
            SpotifyType spotifyType = getSpotifyType(song);
            if (spotifyType != null) {
                switch (spotifyType) {
                    case TRACK -> song = playSpotifyTrack(song, channel);
                    case PLAYLIST -> song = playSpotifyPlaylist(song, shuffle, channel);
                    case ALBUM -> song = playSpotifyAlbum(song, shuffle);
                    case ARTIST -> song = playSpotifyArtist(song, shuffle);
                }
            }
        } else if (requestType == RequestType.SEARCH) {
            song = searchPlay(song, channel);
        }
        if (song != null) ControlsManager.getINSTANCE().sendMessage(reply, guild);

        return song;
    }

    private String playSpotifyArtist(String arg0, Boolean shuffle) {

        logger.info(arg0 + shuffle);
        logger.error("NOT YET IMPLEMENTED!");
        return null;
    }

    private String playSpotifyAlbum(String arg0, Boolean shuffle) {

        logger.info(arg0 + shuffle);
        logger.error("NOT YET IMPLEMENTED!");
        return null;
    }

    private String playSpotifyPlaylist(String arg0, Boolean shuffle, TextChannel channel) throws IOException, ParseException, SpotifyWebApiException {

        String id = null;
        if (arg0.startsWith("spotify:playlist:")) {
            id = arg0.split(":")[2];
        }
        if (arg0.startsWith("open.spotify.com/playlist/")) {
            var temp = arg0.split("/")[2];
            id = temp.split("\\?")[0];
        }
        if (arg0.startsWith("https://open.spotify.com/playlist/")) {
            var temp = arg0.split("/")[4];
            id = temp.split("\\?")[0];
        }

        var playlist = new Spotify().getPlaylist(id);
        if (shuffle) Collections.shuffle(playlist);
        for (String link : playlist) {
            searchPlay(link, channel);
        }
        return arg0;
    }

    private String playSpotifyTrack(String link, TextChannel channel) throws IOException, ParseException, SpotifyWebApiException {

        if (link.contains("open.spotify.com/track/")) {
            link = link.replace("https://", "");
            link = link.split("/")[2];
            link = link.split("\\?")[0];
        } else if (link.startsWith("spotify:track:")) {
            link = link.split(":")[2];
        }

        Spotify spotify = new Spotify();
        String search = spotify.getTrackArtists(link) + " " + spotify.getTrackName(link);
        search = searchPlay(search, channel);
        return search;
    }

    private SpotifyType getSpotifyType(String link) {
        if (link.contains("track")) {
            return SpotifyType.TRACK;
        } else if (link.contains("playlist")) {
            return SpotifyType.PLAYLIST;
        } else if (link.contains("album")) {
            return SpotifyType.ALBUM;
        } else if (link.contains("artist") || link.contains("\uD83E\uDDD1\u200D\uD83C\uDFA8")) {
            return SpotifyType.ARTIST;
        }
        return null;
    }

    private String playYoutubePlaylist(String link, Boolean randomizeOrder, TextChannel channel) throws IOException {

        List<String> ids = YouTube.getPlaylistItemsByLink(link);
        if (randomizeOrder) Collections.shuffle(ids);
        for (String id : ids) {
            PlayerManager.getINSTANCE().loadAndPlay(channel.getGuild(), "https://youtu.be/" + id);
        }
        channel.sendMessage("Added " + ids.size() + " songs to the queue.").queue();
        return link;
    }

    private String playYoutubeTrack(String url, Guild guild) {
        PlayerManager.getINSTANCE().loadAndPlay(guild, url);
        return url;
    }

    private YoutubeType getYouTubeType(String link) {
        if (link.contains("youtube.com/watch?v=") || link.contains("youtu.be/")) {
            return YoutubeType.TRACK;
        } else if (link.contains("youtube.com/playlist?list=")) {
            return YoutubeType.PLAYLIST;
        }
        logger.error("Something went wrong!");
        return null;
    }

    private RequestType getRequestType(String arg0) {
        if (isValidLink(arg0)) {
            if (arg0.contains("youtube") || arg0.contains("youtu.be")) {
                return RequestType.YOUTUBE;
            } else if (arg0.contains("spotify")) {
                return RequestType.SPOTIFY;
            }
        } else {
            return RequestType.SEARCH;
        }
        logger.error("Something went wrong!");
        return null;
    }

    private String searchPlay(String search, TextChannel channel) throws IOException {
        search = cleanForURL(search);
        String id = YouTube.getVideoIdBySearchQuery(search);
        String link = "https://youtu.be/" + id;
        PlayerManager.getINSTANCE().loadAndPlay(channel.getGuild(), link);
        return link;
    }

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) {
        xp = 0;

        if (!join(event.getGuild(), event.getAuthor())) {
            reply.editMessage("Please join a channel, so I can play your request.").queue();
            return;
        }
        try {
//            if (event.getOption("shuffle") != null) {
//                if (event.getOption("shuffle").getAsBoolean()) {
//                    hook.sendMessage(playSong(event.getOption("query").getAsString(), true, event.getTextChannel(), event.getGuild())).queue();
//                }
//            } else {
            var msg = playSong(args, false, event.getChannel(), event.getGuild(), reply);
            if (msg != null) {
                reply.editMessage(msg).queue();

                xp = 30 + (long) (30 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
            } else {

                reply.editMessage("Something went wrong!").queue();
            }
//            }
        } catch (ParseException | SpotifyWebApiException | IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            reply.editMessage("Something went wrong :(").queue();
        }
    }

    @NotNull
    @Override
    public String getName() {
        return "play";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Plays music.";
    }

    @Override
    public long getXp() {
        return xp;
    }

    private enum RequestType {
        YOUTUBE,
        SPOTIFY,
        SEARCH
    }

    private enum YoutubeType {
        TRACK,
        PLAYLIST
    }

    private enum SpotifyType {
        TRACK,
        PLAYLIST,
        ALBUM,
        ARTIST
    }
}
