package qStivi.commands;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Submission;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.tree.RootCommentNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import qStivi.Config;
import qStivi.ICommand;
import qStivi.commands.rpg.SkillsCommand;
import qStivi.DB;

import javax.annotation.CheckReturnValue;
import java.sql.SQLException;
import java.text.Normalizer;

import static org.slf4j.LoggerFactory.getLogger;

public class RedditCommand implements ICommand {

    private static final Logger logger = getLogger(RedditCommand.class);

    UserAgent userAgent = new UserAgent("Discord Bot", "qstivi.napoleon", "1", "src/qStivi");
    Credentials credentials = Credentials.script("qStivi", Config.get("REDDIT_PASSWORD"), Config.get("REDDIT_ID"), Config.get("REDDIT_SECRET"));
    NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
    RedditClient reddit = OAuthHelper.automatic(adapter, credentials);
    private long xp;

    @Override
    public void handle(GuildMessageReceivedEvent event, String[] args, DB db, Message reply) throws SQLException, ClassNotFoundException {
        xp = 0;

        String url;

        var subreddit = args[1];
        subreddit = Normalizer.normalize(subreddit, Normalizer.Form.NFKD);
        subreddit = subreddit.replaceAll("[^a-z0-9A-Z -]", ""); // Remove all non valid chars
        subreddit = subreddit.replaceAll(" {2}", " ").trim(); // convert multiple spaces into one space
        subreddit = subreddit.replaceAll(" ", ""); // //Replace spaces by nothing

        RootCommentNode randomSubmission = reddit.subreddit(subreddit).randomSubmission();
        Submission submissionSubject = randomSubmission.getSubject();

        url = submissionSubject.getUrl();
        String link = submissionSubject.getUrl();

        logger.info(link);
        if (link.contains("i.redd.it") || link.contains("v.redd.it") || link.contains("youtu.be") || link.contains("youtube.com") || link.contains("imgur.com") || link.contains("giphy.com") || link.contains("gfycat.com")) {

            if (link.contains("i.redd.it")) {
                reply.editMessage(sendFancyTitle(submissionSubject)).queue();
                event.getChannel().sendMessage(url).queue();
            } else if (link.contains("v.redd.it")) {
                if (submissionSubject.getEmbeddedMedia() != null) {
                    if (submissionSubject.getEmbeddedMedia().getRedditVideo() != null) {
                        reply.editMessage(sendFancyTitle(submissionSubject)).queue();
                        event.getChannel().sendMessage(submissionSubject.getEmbeddedMedia().getRedditVideo().getFallbackUrl()).queue();
                    }
                } else reply.editMessage(permalink(randomSubmission)).queue(); // This is usually a cross post

            } else {
                reply.editMessage(sendFancyTitle(submissionSubject)).queue();
                event.getChannel().sendMessage(url).queue();
            }

        } else {
            reply.editMessage(permalink(randomSubmission)).queue();
        }

        xp = 3 + (long) (3 * SkillsCommand.getSocialXPPMultiplier(event.getAuthor().getIdLong()));
    }

    @CheckReturnValue
    private MessageEmbed sendFancyTitle(Submission submissionSubject) {
        String postLink = "https://reddit.com";
        postLink = postLink.concat(submissionSubject.getPermalink());
        return new EmbedBuilder()
                .setTitle(submissionSubject.getTitle(), postLink)
                .setAuthor(submissionSubject.getSubreddit())
                .setFooter(submissionSubject.getScore() + " votes and " + submissionSubject.getCommentCount() + " comments")
                .build();
    }

    @CheckReturnValue
    private String permalink(RootCommentNode randomSubmission) {
        String postLink = "https://reddit.com";
        postLink = postLink.concat(randomSubmission.getSubject().getPermalink());
        return postLink;
    }

    @Override
    public @NotNull
    String getName() {
        return "reddit";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Sends random post from given subreddit.";
    }

    @Override
    public long getXp() {
        return xp;
    }
}
