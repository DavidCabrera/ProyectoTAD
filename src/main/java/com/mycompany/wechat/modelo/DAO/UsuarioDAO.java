/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.wechat.modelo.DAO;

import com.mycompany.wechat.modelo.HibernateUtil;
import com.mycompany.wechat.modelo.Usuario;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Sara
 */
public class UsuarioDAO {

    public UsuarioDAO() {
    }

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

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return listausuarios;
    }

    public void addUsuario(Usuario u) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.save(u);
            tx.commit();
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteUsuario(Usuario u) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.delete(u);
            tx.commit();
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void updateUsuario(Usuario u) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.update(u);
            tx.commit();
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
