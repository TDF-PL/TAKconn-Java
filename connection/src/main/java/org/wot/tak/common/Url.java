package org.wot.tak.common;

import lombok.Value;

@SuppressWarnings("RedundantModifiersValueLombok")
@Value
public class Url {
    private final String url;

    public static Url of(String url) {
        return new Url(url);
    }

    @Override
    public String toString() {
        return url;
    }
}
