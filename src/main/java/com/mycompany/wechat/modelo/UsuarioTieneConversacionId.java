package com.mycompany.wechat.modelo;
// Generated 06-abr-2016 11:05:56 by Hibernate Tools 4.3.1



/**
 * UsuarioTieneConversacionId generated by hbm2java
 */
public class UsuarioTieneConversacionId  implements java.io.Serializable {


     private int idUsuario;
     private int idConversacion;

    public UsuarioTieneConversacionId() {
    }

    public UsuarioTieneConversacionId(int idUsuario, int idConversacion) {
       this.idUsuario = idUsuario;
       this.idConversacion = idConversacion;
    }
   
    public int getIdUsuario() {
        return this.idUsuario;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public int getIdConversacion() {
        return this.idConversacion;
    }
    
    public void setIdConversacion(int idConversacion) {
        this.idConversacion = idConversacion;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof UsuarioTieneConversacionId) ) return false;
		 UsuarioTieneConversacionId castOther = ( UsuarioTieneConversacionId ) other; 
         
		 return (this.getIdUsuario()==castOther.getIdUsuario())
 && (this.getIdConversacion()==castOther.getIdConversacion());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdUsuario();
         result = 37 * result + this.getIdConversacion();
         return result;
   }   


}


