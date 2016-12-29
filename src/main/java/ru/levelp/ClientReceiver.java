package ru.levelp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class ClientReceiver extends Thread {
    private Socket socket;

    ClientReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String serverMessage;
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((serverMessage = serverReader.readLine()) != null) {
                Message inputMessage = JsonConvertation.getInstance().parsefromJson(serverMessage);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                String sDate = sdf.format(inputMessage.getTime());
                switch (inputMessage.getReceiver().toLowerCase()) {
                    case Command.LIST:
                        System.out.println(" User " + inputMessage.getBody());
                        break;
                    case Command.HISTORY:
                        System.out.printf("Input message from: %-10s Message: %-25s Time: %s \n",
                                inputMessage.getSender(), inputMessage.getBody(), sDate);
                        break;
                    default:
                        if (inputMessage.getSender().equals(Command.HISTORY)) {
                            System.out.printf("Output message to : %-10s Message: %-25s Time: %s \n",
                                    inputMessage.getReceiver(), inputMessage.getBody(), sDate);
                            System.out.println("---------------------------------------------------");
                        } else System.out.printf("Sender: %10s Message: %-25s Time: %s \n",
                                inputMessage.getSender(), inputMessage.getBody(), sDate);
                }
            }
            serverReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
