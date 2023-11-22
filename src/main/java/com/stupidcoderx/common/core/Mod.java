package com.stupidcoderx.common.core;

import com.mojang.logging.LogUtils;
import com.stupidcoderx.common.element.BaseItem;
import com.stupidcoderx.common.element.ModCreativeTab;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Mod {
    public final String modId;
    public static final boolean isEnvDataGen = "true".equals(System.getProperty("mod.isEnvDataGen"));
    protected static List<IRegistry> registries = new ArrayList<>();
    public static final ElementsRegistry<BaseItem<?>> ITEM_REGISTRY = create("item");
    public static final ElementsRegistry<ModCreativeTab> CREATIVE_TAB_REGISTRY = create("creativeTab");
    public static final Logger logger = LogUtils.getLogger();
    private static Mod instance;

    protected Mod(String modId) {
        this.modId = modId;
        instance = this;
    }

    public void commonInit() {
        for (IRegistry r : registries) {
            logger.info("common init: " + r.debugName());
            r.commonRegister();
        }
    }

    public void finishInit() {
        for (IRegistry r : registries) {
            r.close();
        }
        registries = null;
        logger.info("finish init: " + modId);
    }

    public static <T extends Mod> T getMod() {
        return (T)instance;
    }

    public static String getModId() {
        return instance.modId;
    }

    private static <T extends IRegistry> ElementsRegistry<T> create(String name) {
        ElementsRegistry<T> r = new ElementsRegistry<>(name);
        registries.add(r);
        return r;
    }
}
