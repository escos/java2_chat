package ru.levelp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread {
    private Socket socket;
    private SenderWorker senderWorker;
    private ServerExample server;
    private String userName;

    private enum Requests {
        ALL,
        SERVER,
    }

    public ClientHandler(ServerExample server, Socket socket, String userName) {
        this.server = server;
        this.socket = socket;
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            MessageService messageService = new MessageService();
            senderWorker = new SenderWorker(writer);
            senderWorker.start();
            String inputMessage;

            if ((inputMessage = reader.readLine()) != null) {
                System.out.println(inputMessage);
                Message message = JsonConvertation.getInstance().parsefromJson(inputMessage);
                userName = message.getSender();
            }

            while ((inputMessage = reader.readLine()) != null) {
                System.out.println(inputMessage);
                Message message = JsonConvertation.getInstance().parsefromJson(inputMessage);
                String request = message.getReceiver();
                try {
                    switch (Requests.valueOf(request.toUpperCase())) {
                        case ALL:
                            server.sendToAll(JsonConvertation.getInstance().saveToJson(message), this);
                            messageService.addMessage(message);
                            break;
                        case SERVER:
                            if (message.getBody().equals("list")) {
                                ArrayList<String> clients = server.clientList();
                                for (String client : clients) {
                                    String user = message.getSender();
                                    message.setBody(client);
                                    message.setReceiver("list");
                                    System.out.println(JsonConvertation.getInstance().saveToJson(message));
                                    server.sendToUser(JsonConvertation.getInstance().saveToJson(message), user);
                                }
                            }
                            if (message.getBody().equals("history")) {
                                List<Message> outMessages = messageService.getAllMessagesBySender(message.getSender());
                                for (Message mes : outMessages) {
                                        System.out.println(JsonConvertation.getInstance().saveToJson(mes));
                                        server.sendToUser(JsonConvertation.getInstance()
                                                .saveToJson(mes), message.getSender());
                                    }
                                List<Message> inMessages = messageService.getAllMessagesByReceiver(message.getSender());
                                for (Message mes : inMessages) {
                                    System.out.println(JsonConvertation.getInstance().saveToJson(mes));
                                    server.sendToUser(JsonConvertation.getInstance()
                                            .saveToJson(mes), message.getSender());
                                }
                            }
                            break;
                    }
                } catch (IllegalArgumentException ex) {
                    server.sendToUser(JsonConvertation.getInstance().saveToJson(message), message.getReceiver());
                    messageService.addMessage(message);
                }
            }
            server.disconnectClient(this);
            senderWorker.stopWorker();
            writer.close();
            reader.close();
            socket.close();
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) {
        if (senderWorker != null) {
            senderWorker.addMessage(message);
        }
    }

    public String getUserName() {
        return this.userName;
    }
}