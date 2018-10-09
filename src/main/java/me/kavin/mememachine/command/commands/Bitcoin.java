package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Bitcoin extends Command {

	public Bitcoin() {
		super(">bitcoin", "`Shows the current bitcoin prices`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {

		try {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Bitcoin Price");

			meb.setColor(ColorUtils.getRainbowColor(2000));

			JSONObject jObject = new JSONObject(
					Unirest.get("https://api.coindesk.com/v1/bpi/currentprice.json").asString().getBody());

			JSONObject bpi = jObject.getJSONObject("bpi");
			bpi.keys().forEachRemaining(key -> {
				JSONObject currency = bpi.getJSONObject(key);
				char symbol = 0;
				switch (key) {
				case "USD":
					symbol = '$';
					break;
				case "GBP":
					symbol = '£';
					break;
				case "EUR":
					symbol = '€';
					break;
				}
				meb.addField(key, removeDecimal(currency.getString("rate")) + " " + symbol + "\n", false);
			});

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private String removeDecimal(String in) {

		StringBuilder sb = new StringBuilder();

		for (char ch : in.toCharArray()) {
			if (ch == '.')
				break;
			sb.append(ch);
		}

		return sb.toString();
	}

}