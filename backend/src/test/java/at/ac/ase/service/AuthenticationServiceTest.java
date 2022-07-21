package at.ac.ase.service;

import at.ac.ase.basetest.BaseIntegrationTest;
import at.ac.ase.entities.RegularUser;
import at.ac.ase.service.auth.IAuthService;
import at.ac.ase.service.auth.IRegisterService;

import at.ac.ase.util.exceptions.AuthorizationException;
import at.ac.ase.util.exceptions.UserNotFoundException;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class AuthenticationServiceTest extends BaseIntegrationTest {

    @Autowired
    IRegisterService registerService;

    @Autowired
    IAuthService authService;

    @Before
    public void setUp(){
        RegularUser regularUser = new RegularUser();
        regularUser.setFirstName("firstName");
        regularUser.setLastName("lastName");
        regularUser.setEmail("test@gmail.com");
        regularUser.setPasswordHash("password");
        registerService.registerUser(regularUser);
    }

    @After
    public void cleanup() {cleanDatabase();}

    @Test
    public void test_authenticateRegularUser(){
        Map<String, String> map = new HashMap<>();
        map.put("email", "test@gmail.com");
        map.put("password", "password");
        JSONObject jsonObject = authService.authenticate(map);
        assertNotNull(jsonObject.get("token"));
    }

    @Test (expected = UserNotFoundException.class)
    public void test_authenticateNonExistingUser(){
        Map<String, String> map = new HashMap<>();
        map.put("email", "tt@gmail.com");
        map.put("password", "password");
        JSONObject jsonObject = authService.authenticate(map);
    }

    @Test (expected = AuthorizationException.class)
    public void test_authenticateUserWithWrongPassword(){
        Map<String, String> map = new HashMap<>();
        map.put("email", "test@gmail.com");
        map.put("password", "wrongPassword");
        JSONObject jsonObject = authService.authenticate(map);
    }
}
