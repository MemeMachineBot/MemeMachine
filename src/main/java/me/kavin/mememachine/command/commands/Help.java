package me.kavin.mememachine.command.commands;

import java.awt.Color;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.command.CommandManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Help extends Command{
	public Help(){
	super(">help", "`Shows this message`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(this.getPrefix())){
		EmbedBuilder meb = new EmbedBuilder();
		meb.setColor(getRainbowColor(2000));
		meb.setTitle("Meme Machine's Commands:");
		for(Command cmd : CommandManager.commands){
				meb.addField(cmd.getPrefix(), cmd.getHelp() + '\n', false);
		}
		event.getChannel().sendMessage(meb.build()).queue();
		}
	}
	
	public static Color getRainbowColor(int speed) {
        float hue = (System.currentTimeMillis()) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f);
    }
	
}
