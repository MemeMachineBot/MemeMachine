package me.kavin.mememachine.command.commands;

import kong.unirest.json.JSONObject;

import kong.unirest.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Dog extends Command {

	public Dog() {
		super(">dog", "`Shows a dog image`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		event.getChannel().sendMessage(getDog()).queue();

	}

	private MessageEmbed getDog() {
		try {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Dog");

			meb.setColor(ColorUtils.getRainbowColor(2000));

			JSONObject jObject = new JSONObject(
					Unirest.get("https://dog.ceo/api/breeds/image/random").asString().getBody());

			meb.setImage(jObject.getString("message"));

			return meb.build();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
}