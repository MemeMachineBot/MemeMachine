package me.kavin.mememachine.event;

/**
 * Event types (ex: before a method, after a method, I/O, etc)
 */
public class EventType {
	/**
	 * Usually call this one before a method begins its processes
	 */
	public static final byte PRE = 0;
	/**
	 * Usually call this one after a method begins its processes
	 */
	public static final byte POST = 1;
	/**
	 * Possible usage for outgoing packets in a connection
	 */
	public static final byte OUTGOING = 2;
	/**
	 * Possible usage for incoming packets from a connection
	 */
	public static final byte INCOMING = 3;
}