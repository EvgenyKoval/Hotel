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
import com.vaadin.navigator.ViewChangeListener;
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

    private final TextField nameFilter = new TextField();
    private final TextField addressFilter = new TextField();
    private final Button addHotel = new Button("Add hotel");
    private final Button deleteHotel = new Button("Delete hotel");
    private final Button editHotel = new Button("Edit hotel");
    private final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
    private final HotelEditForm editForm = new HotelEditForm(this);

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
            if (categoryItem == null || categoryItem.getName() == null) return "No category";
            return categoryItem.getName();
        }, new TextRenderer());
        hotelGrid.getColumn("operatesFrom").setRenderer(o -> {
            Long time = (Long) o;
            return LocalDate.ofEpochDay(time).toString();
        }, new TextRenderer());
        hotelGrid.setColumnOrder("name", "address", "rating", "category", "url");
        updateList();
    }

    private void initUI() {
        HorizontalLayout controls = new HorizontalLayout(nameFilter, addressFilter, addHotel, deleteHotel, editHotel);
        HorizontalLayout layout = new HorizontalLayout(hotelGrid, editForm);
        addComponents(new MainMenu(), controls, layout);
        LOGGER.info("initUI method");
//        hotelGrid.setSizeFull();
        hotelGrid.setHeightByRows(12);
        deleteHotel.setEnabled(false);
        editHotel.setEnabled(false);
        nameFilter.setDescription("Filter by name");
        addressFilter.setDescription("Filter by address");
        layout.setWidth(100, Unit.PERCENTAGE);
        hotelGrid.setWidth(100, Unit.PERCENTAGE);
        nameFilter.setPlaceholder("Filter by name");
        addressFilter.setPlaceholder("Filter by address");
        addHotel.setDescription("Add a new hotel");
        deleteHotel.setDescription("Delete selected hotel(s)");
        editHotel.setDescription("Edit selected hotel");

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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        updateList();
    }

}
