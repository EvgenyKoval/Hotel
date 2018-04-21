package com.gp.vaadincourse.entities;

import java.io.Serializable;
import java.util.Objects;

public class Hotel implements Serializable, Cloneable {
    private Long id;
    private String name = "";
    private String address = "";
    private Integer rating = 0;
    private Long operatesFrom = 0L;  //days from epoch day
    private HotelCategoryItem category;
    private String url = "";
    private String description = "";

    public Hotel() {
    }

    public boolean isPersisted() {
        return id != null;
    }

    @Override
    public Hotel clone() throws CloneNotSupportedException {
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getOperatesFrom() {
        return operatesFrom;
    }

    public void setOperatesFrom(Long operatesFrom) {
        this.operatesFrom = operatesFrom;
    }

    public HotelCategoryItem getCategory() {
        return category;
    }

    public void setCategory(HotelCategoryItem category) {
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
        return
                Objects.equals(name, hotel.name) &&
                        Objects.equals(address, hotel.address) &&
                        Objects.equals(rating, hotel.rating) &&
                        Objects.equals(operatesFrom, hotel.operatesFrom) &&
                        category == hotel.category &&
                        Objects.equals(url, hotel.url) &&
                        Objects.equals(description, hotel.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, rating, operatesFrom, category, url, description);
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rating=" + rating +
                ", operatesFrom=" + operatesFrom +
                ", category=" + category +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
