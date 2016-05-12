package com.mycompany.wechat;

import com.mycompany.wechat.modelo.Conversacion;
import com.mycompany.wechat.modelo.DAO.ConversacionDAO;
import com.mycompany.wechat.modelo.DAO.MensajeDAO;
import com.mycompany.wechat.modelo.DAO.UsuarioDAO;
import com.mycompany.wechat.modelo.DAO.UsuarioTieneConversacionDAO;
import com.mycompany.wechat.modelo.Mensaje;
import com.mycompany.wechat.modelo.Usuario;
import com.mycompany.wechat.modelo.UsuarioTieneConversacion;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Property;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import org.apache.log4j.Logger;

/**
 *
 */
@PreserveOnRefresh
@Theme("mytheme")
@Widgetset("com.mycompany.wechat.MyAppWidgetset")
public class MyUI extends UI {
    
    private static final Logger log = Logger.getLogger(MyUI.class);
    
    Navigator navigator;
    public static final String MAINVIEW = "main";
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        
        getPage().setTitle("WECHAT");
        navigator = new Navigator(this, this);
        navigator.addView("", new LoginView());
        navigator.addView("salir", new SalirView());
        navigator.addView(MAINVIEW, new VistaPrincipal());
        navigator.addView("registro", new RegistroView());
        navigator.addView("configuracion", new ConfView());
    }
    
    public class ConfView extends VerticalLayout implements View {
        
        Usuario u;
        
        final TextField nombreUsuario;
        final PasswordField passUsuario;
        
        public ConfView() {
            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
            layout.setSpacing(true);
            
            nombreUsuario = new TextField("Nombre de usuario");
            layout.addComponent(nombreUsuario);
            Button botonNombre = new Button("Ok nombre");
            botonNombre.addClickListener(new ClickListener() {
                
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    String nombre = nombreUsuario.getValue();
                    if ((null != nombre && nombre.trim().length() > 0)) {
                        u.setUsuario(nombre);
                        u.setAdmin("N");
                        UsuarioDAO uDAO = new UsuarioDAO();
                        uDAO.updateUsuario(u);
                        navigator.navigateTo("salir");
                    }
                }
            });
            layout.addComponent(botonNombre);
            
            passUsuario = new PasswordField("Contraseña");
            layout.addComponent(passUsuario);
            
            Button botonPass = new Button("Ok contraseña");
            botonPass.addClickListener(new ClickListener() {
                
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    String pass = passUsuario.getValue();
                    if ((null != pass && pass.trim().length() > 0)) {
                        u.setClave(pass);
                        u.setAdmin("N");
                        UsuarioDAO uDAO = new UsuarioDAO();
                        uDAO.updateUsuario(u);
                        navigator.navigateTo("salir");
                    }
                }
            });
            layout.addComponent(botonPass);
            
            Button eliminarUsu = new Button("Darse de baja");
            eliminarUsu.addClickListener(new ClickListener() {
                
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    UsuarioDAO uDAO = new UsuarioDAO();
                    uDAO.deleteUsuario(u);
                    navigator.navigateTo("salir");
                }
            });
            layout.addComponent(eliminarUsu);
            
            addComponent(layout);
            
        }
        
        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            u = (Usuario) getSession().getAttribute("usuario");
        }
    }
    
    public class RegistroView extends VerticalLayout implements View {
        
        final TextField textUsuario;
        final PasswordField passUsuario;
        final TextField textnombre;
        
        public RegistroView() {
            
            addStyleName("fondo");
            setSizeFull();
            setMargin(true);
            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
            layout.setSpacing(true);
            
            Image img = new Image(null, new ThemeResource("wechat/img/logo.png"));
            layout.addComponent(img);
            
            textUsuario = new TextField("Correo");
            textUsuario.addValidator(new EmailValidator("No es un e-mail"));
            layout.addComponent(textUsuario);
            
            textnombre = new TextField("Nombre de usuario");
            layout.addComponent(textnombre);
            
            passUsuario = new PasswordField("Contraseña");
            layout.addComponent(passUsuario);
            
            Button boton = new Button("Registrarse");
            boton.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    
                    String correo = textUsuario.getValue();
                    String pass = passUsuario.getValue();
                    String nombre = textnombre.getValue();
                    
                    if ((null != correo && correo.trim().length() > 0) && (null != pass && pass.trim().length() > 0) && (null != nombre && nombre.trim().length() > 0)) {
                        // Comprobar que los datos introducidos son correctos
                        UsuarioDAO usuarioDAO = new UsuarioDAO();
                        Usuario u = new Usuario();
                        u.setClave(pass);
                        u.setCorreo(correo);
                        u.setUsuario(nombre);
                        u.setAdmin("N");
                        
                        usuarioDAO.addUsuario(u);
                        
                        navigator.navigateTo("");
                        
                    }
                }
            });
            layout.addComponent(boton);
            
            addComponent(layout);
            
        }
        
        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            textUsuario.clear();
            passUsuario.clear();
            passUsuario.setValue("");
        }
    }
    
    private static class SalirView extends VerticalLayout implements View {
        
        public SalirView() {
            
        }
        
        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            getSession().close();
            Link link = new Link("volver al login", new ExternalResource("/*"));
            addComponent(link);
            
        }
    }
    
    public class LoginView extends VerticalLayout implements View {
        
        final TextField textUsuario;
        final PasswordField passUsuario;
        
        public LoginView() {
            
            addStyleName("fondo");
            setSizeFull();
            setMargin(true);
            final VerticalLayout layout = new VerticalLayout();
            layout.setMargin(true);
            layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
            layout.setSpacing(true);
            
            Image img = new Image(null, new ThemeResource("wechat/img/logo.png"));
            layout.addComponent(img);
            
            textUsuario = new TextField("Usuario");
            textUsuario.addValidator(new EmailValidator("No es un e-mail"));
            layout.addComponent(textUsuario);
            
            passUsuario = new PasswordField("Contraseña");
            layout.addComponent(passUsuario);
            
            Button boton = new Button("Conectar");
            boton.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    // Comprobar que el usuario introducido es correcto. En caso de serlo configurar
                    // la sesión y navegar a la pantalla principal.
                    String correo = textUsuario.getValue();
                    String pass = passUsuario.getValue();
                    
                    if ((null != correo && correo.trim().length() > 0) && (null != pass && pass.trim().length() > 0)) {
                        // Comprobar que los datos introducidos son correctos
                        UsuarioDAO usuarioDAO = new UsuarioDAO();
                        Usuario usuario = usuarioDAO.getUsuarioByCorreo(correo);
                        
                        if (null != usuario) {
                            if (pass.equals(usuario.getClave())) {
                                // Usuario Correcto. Creamos sesion y navegamos a pagina principal
                                getSession().setAttribute("usuario", usuario);
                                navigator.navigateTo(MAINVIEW);
                            } else {
                                navigator.navigateTo("");
                                Notification notif = new Notification("Correo o contraseña erronea.", Notification.Type.ERROR_MESSAGE);
                                notif.setPosition(Position.TOP_RIGHT);
                                notif.setDelayMsec(2000);
                                notif.show(Page.getCurrent());
                            }
                        }
                    } else {
                        
                        navigator.navigateTo("");
                    }
                }
            });
            layout.addComponent(boton);
            
            Link link = new Link("registrarse", new ExternalResource("/#!registro"));
            layout.addComponent(link);
            
            addComponent(layout);
            
        }
        
        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            textUsuario.clear();
            passUsuario.clear();
            passUsuario.setValue("");
        }
        
    }
    
    public class VistaPrincipal extends VerticalLayout implements View {
        
        HorizontalLayout vertusuario = new HorizontalLayout();
        VerticalLayout layoutUsuario = new VerticalLayout();
        VerticalLayout nombreUsuario = new VerticalLayout();
        VerticalLayout texto = new VerticalLayout(new Label("MENSAJE"));
        Usuario usuarioLogueado;
        Usuario usuChat;
        
        public VistaPrincipal() {
            setSizeFull();

            // ------------------ IZQUIERDA ----------------------
            HorizontalLayout todo = new HorizontalLayout();
            
            todo.setStyleName("pagina_principal");
            setDefaultComponentAlignment(Alignment.TOP_CENTER);
            VerticalLayout izq = new VerticalLayout();
            izq.setStyleName("parte_izquierda");
            izq.setWidth("350px");
            izq.setHeight("100%");
            
            final TextField textBuscar = new TextField("Buscar");
            textBuscar.setStyleName("busqueda_usuario");
            
            Button botonBuscar = new Button("Buscar");
            
            botonBuscar.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    String contenido = textBuscar.getValue();
                    UsuarioDAO usuDAO = new UsuarioDAO();
                    // final Usuario usuario = (Usuario) getSession().getAttribute("usuario");
                    String logueado = usuarioLogueado.getUsuario();
                    List<Usuario> Lusu = usuDAO.getListaFiltroUsuarios(contenido, logueado);
                    layoutUsuario.removeAllComponents();
                    if (null != Lusu && !Lusu.isEmpty()) {
                        for (Usuario usuarioItem : Lusu) {
                            final Usuario usuarioChat = usuarioItem;
                            usuChat = usuarioChat;
                            Button usu = new Button(usuarioItem.getUsuario());
                            usu.setWidth("100%");
                            usu.addClickListener(new ClickListener() {
                                @Override
                                public void buttonClick(Button.ClickEvent event) {
                                    nombreUsuario.removeAllComponents();
                                    nombreUsuario.addComponent(new Label(usuarioChat.getUsuario()));
                                    // Cargar conversación
                                    final Conversacion conversacion = crearConversacion(usuarioLogueado, usuarioChat);
                                    List<Mensaje> ms = new MensajeDAO().getMensajesDeConversacion(conversacion);
                                    texto.removeAllComponents();
                                    for (int i = 0; i < ms.size(); i++) {
                                        Mensaje m = ms.get(i);
                                        if (m.getUsuario().getIdUsuario().equals(usuarioLogueado.getIdUsuario())) {
                                            //si soy yo alinear a la derecha
                                            Label l = new Label("<div align='right'>" + ms.get(i).getTexto() + "</div>", ContentMode.HTML);
                                            texto.addComponent(l);
                                        } else {
                                            //si es el otro alinear izq
                                            String aux = "<div align='left'>" + ms.get(i).getTexto() + "</div>";
                                            Label l = new Label(aux, ContentMode.HTML);
                                            texto.addComponent(l);
                                            
                                        }
                                    }
                                }
                            });
                            layoutUsuario.addComponent(usu);
                            
                        }
                    } else {
                        layoutUsuario.addComponent(new Label("No hay usuarios en la aplicación."));
                    }
                    
                }
            });

