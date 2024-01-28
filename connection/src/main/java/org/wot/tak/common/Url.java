package org.wot.tak.common;

public record Url(String url) {

    public static Url url(String url) {
        return new Url(url);
    }

    public static Url of(String url) {
        return new Url(url);
    }

    @Override
    public String toString() {
        return url;
    }
}
