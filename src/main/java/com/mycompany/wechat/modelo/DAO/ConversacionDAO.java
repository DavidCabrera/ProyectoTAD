/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.wechat.modelo.DAO;

import com.mycompany.wechat.modelo.Conversacion;
import com.mycompany.wechat.modelo.HibernateUtil;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Sara
 */
public class ConversacionDAO {

    public List<Conversacion> getAllConversaciones() {
        Session session = null;
        List<Conversacion> listadoConversaciones = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String hql = "From Conversacion";
            Query query = session.createQuery(hql);
            listadoConversaciones = query.list();
            tx.commit();
        } catch (Exception e) {

        } finally {
            if (null != session) {
                session.close();
            }
        }
        return listadoConversaciones;
    }

    public void addConversacion(Conversacion conversacion) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.save(conversacion);
            tx.commit();
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteConversacion(Conversacion conversacion) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.delete(conversacion);
            tx.commit();
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void updateConversacion(Conversacion conversacion) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.update(conversacion);
            tx.commit();
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
