package me.kavin.mememachine.command.commands;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.gargoylesoftware.htmlunit.WebClient;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CopyPasta extends Command {

    WebClient wc = new WebClient();

    public CopyPasta() {
        super(">copypasta", "`Shows a random copypasta!`");
    }

    @Override
    public void onCommand(String message, MessageReceivedEvent event) {
        if (message.equalsIgnoreCase(getPrefix())) {
            event.getChannel().sendMessage(getMeme()).complete();
        }
    }
    private MessageEmbed getMeme() {
        try {
            EmbedBuilder meb = new EmbedBuilder();
            String data = wc.getPage("https://gateway.reddit.com/desktopapi/v1/subreddits/copypasta?sort=hot").getWebResponse().getContentAsString();
            JSONTokener tokener = new JSONTokener(data);
            JSONObject root = new JSONObject(tokener);
            boolean found = false;
            JSONObject posts = root.getJSONObject("posts");
            String[] keys = Arrays.copyOf(posts.keySet().toArray(), posts.keySet().size(), String[].class);
            while (!found) {
                JSONObject post = posts.getJSONObject(keys[ThreadLocalRandom.current().nextInt(keys.length)]);
                if (post.getBoolean("isLocked") || post.getJSONObject("media").isNull("content"))
                    continue;
                found = true;
                meb.setTitle(post.getString("title"));
                meb.setColor(ColorUtils.getRainbowColor(2000));
                meb.appendDescription(post.getJSONObject("media").getString("content"));
                meb.setAuthor(post.getString("author"));
                meb.setDescription("\uD83D\uDC4D" + post.getInt("score") + " | " + "\uD83D\uDCAC" + post.getInt("numComments"));
            }
            return meb.build();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}