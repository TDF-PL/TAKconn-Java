package org.wot.tak.common;

public record Password(String password) {

    public static Password password(String password) {
        return new Password(password);
    }

    public static Password of(String password) {
        return new Password(password);
    }

    public static Password noPassword() {
        return new Password("");
    }

    public char[] asCharArray() {
        return password.toCharArray();
    }

    @Override
    public String toString() {
        return password;
    }
}
