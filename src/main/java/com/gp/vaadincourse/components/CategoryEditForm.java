package com.gp.vaadincourse.components;

import com.gp.vaadincourse.entities.HotelCategoryItem;
import com.gp.vaadincourse.services.CategoryService;
import com.gp.vaadincourse.views.CategoriesView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.ui.*;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;

public class CategoryEditForm extends FormLayout {
    private final CategoriesView ui;

    private static final CategoryService SERVICE = CategoryService.getInstance();

    private HotelCategoryItem categoryItem;
    private final Binder<HotelCategoryItem> binder = new Binder<>(HotelCategoryItem.class);

    private final TextField name = new TextField("Category");

    private final Button save = new Button("Save");
    private final Button close = new Button("Close");

    public CategoryEditForm(CategoriesView ui) {
        this.ui = ui;
        initUI();
        bindFields();
        initListeners();
        close();
    }

    private void initUI() {
        HorizontalLayout controls = new HorizontalLayout(save, close);
        addComponents(name, controls);
    }

    private void bindFields() {
        Validator<String> categoryValidator = (s, valueContext) -> {
            if (s == null || s.isEmpty()) return ValidationResult.error("Must enter category name");
            else return ValidationResult.ok();
        };
        binder.forField(name).asRequired().withValidator(categoryValidator)
                .bind(HotelCategoryItem::getName, HotelCategoryItem::setName);
    }

    private void initListeners() {
        save.addClickListener(clickEvent -> save());
        close.addClickListener(clickEvent -> close());
    }

    public void close() {
        setVisible(false);
    }

    private void save() {
        if (!binder.isValid()) {
            binder.validate();
            Notification.show("Fill fields", ERROR_MESSAGE);
            return;
        }
        binder.writeBeanIfValid(categoryItem);
        SERVICE.save(categoryItem);
        ui.updateList();
        close();
    }

    public void setCategoryItem(HotelCategoryItem item) {
        try {
            this.categoryItem = item.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        binder.readBean(this.categoryItem);
        setVisible(true);
    }

}