//            textBuscar.
//            textBuscar.setImmediate(true);
//            OnEnterKeyHandler onEnterHandler = new OnEnterKeyHandler() {
//                @Override
//                public void onEnterKeyPressed() {
//                    Notification.show("Voight Kampff Test",
//                            Notification.Type.HUMANIZED_MESSAGE);
//                }
//            };
//            onEnterHandler.installOn(textBuscar);
            vertusuario.setStyleName("nombreUsuario");
            vertusuario.setHeight("100%");
            vertusuario.setWidth("100%");
            
            layoutUsuario.setCaption("Listado de Usuarios");
            
            izq.addComponent(vertusuario);
            izq.addComponent(textBuscar);
            botonBuscar.setWidth("100%");
            izq.addComponent(botonBuscar);
            izq.addComponent(layoutUsuario);
            izq.setExpandRatio(vertusuario, 0.1f);
            izq.setExpandRatio(textBuscar, 0.15f);
            izq.setExpandRatio(layoutUsuario, 0.75f);

            //---------------     DERECHA  ------------------------    
            VerticalLayout der = new VerticalLayout();
            der.setStyleName("parte_derecha");
            der.setWidth("500px");
            der.setHeight("500px");
            
            nombreUsuario.setStyleName("nombreUsuario");
            nombreUsuario.setHeight("100%");
            der.addComponent(nombreUsuario);
            
            Button botonEnviar = new Button("Enviar");
            
            texto.setStyleName("mensaje");
            texto.setHeight("100%");
            final TextField escribir = new TextField();
            escribir.setInputPrompt("Escriba su mensaje...");
            escribir.setWidth("100%");
            escribir.setHeight("100%");
            escribir.setStyleName("escribir");
            botonEnviar.setWidth("100%");
            
            botonEnviar.addListener(new ClickListener() {
                
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    MensajeDAO mdao = new MensajeDAO();
                    Mensaje me = new Mensaje();
                    me.setConversacion(crearConversacion(usuarioLogueado, usuChat));
                    me.setFecha(new Timestamp(new Date().getTime()));
                    me.setTexto(escribir.getValue());
                    me.setUsuario(usuarioLogueado);
                    me.setArchivo(null);
                    mdao.addMensaje(me);
                    escribir.setValue("");
                    List<Mensaje> ms = new MensajeDAO().getMensajesDeConversacion(me.getConversacion());
                    texto.removeAllComponents();
                    for (int i = 0; i < ms.size(); i++) {
                        Mensaje m = ms.get(i);
                        if (m.getUsuario().getIdUsuario().equals(usuarioLogueado.getIdUsuario())) {
                            //si soy yo alinear a la derecha
                            Label l = new Label("<div align='right'>" + ms.get(i).getTexto() + "</div>", ContentMode.HTML);
                            texto.addComponent(l);
                        } else {
                            //si es el otro alinear izq
                            String aux = "<div align='left'>" + ms.get(i).getTexto() + "</div>";
                            Label l = new Label(aux, ContentMode.HTML);
                            texto.addComponent(l);
                            
                        }
                    }
                    
                }
            });
            
            der.addComponent(texto);
            der.addComponent(escribir);
            der.addComponent(botonEnviar);
            
            der.setExpandRatio(nombreUsuario, 0.1f);
            der.setExpandRatio(texto, 0.75f);
            der.setExpandRatio(escribir, 0.15f);
            
            todo.addComponent(izq);
            todo.addComponent(der);
            
            addComponent(todo);
            
        }
        
        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            if (getSession().getAttribute("usuario") == null) {
                navigator.navigateTo("");
            } else {
                Notification.show("Bienvenido " + ((Usuario) getSession().getAttribute("usuario")).getUsuario());
                
                usuarioLogueado = (Usuario) getSession().getAttribute("usuario");
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                List<Usuario> Lusu = usuarioDAO.getListaUsuariosParaChatear(usuarioLogueado);
                
                if (null != Lusu && !Lusu.isEmpty()) {
                    for (Usuario usuarioItem : Lusu) {
                        final Usuario usuarioChat = usuarioItem;
                        
                        Button usu = new Button(usuarioItem.getUsuario());
                        usu.setWidth("100%");
                        usu.addClickListener(new ClickListener() {
                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                nombreUsuario.removeAllComponents();
                                nombreUsuario.addComponent(new Label(usuarioChat.getUsuario()));
                                // Cargar conversación
                                usuChat = usuarioChat;
                                final Conversacion conversacion = crearConversacion(usuarioLogueado, usuarioChat);
                                List<Mensaje> ms = new MensajeDAO().getMensajesDeConversacion(conversacion);
                                texto.removeAllComponents();
                                for (int i = 0; i < ms.size(); i++) {
                                    Mensaje m = ms.get(i);
                                    if (m.getUsuario().getIdUsuario().equals(usuarioLogueado.getIdUsuario())) {
                                        //si soy yo alinear a la derecha
                                        Label l = new Label("<div align='right'>" + ms.get(i).getTexto() + "</div>", ContentMode.HTML);
                                        texto.addComponent(l);
                                        
                                    } else {
                                        //si es el otro alinear izq
                                        String aux = "<div align='left'>" + ms.get(i).getTexto() + "</div>";
                                        Label l = new Label(aux, ContentMode.HTML);
                                        texto.addComponent(l);
                                    }
                                }
                            }
                        });
                        layoutUsuario.addComponent(usu);
                        
                    }
                } else {
                    layoutUsuario.addComponent(new Label("No hay usuarios en la aplicación."));
                }
                
                Label usuarioLogado = new Label(usuarioLogueado.getUsuario());
                vertusuario.addComponent(usuarioLogado);
                
                Button botonSalir = new Button("Salir");
                botonSalir.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        //getSession().close();  peta porque antes de navegar se queda colgado
                        getSession().setAttribute("usuario", null);
                        
                        navigator.navigateTo("salir");
                    }
                });
                
                Button configuracion = new Button("configuracion");
                configuracion.addClickListener(new ClickListener() {
                    
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        navigator.navigateTo("configuracion");
                    }
                });
                
                vertusuario.addComponent(configuracion);
                vertusuario.addComponent(botonSalir);
                
                vertusuario.setExpandRatio(usuarioLogado, 0.78f);
                vertusuario.setExpandRatio(botonSalir, 0.22f);
            }
        }
        
    }
    
    @WebServlet(urlPatterns = "/*", name = "wechat", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
    
    public Conversacion crearConversacion(Usuario usuarioLogado, Usuario usuarioChat) {
        
        Conversacion conversacion = null;
        UsuarioTieneConversacionDAO usuarioConversacionDAO = new UsuarioTieneConversacionDAO();
        
        List<UsuarioTieneConversacion> listadoConversaciones = usuarioConversacionDAO.getTieneConversacion(usuarioLogado, usuarioChat);
        
        if (listadoConversaciones == null || listadoConversaciones.isEmpty()) {
            
            conversacion = new Conversacion();
            
            conversacion.setFecha(new Date());
            conversacion.setNumParticipantes(2);
            conversacion.setNombre("Conversación entre " + usuarioLogado.getUsuario() + " y " + usuarioChat.getUsuario());
            
            ConversacionDAO conversacionDAO = new ConversacionDAO();
            conversacionDAO.addConversacion(conversacion);
            
            List<Conversacion> c = conversacionDAO.getAllConversaciones();
            conversacion = c.get(c.size() - 1);
            usuarioConversacionDAO.crearConversacion(usuarioLogado, usuarioChat, conversacion);
            
        } else {
            conversacion = listadoConversaciones.get(0).getConversacion();
        }
        return conversacion;
    }
}
