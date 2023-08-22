package org.wot.tak.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EventReader {
    public static String readFrom(InputStream in) throws IOException {
        var bytes = new ByteArrayOutputStream();
        var end = false;
        var open_tags = 0;
        var started = false;
        var last = -1;
        while (!end) {
            var b = in.read();
            if (b == -1) {
                return null;
            }
            bytes.write(b);
            if (b == '<') {
                open_tags++;
                started = true;
            }
            if (b == '>' && last == '/') open_tags--;
            if (b == '/' && last == '<') {
                open_tags -= 2;
                do {
                    b = in.read();
                    if (b == -1) {
                        return null;
                    }
                    bytes.write(b);
                } while (b != '>');
            }
            if (b == '?' && last == '<') {
                do {
                    b = in.read();
                    if (b == -1) {
                        return null;
                    }
                    bytes.write(b);
                } while (b != '<');
            }
            if (open_tags == 0 && started) {
                end = true;
            }
            last = b;
        }
        return bytes.toString().trim();
    }
}
