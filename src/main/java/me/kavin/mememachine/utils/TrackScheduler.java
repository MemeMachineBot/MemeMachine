package me.kavin.mememachine.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import me.kavin.mememachine.Main;

/**
 * This class schedules tracks for the audio player. It contains the queue of
 * tracks.
 */
public class TrackScheduler extends AudioEventAdapter {

	private final AudioPlayer player;
	private final long guildId;
	private final BlockingQueue<AudioTrack> queue;
	private boolean repeat = false;

	/**
	 * @param player The audio player this scheduler uses
	 */
	public TrackScheduler(AudioPlayer player, long guildId) {
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		this.guildId = guildId;
	}

	/**
	 * Add the next track to queue or play right away if nothing is in the queue.
	 *
	 * @param track The track to play or add to queue.
	 */
	public void queue(AudioTrack track) {
		// Calling startTrack with the noInterrupt set to true will start the track only
		// if nothing is currently playing. If
		// something is playing, it returns false and does nothing. In that case the
		// player was already playing so this
		// track goes to the queue instead.
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	/**
	 * Start the next track, stopping the current one if it is playing.
	 */
	public void nextTrack() {
		// Start the next track, regardless of if something is already playing or not.
		// In case queue was empty, we are
		// giving null to startTrack, which is a valid argument and will simply stop the
		// player.

		if (queue.size() == 0) {
			Main.api.getGuildById(guildId).getAudioManager().closeAudioConnection();
			repeat = false;
		}

		player.startTrack(queue.poll(), false);
	}

	public void stopPlaying() {
		queue.clear();
		nextTrack();
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public BlockingQueue<AudioTrack> getQueue() {
		return queue;
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		// Only start the next track if the end reason is suitable for it (FINISHED or
		// LOAD_FAILED)
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		if (repeat)
			queue.add(track.makeClone());
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		exception.printStackTrace();
	}
}