package at.ac.ase.service;

import at.ac.ase.basetest.BaseIntegrationTest;
import at.ac.ase.dto.AuctionPostSendDTO;
import at.ac.ase.entities.*;
import at.ac.ase.service.auction.IAuctionService;
import at.ac.ase.service.passwordtoken.IPasswordTokenService;
import at.ac.ase.service.user.IAuctionHouseService;
import at.ac.ase.util.exceptions.UserNotFoundException;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


public class PasswordTokenServiceTest extends BaseIntegrationTest {

    @Autowired
    IPasswordTokenService passwordTokenService;

    @After
    public void cleanup() {
        cleanDatabase();
    }

    @Test
    public void testServiceNotNull() {
        assertNotNull(passwordTokenService);
    }

    @Test
    @Transactional
    public void testGetRecentAuctions() {
        insertTestData("multiple-auctions.sql");
    }

    @Test
    @Transactional
    public void testGetValidPasswordResetToken() {
        insertTestData("password-reset-token.sql");
        PasswordResetToken token = passwordTokenService.getPasswordResetTokenByToken("test@test.com", 666666);
        assertNotNull(token);
    }

    @Test
    @Transactional
    public void testGetPasswordResetTokenExpiredToken() {
        insertTestData("password-reset-token.sql");
        PasswordResetToken token = passwordTokenService.getPasswordResetTokenByToken("test2@test.com", 666666);
        assertNull(token);
    }

    @Test
    @Transactional
    public void testGetPasswordResetTokenInvalidToken() {
        insertTestData("password-reset-token.sql");
        PasswordResetToken token = passwordTokenService.getPasswordResetTokenByToken("test@test.com", 555555);
        assertNull(token);
    }

    @Test(expected = UserNotFoundException.class)
    @Transactional
    public void testGetPasswordResetTokenInvalidEmail() {
        insertTestData("password-reset-token.sql");
        passwordTokenService.getPasswordResetTokenByToken("invalid@test.com", 555555);
    }
}

