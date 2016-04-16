/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.wechat.modelo.DAO;

import com.mycompany.wechat.modelo.HibernateUtil;
import com.mycompany.wechat.modelo.Mensaje;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Clase que implementa el DAO de mensajes
 */
public class MensajeDAO {

    /**
     * Log
     */
    private static final Logger log = Logger.getLogger(MensajeDAO.class);

    /**
     * Constructor sin parametros
     */
    public MensajeDAO() {

    }

    /**
     * Obtener el listado de todos los mensajes
     *
     * @return Listado de mensajes
     */
    public List<Mensaje> getAllMensajes() {
        Session session = null;
        List<Mensaje> listadoMensajes = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String hql = "From Mensaje";
            Query query = session.createQuery(hql);
            listadoMensajes = query.list();
            tx.commit();
        } catch (Exception e) {
            log.error("Error obteniendo el listado de mensajes: ", e);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return listadoMensajes;
    }

    /**
     * Inserta un mensaje a la base de datos
     *
     * @param mensaje para insertar
     */
    public void addMensaje(Mensaje mensaje) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.save(mensaje);
            tx.commit();
        } catch (Exception e) {
            log.error("Error insertando el mensaje: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Elimina el mensaje pasado por parámetro
     *
     * @param mensaje a eliminar
     */
    public void deleteMensaje(Mensaje mensaje) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.delete(mensaje);
            tx.commit();
        } catch (Exception e) {
            log.error("Error eliminando el mensaje: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Actualiza el mensaje pasado por parámetro
     *
     * @param mensaje a actualizar
     */
    public void updateMensaje(Mensaje mensaje) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.update(mensaje);
            tx.commit();
        } catch (Exception e) {
            log.error("Error actualizando el mensaje: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
