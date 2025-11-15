package com.uavguard.plugin;

import java.util.function.Consumer;

public interface Video {
    int getPort();
    byte[] getStartBytes();
    void onFrame(Consumer<byte[]> callback);
    void pushFrame(byte[] data);
}
