package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DadJoke extends Command {

	public DadJoke() {
		super(">joke", "`Shows a dad joke`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Dad Joke");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			JSONObject jObject = new JSONObject(Unirest.get("https://icanhazdadjoke.com/")
					.header("Accept", "application/json").asString().getBody());

			meb.setDescription(jObject.getString("joke"));

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}