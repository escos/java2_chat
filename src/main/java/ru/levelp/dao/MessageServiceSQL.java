package ru.levelp.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.TypedValue;
import ru.levelp.HibernateManager;
import ru.levelp.Message;
import ru.levelp.dao.MessageDAO;

import java.util.List;

public class MessageServiceSQL implements MessageDAO{
    private Session session;

    public MessageServiceSQL() {
        this.session = HibernateManager.getInstance().getSession();
    }

    @Override
    public void add(Message message) {
        session.beginTransaction();
        session.saveOrUpdate(message);
        session.getTransaction().commit();
    }

    public List<Message> getMessagesBySender(String sender) {
        return (List<Message>) session.createCriteria(Message.class)
                .add(Restrictions.eq(MessageDAO.FIELD_SENDER, sender)).list();
    }

    public List<Message> getMessagesByReceiver(String receiver) {
        return session.createCriteria(Message.class)
                .add(Restrictions.eq(MessageDAO.FIELD_RECEIVER, receiver)).list();
    }

    @Override
    public List<Message> getMessagesByUser(String user) {
        Criterion cr1 = Restrictions.eq(MessageDAO.FIELD_RECEIVER, user);
        Criterion cr2 = Restrictions.eq(MessageDAO.FIELD_SENDER, user);
        return session.createCriteria(Message.class)
                .add(Restrictions.or(cr1,cr2)).list();
    }

    @Override
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

    @Override
    public List<Message> getAll() {
        return session.createCriteria(Message.class).list();
    }

    @Override
    public Message get(long id) {
        return (Message) session.get(Message.class, id);
    }
}
