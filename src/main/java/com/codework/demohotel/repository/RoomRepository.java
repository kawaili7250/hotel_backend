package com.codework.demohotel.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.codework.demohotel.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long>{
	@Query("SELECT DISTINCT r.roomType from Room r")
	List<String> findDistinctRoomTypes();

	@Query("SELECT r from Room r " +
	"WHERE r.roomType LIKE %:roomType% " +
	"AND r.id NOT IN (" +
	"SELECT br.room.id from BookedRoom br " +
	"WHERE ((br.checkInDate <= :checkOutDate) AND (br.checkOutDate >= :checkInDate))" +
	")")
	List<Room> findAvailableRoomByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
