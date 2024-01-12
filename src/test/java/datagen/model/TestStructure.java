package datagen.model;

import com.stupidcoderx.modding.datagen.model.elements.*;
import com.stupidcoderx.modding.util.ReflectionUtil;
import org.junit.Assert;
import org.junit.Test;

public class TestStructure {

    @Test
    public void test0() {
        new Structure(IBasePointStrategy.CENTER)
                .startRecord()
                .create(10,5,5)
                .stopRecord()
                .runRecord(ar -> ar.setRotation(1, 1))
                .process(c -> {
                    Assert.assertEquals(5, c.length(), 0);
                    Assert.assertEquals(5, c.height(), 0);
                    Assert.assertEquals(10, c.width(), 0);
                });
    }

    @Test
    public void test1() {
        new Structure(IBasePointStrategy.N_CORNER)
                .startRecord()
                .cubeCreateStrategy(ICubeCreateStrategy.N_CORNER)
                .create(10,5,5)
                .shift(5, Direction.PX)
                .stopRecord()
                .runRecord(ar -> ar.setRotation(1, 1))
                .process(c -> {
                    CubeProperty p = ReflectionUtil.getObjectField(c, "data");
                    float[] area = ReflectionUtil.getObjectField(p, "area");

                    Assert.assertEquals(0, area[0], 0);
                    Assert.assertEquals(0, area[1], 0);
                    Assert.assertEquals(5, area[2], 0);
                    Assert.assertEquals(5, area[3], 0);
                    Assert.assertEquals(5, area[4], 0);
                    Assert.assertEquals(15, area[5], 0);
                });
    }
}
