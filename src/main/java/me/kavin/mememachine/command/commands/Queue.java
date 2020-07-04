package me.kavin.mememachine.command.commands;

import java.util.concurrent.BlockingQueue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Queue extends Command {
	public Queue() {
		super(">queue", "`Shows the queue of music to be played`");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) throws Exception {
		{
			EmbedBuilder meb = new EmbedBuilder();
			meb.setColor(ColorUtils.getRainbowColor(2000));
			meb.setTitle("Queue:");

			final BlockingQueue<AudioTrack> queue = PlayerManager.getInstance()
					.getGuildMusicManager(event.getGuild()).scheduler.getQueue();

			if (queue.isEmpty()) {
				meb.setDescription("The queue appears to be empty.");
				event.getChannel().sendMessage(meb.build()).queue();
				return;
			}

			for (AudioTrack track : queue) {
				if (meb.getFields().size() >= 25) {
					event.getChannel().sendMessage(meb.build()).queue();
					meb.clearFields();
					meb.setTitle(" ");
					meb.addField(track.getInfo().title, track.getInfo().uri + '\n', false);
				} else {
					meb.addField(track.getInfo().title, track.getInfo().uri + '\n', false);
				}

			}
			if (meb.getFields().size() > 0)
				event.getChannel().sendMessage(meb.build()).queue();
		}
	}
}