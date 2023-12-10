package org.wot.tak.connection;

import com.sun.istack.NotNull;
import lombok.Value;

import java.util.UUID;

@SuppressWarnings("RedundantModifiersValueLombok")
@Value
public class UID {
    @NotNull private String uid;

    public static UID random() {
        return new UID(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return uid;
    }
}
