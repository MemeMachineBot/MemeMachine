package me.kavin.mememachine.utils;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import me.kavin.mememachine.consts.Constants;

public class XpHelper {

	private MongoHelper mongo;

	public XpHelper() {
		mongo = new MongoHelper(Constants.MONGO_URI);
	}

	public int getXp(long id) {
		MongoCollection<Document> collection = mongo.getCollection(mongo.getDatabase("users"), "xp");
		Document document = collection.find(new Document("id", id)).first();
		return document != null ? document.getInteger("xp") : 0;
	}

	public void setXp(long id, int xp) {
		MongoCollection<Document> collection = mongo.getCollection(mongo.getDatabase("users"), "xp");
		Document search = new Document("id", id);
		if (collection.countDocuments(search) > 0) {
			collection.updateOne(Filters.eq("id", id), new Document("$set", new Document("xp", xp)));
		} else {
			Document doc = new Document("id", id);
			doc.put("xp", xp);
			collection.insertOne(doc);
		}
	}
}
