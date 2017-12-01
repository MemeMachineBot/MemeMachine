package me.kavin.gwhpaladins.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class HttpServer extends Thread{
	public HttpServer() {
		this.start();
	}
	@SuppressWarnings("resource")
	@Override
	public void run() {
		try {
			ServerSocket socket = new ServerSocket(new Random().nextInt(8080));
			while(true){
				socket.accept().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
