package main.java.SocketChat.Client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerListenerThread implements Runnable {
	
	private InputStream is;
	
	public ServerListenerThread(InputStream is) {
		this.is = is;
	}

	@Override
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(this.is))){
			String str = null;
			while ((str = br.readLine()) != null) {
				System.out.println(str);
			}
		} catch (Exception e) {
			System.out.println("Disconnected");
		}
		
	}

}
