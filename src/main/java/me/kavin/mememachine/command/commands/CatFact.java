package me.kavin.mememachine.command.commands;

import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CatFact extends Command {
	public CatFact() {
		super(">catfact", "`Shows a random cat fact!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		try {
			if (message.toLowerCase().equals(getPrefix())) {

				EmbedBuilder meb = new EmbedBuilder();
				
				meb.setTitle("Cat Fact");
				meb.setColor(ColorUtils.getRainbowColor(2000));
				
				meb.addField("Heres a cat fact:", new JSONObject(Unirest.get("https://catfact.ninja/fact").asString().getBody()).getString("fact") + "\n", false);
				
				event.getChannel().sendMessage(meb.build()).complete();
			}
		} catch (Exception e) { }
	}
}
