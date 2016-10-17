package application.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class SendMessageForm {

	@NotEmpty
	private String recipient = "";
	@NotNull
	@NotEmpty
	private String message = "";

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "sendMessageForm{" +
				"Recipient='" + recipient +
				", Message=" + message +
				'}';
	}
}
