package ru.levelp;

import ru.levelp.dao.MessageDAO;

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
    private MessageDAO messageService;
    private String userName;

    private static final String GET_LIST = "list";
    public static final String GET_HISTORY = "history";

    private enum Requests {
        ALL,
        SERVER,
    }

    public ClientHandler(ServerExample server, Socket socket, String userName, MessageDAO messageService) {
        this.server = server;
        this.socket = socket;
        this.userName = userName;
        this.messageService = messageService;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
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
                            messageService.add(message);
                            break;
                        case SERVER:
                            if (message.getBody().equals(GET_LIST)) {
                                ArrayList<String> clients = server.clientList();
                                for (String client : clients) {
                                    String user = message.getSender();
                                    message.setBody(client);
                                    message.setReceiver(GET_LIST);
                                    server.sendToUser(JsonConvertation.getInstance().saveToJson(message), user);
                                }
                            }
                            if (message.getBody().equals(GET_HISTORY)) {
                                List<Message> inMessages = messageService
                                        .getMessagesByReceiver(message.getSender());
                                for (Message mes : inMessages) {
                                    if (mes.getSender().equals(mes.getReceiver())) break;
                                    mes.setReceiver(GET_HISTORY);
                                    server.sendToUser(JsonConvertation.getInstance()
                                            .saveToJson(mes), message.getSender());
                                }
                                List<Message> outMessages = messageService
                                        .getMessagesBySender(message.getSender());
                                for (Message mes : outMessages) {
                                    mes.setSender(GET_HISTORY);
                                    server.sendToUser(JsonConvertation.getInstance()
                                            .saveToJson(mes), message.getSender());
                                }
                            }
                            break;
                    }
                } catch (IllegalArgumentException ex) {
                    server.sendToUser(JsonConvertation.getInstance().saveToJson(message), message.getReceiver());
                    messageService.add(message);
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