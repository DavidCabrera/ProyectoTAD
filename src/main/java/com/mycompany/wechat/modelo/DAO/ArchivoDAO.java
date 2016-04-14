/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.wechat.modelo.DAO;

import com.mycompany.wechat.modelo.Archivo;
import com.mycompany.wechat.modelo.HibernateUtil;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Sara
 */
public class ArchivoDAO {

    public ArchivoDAO() {
    }

    public List<Archivo> getListaArchivos() {
        Session session = null;
        List<Archivo> listaArchivos = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String hql = "From Archivo";
            Query query = session.createQuery(hql);
            listaArchivos = query.list();
            tx.commit();
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }
        return listaArchivos;
    }

    public void addArchivo(Archivo a) {
        Session session = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.save(a);
            tx.commit();

        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteArchivo(Archivo a) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.delete(a);
            tx.commit();
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    public void updateArchivo(Archivo a) {
        Session session = null;

        try {
            
        session = HibernateUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = session.beginTransaction();
        session.update(a);
        tx.commit();
        
        } catch (Exception e) {

        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
