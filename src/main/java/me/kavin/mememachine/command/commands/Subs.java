package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;
import java.text.DecimalFormat;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Subs extends Command {

	private DecimalFormat df = new DecimalFormat("#,###");

	public Subs() {
		super(">subs", "`Checks the number of subscribers the given youtuber has`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Exception {
		String q = null;

		if (message.length() > getPrefix().length()) {
			q = "";
			for (int i = getPrefix().length() + 1; i < message.length(); i++)
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

		String url = "https://invidious.snopyta.org/api/v1/search?type=channel&fields=authorUrl,subCount&q="
				+ URLEncoder.encode(q, "UTF-8");

		JSONArray jArray = Unirest.get(url).asJson().getBody().getArray();

		EmbedBuilder meb = new EmbedBuilder();

		meb.setTitle("YouTube Subscribers");
		meb.setColor(ColorUtils.getRainbowColor(2000));

		if (jArray.length() == 0) {
			meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
			event.getChannel().sendMessage(meb.build()).queue();
			return;
		}

		JSONObject selected = jArray.getJSONObject(0);

		meb.addField("`Channel: `", "https://www.youtube.com" + selected.getString("authorUrl") + "\n", false);

		meb.addField("`Subscribers: `", df.format(selected.getInt("subCount")) + "\n", false);

		event.getChannel().sendMessage(meb.build()).queue();
	}
}