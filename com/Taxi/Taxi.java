package com.Taxi;

import java.util.ArrayList;
import java.util.List;

import com.Booking.Booking;

public class Taxi {
    public int id;
    public char currentPoint = 'A';
    public int totalEarnings = 0;
    public List<Booking> bookings = new ArrayList<>();

    public Taxi(int id) {
        this.id = id;
    }

    public boolean isAvailable(int requestTime) {
        if (bookings.isEmpty())
            return true;
        Booking lastBooking = bookings.get(bookings.size() - 1);
        return lastBooking.dropTime <= requestTime;
    }

    public int calculateEarnings(char from, char to) {
        int distance = Math.abs(to - from) * 15;
        return 100 + Math.max(0, (distance - 5) * 10);
    }

    public void addBooking1(Booking booking) {
        bookings.add(booking);
        totalEarnings += booking.amount;
        currentPoint = booking.to;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        totalEarnings += booking.amount;
        currentPoint = booking.to;
    }

}
