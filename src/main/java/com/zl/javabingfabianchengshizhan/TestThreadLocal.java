package com.zl.javabingfabianchengshizhan;

import jdk.nashorn.internal.ir.annotations.Immutable;

/**
 * @author Allen.zhang
 * @title: TestThreadLocal
 * @projectName zl
 * @description: TODO
 * @date 2021/11/23:51
 */
public class TestThreadLocal {

    private static ThreadLocal<User> threadSession = new ThreadLocal<User>();

    public User getUser(){
        User user = threadSession.get();
        try {
            if (user == null) {
                user = new User();
                user.setId(123);
                user.setUsername("张思密达");
                threadSession.set(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }


    public static class User {
        private int id;
        private String username;

        public User(){}


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}


