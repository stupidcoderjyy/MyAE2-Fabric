package com.stupidcoderx.modding.core;

import com.google.common.base.Preconditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

public class RegistryList<T extends IRegistry> implements IRegistry {
    private List<T> registries = new ArrayList<>();
    private final String name;

    public RegistryList(String name) {
        this.name = name;
    }

    public void add(T obj) {
        registries.add(obj);
    }

    @Override
    public void commonRegister() {
        Preconditions.checkNotNull(registries, "closed: " + debugName());
        registries.forEach(IRegistry::commonRegister);
    }

    @Override
    public void provideData() {
        Preconditions.checkNotNull(registries, "closed: " + debugName());
        registries.forEach(IRegistry::provideData);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void clientRegister() {
        Preconditions.checkNotNull(registries, "closed: " + debugName());
        registries.forEach(IRegistry::clientRegister);
    }

    @Override
    public void close() {
        if (!Mod.isEnvDataGen) {
            registries = null;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
