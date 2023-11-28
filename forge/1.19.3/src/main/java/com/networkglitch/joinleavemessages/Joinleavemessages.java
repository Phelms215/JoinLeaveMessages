package com.networkglitch.joinleavemessages;

import com.networkglitch.common.Config;
import com.networkglitch.common.Logging;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Joinleavemessages.MODID)
public class Joinleavemessages {
    public static final String MODID = "joinleavemessages";

    public static final Config config = new Config();

    public Joinleavemessages() {
        MinecraftForge.EVENT_BUS.register(this);
        Logging.EnabledMessage();
    }

    public static void SendMessage(MutableComponent thisMessage, MinecraftServer thisServer) {
        thisServer.sendSystemMessage(thisMessage);
        for (ServerPlayer serverPlayerEntity : thisServer.getPlayerList().getPlayers()) {
            serverPlayerEntity.sendSystemMessage(thisMessage, false);
        }
    }
}