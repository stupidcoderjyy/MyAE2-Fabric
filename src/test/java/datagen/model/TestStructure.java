package datagen.model;

import com.google.gson.JsonArray;
import com.stupidcoderx.modding.datagen.model.elements.*;
import com.stupidcoderx.modding.util.ReflectionUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class TestStructure {

    @Test
    public void testScoopedTexturedUV() {
        new Structure()
                .create(4,4,4)
                .process(c -> c.faceAll().uv(0,0))
                .scoop(cfg -> cfg.range(2,2,2))
                .process(c -> {
                    System.out.println(c);
                    Map<Direction, Face> faces = ReflectionUtil.getObjectField(c, "faces");
                    float[] data = ReflectionUtil.getObjectField(SeparateObject.class, c, "data");
                    faces.forEach((d, f) -> {
                        float[] uv = ReflectionUtil.getObjectField(f, "uv");
                        float uvArea = (uv[3] - uv[1]) * (uv[2] - uv[0]);
                        float faceArea = switch (d.dim) {
                            case 0 -> faceArea(data, 1, 2);
                            case 1 -> faceArea(data, 0, 2);
                            case 2 -> faceArea(data, 0, 1);
                            default -> 0;
                        };
                        Assert.assertEquals(faceArea, uvArea, 0);
                    });
                    System.out.println(faces);
                });
    }

    private float faceArea(float[] data, int d1, int d2) {
        return (data[d1 + 3] - data[d1]) * (data[d2 + 3] - data[d2]);
    }

    @Test
    public void testGenWoodenTable() {
        String oak = "minecraft:block/oak";
        String dirt = "minecraft:block/dirt";
        Structure struct = new Structure();
        JsonArray json = struct
                .create(16, 16, 16)
                .process(c -> c.faceAll().uv(0, 0).texture(oak))
                .scoop(config -> config
                        .dimensionSeq(SeparationConfig.YZX)
                        .range(12, 14, 16)
                        .align(Direction.DOWN, struct.getOutline(Direction.DOWN)))
                .scoop(config -> config
                        .range(16, 14, 12)
                        .align(Direction.DOWN, struct.getOutline(Direction.DOWN)))
                .findMost(Direction.UP)
                .process(c -> c.face(Direction.DOWN).texture(dirt))
                .globalAlign(0, Direction.DOWN, Direction.NORTH, Direction.EAST)
                .toJson();
        System.out.println(json);
    }
}
