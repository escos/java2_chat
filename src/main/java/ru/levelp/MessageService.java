package ru.levelp;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class MessageService {
    private Session session;

    public MessageService() {
        this.session = HibernateManager.getInstance().getSession();
    }

    public void addMessage(Message message) {
        session.beginTransaction();
        session.saveOrUpdate(message);
        session.getTransaction().commit();
    }

    public List<Message> getAllMessages() {
        List<Message> messages = session.createCriteria(Message.class).list();
        return messages;
    }

    public List<Message> getAllMessagesBySender(String sender) {
        List<Message> outputMessages = session.createCriteria(Message.class)
                .add(Restrictions.like("sender", sender, MatchMode.ANYWHERE)).list();
        for (Message message:outputMessages
             ) {
            message.setSender("history");
        }
        return outputMessages;
    }

    public List<Message> getAllMessagesByReceiver(String receiver) {
        List<Message> inputMessages = session.createCriteria(Message.class)
                .add(Restrictions.like("receiver", receiver, MatchMode.ANYWHERE)).list();
        for (Message message:inputMessages
                ) {
            message.setReceiver("history");
        }
        return inputMessages;
    }

    public Message getMessageByBody(String body) {
        Message message = (Message) session.createCriteria(Message.class)
                .add(Restrictions.eq("body", body))
                .uniqueResult();
        return message;
    }


    public void deleteMessage(long id) {
        session.beginTransaction();
        Message message = (Message) session.createCriteria(Message.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        if (message != null) {
            session.delete(message);
        }
        session.getTransaction().commit();
    }

    public List<Message> getFilteredById() {

        Criterion c1 = Restrictions.gt("id", 10);
        Criterion c2 = Restrictions.le("id", 100);

        List<Message> messages = session.createCriteria(Message.class)
                .add(Restrictions.and(c1, c2))
                .list();

        return messages;
    }
}
