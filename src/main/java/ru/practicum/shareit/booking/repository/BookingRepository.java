package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    List<Booking> findAllBookings();

    Optional<Booking> findBookingById(Long id);

    Booking createBooking(Booking booking);

    Booking updateBooking(Booking booking);

    void deleteBookingById(Long id);
}
