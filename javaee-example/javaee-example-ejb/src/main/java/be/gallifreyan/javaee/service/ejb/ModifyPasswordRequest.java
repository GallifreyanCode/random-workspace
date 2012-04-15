package be.gallifreyan.javaee.service.ejb;

import javax.validation.constraints.*;

public class ModifyPasswordRequest {

	@NotNull(message = "{ModifyPasswordRequest.oldPassword.notNull}")
	@Size(min = 1, max = 500, message = "{ModifyPasswordRequest.oldPassword.size}")
	private char[] oldPassword;

	@NotNull(message = "ModifyPasswordRequest.newPassword.notNull")
	@Size(min = 1, max = 500, message = "ModifyPasswordRequest.newPassword.size")
	private char[] newPassword;

	@NotNull(message = "ModifyPasswordRequest.confirmNewPassword.notNull")
	@Size(min = 1, max = 500, message = "ModifyPasswordRequest.confirmNewPassword.size")
	private char[] confirmNewPassword;

	public ModifyPasswordRequest() {
	}

	public ModifyPasswordRequest(char[] oldPassword, char[] newPassword,
			char[] confirmNewPassword) {
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
		this.confirmNewPassword = confirmNewPassword;
	}

	public char[] getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(char[] oldPassword) {
		this.oldPassword = oldPassword;
	}

	public char[] getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(char[] newPassword) {
		this.newPassword = newPassword;
	}

	public char[] getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(char[] confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
}
