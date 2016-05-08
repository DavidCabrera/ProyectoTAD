/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.wechat.modelo.DAO;

import com.mycompany.wechat.modelo.Conversacion;
import com.mycompany.wechat.modelo.HibernateUtil;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Clase que contiene la implementación del DAO de Conversación
 */
public class ConversacionDAO {

    /**
     * Log
     */
    private static final Logger log = Logger.getLogger(ConversacionDAO.class);

    /**
     * Constructor sin parámetros
     */
    public ConversacionDAO() {

    }

    /**
     * Obtiene el listado de todas las conversaciones
     *
     * @return Listado de conversaciones
     */
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
            log.error("Error obtienedo el listado de conversaciones: ", e);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return listadoConversaciones;
    }

    /**
     * Inserta la conversación pasada por parámetro
     *
     * @param conversacion a insertar
     */
    public void addConversacion(Conversacion conversacion) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(conversacion);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            log.error("Error insertando una conversación: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Elimina la conversación pasada por parámetro
     *
     * @param conversacion a eliminar
     */
    public void deleteConversacion(Conversacion conversacion) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.delete(conversacion);
            tx.commit();
        } catch (Exception e) {
            log.error("Error eliminando la conversación: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Actualiza la conversación pasada por parámetro
     *
     * @param conversacion a actualizar
     */
    public void updateConversacion(Conversacion conversacion) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.update(conversacion);
            tx.commit();
        } catch (Exception e) {
            log.error("Error actualizando la conversación: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
