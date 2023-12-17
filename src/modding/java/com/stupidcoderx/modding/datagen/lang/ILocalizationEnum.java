package com.stupidcoderx.modding.datagen.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface ILocalizationEnum {
    String defaultName();
    String translationKey();

    default MutableComponent text() {
        return Component.translatable(translationKey());
    }

    default MutableComponent text(Object... args) {
        return Component.translatable(translationKey(), args);
    }

    default MutableComponent withSuffix(String text) {
        return text().copy().append(text);
    }

    default MutableComponent withSuffix(Component text) {
        return text().copy().append(text);
    }
}
