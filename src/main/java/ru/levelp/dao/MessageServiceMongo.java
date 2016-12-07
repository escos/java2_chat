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
        return request().createQuery(Message.class)
                .field(MessageDAO.FIELD_SENDER).equal(sender)
                .asList();
    }

    public List<Message> getMessagesByReceiver(String receiver) {
        return request().createQuery(Message.class)
                .field(MessageDAO.FIELD_RECEIVER).equal(receiver)
                .asList();
    }
}
