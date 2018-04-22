package com.gp.vaadincourse.components;

import com.gp.vaadincourse.views.Views;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;

public class MainMenu extends MenuBar {

    public MainMenu() {

        MenuBar.Command command = (MenuBar.Command) menuItem -> {
            for (MenuItem item : getItems()) {
                item.setChecked(false);
            }
            menuItem.setChecked(true);
            getUI().getNavigator().navigateTo(menuItem.getText());

        };
        addItem(Views.HOTELS, VaadinIcons.BUILDING, command);
        addItem(Views.CATEGORIES, VaadinIcons.ACADEMY_CAP, command);

        setStyleName(ValoTheme.MENUBAR_BORDERLESS);

        for (MenuItem item : getItems()) {
            item.setCheckable(true);
        }
    }
}
