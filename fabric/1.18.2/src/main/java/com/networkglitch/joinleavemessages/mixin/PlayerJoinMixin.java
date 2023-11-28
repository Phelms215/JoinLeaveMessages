package com.networkglitch.joinleavemessages.mixin;

import com.mojang.authlib.GameProfile;
import com.networkglitch.common.Definitions;
import com.networkglitch.common.Logging;
import com.networkglitch.joinleavemessages.Joinleavemessages;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.UserCache;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(PlayerManager.class)
public class PlayerJoinMixin {
    @Unique
    private String oldDisplayName = null;

    @Final
    @Shadow
    private MinecraftServer server;

    @Inject(at = @At("HEAD"), method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    void newConnectionStart(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        try {
            GameProfile gameProfile = player.getGameProfile();
            UserCache userCache = this.server.getUserCache();
            if (userCache == null) return;
            Optional<GameProfile> optional = userCache.getByUuid(gameProfile.getId());
            if (optional.isEmpty()) return;
            oldDisplayName = optional.get().getName();
        } catch (Exception exception) {
            Logging.MixinException(exception);
        }
    }

    @Inject(at = @At("TAIL"), method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    void newConnectionEnd(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        try {
            Definitions.SendMessageResponse messageResponse = Joinleavemessages.config.SendJoinMessage(player.getDisplayName().asString(), oldDisplayName);
            Text thisMessage;
            if (player.getServer() == null) return;
            if (messageResponse.getSendMessage()) {
                if (messageResponse.getTranslateKey() == null) {
                    thisMessage = Text.of(messageResponse.getCustomMessage());
                } else {
                    thisMessage = new TranslatableText(messageResponse.getTranslateKey(), new Object[]{player.getDisplayName(), oldDisplayName});
                }
                Joinleavemessages.SendMessage(thisMessage, player.getServer());
            }
            String privateMessage = Joinleavemessages.config.SendPrivateMessage(player.getDisplayName().getString(), oldDisplayName == null);
            if (privateMessage != null) player.sendMessage(Text.of(privateMessage), MessageType.SYSTEM, Util.NIL_UUID);

        } catch (Exception exception) {
            Logging.MixinException(exception);
        }
    }
}
