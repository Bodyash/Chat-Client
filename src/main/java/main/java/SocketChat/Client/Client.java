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
			//Если сокет не пустой после трай блока - печатаем месседж и идем дальше.
			System.out.println("Enstablished connection with " + s.getRemoteSocketAddress());
			//Этот трай блок с ресурсами отправляет данные на сервер
			try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
				
				
				//А этот отдельный поток - слушает с сервера и печатает в консоль весь текст, который приходит
				//с сервера на этот сокет (мы передаем в конструктор инпут стрим этого сокета)
				//-------------------------------------------------------------------------------------------
				new Thread(new ServerListenerThread(s.getInputStream())).start();
				//-------------------------------------------------------------------------------------------
				//Ну и пишем в косоль, что мы будем еще и с сервера принимать сообщения, ничего необычного
				System.out.println("Starting Listen Back from server. Now u connected to Chat and can type messages");
				//Переменная, с которую будем писать текст, а потом отправлять на сервер
				String consoleInput = null;
				// закончим читать из консоли, когда напишем туда "end"
				while (!(consoleInput = br.readLine()).equals("end")) {
					bw.write(consoleInput + "\n");
					bw.flush(); // это для правильной работы буфера:
					// насильно выталкиваем (смываем) файлы из буфера, т.к.
					// могут остаться данные
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
