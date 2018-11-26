package me.kavin.mememachine.command.commands;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.event.EventHandler;
import me.kavin.mememachine.event.events.EventGuildReactionAdd;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;

public class Gif extends Command {

	ObjectArrayList<Message> sent = new ObjectArrayList<>();
	ObjectArrayList<String> queries = new ObjectArrayList<>();
	private static final String API_KEY = "JJHDC7UK73EH";

	public Gif() {
		super(">gif", "`Allows you to search for gifs!`");
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

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Gif Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));

			JSONArray jArray = new JSONObject(Unirest
					.get("https://api.tenor.com/v1/autocomplete?key=" + API_KEY + "&tag=" + q).asString().getBody())
							.getJSONArray("results");

			if (jArray.length() > 0) {

				JSONArray results = new JSONObject(
						Unirest.get("https://api.tenor.com/v1/search?limit=50&tag=" + jArray.getString(0)).asString()
								.getBody()).getJSONArray("results");

				meb.setImage(results.getJSONObject(0).getJSONArray("media").getJSONObject(0).getJSONObject("gif")
						.getString("url"));
				meb.setDescription("Page 1 / 50");

				Message sent = event.getChannel().sendMessage(meb.build()).complete();
				sent.addReaction("◀").complete();
				sent.addReaction("▶").complete();

				this.queries.add(jArray.getString(0));
				this.sent.add(sent);
			} else {
				meb.setDescription("No search results found");
				event.getChannel().sendMessage(meb.build()).queue();
			}

		} catch (Exception e) {
		}
	}

	@EventHandler
	private void onReactionAdd(EventGuildReactionAdd event) {
		GenericGuildMessageReactionEvent reactionEvent = event.getEvent();
		for (int i = 0; i < sent.size(); i++) {
			Message msg = sent.get(i);
			if (msg.getIdLong() == reactionEvent.getMessageIdLong() && !reactionEvent.getUser().isBot()) {
				switch (reactionEvent.getReactionEmote().getName()) {
				case "◀":
					try {
						EmbedBuilder meb = new EmbedBuilder(msg.getEmbeds().get(0));

						int page = Integer.parseInt(meb.getDescriptionBuilder().toString().split(" ")[1]);
						page--;

						if (page < 1)
							break;

						JSONArray results = new JSONObject(
								Unirest.get("https://api.tenor.com/v1/search?limit=50&tag=" + queries.get(i)).asString()
										.getBody()).getJSONArray("results");

						meb.setImage(results.getJSONObject(page - 1).getJSONArray("media").getJSONObject(0)
								.getJSONObject("gif").getString("url"));
						meb.setDescription("Page " + page + " / 50");

						sent.set(i, msg.editMessage(meb.build()).complete());

						break;

					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case "▶":
					try {
						EmbedBuilder meb = new EmbedBuilder(msg.getEmbeds().get(0));

						int page = Integer.parseInt(meb.getDescriptionBuilder().toString().split(" ")[1]);
						page++;

						if (page > 50)
							break;

						JSONArray results = new JSONObject(
								Unirest.get("https://api.tenor.com/v1/search?limit=50&tag=" + queries.get(i)).asString()
										.getBody()).getJSONArray("results");

						meb.setImage(results.getJSONObject(page - 1).getJSONArray("media").getJSONObject(0)
								.getJSONObject("gif").getString("url"));
						meb.setDescription("Page " + page + " / 50");

						sent.set(i, msg.editMessage(meb.build()).complete());

						break;

					} catch (Exception e) {
					}
					break;
				}
				reactionEvent.getReaction().removeReaction(reactionEvent.getUser()).complete();
			}
		}
	}
}