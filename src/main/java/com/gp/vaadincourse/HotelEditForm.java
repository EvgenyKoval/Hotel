package com.gp.vaadincourse;

import com.vaadin.data.Binder;
import com.vaadin.ui.*;

public class HotelEditForm extends FormLayout {
    private HotelUI ui;
    private HotelService service = HotelService.getInstance();
    private Hotel hotel;
    private Binder<Hotel> binder = new Binder<>(Hotel.class);

    final TextField name = new TextField("Name");
    final TextField address = new TextField("Address");
    final TextField rating = new TextField("Rating");
    final DateField operatesFrom = new DateField("Date");
    final NativeSelect<HotelCategory> category = new NativeSelect<>();
    final TextField url = new TextField("Url");
    final TextArea description = new TextArea("Description");

    final Button saveHotel = new Button("Save");
    final Button close = new Button("Close");

    public HotelEditForm(HotelUI ui) {
        this.ui = ui;
        HorizontalLayout controls = new HorizontalLayout();
        controls.addComponents(saveHotel, close);
        addComponents(name, address, rating, operatesFrom, category, url, description, controls);
        category.setItems(HotelCategory.values());
        setVisible(true);
        binder.bindInstanceFields(this);
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
