package com.awesoft.cclink.libs;

import com.awesoft.cclink.CCLinkConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketCooldownManager {
    private static final Map<UUID, Long> lastPacketTime = new HashMap<>();


    public static boolean canSendPacket(UUID playerId) {
        int cooldownTicks = CCLinkConfig.HUD_PACKET_COOLDOWN_TICKS.get();
        if (cooldownTicks <= 0) return true;

        long now = System.currentTimeMillis();
        long lastTime = lastPacketTime.getOrDefault(playerId, 0L);

        if (now - lastTime >= cooldownTicks * 50L) {
            lastPacketTime.put(playerId, now);
            return true;
        }
        return false;
    }

}
