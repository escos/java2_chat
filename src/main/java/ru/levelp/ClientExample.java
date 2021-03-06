package ru.levelp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ClientExample {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 7071;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(IP, PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            ClientReceiver clientReceiver = new ClientReceiver(socket);
            clientReceiver.start();
            String inputConsole;
            String login = "";
            Message message = new Message(login, Command.SERVER, System.currentTimeMillis(), "login");

            if ((inputConsole = console.readLine())!=null) {
                if (inputConsole.contains(" ")) {
                    int pos = inputConsole.indexOf(" ");
                    message.setSender(inputConsole.substring(0, pos));
                } else message.setSender(inputConsole);
                writer.println(JsonConvertation.getInstance().saveToJson(message));
                writer.flush();
            }

            while (!(inputConsole = console.readLine()).equals(Command.EXIT)) {
                message.setTime(System.currentTimeMillis());
                message.setId(UUID.randomUUID().getLeastSignificantBits());
                if ((inputConsole.indexOf("@") == 0) && inputConsole.contains(":")) {
                    int pos = inputConsole.indexOf(":");
                    message.setReceiver(inputConsole.substring(1, pos));
                    message.setBody(inputConsole.substring(pos + 1));
                } else {
                    message.setBody(inputConsole);
                    message.setReceiver(Command.ALL);
                }
                writer.println(JsonConvertation.getInstance().saveToJson(message));
                writer.flush();
            }
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
