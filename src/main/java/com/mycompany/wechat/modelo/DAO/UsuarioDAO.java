/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.wechat.modelo.DAO;

import com.mycompany.wechat.modelo.HibernateUtil;
import com.mycompany.wechat.modelo.Usuario;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Clase que implementa el DAO de Usuario
 */
public class UsuarioDAO {

    /**
     * Log
     */
    private static final Logger log = Logger.getLogger(UsuarioDAO.class);

    /**
     * Constructor sin parámetros
     */
    public UsuarioDAO() {
    }

    /**
     * Obtiene el listado de usuarios
     *
     * @return Listado de usuarios
     */
    public List<Usuario> getListaUsuarios() {
        Session session = null;
        List<Usuario> listausuarios = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String hql = "From Usuario";
            Query query = session.createQuery(hql);
            listausuarios = query.list();
            tx.commit();
        } catch (Exception e) {
            log.error("Error obteniendo el listado de usuarios: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return listausuarios;
    }

    /**
     * Inserta el usuario pasado por parámetro
     *
     * @param usurio a insertar
     */
    public void addUsuario(Usuario usurio) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.save(usurio);
            tx.commit();
        } catch (Exception e) {
            log.error("Error insertando el usuario: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Elimina el usuario pasado por parámetro
     *
     * @param usuario a eliminar
     */
    public void deleteUsuario(Usuario usuario) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.delete(usuario);
            tx.commit();
        } catch (Exception e) {
            log.error("Error eliminando el usuario: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Actualiza el usuario pasado por parámetro
     *
     * @param usuario a actualizar
     */
    public void updateUsuario(Usuario usuario) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.update(usuario);
            tx.commit();
        } catch (Exception e) {
            log.error("Error actualizando el usuario: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Obtiene un usuario a traves del correo introducido
     *
     * @param correo del usuario
     * @return Usuario correspondiente al correo
     */
    public Usuario getUsuarioByCorreo(String correo) {
        Session session = null;
        Usuario usuario = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String hql = "From Usuario where correo like '" + correo + "'";
            Query query = session.createQuery(hql);
            List<Usuario> listausuarios = query.list();
            tx.commit();
            if (null != listausuarios && listausuarios.size() == 1) {
                usuario = listausuarios.get(0);
            }
        } catch (Exception e) {
            log.error("Error obteniendo el listado de usuarios: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return usuario;
    }
}
