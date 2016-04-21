package com.mycompany.wechat;

import com.mycompany.wechat.modelo.DAO.UsuarioDAO;
import com.mycompany.wechat.modelo.Usuario;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
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
import javax.servlet.annotation.WebServlet;
import org.apache.log4j.Logger;

/**
 *
 */
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

    public class LoginView extends VerticalLayout implements View, ClickListener {

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
            textUsuario.setInputPrompt("Correo Usuario");
            textUsuario.addValidator(new EmailValidator("no es un e-mail"));
            layout.addComponent(textUsuario);

            passUsuario = new PasswordField("Contraseña");
            layout.addComponent(passUsuario);

            Button boton = new Button("Conectar");
            boton.addClickListener((Button.ClickListener) this);
            layout.addComponent(boton);

            addComponent(layout);

        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            textUsuario.clear();
            passUsuario.clear();
        }

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
                    if(pass.equals(usuario.getClave()))
                    {
                        // Usuario Correcto. Creamos sesion y navegamos a pagina principal
                        navigator.navigateTo(MAINVIEW);
                    }
                }
            } else {
                
                navigator.navigateTo("");
            }
        }

    }

    public class VistaPrincipal extends VerticalLayout implements View, ClickListener {

        public VistaPrincipal() {
            setSizeFull();
            Button boton = new Button("Salir");
            boton.addClickListener((Button.ClickListener) this);
            HorizontalLayout todo =  new HorizontalLayout();
            todo.setStyleName("pagina_principal");
            setDefaultComponentAlignment(Alignment.TOP_CENTER);
            //todo.setMargin(true);           
            VerticalLayout izq = new VerticalLayout();
            izq.setMargin(true);
            izq.setStyleName("pagina_principal");
            izq.setWidth("40%");
            izq.setHeight("500px");
            VerticalLayout usu = new VerticalLayout();
            
            usu.setCaption("lista de usuarios");
            izq.addComponent(boton);
            izq.addComponent(new TextField("buscar"));
            izq.addComponent(usu);
            
            
            VerticalLayout der = new VerticalLayout();
            der.setMargin(true);
            der.setStyleName("pagina_principal");
            der.setWidth("500px");
            der.setHeight("500px");
//            der.setCaption("Chat");
            der.addComponent(new Label("chat"));
            todo.addComponent(izq);
            todo.addComponent(der);
            
            addComponent(todo);
            
            //addComponent(boton);
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            Notification.show("Estas en la pagina principal");
        }

        @Override
        public void buttonClick(Button.ClickEvent event) {
            navigator.navigateTo("");
        }
    }

    @WebServlet(urlPatterns = "/*", name = "wechat", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
