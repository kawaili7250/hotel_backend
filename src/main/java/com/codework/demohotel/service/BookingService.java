package com.codework.demohotel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codework.demohotel.exception.InvalidBookingRequestException;
import com.codework.demohotel.exception.ResourceNotFoundException;
import com.codework.demohotel.model.BookedRoom;
import com.codework.demohotel.model.Room;
import com.codework.demohotel.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService{
	private final BookingRepository bookingRepository;
	private final IRoomService roomService;

	public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
		// TODO Auto-generated method stub
		return bookingRepository.findByRoomId(roomId);
	}

	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		// TODO Auto-generated method stub
		return bookingRepository.findByBookingConfirmationCode(confirmationCode)
				.orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code: " + confirmationCode));
	}

	@Override
	public List<BookedRoom> getAllBookings() {
		// TODO Auto-generated method stub
		return bookingRepository.findAll();
	}

	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		// TODO Auto-generated method stub
		if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("Check-in date must be earlier than check-out date!");
		}
		Room room = roomService.getRoomById(roomId).get();
		List<BookedRoom> existingBookings = room.getBookings();
		boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
		if (roomIsAvailable) {
			room.addBooking(bookingRequest);
			bookingRepository.save(bookingRequest);
		} else {
			throw new InvalidBookingRequestException("Sorry, this room is not available for the selected dates.");
		}
		return bookingRequest.getBookingConfirmationCode();
	}

	@Override
	public void cancelBooking(Long bookingId) {
		// TODO Auto-generated method stub
		bookingRepository.deleteById(bookingId);
	}

	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
		// TODO Auto-generated method stub
		return existingBookings.stream().noneMatch(existingBooking -> 
		bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
		|| bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
		|| (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate()) && bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate()))
		|| (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()) && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
		|| (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate()) && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
		|| (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
		|| (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
		);
	}

	@Override
	public List<BookedRoom> getBookingsByUserEmail(String email) {
		// TODO Auto-generated method stub
		return bookingRepository.findByGuestEmail(email);
	}
}
