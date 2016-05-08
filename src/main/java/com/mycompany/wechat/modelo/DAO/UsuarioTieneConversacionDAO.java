/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.wechat.modelo.DAO;

import com.mycompany.wechat.modelo.Conversacion;
import com.mycompany.wechat.modelo.HibernateUtil;
import com.mycompany.wechat.modelo.Usuario;
import com.mycompany.wechat.modelo.UsuarioTieneConversacion;
import com.mycompany.wechat.modelo.UsuarioTieneConversacionId;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class UsuarioTieneConversacionDAO {
    
    /**
     * Log
     */
    private static final Logger log = Logger.getLogger(UsuarioTieneConversacionDAO.class);
    
    public List<UsuarioTieneConversacion> getTieneConversacion(Usuario logado, Usuario chats)
    {
        List<UsuarioTieneConversacion> listaConversacion = new ArrayList<UsuarioTieneConversacion>();
         Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String hql = "From UsuarioTieneConversacion where idUsuario in ("+logado.getIdUsuario()+","+chats.getIdUsuario()+")";
            Query query = session.createQuery(hql);
            listaConversacion = query.list();
            tx.commit();
        } catch (Exception e) {
            log.error("Error obteniendo el listado de mensajes: ", e);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        
        return listaConversacion;
    } 
    
    
    public void crearConversacion(Usuario logado, Usuario chats, Conversacion conversacion)
    {
        Session session = null;
        try {
            UsuarioTieneConversacionId usuarioConversacionId = new UsuarioTieneConversacionId();
            usuarioConversacionId.setIdUsuario(logado.getIdUsuario());
            usuarioConversacionId.setIdConversacion(conversacion.getIdConversacion());
            UsuarioTieneConversacion usuarioConversacion = new UsuarioTieneConversacion(usuarioConversacionId, conversacion, logado);
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.save(usuarioConversacion);
//            String hql = "insert into usuario_tiene_conversacion values ("+logado.getIdUsuario()+","+conversacion.getIdConversacion()+")";
//            Query query = session.createQuery(hql);
            session.flush();
            tx.commit();
            session.close();
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            UsuarioTieneConversacionId usuarioConversacionId1 = new UsuarioTieneConversacionId();
            usuarioConversacionId1.setIdUsuario(chats.getIdUsuario());
            usuarioConversacionId1.setIdConversacion(conversacion.getIdConversacion());
            
            UsuarioTieneConversacion usuarioConversacion1 = new UsuarioTieneConversacion(usuarioConversacionId1, conversacion, chats);
//            hql = "insert into usuario_tiene_conversacion values ("+chats.getIdUsuario()+","+conversacion.getIdConversacion()+")";
//            query = session.createQuery(hql);
            session.save(usuarioConversacion1);
            tx.commit();
        } catch (Exception e) {
            log.error("Error insertando la conversación: ", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
