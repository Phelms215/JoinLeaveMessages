package com.networkglitch.joinleavemessages.mixin;

import com.networkglitch.common.Logging;
import com.networkglitch.joinleavemessages.Joinleavemessages;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class BroadcastChatMessageMixin {

    @Inject(at = @At("HEAD"), method = "broadcast(Lnet/minecraft/text/Text;Z)V", cancellable = true)
    void filterBroadCastMessages(Text message, boolean overlay, CallbackInfo ci) {
        try {
            String key = ((TranslatableTextContent) message.getContent()).getKey();
            if (!Joinleavemessages.config.BlockMessage(key)) return;
            Logging.debug("Suppressing a join/leave message");
            ci.cancel();
        } catch (ClassCastException exception) {
            Logging.MixinException(exception);
        }
    }
}