package me.kavin.mememachine.command.commands;

import java.util.ArrayList;
import java.util.Random;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.Multithreading;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Dab extends Command{
	public Dab(){
	super(">dab", "`Shows a person dabbing`");
		daburls.add("https://ibb.co/kjGceb");
		daburls.add("https://ibb.co/cPbokG");
		daburls.add("https://ibb.co/gX8qzb");
		daburls.add("https://ibb.co/cRzKsw");
		daburls.add("https://ibb.co/ejvEQG");
		daburls.add("https://ibb.co/n6E8kG");
		daburls.add("https://ibb.co/dJgAzb");
		daburls.add("https://ibb.co/gSV3Kb");
		daburls.add("https://ibb.co/cN8qzb");
		daburls.add("https://ibb.co/ndSTkG");
	}
	ArrayList<String> daburls = new ArrayList<String>();
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
		Multithreading.runAsync(new Runnable() {
			@Override
			public void run() {
				if (message.equalsIgnoreCase(getPrefix())){
					EmbedBuilder meb = new EmbedBuilder();
					
					meb.setTitle("Dab");
					
					meb.setImage(getDab());
					
					meb.setColor(ColorUtils.getRainbowColor(2000));
					
					event.getChannel().sendMessage(getDab()).queue();
				}
			}
		});
	}
	private String getDab() {
		return daburls.get(new Random().nextInt(daburls.size()));
	}
}
