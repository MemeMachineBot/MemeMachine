package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Dog extends Command {

	public Dog() {
		super(">dog", "`Shows a dog image`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		if (message.equalsIgnoreCase(getPrefix())) {
			event.getChannel().sendMessage(getDog()).complete();
		}

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