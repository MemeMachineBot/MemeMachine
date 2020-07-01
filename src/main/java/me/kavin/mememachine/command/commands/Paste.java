package me.kavin.mememachine.command.commands;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Paste extends Command {
	public Paste() {
		super(">paste", "`Uploads the given text to Dogbin`");
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
				event.getChannel().sendMessage(meb.build()).queue();
				return;
			}

			String url;

			try {
				JSONObject jObject = new JSONObject(
						Unirest.post("https://del.dog/documents").body(q).asString().getBody());
				url = "https://del.dog/" + jObject.getString("key");
			} catch (Exception e) {
				EmbedBuilder meb = new EmbedBuilder();
				meb.setColor(ColorUtils.getRainbowColor(2000));

				meb.setTitle("Paste: Dogbin is down!");
				meb.setDescription("If this is an error, report it on the github.");
				event.getChannel().sendMessage(meb.build()).queue();
				return;
			}

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Paste");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.addField("`Heres your Dogbin link: `", url, true);

			event.getChannel().sendMessage(meb.build()).queue();
		} catch (Exception e) {
		}
	}
}