package me.kavin.mememachine.utils.reddit;

public class TextPostData {

	public String title;

	public String author;

	public String data;

	public int num_comments;

	public int num_upvotes;

	public TextPostData(String title, String author, String data, int num_comments, int num_upvotes) {
		this.title = title;
		this.author = author;
		this.data = data;
		this.num_comments = num_comments;
		this.num_upvotes = num_upvotes;
	}
}
