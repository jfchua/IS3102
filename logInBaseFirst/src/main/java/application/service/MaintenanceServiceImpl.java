package application.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.BookingAppl;
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Level;
import application.entity.Maintenance;
import application.entity.MaintenanceSchedule;
import application.entity.Unit;
import application.entity.Vendor;
import application.repository.BookingApplRepository;
import application.repository.MaintenanceRepository;
import application.repository.MaintenanceScheduleRepository;
import application.repository.UnitRepository;
import application.repository.UserRepository;
import application.repository.VendorRepository;
@Service
public class MaintenanceServiceImpl implements MaintenanceService {
	 private final UserRepository userRepository;
	 private final VendorRepository vendorRepository;
     private final MaintenanceRepository maintenanceRepository;
     private final MaintenanceScheduleRepository maintenanceScheduleRepository;
     private final UnitRepository unitRepository;
     private final BookingApplRepository bookingApplRepository;
     private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

 	@Autowired
 	public MaintenanceServiceImpl(VendorRepository vendorRepository, MaintenanceRepository maintenanceRepository, 
 			 UserRepository userRepository, UnitRepository unitRepository, BookingApplRepository bookingApplRepository,
 			 MaintenanceScheduleRepository maintenanceScheduleRepository) {
 		//super();
 		this.vendorRepository = vendorRepository;
 		this.unitRepository = unitRepository;
 		this.maintenanceRepository = maintenanceRepository;
 		this.userRepository = userRepository;
		this.bookingApplRepository = bookingApplRepository;
		this.maintenanceScheduleRepository= maintenanceScheduleRepository;
 	}
		
	@Override
	public boolean createMaintenance(ClientOrganisation client, String unitsId,String vendorsId, Date start, Date end, String description) {
		// TODO Auto-generated method stub
        Maintenance maint = new Maintenance();
        Set<MaintenanceSchedule> schedule = new HashSet<MaintenanceSchedule>();
        maint.setMaintenanceSchedule(schedule);
        String[] units = unitsId.split(" ");
  		System.out.println(units[0]);
  		String[] vendors = vendorsId.split(" ");
  		//check role
  		/*
  		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		for(User u: eventOrgs){
			 Set<Role> roles = u.getRoles();
			   for(Role r: roles){
			    if(r.getName().equals("ROLE_PROPERTY") && u.equals(eventOrg))
			    doesHave = true;
			   }
		}
		if(!doesHave)
			return false;*/
		//check start and end date
  		Date d1 = start;
		Date d2 = end;
		if(d1.compareTo(d2)>0)
			return false;

		boolean isAvailable = true;
		
		//check availability and checkUnit
		for(int i = 0; i<units.length; i ++){
			long uId = Long.valueOf(units[i]);
			Optional<Unit> unit1 = unitRepository.getUnitById(uId);
			if(unit1.isPresent()&&isAvailable){
				Unit unit = unit1.get();
				if(!checkUnit(client, unit.getId()))
					return false;
				int count = maintenanceScheduleRepository.getNumberOfMaintenanceSchedules(uId, d1, d2);
				int count2 = bookingApplRepository.getNumberOfBookings(uId, d1, d2);
				if((count != 0)||(count2!=0)){
					isAvailable = false;
					break;
				}
			}
		}


		if(isAvailable){
			 Set<Vendor> vendorList = new HashSet<Vendor>();
			for(int i = 0; i<vendors.length; i ++){
				long vId=Long.valueOf(vendors[i]);
	        Optional<Vendor> ven1 = vendorRepository.getVendorById(vId);
				if(ven1.isPresent()){
					Vendor vendor=ven1.get();
				System.out.println(vendor.getName());
				  if(client.getVendors().contains(vendor))
	        	  vendorList.add(vendor);
				  else
					  return false;
				}
			}
			for(int i = 0; i<units.length; i ++){
				long uId = Long.valueOf(units[i]);
				Optional<Unit> unit1 = unitRepository.getUnitById(uId);
				if(unit1.isPresent()&&isAvailable){
					Unit unit = unit1.get();	
					MaintenanceSchedule maintSchedule = new MaintenanceSchedule();
					maintSchedule.setStart_time(start);
					System.out.println("inside the controller");
					maintSchedule.setEnd_time(end);
					maintSchedule.setUnit(unit);
					maintSchedule.setMaintenance(maint);
					maintSchedule.setRoom(unit.getId());
					schedule.add(maintSchedule);
			        Set<MaintenanceSchedule> maintList = unit.getMaintenanceSchedule();
			        maintList.add(maintSchedule);
			        unit.setMaintenanceSchedule(maintList);		
				}
			}
        	  maint.setDescription(description);
        	  maint.setStart(start);
        	  maint.setEnd(end);        	 
        	  maint.setVendors(vendorList);
        	  maintenanceRepository.save(maint);      	  
			}
			return isAvailable;  
	}

	
	
