package application.domain;

import org.hibernate.validator.constraints.NotEmpty;

public class EventCreateForm {

	@NotEmpty
	private String event_title = "";
	
	@NotEmpty
	private String event_content = "";
	
	@NotEmpty
	private String event_description = "";
	
	
	

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




	@Override
	    public String toString() {
	        return "UserCreateForm{" +
	                "title='" + event_title;
	 }
	               
}
