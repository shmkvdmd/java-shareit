package ru.practicum.shareit.booking.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;

import java.util.*;

@Repository
public class InMemoryBookingRepository implements BookingRepository {
    private final Map<Long, Booking> bookings = new HashMap<>();

    @Override
    public List<Booking> findAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public Optional<Booking> findBookingById(Long id) {
        return Optional.ofNullable(bookings.get(id));
    }

    @Override
    public Booking createBooking(Booking booking) {
        Long id = booking.getId();
        bookings.put(id, booking);
        return bookings.get(id);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        Long id = booking.getId();
        bookings.put(id, booking);
        return bookings.get(id);
    }

    @Override
    public void deleteBookingById(Long id) {
        bookings.remove(id);
    }
}
