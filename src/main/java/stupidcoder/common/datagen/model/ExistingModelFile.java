package stupidcoder.common.datagen.model;

import net.minecraft.resources.ResourceLocation;

public class ExistingModelFile extends ModelFile {
    private final ResourceFileHelper helper;

    public ExistingModelFile(ResourceLocation location, ResourceFileHelper helper) {
        super(location);
        this.helper = helper;
    }

    @Override
    protected boolean exists() {
        if (location.getPath().contains(".")) {
            return helper.exists(location, ResourceType.MODEL_WITH_EXTENSION);
        }
        return helper.exists(location, ResourceType.MODEL);
    }
}