	@Override
	public boolean editMaintenance(ClientOrganisation client, long id,String unitsId, String vendorsId, Date start, Date end, String description) {
		// TODO Auto-generated method stub
		String[] vendors = vendorsId.split(" ");
		/*
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		for(User u: eventOrgs){
			 Set<Role> roles = u.getRoles();
			   for(Role r: roles){
			    if(r.getName().equals("ROLE_PROPERTY") && u.equals(eventOrg))
			    doesHave = true;
			   }
		}
		if(!doesHave)
			return false;*/
		System.out.println("1");

		Date d1 = start;
		Date d2 = end;
		if(d1.compareTo(d2)>0)
			return false;
		System.out.println("1");
		boolean isAvailable = true;
		
		try{
			//Optional<Vendor> vendor1 = Optional.ofNullable(vendorRepository.findOne(vendorId));
	        Optional<Maintenance> maint1 = Optional.ofNullable(maintenanceRepository.findOne(id));
	    	if(maint1.isPresent()&&isAvailable){			
				Maintenance maint = maint1.get();
				Set<MaintenanceSchedule> maintenances = maint.getMaintenanceSchedule();
				Set<Unit> unitsOld = new HashSet<Unit>();
				for(MaintenanceSchedule m: maintenances)
					unitsOld.add(m.getUnit());

				String[] units = unitsId.split(" ");
				Set<Unit> unitsNew = new HashSet<Unit>();
                System.out.println(units.length);

                	//check availability of units
				for(int i = 0; i<units.length; i ++){
					System.out.println("inside the loop now");
				if(!checkUnit(client, Long.valueOf(units[i])))
					return false;
				Optional<Unit> unitNew = unitRepository.getUnitById(Long.valueOf(units[i]));
				if(unitNew.isPresent()&&(!unitsOld.contains(unitNew.get()))){	
					System.out.println("iffff");
					Unit unit1 = unitNew.get();		
					unitsNew.add(unit1);
					int count = maintenanceScheduleRepository.getNumberOfMaintenanceSchedules(Long.valueOf(units[i]), d1, d2);
					int count2 = bookingApplRepository.getNumberOfBookings(Long.valueOf(units[i]), d1, d2);
					System.out.println(count);
					if((count != 0)||(count2!=0)){
						isAvailable = false;
						break;
					}		
				}//DONE
				else if (unitNew.isPresent()&&(unitsOld.contains(unitNew.get()))){
				    Unit unit1 = unitNew.get();
				    unitsNew.add(unit1);
				    System.out.println("elseeee");
		            BookingAppl b = bookingApplRepository.getBookingEntity(Long.valueOf(units[i]), d1, d2);
		            MaintenanceSchedule m = maintenanceScheduleRepository.getMaintenanceScheduleEntity(Long.valueOf(units[i]), d1, d2);
		            //System.out.println(b.getId());
		            Maintenance maintFromSchedule = m.getMaintenance();
		            //Event eventFromB = b.getEvent(); 
		            //b is not null           
		            if(!(maint.getId().equals(maintFromSchedule.getId()))||(b!=null)){
		            	isAvailable = false;
		            	break;
		            }
				}
				}
				if(isAvailable){
			//check vendor 
			 Set<Vendor> vendorList = new HashSet<Vendor>();
			for(int i = 0; i<vendors.length; i ++){
	        Optional<Vendor> ven1 = vendorRepository.getVendorById(Long.valueOf(vendors[i]));
				if(ven1.isPresent()){
				System.out.println(ven1.get().getName());
				  if(client.getVendors().contains(ven1.get()))
	        	  vendorList.add(ven1.get());
				  else
					  return false;
				}
			}
			for(MaintenanceSchedule m1: maintenances){
					Unit unit = m1.getUnit();
					if(!unitsNew.contains(unit)){
					Set<MaintenanceSchedule> maintFromUnit = unit.getMaintenanceSchedule();
					maintFromUnit.remove(m1);
					//maintenances.remove(m1);
					maintenanceScheduleRepository.delete(m1);
					}
				}
			for(MaintenanceSchedule m2: maintenances){
				m2.setStart_time(start);
				m2.setEnd_time(end);
				
			}

			for(Unit i : unitsNew){
				if(!unitsOld.contains(i)){
					MaintenanceSchedule maintSchedule = new MaintenanceSchedule();
					maintSchedule.setStart_time(start);
					maintSchedule.setEnd_time(end);
					maintSchedule.setUnit(i);
					maintSchedule.setMaintenance(maint);
					maintSchedule.setRoom(i.getId());
					maintenances.add(maintSchedule);
			        Set<MaintenanceSchedule> maintList = i.getMaintenanceSchedule();
			        maintList.add(maintSchedule);
			        maintenances.add(maintSchedule);		
				}
			}
        	  maint.setDescription(description);
        	  maint.setStart(start);
        	  maint.setEnd(end);        	 
        	  maint.setVendors(vendorList);
        	  maint.setMaintenanceSchedule(maintenances);
        	  maintenanceRepository.flush();      	  
			}		
	    	}
		}catch(Exception e){
			return false;
		}
		return true; 
	}

