package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Stop extends Command {

	public Stop() {
		super(">stop", "`Allows you to stop playing music.`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Exception {
		PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).scheduler.stopPlaying();
		EmbedBuilder meb = new EmbedBuilder();
		meb.setTitle("YouTube Playback");
		meb.setColor(ColorUtils.getRainbowColor(2000));

		meb.setDescription("Stopped!");

		event.getChannel().sendMessage(meb.build()).queue();
	}
}