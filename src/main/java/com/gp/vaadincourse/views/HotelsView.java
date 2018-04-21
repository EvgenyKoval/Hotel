package com.gp.vaadincourse.views;

import com.gp.vaadincourse.components.HotelEditForm;
import com.gp.vaadincourse.components.MainMenu;
import com.gp.vaadincourse.entities.Hotel;
import com.gp.vaadincourse.entities.HotelCategoryItem;
import com.gp.vaadincourse.services.HotelService;
import com.vaadin.annotations.Theme;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.TextRenderer;

import java.time.LocalDate;
import java.util.Set;

@Theme("mytheme")
public class HotelsView extends VerticalLayout implements View {
    private final static Logger LOGGER = LoggerFactory.getLogger(HotelsView.class);
    private static final HotelService SERVICE = HotelService.getInstance();
    private final VerticalLayout content = new VerticalLayout();
    private final HorizontalLayout controls = new HorizontalLayout();
    private final TextField nameFilter = new TextField();
    private final TextField addressFilter = new TextField();
    private final Button addHotel = new Button("Add hotel");
    private final Button deleteHotel = new Button("Delete hotel");
    private final Button editHotel = new Button("Edit hotel");
    private final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
    private final HotelEditForm editForm = new HotelEditForm(this);
    private final MainMenu menu = new MainMenu();


    public HotelsView() {
        LOGGER.info("init method");
        initUI();
        initListeners();
        hotelGrid.getColumn("url").setRenderer(hotel -> {
            Link link = new Link();
            if (hotel == null || ((String) hotel).isEmpty()) {
                return link;
            } else {
                link.setCaption("More info");
                link.setResource(new ExternalResource(String.valueOf(hotel)));
                link.setTargetName("_blank");
            }
            return link;
        }, new ComponentRenderer());
        hotelGrid.getColumn("category").setRenderer(item -> {
            HotelCategoryItem categoryItem = (HotelCategoryItem) item;
            if (categoryItem == null || categoryItem.getName() == null) return "No cat";
            return categoryItem.getName();
        }, new TextRenderer());
        hotelGrid.getColumn("operatesFrom").setRenderer(o -> {
            Long time = (Long) o;
            return LocalDate.ofEpochDay(time).toString();
        },new TextRenderer());
        hotelGrid.setColumnOrder("name", "address", "rating", "category", "url");
        updateList();
    }

    private void initUI() {

        LOGGER.info("initUI method");
//        hotelGrid.setSizeFull();
        controls.addComponents(nameFilter, addressFilter, addHotel, deleteHotel, editHotel);
        HorizontalLayout layout = new HorizontalLayout(hotelGrid, editForm);
        hotelGrid.setHeightByRows(12);
        content.addComponents(menu, controls, layout);
        deleteHotel.setEnabled(false);
        editHotel.setEnabled(false);
        addComponent(content);
        nameFilter.setDescription("Filter by name");
        addressFilter.setDescription("Filter by address");
        content.setWidth(100, Unit.PERCENTAGE);
        layout.setWidth(100, Unit.PERCENTAGE);
        hotelGrid.setWidth(100, Unit.PERCENTAGE);
        nameFilter.setPlaceholder("Filter by name");
        addressFilter.setPlaceholder("Filter by address");

    }

    private void initListeners() {
        deleteHotel.addClickListener(e -> {
            for (Hotel hotel : hotelGrid.getSelectedItems()) {
                SERVICE.delete(hotel);
                LOGGER.info("deleted hotel " + hotel);
            }
            updateList();
        });
        addHotel.addClickListener(e -> editForm.setHotel(new Hotel()));
        editHotel.addClickListener(e -> editForm.setHotel(hotelGrid.getSelectedItems().iterator().next()));
        nameFilter.addValueChangeListener(e -> updateList());
        addressFilter.addValueChangeListener(e -> updateList());
        hotelGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        hotelGrid.asMultiSelect().addValueChangeListener(e -> {
            Set<Hotel> value = e.getValue();
            if (value != null) {
                int size = value.size();
                deleteHotel.setEnabled(size > 0);
                editHotel.setEnabled(size == 1);
            }
        });
    }

    public void updateList() {
        LOGGER.info("updateList method");
        deleteHotel.setEnabled(false);
        editHotel.setEnabled(false);
        editForm.setVisible(false);
        hotelGrid.setItems(SERVICE.findAll(nameFilter.getValue(), addressFilter.getValue()));
    }

//    @WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
//    @VaadinServletConfiguration(ui = HotelsView.class, productionMode = false)
//    public static class HotelUIServlet extends VaadinServlet {
//    }
}