package com.networkglitch.joinleavemessages.mixin;

import com.networkglitch.common.Logging;
import com.networkglitch.joinleavemessages.Joinleavemessages;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class BroadcastChatMessageMixin {

    @Inject(at = @At("HEAD"), method = "broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V",  cancellable = true)
    void filterBroadCastMessages(Component message, boolean overlay, CallbackInfo ci) {
        try {
            if( !(message.getContents() instanceof TranslatableContents translatableMessage)) return;
            String key = translatableMessage.getKey();
            if (!Joinleavemessages.config.BlockMessage(key)) return;
            Logging.debug("Suppressing a join/leave message");
            ci.cancel();
        } catch (ClassCastException exception) {
            Logging.MixinException(exception);
        }
    }
}