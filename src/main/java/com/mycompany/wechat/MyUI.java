package com.mycompany.wechat;

import com.mycompany.wechat.modelo.Conversacion;
import com.mycompany.wechat.modelo.DAO.ConversacionDAO;
import com.mycompany.wechat.modelo.DAO.UsuarioDAO;
import com.mycompany.wechat.modelo.DAO.UsuarioTieneConversacionDAO;
import com.mycompany.wechat.modelo.Usuario;
import com.mycompany.wechat.modelo.UsuarioTieneConversacion;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
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
        navigator.addView(MAINVIEW, new VistaPrincipal());
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
                    final Usuario usuario = (Usuario) getSession().getAttribute("usuario");
                    String logueado = usuario.getUsuario();
                    List<Usuario> Lusu = usuDAO.getListaFiltroUsuarios(contenido, logueado);
                    layoutUsuario.removeAllComponents();
                     if (null != Lusu && !Lusu.isEmpty()) {
                        for (Usuario usuarioItem : Lusu) {
                            final Usuario usuarioChat = usuarioItem;
                            Button usu = new Button(usuarioItem.getUsuario());
                            usu.setWidth("100%");
//                            usu.addClickListener(new ClickListener() {
//                                @Override
//                                public void buttonClick(Button.ClickEvent event) {
//                                    nombreUsuario.removeAllComponents();
//                                    nombreUsuario.addComponent(new Label(usuarioChat.getUsuario()));
//                                    // Cargar conversación
//                                    final Conversacion conversacion = crearConversacion(usuario, usuarioChat);
//                                }
//                            });
                            layoutUsuario.addComponent(usu);

                        }
                    } else {
                        layoutUsuario.addComponent(new Label("No hay usuarios en la aplicación."));
                    }

                    
                }
            });
            izq.addComponent(botonBuscar);

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

            texto.setStyleName("mensaje");
            texto.setHeight("100%");
            TextField escribir = new TextField();
            escribir.setInputPrompt("Escriba su mensaje...");
            escribir.setWidth("100%");
            escribir.setHeight("100%");
            escribir.setStyleName("escribir");
            der.addComponent(texto);
            der.addComponent(escribir);

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

                final Usuario usuario = (Usuario) getSession().getAttribute("usuario");
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                List<Usuario> Lusu = usuarioDAO.getListaUsuariosParaChatear(usuario);

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
                                final Conversacion conversacion = crearConversacion(usuario, usuarioChat);
                            }
                        });
                        layoutUsuario.addComponent(usu);

                    }
                } else {
                    layoutUsuario.addComponent(new Label("No hay usuarios en la aplicación."));
                }

                Label usuarioLogado = new Label(usuario.getUsuario());
                vertusuario.addComponent(usuarioLogado);

                Button botonSalir = new Button("Salir");
                botonSalir.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        //getSession().close();  peta porque antes de navegar se queda colgado
                        getSession().setAttribute("usuario", null);
                        navigator.navigateTo("");
                    }
                });
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

        if (null == listadoConversaciones || listadoConversaciones.isEmpty()) {

            conversacion = new Conversacion();

            conversacion.setFecha(new Date());
            conversacion.setNumParticipantes(2);
            conversacion.setNombre("Conversación entre " + usuarioLogado.getUsuario() + " y " + usuarioChat.getUsuario());

            ConversacionDAO conversacionDAO = new ConversacionDAO();
            conversacionDAO.addConversacion(conversacion);

            usuarioConversacionDAO.crearConversacion(usuarioLogado, usuarioChat, conversacion);

        } else {
            conversacion = listadoConversaciones.get(0).getConversacion();
        }
        return conversacion;
    } 
}
