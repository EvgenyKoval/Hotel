package com.gp.vaadincourse.services;

import com.gp.vaadincourse.entities.HotelCategory;
import com.gp.vaadincourse.entities.HotelCategoryItem;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    private static CategoryService instance = getInstance();

    private final Set<HotelCategoryItem> categories = new HashSet<>();

    private CategoryService() {
    }

    public static CategoryService getInstance() {
        if (instance == null) {
            instance = new CategoryService();
            instance.saveExistingCategories();
        }
        return instance;
    }

    private void saveExistingCategories() {
        for (HotelCategory category : HotelCategory.values()) {
            categories.add(new HotelCategoryItem(category.name()));
        }
    }

    public synchronized HotelCategoryItem findByName(String name) {
        if (name != null)
            for (HotelCategoryItem item : categories) {
                if (item.getName().equals(name)) {
                    return item;
                }
            }

        return null;
    }

    public synchronized Set<HotelCategoryItem> findAll() {
        return categories;
    }

    public synchronized void save(HotelCategoryItem category) {
        if (category == null) {
            LOGGER.error("Category is null");
            return;
        }
        categories.add(category);
    }

    public synchronized void delete(HotelCategoryItem item) {
        categories.remove(item);
        item.setName(null);
    }

    public synchronized int count() {
        return categories.size();
    }

    public boolean exists(String s) {
        return findByName(s) != null;
    }
}
