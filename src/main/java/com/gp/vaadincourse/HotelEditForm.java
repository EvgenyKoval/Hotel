package com.gp.vaadincourse;

import com.vaadin.data.Binder;
import com.vaadin.ui.*;

public class HotelEditForm extends FormLayout {
    private HotelUI ui;
    private HotelService service = HotelService.getInstance();
    private Hotel hotel;
    private Binder<Hotel> binder = new Binder<>(Hotel.class);

    private final TextField name = new TextField("Name");
    private final TextField address = new TextField("Address");
    private final TextField rating = new TextField("Rating");
    private final DateField operatesFrom = new DateField("Date");
    private final NativeSelect<HotelCategory> category = new NativeSelect<>();
    private final TextField url = new TextField("Url");
    private final TextArea description = new TextArea("Description");

    private final Button saveHotel = new Button("Save");
    private final Button close = new Button("Close");

    public HotelEditForm(HotelUI ui) {
        this.ui = ui;
        binder.bindInstanceFields(this);
        initUI();
        initListeners();
    }

    private void initUI() {
        HorizontalLayout controls = new HorizontalLayout(saveHotel, close);
        addComponents(name, address, rating, operatesFrom, category, url, description, controls);
        category.setItems(HotelCategory.values());
    }

    private void initListeners() {
        saveHotel.addClickListener(e -> save());
        close.addClickListener(e -> close());
    }

    private void close() {
        setVisible(false);
    }

    private void save() {
        close();
        if (hotel.getUrl() == null) {
            return;
        }
        service.save(hotel);
        ui.updateList();
    }


    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        binder.setBean(this.hotel);
        setVisible(true);
    }
}
