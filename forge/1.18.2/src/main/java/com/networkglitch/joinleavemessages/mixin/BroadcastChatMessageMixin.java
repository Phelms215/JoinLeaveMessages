package com.networkglitch.joinleavemessages.mixin;

import com.networkglitch.common.Logging;
import com.networkglitch.joinleavemessages.Joinleavemessages;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerList.class)
public class BroadcastChatMessageMixin {

    @Inject(at = @At("HEAD"), method = "broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V",  cancellable = true)
    void filterBroadCastMessages(Component message, ChatType pChatType, UUID pSenderUuid, CallbackInfo ci) {
        try {
            if( !(message instanceof TranslatableComponent translatableMessage)) return;
            String key = translatableMessage.getKey();
            if (!Joinleavemessages.config.BlockMessage(key)) return;
            Logging.debug("Suppressing a join/leave message");
            ci.cancel();
        } catch (ClassCastException exception) {
            Logging.MixinException(exception);
        }
    }
}