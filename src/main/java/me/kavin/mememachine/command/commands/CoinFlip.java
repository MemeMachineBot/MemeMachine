package me.kavin.mememachine.command.commands;

import java.util.concurrent.ThreadLocalRandom;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.Multithreading;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CoinFlip extends Command{
	public CoinFlip(){
	super(">cf", "`Flips a coin`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
		Multithreading.runAsync(new Runnable() {
			@Override
			public void run() {
				if (message.equalsIgnoreCase(getPrefix())){
					EmbedBuilder meb = new EmbedBuilder();
					
					meb.setTitle("Coinflip");
					meb.setColor(ColorUtils.getRainbowColor(2000));
					
					meb.addField("", "It was " + (ThreadLocalRandom.current().nextBoolean() ? "`Heads`" : "`Tails`"), true);
					
					event.getChannel().sendMessage(meb.build()).queue();
				}
			}
		});
	}
}
