package me.kavin.mememachine.command.commands;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.Multithreading;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Roll extends Command{
	public Roll(){
	super(">roll", "`Rolls a dice`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
		Multithreading.runAsync(new Runnable() {
			@Override
			public void run() {
				if (message.equalsIgnoreCase(getPrefix())){
					EmbedBuilder meb = new EmbedBuilder();
					
					meb.setTitle("Dice");
					meb.setColor(getRainbowColor(2000));
					
					meb.addField("It was `" + (ThreadLocalRandom.current().nextInt(6) + 1) + "`", "", true);
					
					event.getChannel().sendMessage(meb.build()).queue();
				}
			}
		});
	}
	
	public static Color getRainbowColor(int speed) {
        float hue = (System.currentTimeMillis()) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f);
    }
	
}
