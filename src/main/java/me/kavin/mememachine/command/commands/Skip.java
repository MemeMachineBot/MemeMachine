package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Skip extends Command {

	public Skip() {
		super(">skip", "`Allows you to skip the current song.`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Exception {
		PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).scheduler.nextTrack();
		EmbedBuilder meb = new EmbedBuilder();
		meb.setTitle("YouTube Playback");
		meb.setColor(ColorUtils.getRainbowColor(2000));

		meb.setDescription("Skipped! ");

		event.getChannel().sendMessage(meb.build()).queue();
	}
}