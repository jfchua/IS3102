package application.domain;


public class ResetPasswordForm {

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	@Override
	public String toString() {
		return "resetPasswordForm{" +
				"Email='" + email +
				'}';
	}
}



