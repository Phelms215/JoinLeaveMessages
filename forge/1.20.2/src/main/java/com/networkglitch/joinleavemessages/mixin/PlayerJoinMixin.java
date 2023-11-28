package com.networkglitch.joinleavemessages.mixin;
import com.mojang.authlib.GameProfile;
import com.networkglitch.common.Definitions;
import com.networkglitch.common.Logging;
import com.networkglitch.joinleavemessages.Joinleavemessages;
import net.minecraft.Util;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;


@Mixin(PlayerList.class)
public class PlayerJoinMixin {
    @Unique
    private String joinLeaveMessages$oldDisplayName = null;


    @Shadow
    @Final
    private MinecraftServer server;


    @Inject(at = @At("HEAD"), method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V")
    void newConnectionStart(Connection pConnection, ServerPlayer player, CommonListenerCookie pCookie, CallbackInfo ci) {
        try {
            if (this.server.getProfileCache() == null) return;
            Optional<GameProfile> optional = this.server.getProfileCache().get(player.getGameProfile().getId());
            if (optional.isPresent())
                optional.map(GameProfile::getName).ifPresent(oldName -> joinLeaveMessages$oldDisplayName = oldName);

        } catch (Exception exception) {
            Logging.MixinException(exception);
        }
    }


    @Inject(at = @At("TAIL"), method = "placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V")
    void newConnectionEnd(Connection pConnection, ServerPlayer player, CommonListenerCookie pCookie, CallbackInfo ci) {
        try {
            Definitions.SendMessageResponse messageResponse = Joinleavemessages.config.SendJoinMessage(player.getDisplayName().getString(), joinLeaveMessages$oldDisplayName);
            MutableComponent thisMessage;
            if (messageResponse.getSendMessage()) {
                if (messageResponse.getTranslateKey() == null)
                    thisMessage = Component.literal(messageResponse.getCustomMessage());
                else
                    thisMessage = Component.translatable(messageResponse.getTranslateKey(), new Object[]{player.getDisplayName(), joinLeaveMessages$oldDisplayName});
                Joinleavemessages.SendMessage(thisMessage, this.server);
            }
            String privateMessage = Joinleavemessages.config.SendPrivateMessage(player.getDisplayName().getString(), joinLeaveMessages$oldDisplayName == null);
            if (privateMessage == null) return;
            ServerPlayer thisPlayer = this.server.getPlayerList().getPlayer(player.getUUID());
            if (thisPlayer == null) return;
            thisPlayer.sendSystemMessage(Component.literal(privateMessage));

        } catch (Exception exception) {
            Logging.MixinException(exception);
        }
    }
}