package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.AudioConnectionListener;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Ping extends Command {
	public Ping() {
		super(">ping", "`Displays the ping to discord servers!`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		EmbedBuilder meb = new EmbedBuilder();
		meb.setColor(ColorUtils.getRainbowColor(2000));
		meb.setTitle("Meme Machine: Ping");
		meb.addField("Gateway Ping", "`" + event.getJDA().getGatewayPing() + "`", false);
		if (event.getGuild().getAudioManager().isConnected() && AudioConnectionListener.hasPing(event.getGuild()))
			meb.addField("Audio Ping", "`" + AudioConnectionListener.getPing(event.getGuild()) + "`", false);
		event.getChannel().sendMessage(meb.build()).queue();
	}
}