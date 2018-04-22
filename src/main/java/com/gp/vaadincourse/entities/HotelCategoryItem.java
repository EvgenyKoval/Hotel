package com.gp.vaadincourse.entities;

import java.io.Serializable;
import java.util.Objects;

public class HotelCategoryItem implements Serializable, Cloneable {

    private String name;

    public HotelCategoryItem() {
    }

    public HotelCategoryItem(String name) {
        this.name = name;
    }

    @Override
    public HotelCategoryItem clone() throws CloneNotSupportedException {
        return (HotelCategoryItem) super.clone();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelCategoryItem that = (HotelCategoryItem) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return name;
    }

}
