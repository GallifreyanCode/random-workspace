package be.gallifreyan.javaee.view.user;

import javax.validation.constraints.*;

public class SignupRequest
{
	@Size(min = 1, max = 50, message = "{SignupBean.userId.size}")
	@NotNull(message = "{SignupBean.userId.notNull}")
	private String userId;

	@Size(min = 1, max = 500, message = "{SignupBean.password.size}")
	@NotNull(message = "{SignupBean.password.notNull}")
	private char[] password;

	@Size(min = 1, max = 500, message = "{SignupBean.confirmPassword.size}")
	@NotNull(message = "{SignupBean.confirmPassword.notNull}")
	private char[] confirmPassword;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public char[] getPassword()
	{
		return password;
	}

	public void setPassword(char[] password)
	{
		this.password = password;
	}

	public char[] getConfirmPassword()
	{
		return confirmPassword;
	}

	public void setConfirmPassword(char[] confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}

}