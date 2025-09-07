package com.codework.demohotel.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codework.demohotel.exception.InternalServerException;
import com.codework.demohotel.exception.ResourceNotFoundException;
import com.codework.demohotel.model.Room;
import com.codework.demohotel.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import java.sql.Blob;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
	
	private final RoomRepository roomRepository;

	@Override
	public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
		// TODO Auto-generated method stub
		Room room = new Room();
		room.setRoomType(roomType);
		room.setRoomPrice(roomPrice);
		if (!file.isEmpty()) {
			byte[] photoBytes = file.getBytes();
			Blob photoBlob = new SerialBlob(photoBytes);
			room.setPhoto(photoBlob);
		}
		return roomRepository.save(room);
	}

	@Override
	public List<String> getAllRoomTypes() {
		// TODO Auto-generated method stub
		
		return roomRepository.findDistinctRoomTypes();
	}

	@Override
	public List<Room> getAllRooms() {
		// TODO Auto-generated method stub
		return roomRepository.findAll();
	}

	@Override
	public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
		// TODO Auto-generated method stub
		Optional<Room> theRoom = roomRepository.findById(roomId);
		if (theRoom.isEmpty()) {
			throw new ResourceNotFoundException("Sorry, room not found!");
		}
		Blob photoBlob = theRoom.get().getPhoto();
		if (photoBlob != null) {
			return photoBlob.getBytes(1, (int)photoBlob.length());
		}
		return null;
	}

	@Override
	public void deleteRoom(Long roomId) {
		// TODO Auto-generated method stub
		Optional<Room> theRoom = roomRepository.findById(roomId);
		if (theRoom.isPresent()) {
			roomRepository.deleteById(roomId);
		}
	}

	@Override
	public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
		// TODO Auto-generated method stub
		Room room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
		if (roomType != null) {
			room.setRoomType(roomType);
		}
		if (roomPrice != null) {
			room.setRoomPrice(roomPrice);
		}
		if (photoBytes != null && photoBytes.length > 0) {
			try {
				room.setPhoto(new SerialBlob(photoBytes));
			} catch(SQLException ex) {
				throw new InternalServerException("Error updating room");
			}
		}
		return roomRepository.save(room);
	}

	@Override
	public Optional<Room> getRoomById(Long roomId) {
		// TODO Auto-generated method stub
		return Optional.of(roomRepository.findById(roomId).get());
	}

	@Override
	public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
		// TODO Auto-generated method stub
		return roomRepository.findAvailableRoomByDateAndType(checkInDate, checkOutDate, roomType);
	}
	
		
}
