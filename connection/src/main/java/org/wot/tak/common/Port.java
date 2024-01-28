package org.wot.tak.common;

public record Port(int number) {

    public Port {
        if (number < 0 || number > 65535) {
            throw new IllegalArgumentException("Port number must be between 0 and 65535");
        }
    }

    public static Port port(int number) {
        return new Port(number);
    }

    public static Port of(int number) {
        return new Port(number);
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
