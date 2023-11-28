package com.networkglitch.joinleavemessages;

import com.networkglitch.common.Config;
import com.networkglitch.common.Logging;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class Joinleavemessages implements ModInitializer {

    public static final Config config = new Config();

    @Override
    public void onInitialize() {
        Logging.EnabledMessage();
    }

    public static void SendMessage(Text thisMessage, MinecraftServer thisServer) {
        thisServer.sendMessage(thisMessage);
        for (ServerPlayerEntity serverPlayerEntity : thisServer.getPlayerManager().getPlayerList()) {
            serverPlayerEntity.sendMessageToClient(thisMessage, false);
        }
    }
}