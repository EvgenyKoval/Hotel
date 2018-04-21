package com.gp.vaadincourse;

import com.gp.vaadincourse.entities.Hotel;
import com.gp.vaadincourse.services.HotelService;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HotelServiceTest {
    private static final HotelService service = HotelService.getInstance();

    @Before
    public void setUp() {
        service.deleteAll();
        service.ensureTestData();
    }

    @Test
    public void findAll() {
        long count = service.count();
        assertEquals(count, service.findAll().size());
    }

    @Test
    public void findAll1() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Name");
        service.save(hotel);
        List<Hotel> hotels = service.findAll("test name");
        assertEquals(1, hotels.size());
        assertEquals(hotel, hotels.get(0));
    }

    @Test
    public void findAll2() {
        Hotel hotel = new Hotel();
        hotel.setName("Test Name");
        hotel.setAddress("Test address");
        service.save(hotel);
        List<Hotel> hotels = service.findAll("test name", "test address");
        assertTrue(hotels.size() == 1);
        assertEquals(hotel, hotels.get(0));
        hotels = service.findAll(null, null);
        assertEquals(service.count(), hotels.size());
    }

    @Test
    public void findAll3() {
        List<Hotel> hotels = service.findAll("", 0, 15);
        assertTrue(hotels.size() == 15);
        List<Hotel> hotels2 = service.findAll("", 15, 15);
        assertTrue(hotels2.size() <= 15);
        assertTrue(Collections.disjoint(hotels, hotels2));
    }

    @Test
    public void count() {
        int count = 23;
        assertEquals(service.count(), count);
        service.save(new Hotel());
        assertEquals(service.count(), (count + 1));
    }

    @Test
    public void delete() {
        int count = 23;
        assertEquals(service.count(), count);
        service.delete(service.findAll().get(0));
        assertEquals(service.count(), (count - 1));
    }

    @Test
    public void save() {
        long count = service.count();
        service.save(null);
        assertEquals(count, service.count());
    }

    @Test
    public void ensureTestData() {
    }

}