	@Override
	public boolean deleteMaintenance(ClientOrganisation client, long id) {
		try{
			System.out.println("MAINTENANCESERVICE: START DELETING");
	        Optional<Maintenance> maintenance1 = Optional.ofNullable(maintenanceRepository.findOne(id));
	        //boolean doesHave = false;
			//Set<Building> buildings = client.getBuildings();
	        if(maintenance1.isPresent()){
	        	  Maintenance maint = maintenance1.get();
	        	  Set<MaintenanceSchedule> maintS = maint.getMaintenanceSchedule();
	        	  for(MaintenanceSchedule m: maintS){
	        	  	Unit unit = m.getUnit();
	        	  	Set<MaintenanceSchedule> maintS2 = unit.getMaintenanceSchedule();
	        	  	maintS2.remove(m);
	        	  	unit.setMaintenanceSchedule(maintS2);
	        	  	unitRepository.save(unit);
	        	  	//maintS.remove(m);
	        	  	maintenanceScheduleRepository.delete(m);
	        	  }
	        	  System.out.println("delete successfully");
	        	  maintenanceRepository.delete(maint);
	        		 
	        	} 
	        return true;
	        }catch(Exception e){
			return false;
		}
	
	}

	@Override
	public Set<Maintenance> getMaintenanceByUnitId(long unitId) {
		// TODO Auto-generated method stub
		Optional<Unit> unit1 = Optional.ofNullable(unitRepository.findOne(unitId));
		if(unit1.isPresent()){
			Unit unit = unit1.get();
			Set<MaintenanceSchedule> maints = unit.getMaintenanceSchedule();
			Set<Maintenance> maintenances = new HashSet<Maintenance>();
			for(MaintenanceSchedule m : maints)
				maintenances.add(m.getMaintenance());
			return maintenances;
		}
		return null;
	}
	
