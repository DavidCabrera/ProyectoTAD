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
        List<UsuarioTieneConversacion> listaConversacion2 = new ArrayList<UsuarioTieneConversacion>();
        List<UsuarioTieneConversacion> listaConversacion3 = new ArrayList<UsuarioTieneConversacion>();
         Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String hql = "From UsuarioTieneConversacion where idUsuario ='"+logado.getIdUsuario()+"'";
            String hql2 = "From UsuarioTieneConversacion where idUsuario ='"+chats.getIdUsuario()+"'";
//            String hql = "From usuario_tiene_conversacion as ustc , conversacion as c, mensaje as m where ustc.idConversacion = c.idConversacion and c.idConversacion=m.idConversacion  and ustc.idUsuario='"+logado+"'";
            Query query = session.createQuery(hql);
            Query query2 = session.createQuery(hql2);
            listaConversacion = query.list();
            listaConversacion2 = query2.list();
            for (UsuarioTieneConversacion u2 : listaConversacion2) {
                for (UsuarioTieneConversacion u : listaConversacion) {                    
                    if(u.getConversacion().getIdConversacion()==(u2.getConversacion().getIdConversacion())){
                        listaConversacion3.add(u);
                    }
                }
                
            }
            
            tx.commit();
        } catch (Exception e) {
            log.error("Error obteniendo el listado de mensajes: ", e);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        
        return listaConversacion3;
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
