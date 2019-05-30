package com.interview.todo.controllers;

import com.interview.todo.cache.MemCache;
import com.interview.todo.consts.ErrorCodes;
import com.interview.todo.entity.User;
import com.interview.todo.models.UserModel;
import com.interview.todo.response.UserResponse;
import com.interview.todo.repositories.UserRepository;
import com.interview.todo.security.SecurityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository mUserRepository;

    @Autowired
    private SecurityWrapper mSecurityWrapper;

    @Autowired
    private MemCache mCache;

    private static Logger mLogger = Logger.getLogger("UserController");

    @RequestMapping("")
    public String index() {
        return "<html>" +
                "<h1 align=\"center\">" +
                "Welcome to User Rest APIs</h1><h2>Please use following apis to get the details" +
                "</h2>" +
                "<ul>" +
                "<li><b>POST <i>/user/create</i></b> --> Create new user</li>" +
                "<li><b>GET <i>/user{id}</i></b> --> Get the details of particular userId</li>" +
                "</ul>" +
                "</html>";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createNewUser(@RequestBody UserModel user) {
        mLogger.log(Level.INFO, "User Create Request with Body: "+user.toString());

        UserResponse response = new UserResponse();
        if (null == user.getName()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_NAME);
        } else if (null == user.getEmail()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_EMAIL);
        } else if (null == user.getPassword()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_PASSWORD);
        }else {
            //First check if user with this email already exists or not..
            User existingUser = mUserRepository.findUserByEmail(user.getEmail());
            if (null != existingUser) {
                response.setStatus(HttpStatus.CONFLICT);
                response.setError(ErrorCodes.ERROR_CODE_USER_EMAIL_EXISTS);
            } else {
                //Everything looks good. Now lets go and create new user..
                User userToCreate = new User();
                userToCreate.setName(user.getName());
                userToCreate.setEmail(user.getEmail());
                userToCreate.setUserId(UUID.randomUUID().toString());

                String encryptedPassword = mSecurityWrapper.encrypt(user.getPassword());
                mLogger.log(Level.INFO, "Encrypted User Password: "+encryptedPassword);
                userToCreate.setPassword(encryptedPassword);

                mUserRepository.save(userToCreate);

                response.setStatus(HttpStatus.CREATED);
                String accessToken = generateAccessToken();
                response.setAccessToken(accessToken);
                response.setUserId(userToCreate.getUserId());
                mCache.storeAccessToken(userToCreate.getUserId(), accessToken);

                response.setMessage("Successfully created new User");
            }
        }

        return new ResponseEntity<>(response.toString(), response.getStatus());
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserModel user) {
        mLogger.log(Level.INFO, "User Login Request with Body: "+user.toString());

        UserResponse response = new UserResponse();

        if (null == user.getEmail()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_EMAIL);
        } else if (null == user.getPassword()) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setError(ErrorCodes.ERROR_CODE_MISSING_PASSWORD);
        } else {
            User existingUser = mUserRepository.findUserByEmail(user.getEmail());
            if (null == existingUser) {
                response.setStatus(HttpStatus.UNAUTHORIZED);
                response.setError(ErrorCodes.ERROR_CODE_USER_DOES_NOT_EXISTS);
            } else {
                String plainPassword = mSecurityWrapper.decrypt(existingUser.getPassword());
                if (!plainPassword.equals(user.getPassword())) {
                    response.setStatus(HttpStatus.UNAUTHORIZED);
                    response.setError(ErrorCodes.ERROR_CODE_INVALID_PASSWORD);
                } else {
                    //Now user entered the correct password. Create access token and pass on the access token...
                    String accessToken = generateAccessToken();
                    mCache.storeAccessToken(existingUser.getUserId(), accessToken);
                    response.setStatus(HttpStatus.OK);
                    response.setUserId(existingUser.getUserId());
                    response.setMessage("Login Successful");
                    response.setAccessToken(accessToken);
                }
            }
        }

        return new ResponseEntity<>(response.toString(), response.getStatus());
    }

    private String generateAccessToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[40];
        random.nextBytes(bytes);
        String token = Base64.getEncoder().encodeToString(bytes);
        return token;
    }
}
