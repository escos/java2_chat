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
                switch (request.toLowerCase()) {
                    case Command.ALL:
                        server.sendToAll(JsonConvertation.getInstance().saveToJson(message), this);
                        messageService.add(message);
                        break;
                    case Command.SERVER:
                        if (message.getBody().equals(Command.LIST)) {
                            List<String> clients = server.clientList();
                            for (String client : clients) {
                                message.setBody(client);
                                message.setReceiver(Command.LIST);
                                server.sendToUser(JsonConvertation.getInstance().saveToJson(message),
                                        message.getSender());
                            }
                        }
                        if (message.getBody().equals(Command.HISTORY)) {
                            List<Message> messages = messageService
                                    .getMessagesByUser(message.getSender());
                            for (Message mes : messages) {
                                if (!mes.getSender().equals(mes.getReceiver())) {
                                    if (mes.getSender().equals(message.getSender()))
                                        mes.setSender(Command.HISTORY);
                                    else mes.setReceiver(Command.HISTORY);
                                    server.sendToUser(JsonConvertation.getInstance()
                                            .saveToJson(mes), message.getSender());
                                }
                            }
                        }
                        break;
                    default:
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