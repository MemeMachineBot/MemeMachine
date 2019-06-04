package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import kong.unirest.Unirest;

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

			String q = null;

			for (int i = getPrefix().length() + 1; i < message.length(); i++) {
				if (q == null)
					q = "";
				q += message.charAt(i);
			}

			if (q == null) {
				EmbedBuilder meb = new EmbedBuilder();
				meb.setColor(ColorUtils.getRainbowColor(2000));

				meb.setTitle("Error: No Arguments provided!");
				meb.setDescription("Please add an argument like " + this.getPrefix() + " `<args>`");
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			String url;

			try {
				JSONObject jObject = new JSONObject(
						Unirest.post("https://hastebin.com/documents").body(q).asString().getBody());
				url = "https://hastebin.com/" + jObject.getString("key");
			} catch (Exception e) {
				EmbedBuilder meb = new EmbedBuilder();
				meb.setColor(ColorUtils.getRainbowColor(2000));

				meb.setTitle("Hastebin: Hastebin is down!");
				meb.setDescription("If this is an error, report it on the github.");
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Hastebin");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.addField("`Heres your hastebin link: `", url, true);

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}