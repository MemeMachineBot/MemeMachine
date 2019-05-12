package me.kavin.mememachine.utils;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import me.kavin.mememachine.consts.Constants;

public class XpHelper {

	private MongoHelper mongo;

	public XpHelper() {
		mongo = new MongoHelper(Constants.MONGO_URI);
	}

	public int getXp(long id) {
		MongoCollection<Document> collection = mongo.getCollection(mongo.getDatabase("users"), "xp");
		int xp = mongo.getValueInt(collection, String.valueOf(id));
		return xp > 0 ? xp : 0;
	}

	public void setXp(long id, int xp) {
		MongoCollection<Document> collection = mongo.getCollection(mongo.getDatabase("users"), "xp");
		mongo.setValueInt(collection, String.valueOf(id), xp);
	}
}
