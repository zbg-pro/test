package com.zl.javabingfabianchengshizhan.xingnengyushengsuoxing11;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Allen.zhang
 * @title: AttributeStore
 * @projectName zl
 * @description: TODO
 * @date 2022/3/917:18
 */
@ThreadSafe
public class AttributeStore {

    @GuardedBy("this") private final Map<String, String>
            attributes = new HashMap<String, String>();

    public synchronized boolean userLocationMatches(String name,
                                                    String regexp) {
        String key = "users." + name + ".location";
        String location = attributes.get(key);
        if (location == null)
            return false;
        else
            return Pattern.matches(regexp, location);
    }



}
