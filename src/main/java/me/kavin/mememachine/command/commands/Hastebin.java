package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Hastebin extends Command {
	public Hastebin() {
		super(">hastebin", "`Uploads the given text to hastebin`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			if (message.toLowerCase().startsWith(getPrefix())) {

				String q = null;

				if (message.length() > getPrefix().length()) {
					q = "";
					for (int i = getPrefix().length() + 1; i < message.length(); i++)
						q += message.charAt(i);
				}
				
				JSONObject jObject = new JSONObject(Unirest.post("https://hastebin.com/documents").body(q).asString().getBody());
				
				String url = "https://hastebin.com/" + jObject.getString("key");
				
				EmbedBuilder meb = new EmbedBuilder();
				
				meb.setTitle("Hastebin");
				meb.setColor(ColorUtils.getRainbowColor(2000));
				
				meb.addField("`Heres your hastebin link: `", url, true);
				
				event.getChannel().sendMessage(meb.build()).complete();
			}
		} catch (Exception e) { }
	}
}