package me.kavin.mememachine.utils;

public class WYRGame {
	
	private int votes1;
	private int votes2;
	private String choice1;
	private String choice2;
	
	public WYRGame(String choice1, String choice2) {
		this.votes1 = 0;
		this.votes2 = 0;
		this.choice1 = choice1;
		this.choice2 = choice2;
	}
	
	public String getChoice1() {
		return choice1;
	}
	
	public String getChoice2() {
		return choice2;
	}
	
	public int getVotes1() {
		return votes1;
	}
	
	public int getVotes2() {
		return votes2;
	}
	
	public void addVotes1() {
		votes1++;
	}
	
	public void removeVotes1() {
		votes1--;
	}
	
	public void addVotes2() {
		votes2++;
	}
	
	public void removeVotes2() {
		votes2--;
	}
}
