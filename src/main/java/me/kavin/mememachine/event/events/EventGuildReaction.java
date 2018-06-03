package me.kavin.mememachine.event.events;

import me.kavin.mememachine.event.AbstractEvent;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;

public class EventGuildReaction extends AbstractEvent {

	private GenericGuildMessageReactionEvent event;

	public EventGuildReaction(GenericGuildMessageReactionEvent event) {
		this.event = event;
	}

	public GenericGuildMessageReactionEvent getEvent() {
		return event;
	}
}
