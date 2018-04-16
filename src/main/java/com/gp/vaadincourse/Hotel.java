package com.gp.vaadincourse;

import com.vaadin.ui.Link;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Hotel implements Serializable, Cloneable {
    private Long id;
    private String name;
    private String address;
    private String rating;
    private LocalDate operatesFrom;
    private HotelCategory category;
    private String url;
    private String description;

    public boolean isPersisted() {
        return id != null;
    }

    public Hotel() {
    }

    @Override
    protected Hotel clone() throws CloneNotSupportedException {
        return (Hotel) super.clone();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public LocalDate getOperatesFrom() {
        return operatesFrom;
    }

    public void setOperatesFrom(LocalDate operatesFrom) {
        this.operatesFrom = operatesFrom;
    }

    public HotelCategory getCategory() {
        return category;
    }

    public void setCategory(HotelCategory category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id) &&
                Objects.equals(name, hotel.name) &&
                Objects.equals(address, hotel.address) &&
                Objects.equals(rating, hotel.rating) &&
                Objects.equals(operatesFrom, hotel.operatesFrom) &&
                category == hotel.category &&
                Objects.equals(url, hotel.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, address, rating, operatesFrom, category, url);
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rating='" + rating + '\'' +
                ", operatesFrom=" + operatesFrom +
                ", category=" + category +
                ", url='" + url + '\'' +
                '}';
    }
}
