package qStivi.commands;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import qStivi.apis.Spotify;
import qStivi.apis.YouTube;
import qStivi.audioManagers.PlayerManager;
import qStivi.listeners.ControlsManager;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.slf4j.LoggerFactory.getLogger;

class PlayCommandTest {
    private static final Logger logger = getLogger(PlayCommandTest.class);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handle() {
        assertTrue(isValidLink("https://open.spotify.com/track/5f2TWu6R2YYCJtLQ0fP78H?si=ejJXh7GYSr-nC_d8hGVrwg"));
        assertTrue(isValidLink("spotify:track:3nQlah7tXh51pled8W5eEP"));
        assertTrue(isValidLink("https://www.youtube.com/watch?v=7qKG3YnRr8U"));
        assertFalse(isValidLink("soda city funk"));

        assertEquals(RequestType.SEARCH, getRequestType("soda city funk"));
        assertEquals(RequestType.YOUTUBE, getRequestType("https://www.youtube.com/watch?v=7qKG3YnRr8U"));
        assertEquals(RequestType.SPOTIFY, getRequestType("spotify:track:3nQlah7tXh51pled8W5eEP"));
        assertEquals(RequestType.SPOTIFY, getRequestType("https://open.spotify.com/track/5f2TWu6R2YYCJtLQ0fP78H?si=ejJXh7GYSr-nC_d8hGVrwg"));

        assertEquals(SpotifyType.TRACK, getSpotifyType("https://open.spotify.com/track/5f2TWu6R2YYCJtLQ0fP78H?si=ejJXh7GYSr-nC_d8hGVrwg"));
        assertEquals(SpotifyType.TRACK, getSpotifyType("spotify:track:3nQlah7tXh51pled8W5eEP"));

        logger.info("https://open.spotify.com/track/5f2TWu6R2YYCJtLQ0fP78H?si=ejJXh7GYSr-nC_d8hGVrwg");
        logger.info("spotify:track:3nQlah7tXh51pled8W5eEP");
        logger.info("https://www.youtube.com/watch?v=7qKG3YnRr8U");
    }

    String cleanForURL(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFKD);
        str = str.replaceAll("[^a-z0-9A-Z -]", ""); // Remove all non valid chars
        str = str.replaceAll(" {2}", " ").trim(); // convert multiple spaces into one space
        str = str.replaceAll(" ", "+"); // //Replace spaces by dashes
        return str;
    }

    String playSong(String[] args, boolean shuffle, TextChannel channel, Guild guild) throws ParseException, SpotifyWebApiException, IOException {

        StringBuilder query = new StringBuilder();
        for (String arg: args){
            query.append(arg).append(" ");
        }
        var song = query.toString();


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
                    case PLAYLIST -> song = playSpotifyPlaylist(song, shuffle);
                    case ALBUM -> song = playSpotifyAlbum(song, shuffle);
                    case ARTIST -> song = playSpotifyArtist(song, shuffle);
                }
            }
        } else if (requestType == RequestType.SEARCH) {
            song = searchPlay(song, channel);
        }
        if (song != null) ControlsManager.getINSTANCE().sendMessage(channel, guild);

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

    private String playSpotifyPlaylist(String arg0, Boolean shuffle) {

        logger.info(arg0 + shuffle);
        logger.error("NOT YET IMPLEMENTED!");
        return null;
    }
    private String searchPlay(String search, TextChannel channel) throws IOException {
        search = cleanForURL(search);
        String id = YouTube.getVideoIdBySearchQuery(search);
        String link = "https://youtu.be/" + id;
        PlayerManager.getINSTANCE().loadAndPlay(channel.getGuild(), link);
        return link;
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
    private boolean isValidLink(String link) {
        return link.matches("(.*)open.spotify.com(.*)|spotify(.*)|(.*)youtube.com(.*)|(.*)youtu.be(.*)");
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