package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.PlayerManager;
import me.kavin.mememachine.utils.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Repeat extends Command {

	public Repeat() {
		super(">repeat", "`Allows you to repeat the current queue of songs.`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Exception {

		TrackScheduler scheduler = PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).scheduler;

		EmbedBuilder meb = new EmbedBuilder();
		meb.setTitle("YouTube Playback");
		meb.setColor(ColorUtils.getRainbowColor(2000));

		boolean repeat = !scheduler.isRepeat();
		scheduler.setRepeat(repeat);

		meb.setDescription("Repeat Status: " + repeat);

		event.getChannel().sendMessage(meb.build()).queue();
	}
}