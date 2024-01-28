package org.wot.tak.common;

public final class Path {

    private Path() {
    }

    public static java.nio.file.Path path(String path) {
        return java.nio.file.Path.of(path);
    }

    public static java.nio.file.Path noPath() {
        return java.nio.file.Path.of("");
    }
}
