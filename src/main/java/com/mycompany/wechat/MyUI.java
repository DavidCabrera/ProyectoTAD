package com.mycompany.wechat;

import com.mycompany.wechat.modelo.DAO.UsuarioDAO;
import com.mycompany.wechat.modelo.Usuario;
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
                                getSession().setAttribute("nombre", usuario.getUsuario());
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

        public VistaPrincipal() {
            setSizeFull();

            // ------------------ IZQUIERDA ----------------------
            Button botonSalir = new Button("Salir");
            botonSalir.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    //getSession().close();  peta porque antes de navegar se queda colgado
                    getSession().setAttribute("nombre", null);
                    navigator.navigateTo("");
                }
            });

            HorizontalLayout todo = new HorizontalLayout();

            todo.setStyleName("pagina_principal");
            setDefaultComponentAlignment(Alignment.TOP_CENTER);
            VerticalLayout izq = new VerticalLayout();
            izq.setStyleName("parte_izquierda");
            izq.setWidth("350px");
            izq.setHeight("100%");

            TextField textBuscar = new TextField("Buscar");
            textBuscar.setStyleName("busqueda_usuario");

            VerticalLayout vertusuario = new VerticalLayout();
            vertusuario.addComponent(botonSalir);
            vertusuario.setStyleName("nombreUsuario");
            vertusuario.setHeight("100%");

            VerticalLayout usu = new VerticalLayout();
            usu.setCaption("lista de usuarios");

            izq.addComponent(vertusuario);
            izq.addComponent(textBuscar);
            izq.addComponent(usu);
            izq.setExpandRatio(vertusuario, 0.1f);
            izq.setExpandRatio(textBuscar, 0.15f);
            izq.setExpandRatio(usu, 0.75f);
            //puede ir en una funcion
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> Lusu = usuarioDAO.getListaUsuarios();
            for (Usuario usuario : Lusu) {
                usu.addComponent(new Label(usuario.getUsuario()));

            }

            //---------------     DERECHA  ------------------------    
            VerticalLayout der = new VerticalLayout();
            der.setStyleName("parte_derecha");
            der.setWidth("500px");
            der.setHeight("500px");

            VerticalLayout nombreUsuario = new VerticalLayout(new Label("NOMBRE"));
            nombreUsuario.setStyleName("nombreUsuario");
            nombreUsuario.setHeight("100%");
            der.addComponent(nombreUsuario);
            VerticalLayout texto = new VerticalLayout(new Label("MENSAJE"));
            texto.setStyleName("mensaje");
            texto.setHeight("100%");
            TextField escribir = new TextField();
            escribir.setInputPrompt("Escriba su mensaje...");
            escribir.setWidth("100%");
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
            if (getSession().getAttribute("nombre") == null || getSession().getAttribute("nombre") == "") {
                navigator.navigateTo("");
            } else {
                Notification.show("Bienvenido: " + getSession().getAttribute("nombre"));
            }
        }

    }

    @WebServlet(urlPatterns = "/*", name = "wechat", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
