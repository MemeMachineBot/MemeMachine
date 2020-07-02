package me.kavin.mememachine.utils;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class MongoHelper {

	private MongoClient mongoClient;

	private Object2ObjectOpenHashMap<String, MongoDatabase> databases = new Object2ObjectOpenHashMap<>();

	public MongoHelper(String uri) {
		mongoClient = new MongoClient(new MongoClientURI(uri));
	}

	public MongoDatabase getDatabase(String name) {
		if (databases.containsKey(name))
			return databases.get(name);
		else {
			MongoDatabase database = mongoClient.getDatabase(name);
			databases.put(name, database);
			return database;
		}
	}

	public MongoCollection<Document> getCollection(MongoDatabase database, String name) {
		return database.getCollection(name);
	}
}
