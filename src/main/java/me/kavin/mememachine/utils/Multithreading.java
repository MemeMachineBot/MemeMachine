package me.kavin.mememachine.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Multithreading {

	private static final ExecutorService es = Executors.newFixedThreadPool(128);

	public static void runAsync(final Runnable runnable) {
		es.submit(runnable);
	}
}