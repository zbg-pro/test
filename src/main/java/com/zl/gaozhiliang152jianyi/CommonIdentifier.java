package com.zl.gaozhiliang152jianyi;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/21 8:29 下午
 * @auth ALLEN
 */
public enum CommonIdentifier implements Identifier {

    Reader, Author, Admin
    ;

    @Override
    public boolean identify() {
        return false;
    }
}
