package com.zl.javabingfabianchengshizhan.xingnengyushengsuoxing11;

import net.jcip.annotations.GuardedBy;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Allen.zhang
 * @title: BetterAttributeStore
 * @projectName zl
 * @description: TODO
 * @date 2022/3/920:40
 */
public class BetterAttributeStore {

    @GuardedBy("this")
    private final Map<String, String> attributes = new HashMap<>();

    private boolean userLocationMatches(String username, String regexp){
        String key = "user." + username + ".location";

        String location;
        synchronized (this) {
            location = attributes.get(key);
        }

        if (location == null)
            return false;

        return Pattern.matches(regexp, location);
    }

}
