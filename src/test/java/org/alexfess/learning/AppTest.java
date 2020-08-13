package org.alexfess.learning;

import org.alexfess.learning.model.Message;
import org.alexfess.learning.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testJdbc() {
        assertTrue(JdbcDataProvider.execute("select sysdate from dual") != null);
    }

    @Test
    public void testHibernate() throws Exception {
        Message message1 = new Message();
        message1.setText("Message1");
        Message message2 = new Message();
        message2.setText("Message2");

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the student objects
            session.persist(message1);
            session.save(message2);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }


        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            CriteriaQuery<Message> criteriaQuery = session.getCriteriaBuilder().createQuery(Message.class);
            criteriaQuery.from(Message.class);
            List<Message> messages = session.createQuery(criteriaQuery).getResultList();
            messages.forEach(m -> System.out.println(m.getText()));
            assertEquals(messages.size(), 2);
            assertEquals(messages.get(0).getText(), "Message1");
            transaction.commit();
        }
    }
}
