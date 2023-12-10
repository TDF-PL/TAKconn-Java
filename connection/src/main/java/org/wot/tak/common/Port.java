package org.wot.tak.common;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
@EqualsAndHashCode
public final class Port {

    private final int number;

    /**
     * Creates TCP/UDP Port number.
     * @param number port number
     */
    public Port(int number) {
        if (number < 0 || number > 65535) {
            throw new IllegalArgumentException("Port number must be between 0 and 65535");
        }
        this.number = number;
    }

    /**
     * Creates TCP/UDP Port number.
     * @param number port number
     * @return Port object
     */
    public static Port of(int number) {
        return new Port(number);
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
