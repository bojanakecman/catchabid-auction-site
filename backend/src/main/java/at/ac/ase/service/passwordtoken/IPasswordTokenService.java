package at.ac.ase.service.passwordtoken;

import at.ac.ase.entities.PasswordResetToken;

public interface IPasswordTokenService {

    /**
     * Method which retrieves a password reset token for the specific user
     * @param email an email address of the user
     * @param token a password reset token provided by user
     * @return a password reset token for specific user if found, null otherwise
     */
    PasswordResetToken getPasswordResetTokenByToken(String email, long token);
}
