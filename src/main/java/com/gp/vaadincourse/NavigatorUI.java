package com.gp.vaadincourse;

import com.gp.vaadincourse.views.CategoriesView;
import com.gp.vaadincourse.views.HotelsView;
import com.gp.vaadincourse.views.Views;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

import javax.servlet.annotation.WebServlet;

public class NavigatorUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Navigator navigator = new Navigator(this, this);
        navigator.addView(Views.HOTELS, new HotelsView());
        navigator.addView(Views.CATEGORIES, new CategoriesView());
        navigator.navigateTo(Views.HOTELS);
    }

    @WebServlet(urlPatterns = "/*", name = "NavigatorUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = NavigatorUI.class, productionMode = false)
    public static class NavigatorUIServlet extends VaadinServlet {
    }
}
