package me.kavin.mememachine.command.commands;

import java.util.List;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.consts.Constants;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.EbayClient;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Ebay extends Command {

	EbayClient ebay = new EbayClient(Constants.EBAY_CLIENT_ID, Constants.EBAY_CLIENT_SECRET);

	public Ebay() {
		super(">ebay", "`Allows you to search products on ebay! (Mainly for the PCMR)`");
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

			List<Any> items = JsonIterator.deserialize(Unirest.post("https://api.ebay.com/search/v2")
					.header("user-agent", ebay.getUserAgent()).header("x-ebay-c-marketplace-id", "EBAY-US")
					.header("Authorization", "Bearer " + ebay.getAccessToken())
					.header("Content-Type", "application/json")
					.body("{\"searchRequest\":{\"keyword\":\"" + q
							+ "\",\"requestConfig\":[{\"name\":\"SearchServiceDictionary.ENABLE_AUTO_SPELLER\",\"value\":[\"1\"]}],\"scope\":[\"ItemTitleSearch\"],\"sortOrder\":\"BestMatch\"}}")
					.asString().getBody()).get("items").get("items").get("item").asList();

			if (items.size() <= 0) {
				meb.setTitle("eBay Search: " + q);
				meb.setDescription("No search results found!");
				event.getChannel().sendMessage(meb.build()).complete();
				return;
			}

			Any item = items.get(0);

			meb.setTitle("eBay Search: " + q, item.get("viewItemURL").as(String.class));
			meb.addField("Title: ", item.get("title").as(String.class), false);
			meb.addField("Brand: ", item.get("brand").as(String.class), false);
			meb.addField("Condition: ", item.get("normalizedCondition").get("name").as(String.class), false);
			meb.addField("Category: ", item.get("category").asList().get(0).get("name").as(String.class), false);
			meb.addField("Watching: ", String.valueOf(item.get("watchCount").as(Integer.class)), false);
			meb.addField("Price: ",
					String.valueOf(item.get("sellingStatus").get("currentPrice").get("value").as(Double.class)), false);
			meb.addField("Payment Methods: ", String.valueOf(item.get("paymentMethod").asList()), false);

			if (item.get("itemFeature").get("gallery").as(Boolean.class)) {
				meb.setImage(item.get("itemImageInfo").asList().get(0).get("primaryImageURL").as(String.class)
						.replaceFirst("s-l225", "s-l" + item.get("itemImageInfo").asList().get(0).get("extended")
								.get("masterImageSize").get("width")));
			}

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}