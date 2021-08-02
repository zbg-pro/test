package com.zl.gaozhiliang152jianyi.decorate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/24 1:38 下午
 * @auth ALLEN
 */
public abstract class AbsPopulator {

    public final void dataInitialing(){

        Method[] methods = getClass().getMethods();
        List<Method> list = Arrays.stream(methods).filter(e -> isInitDataMethod(e)).collect(Collectors.toList());

        list.forEach(e -> {
            try {
                e.invoke(this);
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            } catch (InvocationTargetException invocationTargetException) {
                invocationTargetException.printStackTrace();
            }
        });

    }

    private boolean isInitDataMethod(Method method) {

        return method.getName().startsWith("init")
                && Modifier.isPublic(method.getModifiers())
                && method.getReturnType().equals(Void.TYPE)
                && !method.isVarArgs()
                && !Modifier.isAbstract(method.getModifiers());


    }
}

class UserPopuator extends AbsPopulator{

    public static void initUser() {
        System.out.println("UserPopuator initUser()...");
    }

    public static void initPassword() {
        System.out.println("UserPopuator initPassword()...");
    }

    public static void initJobs() {
        System.out.println("UserPopuator initJobs()...");
    }

    public static void main(String[] args) {
        UserPopuator userPopuator = new UserPopuator();
        userPopuator.dataInitialing();
    }
}
