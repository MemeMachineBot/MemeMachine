package me.kavin.mememachine.command.commands;

import java.net.URLEncoder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.event.EventHandler;
import me.kavin.mememachine.event.events.EventGuildReactionAdd;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;

public class Image extends Command {

	ObjectArrayList<Message> sent = new ObjectArrayList<>();
	ObjectArrayList<String> queries = new ObjectArrayList<>();

	public Image() {
		super(">img", "`Allows you to search google for images`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Exception {
		String q = null;

		if (message.length() > getPrefix().length()) {
			q = "";
			for (int i = getPrefix().length() + 1; i < message.length(); i++)
				q += message.charAt(i);
		}

		EmbedBuilder meb = new EmbedBuilder();
		String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&searchType=image&" + "q="
				+ URLEncoder.encode(q + " filetype:png", "UTF-8") + "&cx=008677437472124065250%3Ajljeb59kuse&num=1&key="
				+ Constants.GOOGLE_API_KEY;
		JSONObject root = new JSONObject(Unirest.get(url).asString().getBody());
		meb.setTitle("Google Image Search: " + q);
		meb.setColor(ColorUtils.getRainbowColor(2000));

		if (!root.has("items")) {
			meb.addField("No Results", "Unfortunately I couldn't find any results for `" + q + "`", true);
		} else {
			meb.setImage(Constants.IMAGE_PROXY_URL
					+ URLEncoder.encode(root.getJSONArray("items").getJSONObject(0).getString("link"), "UTF-8"));
			meb.setDescription("Page 1 / 100");

			Message sent = event.getChannel().sendMessage(meb.build()).submit().get();
			sent.addReaction("◀").queue();
			sent.addReaction("▶").queue();
			this.queries.add(q);
			this.sent.add(sent);
		}
	}

	@EventHandler
	private void onReaction(EventGuildReactionAdd event) throws Exception {
		GenericGuildMessageReactionEvent reactionEvent = event.getEvent();
		for (int i = 0; i < sent.size(); i++) {

			Message msg = sent.get(i);
			User user = reactionEvent.getJDA().retrieveUserById(reactionEvent.getUserIdLong()).submit().get();

			if (msg.getIdLong() == reactionEvent.getMessageIdLong() && !user.isBot()) {
				switch (reactionEvent.getReactionEmote().getName()) {
				case "◀": {
					EmbedBuilder meb = new EmbedBuilder(msg.getEmbeds().get(0));

					int page = Integer.parseInt(meb.getDescriptionBuilder().toString().split(" ")[1]);
					page--;

					if (page < 1)
						return;

					int start = 10 * (page / 10);
					String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&searchType=image&" + "q="
							+ URLEncoder.encode(queries.get(i) + " filetype:png", "UTF-8")
							+ "&cx=008677437472124065250%3Ajljeb59kuse&num=10" + "&key=" + Constants.GOOGLE_API_KEY;
					if (start != 0)
						url += "&start=" + start;
					JSONObject root = new JSONObject(Unirest.get(url).asString().getBody());

					meb.setImage(Constants.IMAGE_PROXY_URL + URLEncoder.encode(root.getJSONArray("items")
							.getJSONObject(page < 10 ? (page % 10) - 1 : page % 10).getString("link"), "UTF-8"));
					meb.setDescription("Page " + page + " / 100");

					sent.set(i, msg.editMessage(meb.build()).submit().get());

					break;
				}

				case "▶": {
					EmbedBuilder meb = new EmbedBuilder(msg.getEmbeds().get(0));

					int page = Integer.parseInt(meb.getDescriptionBuilder().toString().split(" ")[1]);
					page++;

					if (page > 100)
						return;

					int start = 10 * (page / 10);
					String url = "https://www.googleapis.com/customsearch/v1?" + "safe=medium&searchType=image&" + "q="
							+ URLEncoder.encode(queries.get(i) + " filetype:png", "UTF-8")
							+ "&cx=008677437472124065250%3Ajljeb59kuse&num=10" + "&key=" + Constants.GOOGLE_API_KEY;
					if (start != 0)
						url += "&start=" + start;
					JSONObject root = new JSONObject(Unirest.get(url).asString().getBody());

					meb.setImage(Constants.IMAGE_PROXY_URL + URLEncoder.encode(root.getJSONArray("items")
							.getJSONObject(page < 10 ? (page % 10) - 1 : page % 10).getString("link"), "UTF-8"));
					meb.setDescription("Page " + page + " / 100");

					sent.set(i, msg.editMessage(meb.build()).submit().get());

					break;
				}

				}
				reactionEvent.getReaction().removeReaction(reactionEvent.getUser()).queue();
			}
		}
	}
}