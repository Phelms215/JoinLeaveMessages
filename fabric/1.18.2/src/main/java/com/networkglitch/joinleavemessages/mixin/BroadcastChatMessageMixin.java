package com.networkglitch.joinleavemessages.mixin;

import com.networkglitch.common.Logging;
import com.networkglitch.joinleavemessages.Joinleavemessages;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerManager.class)
public class BroadcastChatMessageMixin {

    @Inject(at = @At("HEAD"), method = "broadcast(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V", cancellable = true)
    void filterBroadCastMessages(Text message, MessageType type, UUID sender, CallbackInfo ci) {
        try {
            if (type != MessageType.SYSTEM || sender != Util.NIL_UUID) return;
            String key = ((TranslatableText) message).getKey();
            if (!Joinleavemessages.config.BlockMessage(key)) return;
            Logging.debug("Suppressing a join/leave message");
            ci.cancel();
        } catch (ClassCastException exception) {
            Logging.MixinException(exception);
        }
    }
}