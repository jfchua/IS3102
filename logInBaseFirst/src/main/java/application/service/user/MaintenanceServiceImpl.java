package application.service.user;

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

import application.domain.Building;
import application.domain.ClientOrganisation;
import application.domain.Level;
import application.domain.Maintenance;
import application.domain.Unit;
import application.domain.Vendor;
import application.repository.MaintenanceRepository;
import application.repository.UnitRepository;
import application.repository.VendorRepository;
@Service
public class MaintenanceServiceImpl implements MaintenanceService {

	 private final VendorRepository vendorRepository;
     private final MaintenanceRepository maintenanceRepository;
     private final UnitRepository unitRepository;
     private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

 	@Autowired
 	public MaintenanceServiceImpl(VendorRepository vendorRepository, MaintenanceRepository maintenanceRepository, 
 			UnitRepository unitRepository) {
 		//super();
 		this.vendorRepository = vendorRepository;
 		this.unitRepository = unitRepository;
 		this.maintenanceRepository = maintenanceRepository;
 	}
		
	@Override
	public boolean createMaintenance(ClientOrganisation client, String unitsId,String vendorsId, Date start, Date end, String description) {
		// TODO Auto-generated method stub
          Maintenance maint = new Maintenance();
          String[] units = unitsId.split(" ");
  		  System.out.println(units[0]);
  		  String[] vendors = vendorsId.split(" ");
  		Date d1 = start;
		Date d2 = end;
		if(d1.compareTo(d2)>0)
			return false;
		boolean isAvailable = true;
		boolean doesHave = false;
		Set<Building> buildings = client.getBuildings();
		for(int i = 0; i<units.length; i ++){
			long uId = Long.valueOf(units[i]);
			Optional<Unit> unit1 = unitRepository.getUnitById(uId);
			System.out.println(uId);
			System.out.println(unit1.isPresent());
			if(unit1.isPresent()&&isAvailable){
				System.out.println("inside if");
				Unit unit = unit1.get();				
				Level level = unit.getLevel();
				for(Building b: buildings){
					if(b.getLevels().contains(level)){
						doesHave = true;
						break;
					}
				}
				if(!doesHave){
					isAvailable = false;
					break;
				}
				System.out.println("get unit");
				maint.getUnits().add(unit);
				System.out.println("add unit lol");
				Set<Maintenance> maintFromUnit = unit.getMaintenances();
				maintFromUnit.add(maint);
				unit.setMaintenances(maintFromUnit);	
				unit.createList();
				ArrayList<Date> avail = unit.getAvail();		
				int diffInDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
				System.out.println(d1);
				System.out.println(d2);
				System.out.println(diffInDays);
				for(int startNum = 0; startNum <= diffInDays; startNum ++){
					System.out.println("inside the loop");
					if ( avail != null ) System.err.println("not null");
					if(avail.contains(d1)){
						isAvailable = false;
						break;
					}
					else{
						System.out.println("else");
					unit.getAvail().add(d1);
				    Date nextDay = new Date(d1.getTime() + (1000 * 60 * 60 * 24));
					d1 = nextDay;	
					}
					System.out.println("inside the loop for add");
				}
				unit.setAvail(avail);
				System.out.println("save unit");
				System.out.println("saved by repo");
			}
			else{
				System.out.println("no units");
			}
		}
		if(isAvailable){
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
        	  maint.setDescription(description);
        	  maint.setStart(start);
        	  maint.setEnd(end);        	 
        	  maint.setVendors(vendorList);
        	  maintenanceRepository.save(maint);      	  
			}
			return isAvailable;  
	}

	
	
