package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.event.EventHandler;
import me.kavin.mememachine.event.events.EventGuildReaction;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;

public class Image extends Command {

	ObjectArrayList<Message> sent = new ObjectArrayList<>();
	ObjectArrayList<String> queries = new ObjectArrayList<>();

	public Image() {
		super(">img", "`Allows you to search google for images`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		String q = null;

		if (message.length() > getPrefix().length()) {
			q = "";
			for (int i = getPrefix().length() + 1; i < message.length(); i++)
				q += message.charAt(i);
		}

		Message sent = event.getChannel().sendMessage(getSearch(q)).complete();
		sent.addReaction("◀").complete();
		sent.addReaction("▶").complete();
		this.queries.add(q);
		this.sent.add(sent);
	}

	private MessageEmbed getSearch(String q) {
		try {
			EmbedBuilder meb = new EmbedBuilder();
			String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&searchType=image&" + "q="
					+ URLEncoder.encode(q + " filetype:png", "UTF-8") + "&cx=008677437472124065250%3Ajljeb59kuse&num=1&key="
					+ Constants.GOOGLE_API_KEY;
			JSONObject root = new JSONObject(Unirest.get(url).asString().getBody());
			meb.setTitle("Google Image Search: " + q);
			meb.setColor(ColorUtils.getRainbowColor(2000));
			if (!root.has("items")) {
				meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
				return meb.build();
			}
			meb.setImage(Constants.GOOGLE_PROXY_IMAGE
					+ URLEncoder.encode(root.getJSONArray("items").getJSONObject(0).getString("link"), "UTF-8"));
			meb.setDescription("Page 1 / 100");
			return meb.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@EventHandler
	private void onReaction(EventGuildReaction event) {
		GenericGuildMessageReactionEvent reactionEvent = event.getEvent();
		for (int i = 0; i < sent.size(); i++) {
			Message msg = sent.get(i);
			if (msg.getIdLong() == reactionEvent.getMessageIdLong()
					&& reactionEvent.getUser().getIdLong() != Main.api.getSelfUser().getIdLong()) {
				switch (reactionEvent.getReactionEmote().getName()) {
				case "◀":
					try {
						EmbedBuilder meb = new EmbedBuilder(msg.getEmbeds().get(0));

						int page = Integer.parseInt(meb.getDescriptionBuilder().toString().split(" ")[1]);
						page--;

						if (page < 1)
							return;

						int start = 10 * (page / 10);
						String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&searchType=image&"
								+ "q=" + URLEncoder.encode(queries.get(i) + " filetype:png", "UTF-8")
								+ "&cx=008677437472124065250%3Ajljeb59kuse&num=10" + "&key=" + Constants.GOOGLE_API_KEY;
						if (start != 0)
							url += "&start=" + start;
						JSONObject root = new JSONObject(Unirest.get(url).asString().getBody());

						meb.setImage(Constants.GOOGLE_PROXY_IMAGE + URLEncoder.encode(root.getJSONArray("items")
								.getJSONObject(page < 10 ? (page % 10) - 1 : page % 10).getString("link"), "UTF-8"));
						meb.setDescription("Page " + page + " / 100");

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

						if (page > 100)
							return;

						int start = 10 * (page / 10);
						String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&searchType=image&"
								+ "q=" + URLEncoder.encode(queries.get(i) + " filetype:png", "UTF-8")
								+ "&cx=008677437472124065250%3Ajljeb59kuse&num=10" + "&key=" + Constants.GOOGLE_API_KEY;
						if (start != 0)
							url += "&start=" + start;
						JSONObject root = new JSONObject(Unirest.get(url).asString().getBody());

						meb.setImage(Constants.GOOGLE_PROXY_IMAGE + URLEncoder.encode(root.getJSONArray("items")
								.getJSONObject(page < 10 ? (page % 10) - 1 : page % 10).getString("link"), "UTF-8"));
						meb.setDescription("Page " + page + " / 100");

						sent.set(i, msg.editMessage(meb.build()).complete());

						break;

					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}
}