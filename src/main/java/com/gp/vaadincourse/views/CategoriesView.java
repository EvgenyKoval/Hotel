package com.gp.vaadincourse.views;

import com.gp.vaadincourse.components.CategoryEditForm;
import com.gp.vaadincourse.components.MainMenu;
import com.gp.vaadincourse.entities.HotelCategoryItem;
import com.gp.vaadincourse.services.CategoryService;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import java.util.Set;

public class CategoriesView extends VerticalLayout implements View {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriesView.class);
    private static final CategoryService SERVICE = CategoryService.getInstance();

    private final Grid<HotelCategoryItem> categoryGrid = new Grid<>(HotelCategoryItem.class);
    private final Button addCategory = new Button("Add category");
    private final Button editCategory = new Button("Edit category");
    private final Button deleteCategory = new Button("Delete category");

    private final CategoryEditForm categoryEditForm = new CategoryEditForm(this);

    public CategoriesView() {
        updateList();
        initUI();
        initListeners();
    }

    private void initUI() {
        HorizontalLayout controls = new HorizontalLayout(addCategory, deleteCategory, editCategory);
        HorizontalLayout content = new HorizontalLayout(categoryGrid, categoryEditForm);
        addComponents(new MainMenu(), controls, content);
    }

    private void initListeners() {
        addCategory.addClickListener(clickEvent -> categoryEditForm.setCategoryItem(new HotelCategoryItem()));
        editCategory.addClickListener(clickEvent -> categoryEditForm.setCategoryItem(categoryGrid.getSelectedItems().iterator().next()));
        deleteCategory.addClickListener(clickEvent -> {
            Set<HotelCategoryItem> items = categoryGrid.getSelectedItems();
            for (HotelCategoryItem item : items) {
                SERVICE.delete(item);
            }
            updateList();
        });
        categoryGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        categoryGrid.asMultiSelect().addValueChangeListener(e -> {
            Set<HotelCategoryItem> value = e.getValue();
            if (value != null) {
                int size = value.size();
                deleteCategory.setEnabled(size > 0);
                editCategory.setEnabled(size == 1);
            }
        });
    }

    public void updateList() {
        categoryGrid.setItems(SERVICE.findAll());
        categoryEditForm.close();
        deleteCategory.setEnabled(false);
        editCategory.setEnabled(false);
    }
}
