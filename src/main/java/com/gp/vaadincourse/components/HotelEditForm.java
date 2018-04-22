package com.gp.vaadincourse.components;

import com.gp.vaadincourse.entities.Hotel;
import com.gp.vaadincourse.entities.HotelCategoryItem;
import com.gp.vaadincourse.services.CategoryService;
import com.gp.vaadincourse.services.HotelService;
import com.gp.vaadincourse.views.HotelsView;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.ui.*;

import java.time.LocalDate;
import java.util.Set;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;

public class HotelEditForm extends FormLayout {
    private final HotelsView ui;

    private final HotelService service = HotelService.getInstance();
    private final CategoryService categoryService = CategoryService.getInstance();
    private static final Logger LOGGER = LoggerFactory.getLogger(HotelEditForm.class);

    private Hotel hotel;
    private final Binder<Hotel> binder = new Binder<>(Hotel.class);

    private final TextField name = new TextField("Name");
    private final TextField address = new TextField("Address");
    private final TextField rating = new TextField("Rating");
    private final DateField operatesFrom = new DateField("Date");
    private final NativeSelect<HotelCategoryItem> category = new NativeSelect<>("Category");
    private final TextField url = new TextField("Url");
    private final TextArea description = new TextArea("Description");

    private final Button saveHotel = new Button("Save");
    private final Button close = new Button("Close");

    public HotelEditForm(HotelsView ui) {
        this.ui = ui;
        bindFields();
        initUI();
        initListeners();
    }

    private void bindFields() {
        Validator<String> nameValidator = (s, valueContext) -> {
            if (s == null || s.isEmpty()) return ValidationResult.error("Enter name!");
            else return ValidationResult.ok();
        };
        Validator<Integer> ratingValidator = (rat, valueContext) -> {
            if (rat == null || rat < 0 || rat > 5) return ValidationResult.error("Rating must be between 0 and 5");
            else return ValidationResult.ok();
        };
        Validator<String> addressValidator = (s, valueContext) -> {
            if (s == null || s.length() < 5) return ValidationResult.error("Address is too short");
            else return ValidationResult.ok();
        };
        Validator<LocalDate> dateValidator = (localDate, valueContext) -> {
            if (localDate.isAfter(LocalDate.now())) return ValidationResult.error("Err");
            else return ValidationResult.ok();
        };
        binder.forField(name).asRequired().withValidator(nameValidator).bind(Hotel::getName, Hotel::setName);
        binder.forField(address).asRequired().withValidator(addressValidator).bind(Hotel::getAddress, Hotel::setAddress);
        binder.forField(rating).asRequired().withConverter(new StringToIntegerConverter("Must enter a number"))
                .withValidator(ratingValidator).bind(Hotel::getRating, Hotel::setRating);
        binder.forField(operatesFrom).asRequired().withValidator(dateValidator).withConverter(LocalDate::toEpochDay, LocalDate::ofEpochDay).bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
        binder.forField(category).asRequired().bind(Hotel::getCategory, Hotel::setCategory);
        binder.forField(url).asRequired().bind(Hotel::getUrl, Hotel::setUrl);
        binder.forField(description).bind(Hotel::getDescription, Hotel::setDescription);
    }

    private void initUI() {
        //todo Ко всем полям на форме добавить ToolTip с описанием значения поля.
        HorizontalLayout controls = new HorizontalLayout(saveHotel, close);
        addComponents(name, address, rating, operatesFrom, category, url, description, controls);
        name.setDescription("Enter hotel name");
        address.setDescription("Enter hotel address");
        rating.setDescription("Enter hotel rating from 0 to 5");
        category.setDescription("Choose hotel category");
        url.setDescription("Enter hotel url");
        description.setDescription("Enter hotel description");
        saveHotel.setDescription("Save hotel");
        close.setDescription("Close edit form");
    }

    private void initListeners() {
        saveHotel.addClickListener(e -> save());
        close.addClickListener(e -> close());
        binder.addStatusChangeListener(s -> {
            boolean valid = !s.hasValidationErrors();
            saveHotel.setEnabled(valid);
        });
    }

    private void close() {
        setVisible(false);
    }

    private void save() {
        if (!binder.isValid()) {
            binder.validate();
            Notification.show("Fill all fields", ERROR_MESSAGE);
            return;
        }
        LOGGER.info("save " + hotel);
        binder.writeBeanIfValid(hotel);
        service.save(hotel);
        ui.updateList();
        close();
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        binder.readBean(this.hotel);
        initCategoriesItems();
        setVisible(true);
    }

    private void initCategoriesItems() {
        Set<HotelCategoryItem> categoryItems = categoryService.findAll();
        if (categoryItems == null || categoryItems.isEmpty()) {
            category.setItems(new HotelCategoryItem("No category"));

        }else {
            category.setItems(categoryItems);
        }
    }

}
