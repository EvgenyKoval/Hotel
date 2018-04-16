package com.gp.vaadincourse;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;

import javax.servlet.annotation.WebServlet;
import java.util.logging.Logger;

@Theme("mytheme")
public class HotelUI extends UI {
    private static final Logger LOGGER = Logger.getLogger(HotelUI.class.getName());
    private HotelService service = HotelService.getInstance();
    private final VerticalLayout content = new VerticalLayout();
    private final HorizontalLayout controls = new HorizontalLayout();
    private final TextField nameFilter = new TextField();
    private final TextField addressFilter = new TextField();
    private final Button addHotel = new Button("Add hotel");
    private final Button deleteHotel = new Button("Delete hotel");
    private final Button editHotel = new Button("Edit hotel");
    private final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
    private HotelEditForm editForm = new HotelEditForm(this);


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        initUI();
        initListeners();
        hotelGrid.setItems(service.findAll());
        hotelGrid.removeAllColumns();
        hotelGrid.addColumn("name");
        hotelGrid.addColumn("address");
        hotelGrid.addColumn("rating");
        hotelGrid.addColumn("category");
        hotelGrid.addColumn(hotel -> new Link(hotel.getUrl(), new ExternalResource(hotel.getUrl())), new ComponentRenderer()).setCaption("Url");
//        hotelGrid.setColumnOrder("name", "address", "rating", "category", "url");
        LOGGER.info("init method");

    }

    private void initUI() {
//        hotelGrid.setSizeFull();
        controls.addComponents(nameFilter, addressFilter, addHotel, deleteHotel, editHotel);
        HorizontalLayout layout = new HorizontalLayout(hotelGrid, editForm);
        editForm.setVisible(false);
        content.addComponents(controls, layout);
        deleteHotel.setEnabled(false);
        editHotel.setEnabled(false);
        setContent(content);
        nameFilter.setDescription("Filter by name");
        addressFilter.setDescription("Filter by address");
        content.setWidth(100, Unit.PERCENTAGE);
        layout.setWidth(100, Unit.PERCENTAGE);
        hotelGrid.setWidth(100, Unit.PERCENTAGE);
        LOGGER.info("initUI method");
    }

    private void initListeners() {
        deleteHotel.addClickListener(e -> {
            Hotel hotel = hotelGrid.getSelectedItems().iterator().next();
            service.delete(hotel);
            updateList();
            deleteHotel.setEnabled(false);
            editHotel.setEnabled(false);
            editForm.setVisible(false);
            LOGGER.info("deleted hotel " + hotel);
        });
        addHotel.addClickListener(e -> editForm.setHotel(new Hotel()));
        nameFilter.addValueChangeListener(e -> updateList());
        addressFilter.addValueChangeListener(e -> updateList());
        hotelGrid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                deleteHotel.setEnabled(true);
                editHotel.setEnabled(true);
//                editForm.setHotel(e.getValue());
            }
        });
        editHotel.addClickListener(e -> {
            try {
                editForm.setHotel(hotelGrid.getSelectedItems().iterator().next().clone());
            } catch (CloneNotSupportedException e1) {
                LOGGER.warning("something went wrong " + e);
            }
        });
    }

    public void updateList() {
        deleteHotel.setEnabled(false);
        editHotel.setEnabled(false);
        hotelGrid.setItems(service.findAll(nameFilter.getValue(), addressFilter.getValue()));
        LOGGER.info("updateList method");
    }

    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class HotelUIServlet extends VaadinServlet {
    }
}
