package com.networkglitch.joinleavemessages.mixin;

import com.mojang.authlib.GameProfile;
import com.networkglitch.common.Definitions;
import com.networkglitch.joinleavemessages.Joinleavemessages;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class PlayerLeaveMixin extends Player {

    @Shadow @Final public MinecraftServer server;

    public PlayerLeaveMixin(Level p_250508_, BlockPos p_250289_, float p_251702_, GameProfile p_252153_) {
        super(p_250508_, p_250289_, p_251702_, p_252153_);
    }

    @Inject(at = @At("HEAD"), method = "disconnect")
    private void PlayerDisconnectLog(CallbackInfo info) {
        Definitions.SendMessageResponse response;
        response = Joinleavemessages.config.SendLeavingMessage(this.getGameProfile().getName());
        if (!response.getSendMessage()) return;
        MutableComponent message;
        if (response.getTranslateKey() == null)
            message = Component.literal(response.getCustomMessage());
        else
            message = Component.translatable(response.getTranslateKey(), new Object[]{this.getGameProfile().getName()});

        Joinleavemessages.SendMessage(message, this.server);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}