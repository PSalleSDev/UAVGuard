package com.uavguard.plugin;

import com.uavguard.plugin.Action;
import com.uavguard.plugin.Movement;

public interface Command {
    int getPort();

    byte[] getPacket();

    Action[] getActions();

    void setParameter(Movement action, int percent);
}