	@Override
	public boolean editMaintenance(ClientOrganisation client, long id,String vendorsId, Date start, Date end, String description) {
		// TODO Auto-generated method stub
		  String[] vendors = vendorsId.split(" ");
		Date d1 = start;
		Date d2 = end;
		if(d1.compareTo(d2)>0)
			return false;
		boolean isAvailable = true;
		int diffInDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
		Calendar c = Calendar.getInstance();
		try{
			//Optional<Vendor> vendor1 = Optional.ofNullable(vendorRepository.findOne(vendorId));
	        Optional<Maintenance> maint1 = Optional.ofNullable(maintenanceRepository.findOne(id));
	    	if(maint1.isPresent()&&isAvailable){			
				Maintenance maint = maint1.get();
				Date d3 = maint.getStart();
				Date d4 = maint.getEnd();
			    int diffInDays2 = (int) ((d4.getTime() - d3.getTime()) / (1000 * 60 * 60 * 24));
				Set<Unit> units = maint.getUnits();
				boolean doesHave = false;
				Set<Building> buildings = client.getBuildings();
				for(Unit u: units){
					ArrayList<Date> avail = u.getAvail();
					//ArrayList<Date> newAvail = new ArrayList<Date>();				
					Level level = u.getLevel();
					for(Building b: buildings){
						if(b.getLevels().contains(level)){
							doesHave = true;
							break;
						}
					}
					if(!doesHave){
						isAvailable = false;
						break;
					}
					for(int startNum = 0; startNum <= diffInDays; startNum ++){
						if(avail.contains(d1)){
						if(d1.before(d3) || d1.after(d4)){
						isAvailable = false;
						break;
							}
						}
						else{
						avail.add(d1);
						c.setTime(d1); 
						c.add(Calendar.DATE, 1);
						d1 = c.getTime();
						}						
					}	
					unitRepository.save(u);
				}
				if(isAvailable)	{
				Set<Vendor> vendorList = new HashSet<Vendor>();
				Set<Vendor> vendorListOld = maint.getVendors();
					for(int i = 0; i<vendors.length; i ++){
			      Optional<Vendor> ven1 = vendorRepository.getVendorById(Long.valueOf(vendors[i]));
						if(ven1.isPresent()&&(!vendorListOld.contains(ven1))){
							if(client.getVendors().contains(ven1.get()))
					        	  vendorList.add(ven1.get());
								  else
									  return false;
			        	  vendorList.add(ven1.get());
						}
					}
				maint.setDescription(description);
		        maint.setStart(start);
		        maint.setEnd(end);        	
		        maint.setVendors(vendorList);
	        	maintenanceRepository.save(maint);  						
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
	        Optional<Maintenance> maintenance1 = Optional.ofNullable(maintenanceRepository.findOne(id));
	        boolean doesHave = false;
			Set<Building> buildings = client.getBuildings();
	        if(maintenance1.isPresent()){
	        	  Maintenance maint = maintenance1.get();
	        	  Set<Unit> units = maint.getUnits();
	        	  for(Unit u: units){
						ArrayList<Date> avail = u.getAvail();
						Level level = u.getLevel();
						for(Building b: buildings){
							if(b.getLevels().contains(level)){
								doesHave = true;
								break;
							}
						}
					    if(!doesHave)
					    	return false;
					    else{
						Date d1 = maint.getStart();
						Date d2 = maint.getEnd();
						int diffInDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
						Calendar c = Calendar.getInstance(); 		
						for(int startNum = 0; startNum <= diffInDays; startNum ++){
							avail.remove(d1);
							c.setTime(d1); 
							c.add(Calendar.DATE, 1);
							d1 = c.getTime();
						}
						Set<Maintenance> maintFromUnit = u.getMaintenances();
						maintFromUnit.remove(maint);
						u.setMaintenances(maintFromUnit);
					}
	        	  }
	        	  System.out.println("delete successfully");
	        	  maintenanceRepository.delete(maint);
	        }
		}catch(Exception e){
			return false;
		}
		return true; 
	}

	@Override
	public Set<Maintenance> getMaintenanceByUnitId(long unitId) {
		// TODO Auto-generated method stub
		Optional<Unit> unit1 = Optional.ofNullable(unitRepository.findOne(unitId));
		if(unit1.isPresent()){
			Unit unit = unit1.get();
			return unit.getMaintenances();
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
		Set<Unit> unitList = maintenance.getUnits();
		for(Unit u : unitList)
			unitsId = unitsId + u.getId() +" ";
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
					Set<Maintenance> maints = u.getMaintenances();
					for(Maintenance m : maints)
						maint.add(m);
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
				for(Unit u : units){
					if(u.getMaintenances().contains(maint))
						doesHave = true;
				}
			}
		}
	      }
		}catch(Exception e){
			return false;
		}
		return doesHave;
	}

}
