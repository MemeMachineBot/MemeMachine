package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kong.unirest.Unirest;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.event.EventHandler;
import me.kavin.mememachine.event.events.EventGuildReactionAdd;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;

public class Gif extends Command {

	ObjectArrayList<Message> sent = new ObjectArrayList<>();
	ObjectArrayList<String> queries = new ObjectArrayList<>();

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

			if (q == null) {
				EmbedBuilder meb = new EmbedBuilder();
				meb.setColor(ColorUtils.getRainbowColor(2000));

				meb.setTitle("Error: No Arguments provided!");
				meb.setDescription("Please add an argument like " + this.getPrefix() + " `<args>`");
				event.getChannel().sendMessage(meb.build()).queue();
				return;
			}

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Gif Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));

			JSONArray results = new JSONObject(
					Unirest.get("https://api.tenor.com/v1/search?limit=50&tag=" + URLEncoder.encode(q, "UTF-8"))
							.asString().getBody()).getJSONArray("results");

			if (results.length() > 0) {

				meb.setImage(results.getJSONObject(0).getJSONArray("media").getJSONObject(0).getJSONObject("gif")
						.getString("url"));
				meb.setDescription("Page 1 / 50");

				Message sent = event.getChannel().sendMessage(meb.build()).submit().get();
				sent.addReaction("◀").queue();
				sent.addReaction("▶").queue();

				this.queries.add(q);
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

						sent.set(i, msg.editMessage(meb.build()).submit().get());

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

						sent.set(i, msg.editMessage(meb.build()).submit().get());

						break;

					} catch (Exception e) {
					}
					break;
				}
				reactionEvent.getReaction().removeReaction(reactionEvent.getUser()).queue();
			}
		}
	}
}