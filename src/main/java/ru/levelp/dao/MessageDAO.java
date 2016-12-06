package ru.levelp.dao;

import ru.levelp.Message;

import java.util.List;

public interface MessageDAO {

    void add(Message message);

    List<Message> getMessagesBySender(String sender);

    List<Message> getMessagesByReceiver(String receiver);

    List<Message> getAll();

    Message delete(long id);

    Message get(long id);
}
