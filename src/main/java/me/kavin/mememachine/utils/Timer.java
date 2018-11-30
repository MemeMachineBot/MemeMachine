package me.kavin.mememachine.utils;

public class Timer {

	private long prevMS = getTime();

	public boolean delay(float milliSec) {
		if (this.getTime() - this.prevMS >= milliSec) {
			return true;
		}
		return false;
	}

	public void reset() {
		this.prevMS = this.getTime();
	}

	public long getTime() {
		return System.nanoTime() / 1000000;
	}

	public long getDifference() {
		return this.getTime() - this.prevMS;
	}
}