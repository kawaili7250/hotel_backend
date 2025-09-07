package com.codework.demohotel.service;

import java.util.List;

import com.codework.demohotel.model.BookedRoom;

public interface IBookingService {
	
	BookedRoom findByBookingConfirmationCode(String confirmationCode);

	List<BookedRoom> getAllBookings();
	
	List<BookedRoom> getAllBookingsByRoomId(Long roomId);
	
	List<BookedRoom> getBookingsByUserEmail(String email);

	String saveBooking(Long roomId, BookedRoom bookingRequest);

	void cancelBooking(Long bookingId);

}
