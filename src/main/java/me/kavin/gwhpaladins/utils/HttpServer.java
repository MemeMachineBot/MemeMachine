package me.kavin.gwhpaladins.utils;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer extends Thread{
	public HttpServer() {
		this.start();
	}
	@SuppressWarnings("resource")
	@Override
	public void run() {
		try {
			ServerSocket socket = new ServerSocket(80);
			while(true){
				socket.accept().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
