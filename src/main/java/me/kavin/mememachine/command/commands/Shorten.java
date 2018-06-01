package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Shorten extends Command {

    public Shorten() {
        super(">shorten", "`Shortens the given URL!`");
    }

    @Override
    public void onCommand(String message, MessageReceivedEvent event) {
    	try {
    		if (message.toLowerCase().startsWith(getPrefix())) {
                String[] split = message.split(" ");
                if (split.length != 1) {
                	JSONObject body = new JSONObject().put("longUrl", split[1]);
                	JSONObject jObject = new JSONObject(
                	Unirest.post("https://www.googleapis.com/urlshortener/v1/url?key=" + Constants.GOOGLE_API_KEY).
                	header("Content-Type", "application/json").
                	body(body).
                	asString().getBody());
                	
                	EmbedBuilder meb = new EmbedBuilder();
                	
                	meb.setTitle("URL Shortener");
                	meb.setColor(ColorUtils.getRainbowColor(2000));
                	
                	meb.addField("Shortened URL ", jObject.getString("id"), true);
                	
                	event.getChannel().sendMessage(meb.build()).complete();
                } else {
                	event.getChannel().sendMessage("`Please provide a URL as your argument like` \n>shorten <URL>").complete();
                }
            }
		} catch (Exception e) { }
    }
}