/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.wechat.modelo.DAO;

import com.mycompany.wechat.modelo.Archivo;
import com.mycompany.wechat.modelo.HibernateUtil;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Clase que contiene la implementación del DAO de archivo
 */
public class ArchivoDAO {

    /**
     * Log
     */
    private static final Logger log = Logger.getLogger(ArchivoDAO.class);

    public ArchivoDAO() {
    }

    /**
     * Obtiene el listado de todos los archivos
     *
     * @return Listado de archivos
     */
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
            log.error("Error obteniendo el listado de archivos: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return listaArchivos;
    }

    /**
     * Inserta un nuevo archivo a la base de datos
     *
     * @param archivo para añadir
     */
    public void addArchivo(Archivo archivo) {
        Session session = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.save(archivo);
            tx.commit();

        } catch (Exception e) {
            log.error("Error insertando el archivo: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Elimina un archivo de la base de datos
     *
     * @param archivo a eliminar
     */
    public void deleteArchivo(Archivo archivo) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.delete(archivo);
            tx.commit();
        } catch (Exception e) {
            log.error("Error eliminando el archivo: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    /**
     * Actualiza un archivo de la base de datos
     *
     * @param archivo a actualizar
     */
    public void updateArchivo(Archivo archivo) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            org.hibernate.Transaction tx = session.beginTransaction();
            session.update(archivo);
            tx.commit();

        } catch (Exception e) {
            log.error("Error actualizando el archivo: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
