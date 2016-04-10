package com.mycompany.wechat;

import com.mycompany.wechat.modelo.DAO.UsuarioDAO;
import com.mycompany.wechat.modelo.Usuario;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.mycompany.wechat.MyAppWidgetset")
public class MyUI extends UI {
    UsuarioDAO dao= new  UsuarioDAO();
    List<Usuario> l = new ArrayList<>();
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                l = dao.getListaUsuarios();
                layout.addComponent(new Label("Thank you for clicking" + l.get(0)));
            }
        });
        layout.addComponent(button);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
