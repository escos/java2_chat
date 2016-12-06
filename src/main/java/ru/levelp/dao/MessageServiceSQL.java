package ru.levelp.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import ru.levelp.HibernateManager;
import ru.levelp.Message;
import ru.levelp.dao.MessageDAO;

import java.util.List;

public class MessageServiceSQL implements MessageDAO{
    private Session session;

    public MessageServiceSQL() {
        this.session = HibernateManager.getInstance().getSession();
    }

    public void add(Message message) {
        session.beginTransaction();
        session.saveOrUpdate(message);
        session.getTransaction().commit();
    }

    public List<Message> getMessagesBySender(String sender) {
        List<Message> outputMessages = session.createCriteria(Message.class)
                .add(Restrictions.like("sender", sender, MatchMode.ANYWHERE)).list();
        for (Message message:outputMessages
             ) {
            message.setSender("history");
        }
        return outputMessages;
    }

    public List<Message> getMessagesByReceiver(String receiver) {
        List<Message> inputMessages = session.createCriteria(Message.class)
                .add(Restrictions.like("receiver", receiver, MatchMode.ANYWHERE)).list();
        for (Message message:inputMessages
                ) {
            message.setReceiver("history");
        }
        return inputMessages;
    }

    public Message delete(long id) {
        session.beginTransaction();
        Message message = (Message) session.createCriteria(Message.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        if (message != null) {
            session.delete(message);
        }
        session.getTransaction().commit();
        return message;
    }

//    //public List<Message> getFilteredById() {
//
//        Criterion c1 = Restrictions.gt("id", 10);
//        Criterion c2 = Restrictions.le("id", 100);
//
//        List<Message> messages = session.createCriteria(Message.class)
//                .add(Restrictions.and(c1, c2))
//                .list();
//
//        return messages;
//    }

    public List<Message> getAll() {
        List<Message> messages = session.createCriteria(Message.class).list();
        return messages;
    }

    public Message get(long id) {
        return (Message) session.get(Message.class, id);
    }
}
