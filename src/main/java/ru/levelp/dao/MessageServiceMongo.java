package ru.levelp.dao;

import ru.levelp.Message;

import java.util.List;

public class MessageServiceMongo extends BaseMongoService<Message> implements MessageDAO {

    public MessageServiceMongo() {
        super(Message.class);
    }

    public List<Message> getAll() {
        return request().createQuery(Message.class)
                .asList();
    }

    public List<Message> getMessagesBySender(String sender) {
        List<Message> outputMessages = request().createQuery(Message.class)
                .field("sender").equal(sender)
                .asList();
        for (Message message:outputMessages
                ) {
            message.setSender("history");
        }
        return outputMessages;
    }

    public List<Message> getMessagesByReceiver(String receiver) {
        List<Message> inputMessages = request().createQuery(Message.class)
                .field("receiver").equal(receiver)
                .asList();
        for (Message message:inputMessages
                ) {
            message.setReceiver("history");
        }
        return inputMessages;
    }
}
