package com.interview.todo.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component("MemCache")
public class MemCache {
    private static Map<String, String> accessTokenMap = new HashMap<>();
    private Logger mLogger = Logger.getLogger("MemCache");

    public void storeAccessToken(String userId, String accessToken) {
        if (!accessTokenMap.containsKey(accessToken)) {
            mLogger.log(Level.INFO, "Setting the accessToken for user: "+userId +" accessToken: "+accessToken);
            accessTokenMap.put(accessToken, userId);
        }
    }

    public String validateAccessToken(String accessToken) {
        mLogger.log(Level.INFO, "Validating the access token: "+accessToken);
        if (accessTokenMap.containsKey(accessToken)) {
            mLogger.log(Level.INFO, "AccessToken is valid.");
            return accessTokenMap.get(accessToken);
        }
        mLogger.log(Level.INFO, "AccessToken is invalid. accessTokenMap size: "+accessTokenMap.size());

        return null;
    }
}
