package org.wot.tak.common;

public record UserName(String userName) {

    public static UserName userName(String userName) {
        return new UserName(userName);
    }

    public static UserName of(String userName) {
        return new UserName(userName);
    }

    public static UserName noUserName() {
        return new UserName("");
    }

    @Override
    public String toString() {
        return userName;
    }
}
