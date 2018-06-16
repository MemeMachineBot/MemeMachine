package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BotStats extends Command {

	public BotStats() {
		super(">botstats", "`Shows you the bot's stastics`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		if (message.equalsIgnoreCase(getPrefix())) {
			EmbedBuilder meb = new EmbedBuilder();

			meb.setTitle("Bot Statistics");
			meb.setColor(ColorUtils.getRainbowColor(2000));

			long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);
			long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
			long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
			double usedMemory = totalMemory - freeMemory;

			meb.addField("Users: ", String.valueOf(Main.api.getUsers().size()) + "\n", false);
			meb.addField("Servers: ", String.valueOf(Main.api.getGuilds().size()), false);

			meb.addBlankField(false);

			meb.addField("Max Memory: ", String.valueOf(maxMemory) + "\n", false);
			meb.addField("Total Memory: ", String.valueOf(totalMemory) + "\n", false);
			meb.addField("Free Memory: ", String.valueOf(freeMemory) + "\n", false);
			meb.addField("Used Memory: ", String.valueOf(usedMemory) + "\n", false);

			event.getChannel().sendMessage(meb.build()).complete();
		}
	}
}