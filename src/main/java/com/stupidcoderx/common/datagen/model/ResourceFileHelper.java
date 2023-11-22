package com.stupidcoderx.common.datagen.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResourceFileHelper {
    private final MultiPackResourceManager clientResources, serverData;
    private final Multimap<PackType, ResourceLocation> modResources = HashMultimap.create();

    public ResourceFileHelper(Collection<Path> resourcePaths) {
        List<PackResources> clientPacks = new ArrayList<>(), serverPacks = new ArrayList<>();
        //原版资源
        VanillaPackResources mcResources = new VanillaPackResourcesBuilder()
                .setMetadata(BuiltInMetadata.of())
                .exposeNamespace("minecraft")
                .pushJarResources()
                .build();
        //模组资源
        for (Path path : resourcePaths) {
            File f = path.toFile();
            PackResources pack = f.isDirectory() ?
                    new PathPackResources(f.getName(), f.toPath(), false) :
                    new FilePackResources.FileResourcesSupplier(path, false).openPrimary(f.getName());
            clientPacks.add(pack);
            serverPacks.add(pack);
        }
        clientPacks.add(mcResources);
        serverPacks.add(mcResources);
        this.clientResources = new MultiPackResourceManager(PackType.CLIENT_RESOURCES, clientPacks);
        this.serverData = new MultiPackResourceManager(PackType.SERVER_DATA, serverPacks);
    }

    public boolean exists(ResourceLocation location, PackType type) {
        if (modResources.get(type).contains(location)) {
            return true;
        }
        ResourceManager manager = type == PackType.CLIENT_RESOURCES ? clientResources : serverData;
        return manager.getResource(location).isPresent();
    }

    private ResourceLocation buildLoc(ResourceLocation base, ResourceType type) {
        return new ResourceLocation(base.getNamespace(),
                type.pathPrefix() + "/" + base.getPath() + type.pathSuffix());
    }

    public boolean exists(ResourceLocation base, ResourceType type) {
        return exists(buildLoc(base, type), type.type());
    }

    public void trackGenerated(ResourceLocation loc, ResourceType type) {
        modResources.put(type.type(), buildLoc(loc, type));
    }
}
