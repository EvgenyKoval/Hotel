package com.gp.vaadincourse;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;

import javax.servlet.annotation.WebServlet;

@Theme("mytheme")
public class HotelUI extends UI {
    private final static Logger LOGGER = LoggerFactory.getLogger(HotelUI.class);
    //    private static final Logger LOGGER = Logger.getLogger(HotelUI.class.getName());
    private static final HotelService service = HotelService.getInstance();
    private final VerticalLayout content = new VerticalLayout();
    private final HorizontalLayout controls = new HorizontalLayout();
    private final TextField nameFilter = new TextField();
    private final TextField addressFilter = new TextField();
    private final Button addHotel = new Button("Add hotel");
    private final Button deleteHotel = new Button("Delete hotel");
    private final Button editHotel = new Button("Edit hotel");
    private final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
    private final HotelEditForm editForm = new HotelEditForm(this);


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        LOGGER.info("init method");
        initUI();
        initListeners();
        hotelGrid.setItems(service.findAll());
        hotelGrid.getColumn("url").setRenderer(hotel -> {
            Link link = new Link((String) hotel, new ExternalResource(String.valueOf(hotel)));
            link.setTargetName("_blank");
            return link;
        }, new ComponentRenderer());
        hotelGrid.setColumnOrder("name", "address", "rating", "category", "url");
    }

    private void initUI() {
        LOGGER.info("initUI method");
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

    }

    private void initListeners() {
        deleteHotel.addClickListener(e -> {
            Hotel hotel = hotelGrid.getSelectedItems().iterator().next();
            service.delete(hotel);
            updateList();
            editForm.setVisible(false);
            LOGGER.info("deleted hotel " + hotel);
        });
        addHotel.addClickListener(e -> editForm.setHotel(new Hotel()));
        nameFilter.addValueChangeListener(e -> updateList());
        addressFilter.addValueChangeListener(e -> updateList());
        hotelGrid.asSingleSelect().addValueChangeListener(e -> {
            boolean isSelected = e.getValue() != null;
            deleteHotel.setEnabled(isSelected);
            editHotel.setEnabled(isSelected);
        });
        editHotel.addClickListener(e -> {
            try {
                editForm.setHotel(hotelGrid.getSelectedItems().iterator().next().clone());
            } catch (CloneNotSupportedException e1) {
                LOGGER.warn("something went wrong " + e);
            }
        });
    }

    public void updateList() {
        LOGGER.info("updateList method");
        deleteHotel.setEnabled(false);
        editHotel.setEnabled(false);
        hotelGrid.setItems(service.findAll(nameFilter.getValue(), addressFilter.getValue()));
    }

    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
    public static class HotelUIServlet extends VaadinServlet {
    }
}
