package com.gp.vaadincourse.components;

import com.gp.vaadincourse.views.Views;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

public class MainMenu extends HorizontalLayout {

    public MainMenu() {
        MenuBar menuBar = new MenuBar();
        MenuBar.Command command = new MenuBar.Command() {
            private MenuItem previous;

            @Override
            public void menuSelected(MenuItem menuItem) {
                if (previous == null) {
                    previous = menuItem;
                }
                previous.setChecked(false);
                previous.setStyleName("highlight");
                previous = menuItem;
                menuItem.setStyleName(null);
                menuItem.setChecked(true);

                getUI().getNavigator().navigateTo(menuItem.getText());
            }
        };

        menuBar.addItem(Views.HOTELS, VaadinIcons.BUILDING, command);
        menuBar.addItem(Views.CATEGORIES, VaadinIcons.ACADEMY_CAP, command);

        menuBar.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
        addComponent(menuBar);
    }

}
