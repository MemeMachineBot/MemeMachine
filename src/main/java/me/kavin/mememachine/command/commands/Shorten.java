package me.kavin.mememachine.command.commands;

import kong.unirest.json.JSONObject;

import kong.unirest.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Shorten extends Command {

	public Shorten() {
		super(">shorten", "`Shortens the given URL!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			String[] split = message.split(" ");
			if (split.length != 1) {
				JSONObject jObject = new JSONObject(Unirest.post("https://elbo.in/~shorten")
						.header("Content-Type", "application/x-www-form-urlencoded").body("url=" + split[1]).asString()
						.getBody());

				EmbedBuilder meb = new EmbedBuilder();

				meb.setTitle("URL Shortener");
				meb.setColor(ColorUtils.getRainbowColor(2000));

				System.out.println(jObject);
				meb.addField("Shortened URL ", "https://elbo.in/" + jObject.getString("shorturl"), true);

				event.getChannel().sendMessage(meb.build()).queue();
			} else {
				event.getChannel().sendMessage("`Please provide a URL as your argument like` \n>shorten <URL>")
						.queue();
			}
		} catch (Exception e) {
		}
	}
}