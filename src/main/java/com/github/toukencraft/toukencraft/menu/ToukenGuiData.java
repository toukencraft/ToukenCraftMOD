package com.github.toukencraft.toukencraft.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;


public record ToukenGuiData(int entityId) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ToukenGuiData> CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ToukenGuiData decode(RegistryFriendlyByteBuf buf) {
            return new ToukenGuiData(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ToukenGuiData data) {
            buf.writeInt(data.entityId);
        }
    };
}