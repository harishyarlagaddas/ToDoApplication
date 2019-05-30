package com.interview.todo.console.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component("MemCache")
public class MemCache {
    private static Map<String, String> accessTokenMap = new HashMap<>();
    private Logger mLogger = Logger.getLogger("MemCache");

    public void store(String key, String accessToken) {
        accessTokenMap.put(key, accessToken);
    }

    public String get(String key) {
        if (accessTokenMap.containsKey(key)) {
            return accessTokenMap.get(key);
        }
        mLogger.log(Level.INFO, "No stored value for key: "+key);
        return null;
    }
}
