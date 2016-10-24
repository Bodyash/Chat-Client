package main.java.SocketChat.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		Socket s = null;
		try {
			s = new Socket("localhost", 26789);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (s != null) {
			System.out.println("Enstablished connection with " + s.getRemoteSocketAddress());
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
				new Thread(new ServerListenerThread(s.getInputStream())).start();
				System.out.println("Starting Listen Back from server. Now u connected to Chat and can type messages");
				String consoleInput = null;
				// закончим читать из консоли, когда напишем туда "end"
				while (!(consoleInput = br.readLine()).equals("end")) {
					bw.write(consoleInput + "\n");
					bw.flush(); // это для правильной работы буфера:
					// насильно выталкиваем (смываем) файлы из буфера, т.к.
					// могут остаться
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
