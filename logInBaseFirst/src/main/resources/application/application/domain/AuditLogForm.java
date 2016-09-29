package application.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class AuditLogForm {

	private String startDate;
	
	private String endDate;
	
	private String userEmail = "";

	
	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	private Date convertStringToDate(String str){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date parsed = new Date();
		try {
			parsed = format.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parsed;
	}
	
	
}
