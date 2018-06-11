package me.kavin.mememachine.event.events;

import me.kavin.mememachine.event.AbstractEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;

public class EventGuildReactionAdd extends AbstractEvent {

	private GuildMessageReactionAddEvent event;

	public EventGuildReactionAdd(GuildMessageReactionAddEvent event) {
		this.event = event;
	}

	public GuildMessageReactionAddEvent getEvent() {
		return event;
	}
}
