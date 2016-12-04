package ru.levelp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class ClientReceiver extends Thread {
    private Socket socket;

    private enum Answers {
        LIST,
        HISTORY
    }

    ClientReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String serverMessage;
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!(serverMessage = serverReader.readLine()).equals(null)) {
                Message inputMessage = JsonConvertation.getInstance().parsefromJson(serverMessage);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                String sDate = sdf.format(inputMessage.getTime());
                try {
                    switch (Answers.valueOf(inputMessage.getReceiver().toUpperCase())) {
                        case LIST:
                            System.out.println(" User " + inputMessage.getBody());
                            break;
                        case HISTORY:
                            System.out.println("Input message from: " + inputMessage.getSender() +
                                    " Message: " + inputMessage.getBody() + " Time: " + sDate);
                            break;
                    }
                } catch (IllegalArgumentException ex) {
                    if (inputMessage.getSender().equals("history")) {
                        System.out.println("Output message to: " + inputMessage.getReceiver() +
                                " Message: " + inputMessage.getBody() + " Time: " + sDate);
                    } else System.out.println("Sender: " + inputMessage.getSender() +
                            " Message: " + inputMessage.getBody() + " Time: " + sDate);
                }
            }
            serverReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
