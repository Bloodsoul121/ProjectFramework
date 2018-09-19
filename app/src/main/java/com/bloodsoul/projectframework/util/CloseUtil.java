package com.bloodsoul.projectframework.util;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtil {

    public static void close(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
