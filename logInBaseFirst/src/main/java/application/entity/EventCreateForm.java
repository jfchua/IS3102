package application.entity;

import org.hibernate.validator.constraints.NotEmpty;

//This class is shared between event manager and event organiser.
public class EventCreateForm {

	@NotEmpty
	private String event_title = "";
	
	@NotEmpty
	private String event_content = "";
	
	@NotEmpty
	private String event_description = "";
	
	@NotEmpty
	private String event_approval_status = "";
	
	@NotEmpty
	private String event_organisation = "";
	
	@NotEmpty
	private String event_start_date = "";
	
	@NotEmpty
	private String event_end_date = "";
	
	@NotEmpty
	private String event_period = "";

	public String getEvent_title() {
		return event_title;
	}

	public void setEvent_title(String event_title) {
		this.event_title = event_title;
	}




	public String getEvent_content() {
		return event_content;
	}




	public void setEvent_content(String event_content) {
		this.event_content = event_content;
	}




	public String getEvent_description() {
		return event_description;
	}

	public void setEvent_description(String event_description) {
		this.event_description = event_description;
	}
	public String getEvent_approval_status() {
		return event_approval_status;
	}

	public void setEvent_approval_status(String event_approval_status) {
		this.event_approval_status = event_approval_status;
	}
	
	
	public String getEvent_organisation() {
		return event_organisation;
	}

	public void setEvent_organisation(String event_organisation) {
		this.event_organisation = event_organisation;
	}

	
	
	public String getEvent_start_date() {
		return event_start_date;
	}

	public void setEvent_start_date(String event_start_date) {
		this.event_start_date = event_start_date;
	}

	
	public String getEvent_end_date() {
		return event_end_date;
	}

	public void setEvent_end_date(String event_end_date) {
		this.event_end_date = event_end_date;
	}

	
	public String getEvent_period() {
		return event_period;
	}

	public void setEvent_period(String event_period) {
		this.event_period = event_period;
	}
	
	@Override
	    public String toString() {
	        return "UserCreateForm{" +
	                "title='" + event_title;
	 }
	               
}
