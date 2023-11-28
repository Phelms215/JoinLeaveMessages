package com.networkglitch.joinleavemessages.mixin;

import com.networkglitch.common.Definitions;
import com.networkglitch.joinleavemessages.Joinleavemessages;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class PlayerLeaveMixin {

    @Shadow
    public ServerPlayerEntity player;
    @Inject(at = @At("HEAD"), method = "onDisconnected")
    private void PlayerDisconnectLog(Text reason, CallbackInfo info) {
        Definitions.SendMessageResponse response;
        response = Joinleavemessages.config.SendLeavingMessage(player.getDisplayName().getString());
        if (!response.getSendMessage()) return;
        Text message;
        if (response.getTranslateKey() == null)
            message = Text.of(response.getCustomMessage());
        else
            message = Text.translatable(response.getTranslateKey(), new Object[]{player.getDisplayName()});

        Joinleavemessages.SendMessage(message, player.server);
    }

}