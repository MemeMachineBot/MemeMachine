package me.kavin.mememachine.event;

public abstract class AbstractEvent {

	// No need to change the accessibility of these
	private boolean cancelled, skipFutureCalls;
	private byte type;

	/**
	 * Check if the event is cancelled, if true: dont let the method run (do
	 * something to not let the void do its normal functions)
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Set if the event should be cancelled or not
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * Check if this event is skipping any future calls after one of its handlers
	 * gets called
	 */
	public boolean skippingFutureCalls() {
		return this.skipFutureCalls;
	}

	/**
	 * Set if the event should skip future calls after one of its handlers gets
	 * called
	 */
	public void skipFutureCalls(boolean skipFutureCalls) {
		this.skipFutureCalls = skipFutureCalls;
	}

	/**
	 * Get the events type (should be set using values in the EventType class)
	 */
	public byte getType() {
		return this.type;
	}

	/**
	 * Set the event type according to values set in the EventType class
	 */
	public void setType(byte type) {
		this.type = type;
	}
}