package me.kavin.mememachine.event.events;

import me.kavin.mememachine.event.AbstractEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;

public class EventGuildReactionRemove extends AbstractEvent {

	private GuildMessageReactionRemoveEvent event;

	public EventGuildReactionRemove(GuildMessageReactionRemoveEvent event) {
		this.event = event;
	}

	public GuildMessageReactionRemoveEvent getEvent() {
		return event;
	}
}
