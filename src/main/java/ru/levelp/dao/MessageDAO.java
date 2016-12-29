package ru.levelp.dao;

import ru.levelp.Message;

import java.util.List;

public interface MessageDAO {

    String FIELD_SENDER = "sender";
    String FIELD_RECEIVER = "receiver";

    void add(Message message);

    List<Message> getMessagesBySender(String sender);

    List<Message> getMessagesByReceiver(String receiver);

    List<Message> getMessagesByUser(String user);

    List<Message> getAll();

    Message delete(long id);

    Message get(long id);
}
