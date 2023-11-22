package com.stupidcoderx.common.datagen.model;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModelBuilder extends ModelFile{
    @Nullable
    private ModelFile parent;

    protected final ResourceFileHelper helper;
    protected final Map<String, String> textures = new HashMap<>();

    public ModelBuilder(ResourceLocation location, ResourceFileHelper helper) {
        super(location);
        this.helper = helper;
    }

    public void setParent(@NotNull ModelFile parent) {
        this.parent = parent;
        parent.ensureExistence();
    }

    public void texture(String key, ResourceLocation textureLoc) {
        Preconditions.checkArgument(helper.exists(textureLoc, ResourceType.TEXTURE),
                "Texture %s does not exist in any known resource pack", textureLoc);
        textures.put(key, textureLoc.toString());
    }

    /**
     * 将构造的模型转化为等价的Json对象
     * @return Json对象
     */
    protected JsonObject toJson() {
        JsonObject root = new JsonObject();
        if (parent != null) {
            root.addProperty("parent", parent.location.toString());
        }
        if (!textures.isEmpty()) {
            JsonObject textureEntries = new JsonObject();
            for (var e : textures.entrySet()) {
                textureEntries.addProperty(e.getKey(), e.getValue());
            }
            root.add("textures", textureEntries);
        }
        return root;
    }
}
