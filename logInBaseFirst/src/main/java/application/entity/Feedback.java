package application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "feedback")
public class Feedback {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date feedbackDate;
	
	private String message;
	private String category;
	private String imageFilePath;
	
	@OneToOne
	private User user;
	
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public java.util.Date getfeedbackDate() {
		return feedbackDate;
	}
	public void setTfeedbackDate(java.util.Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getImageFilePath() {
		return imageFilePath;
	}
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	public java.util.Date getFeedbackDate() {
		return feedbackDate;
	}
	public void setFeedbackDate(java.util.Date feedbackDate) {
		this.feedbackDate = feedbackDate;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	

	
}
