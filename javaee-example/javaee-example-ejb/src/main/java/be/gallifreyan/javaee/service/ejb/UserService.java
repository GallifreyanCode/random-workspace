package be.gallifreyan.javaee.service.ejb;

import be.gallifreyan.javaee.entity.User;

/**
 * The application service that acts as a facade for actions relevant to the
 * User domain object.
 * 
 * A general note on exception handling for all checked exceptions thrown by the
 * facade:
 * 
 * When an exception is encountered, the exception may contain a set of
 * ConstraintViolation instances when bean validation of the inputs have failed.
 * 
 * Also, the message property of the exception may not contain a human-readable
 * message. Instead it would contain a key to a resource bundle that will be
 * used to display localized human readable messages to users.
 * 
 * Clients of this service must first parse the Set of ConstraintViolations, and
 * then parse the message property of the Exception to prepare human readable
 * message.
 * 
 * @author Vineet Reynolds
 * 
 */
public interface UserService
{
	/**
	 * Creates a new user.
	 * 
	 * @param user
	 *            The user to be created
	 * @return The persisted User instance
	 * @throws UserException
	 *             When bean validation of the input has failed, or when a user
	 *             already exists in the repository.
	 */
	public User signupUser(User user) throws UserException;

	/**
	 * Modifies the password of the user.
	 * 
	 * @param request
	 *            The password modification request containing the new and old
	 *            passwords of the user.
	 * @throws ModifyPasswordException
	 *             When bean validation of the input has failed, or
	 *             <ul>
	 *             <li>the new password is the same as the old password, or</li>
	 *             <li>the old password provided does not match the stored
	 *             password in the database</li>
	 *             </ul>
	 */
	public void modifyPassword(ModifyPasswordRequest request) throws ModifyPasswordException;

	/**
	 * Deletes the user account of the currently logged in user. This method
	 * cannot be used to delete other user accounts, as it relies on the
	 * Principal instance associated with the logged in user.
	 * 
	 * If the Principal does not exist in the repository, the account will not
	 * be deleted.
	 * 
	 * @throws UserException
	 *             When an error is encountered during deletion of the user from
	 *             the repository. This could be due to a missing account, but
	 *             not necessarily so.
	 */
	public void deleteUserAccount() throws UserException;

	/**
	 * The key for the message in the resource bundle to be shown when the
	 * password keyed by the user does not match the password in the database.
	 */
	public static final String MISMATCH_PROVIDED_AND_CURRENT_PASSWORDS = "AccountPreferences.MismatchProvidedAndCurrentPasswordsMessage";

	/**
	 * The key for the message in the resource bundle to be shown when the
	 * password keyed by the user does not match the confirm password also keyed
	 * in by the user.
	 */
	public static final String MISMATCH_NEW_AND_CONFIRMED_PASSWORDS = "AccountPreferences.MismatchNewAndConfirmedPasswordsMessage";

	/**
	 * The key for the message in the resource bundle to be shown when the user
	 * keys in a new password that is the same as the old password.
	 */
	public static final String SAME_OLD_AND_NEW_PASSWORDS = "AccountPreferences.SameOldAndNewPasswordsMessage";

	/**
	 * The key for the message in the resource bundle to be shown when an
	 * internal failure is encountered when modifying the user password.
	 */
	public static final String INTERNAL_ERROR_ON_PASSWORD_CHANGE = "AccountPreferences.InternalModifyErrorMessage";

	/**
	 * The key for the message in the resource bundle to be shown when an
	 * internal failure is encountered when deleting the user account.
	 */
	public static final String INTERNAL_ERROR_ON_DELETE = "AccountPreferences.InternalDeleteErrorMessage";

	/**
	 * The key for the message in the resource bundle to be shown when the user
	 * account already exists in the database, and another account with the same
	 * Id is being created.
	 */
	public static final String DUPLICATE_USER = "Signup.DuplicateUserMessage";
}
