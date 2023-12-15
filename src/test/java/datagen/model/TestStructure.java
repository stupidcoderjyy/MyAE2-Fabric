package datagen.model;

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
                });
    }

    private float faceArea(float[] data, int d1, int d2) {
        return (data[d1 + 3] - data[d1]) * (data[d2 + 3] - data[d2]);
    }

    @Test
    public void testStackToCubeSimple() {
        new Structure(IBasePointStrategy.N_CORNER)
                .cubeCreateStrategy(ICubeCreateStrategy.N_CORNER)
                .create(16,2,16) //干扰项（位于下层）
                .create(2,2,2)   //干扰项（遇不到）
                .shift(10, Direction.PZ)
                .create(16,2,16)
                .shift(2, Direction.PY)
                .create(4,4,4)
                .shift(50, Direction.PY)
                .stackTo(Direction.NY)
                .process(c -> {
                    float[] data = ReflectionUtil.getObjectField(SeparateObject.class, c, "data");
                    Assert.assertEquals(4, data[1], 0);
                    Assert.assertEquals(8, data[4], 0);
                });
    }

    @Test
    public void testStackToCubeOverlapped() {
        new Structure(IBasePointStrategy.N_CORNER)
                .cubeCreateStrategy(ICubeCreateStrategy.N_CORNER)
                .create(16,4,16)
                .create(4,4,4)
                .stackTo(Direction.NY)
                .process(c -> {
                    float[] data = ReflectionUtil.getObjectField(SeparateObject.class, c, "data");
                    Assert.assertEquals(4, data[1], 0);
                    Assert.assertEquals(8, data[4], 0);
                });
    }

    @Test
    public void testStackToWorkspace() {
        new Structure(IBasePointStrategy.N_CORNER)
                .cubeCreateStrategy(ICubeCreateStrategy.N_CORNER)
                .create(4,4,4)
                .shift(10, Direction.NY)
                .stackTo(Direction.NY)
                .process(c -> {
                    float[] data = ReflectionUtil.getObjectField(SeparateObject.class, c, "data");
                    Assert.assertEquals(0, data[1], 0);
                    Assert.assertEquals(4, data[4], 0);
                });
    }

    @Test
    public void testStrategyNY() {
        new Structure(IBasePointStrategy.NY)
                .cubeCreateStrategy(ICubeCreateStrategy.NY)
                .create(4,4,4)
                .process(c -> {
                    float[] data = ReflectionUtil.getObjectField(SeparateObject.class, c, "data");
                    Assert.assertEquals(6, data[0], 0);
                    Assert.assertEquals(0, data[1], 0);
                    Assert.assertEquals(6, data[2], 0);
                    Assert.assertEquals(10, data[3], 0);
                    Assert.assertEquals(4, data[4], 0);
                    Assert.assertEquals(10, data[5], 0);
                });
    }

    @Test
    public void testStrategyPY() {
        new Structure(IBasePointStrategy.NY)
                .cubeCreateStrategy(ICubeCreateStrategy.PY)
                .create(4,4,4)
                .process(c -> {
                    float[] data = ReflectionUtil.getObjectField(SeparateObject.class, c, "data");
                    Assert.assertEquals(6, data[0], 0);
                    Assert.assertEquals(-4, data[1], 0);
                    Assert.assertEquals(6, data[2], 0);
                    Assert.assertEquals(10, data[3], 0);
                    Assert.assertEquals(0, data[4], 0);
                    Assert.assertEquals(10, data[5], 0);
                });
    }
}