	@Override
	public String getUnitsId(long id) {
		String unitsId = "";	
		try{
			Optional<Maintenance> main1 = getMaintenanceById(id);
			if(main1.isPresent()){
		Maintenance maintenance = main1.get();	
		Set<MaintenanceSchedule> maints = maintenance.getMaintenanceSchedule();
		for(MaintenanceSchedule maintenanceS: maints){
	       Unit unitFromM = maintenanceS.getUnit();
			unitsId = unitsId + unitFromM.getId() +" ";
		}
	     }
		}catch(Exception e){
		
		}
     return unitsId;
	}
	
	@Override
	public String getVendorsId(long id) {
		String vendorsId = "";	
		try{
			Optional<Maintenance> main1 = getMaintenanceById(id);
			if(main1.isPresent()){
		Maintenance maintenance = main1.get();	
		Set<Vendor> vendorList = maintenance.getVendors();
		for(Vendor v: vendorList)
			vendorsId = vendorsId + v.getId() +" ";
	     }
		}catch(Exception e){
		
		}
     return vendorsId;
	}
	
	@Override
	public Optional<Maintenance> getMaintenanceById(long id) {
		LOGGER.debug("Getting maintenance={}", id);
		return Optional.ofNullable(maintenanceRepository.findOne(id));
	}
	
	@Override
	public Set<Maintenance> getAllMaintenance(ClientOrganisation client){
		Set<Building> buildings = client.getBuildings();
		Set<Maintenance> maint = new HashSet<Maintenance>();
		for(Building b: buildings){
			Set<Level> levels = b.getLevels();
			for(Level l: levels){
				Set<Unit> units = l.getUnits();
				for(Unit u : units){
					Set<MaintenanceSchedule> maints = u.getMaintenanceSchedule();
					for(MaintenanceSchedule m : maints)
						maint.add(m.getMaintenance());
				}
			}
		}
		return maint;
	}

	@Override
	public boolean checkMaintenance(ClientOrganisation client, long id) {
		boolean doesHave = false;
		try{
	   Optional<Maintenance> maintenance1 = Optional.ofNullable(maintenanceRepository.findOne(id));
	   if(maintenance1.isPresent()){
		   Maintenance maint = maintenance1.get();
		Set<Building> buildings = client.getBuildings();		
		for(Building b: buildings){
			Set<Level> levels = b.getLevels();
			for(Level l: levels){
				Set<Unit> units = l.getUnits();
				for(Unit unit : units){
					Set<MaintenanceSchedule> mS = unit.getMaintenanceSchedule();
					for(MaintenanceSchedule m: mS){
					if(m.getMaintenance().equals(maint))
						doesHave = true;
					}
				}
			}
		}
	      }
		}catch(Exception e){
			return false;
		}
		return doesHave;
	}

	@Override
	public boolean checkUnit(ClientOrganisation client, long unitId) {
		Set<Building> buildings = client.getBuildings();
		boolean doesHave = false;
		for(Building b: buildings){
			Set<Level> levels = b.getLevels();
			for(Level l : levels){
				Optional<Unit> unit1 = unitRepository.getUnitById(unitId);
				if(unit1.isPresent()&& l.getUnits().contains(unit1.get())){
					doesHave = true;
					break;
				}
			}
		}
		return doesHave;
	}

   @Override
	public Set<MaintenanceSchedule> getMaintenanceSchedule(ClientOrganisation client, long id) {
		Set<MaintenanceSchedule> bookings = null;
		try{
			Optional<Maintenance> maint1 = getMaintenanceById(id);
			if(maint1.isPresent()&&checkMaintenance(client, id)){
			Maintenance maint = maint1.get();
		    bookings = maint.getMaintenanceSchedule();
			}
		}catch(Exception e){
			
			}
		return bookings;
	}


}