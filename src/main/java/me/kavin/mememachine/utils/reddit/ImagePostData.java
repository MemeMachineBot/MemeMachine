package me.kavin.mememachine.utils.reddit;

public class ImagePostData {

	public String title;

	public String author;

	public String img_url;

	public int num_comments;

	public int num_upvotes;

	public ImagePostData(String title, String author, String img_url, int num_comments, int num_upvotes) {
		this.title = title;
		this.author = author;
		this.img_url = img_url;
		this.num_comments = num_comments;
		this.num_upvotes = num_upvotes;
	}
}
