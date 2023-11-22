package com.stupidcoderx.common.core;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class ElementsRegistry<T extends IRegistry> implements IRegistry {
    private List<T> registries = new ArrayList<>();
    private final String name;

    public ElementsRegistry(String name) {
        this.name = name;
    }

    public ElementsRegistry() {
        this.name = this.getClass().getName();
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
    public void buildData() {
        Preconditions.checkNotNull(registries, "closed: " + debugName());
        registries.forEach(IRegistry::buildData);
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
