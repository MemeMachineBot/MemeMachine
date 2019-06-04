package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import kong.unirest.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.SpotifyClient;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Spotify extends Command {

	SpotifyClient sp = new SpotifyClient(Constants.SPOTIFY_CLIENT_ID, Constants.SPOTIFY_CLIENT_SECRET);

	public Spotify() {
		super(">spotify", "`Allows you to search spotify for songs!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
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
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			EmbedBuilder meb = new EmbedBuilder();

			meb.setColor(ColorUtils.getRainbowColor(2000));

			JSONArray items = new JSONObject(Unirest
					.get("https://api.spotify.com/v1/search?q=" + URLEncoder.encode(q, "UTF-8") + "&type=track&limit=1")
					.header("Authorization", "Bearer " + sp.getAccessToken()).asString().getBody())
							.getJSONObject("tracks").getJSONArray("items");

			if (items.length() <= 0) {
				meb.setTitle("Spotify Search: " + q);
				meb.setDescription("No search results found!");
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			JSONObject jObject = items.getJSONObject(0);

			meb.setTitle("Spotify Search: " + q, jObject.getJSONObject("external_urls").getString("spotify"));

			long duration_ms = jObject.getLong("duration_ms");
			long minutes = (duration_ms / 1000) / 60;
			long seconds = (duration_ms / 1000) % 60;

			String time = minutes + ":" + seconds;

			meb.setImage(jObject.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url"));

			meb.addField("Name: ", jObject.getString("name"), false);
			meb.addField("Artist: ", jObject.getJSONArray("artists").getJSONObject(0).getString("name"), false);
			meb.addField("Time: ", time, false);

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}