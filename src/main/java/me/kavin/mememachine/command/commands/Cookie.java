package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import kong.unirest.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Cookie extends Command {
	public Cookie() {
		super(">cookie", "`Opens a fortune cookie!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {

			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Fortune Cookie");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			meb.addField("You opened a fortune cookie:",
					new JSONObject(Unirest.get("http://www.yerkee.com/api/fortune").asString().getBody())
							.getString("fortune") + "\n",
					false);

			event.getChannel().sendMessage(meb.build()).complete();
		} catch (Exception e) {
		}
	}
}
