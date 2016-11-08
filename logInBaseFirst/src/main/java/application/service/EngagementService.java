package application.service;

import java.util.Set;

import application.entity.Beacon;
import application.entity.Discount;
import application.entity.User;
import application.exception.EventNotFoundException;

public interface EngagementService {

	
	public void setFeedback(User usr,Long eventId, String cat, String msg);
	
	public Discount getDiscount(String code);
	
	public Set<Discount> getDiscounts(Long eventId) throws EventNotFoundException;

	public boolean deleteDiscount(User user, Long valueOf);

	boolean addDiscount(String email, Long eventId, String retailerName, String message) throws EventNotFoundException;

	boolean updateDiscount(Long discountId, String name, String msg);
	
	boolean addBeacon(Long eventId, String uuid, String msg) throws EventNotFoundException;
	
	boolean updateBeacon(Long beaconId, String msg);
	
	boolean deleteBeacon(User user, Long beaconId) throws EventNotFoundException;

	public Set<Beacon> getBeacons(Long eventId) throws EventNotFoundException;
}

