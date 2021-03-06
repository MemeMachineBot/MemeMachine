package me.kavin.mememachine.command.commands;

import kong.unirest.json.JSONObject;

import kong.unirest.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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

			event.getChannel().sendMessage(meb.build()).queue();
		} catch (Exception e) {
		}
	}
}