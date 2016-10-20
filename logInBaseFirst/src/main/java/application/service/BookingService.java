package application.service;

import java.util.Optional;

import application.entity.BookingAppl;
import application.entity.ClientOrganisation;
import application.entity.Event;

public interface BookingService {
	boolean deleteBooking(ClientOrganisation client, long id);
	
	boolean updateBooking(ClientOrganisation client, long bookingId, long unitId);

	Optional<BookingAppl> getBookingById(long id);
	
	boolean checkUnit(ClientOrganisation client, long unitId) ;
	
	boolean checkBooking(ClientOrganisation client, long id);
	
	long getUnitId(long id);
	
	long getEventId(long id);
}