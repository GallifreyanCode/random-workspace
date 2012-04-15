package be.gallifreyan.javaee.service.ejb;

import be.gallifreyan.javaee.entity.User;

public interface UserService {
	
	public User signupUser(User user) throws UserException;

	public void modifyPassword(ModifyPasswordRequest request)
			throws ModifyPasswordException;

	public void deleteUserAccount() throws UserException;

	public static final String MISMATCH_PROVIDED_AND_CURRENT_PASSWORDS = "AccountPreferences.MismatchProvidedAndCurrentPasswordsMessage";

	public static final String MISMATCH_NEW_AND_CONFIRMED_PASSWORDS = "AccountPreferences.MismatchNewAndConfirmedPasswordsMessage";

	public static final String SAME_OLD_AND_NEW_PASSWORDS = "AccountPreferences.SameOldAndNewPasswordsMessage";

	public static final String INTERNAL_ERROR_ON_PASSWORD_CHANGE = "AccountPreferences.InternalModifyErrorMessage";

	public static final String INTERNAL_ERROR_ON_DELETE = "AccountPreferences.InternalDeleteErrorMessage";

	public static final String DUPLICATE_USER = "Signup.DuplicateUserMessage";
}
