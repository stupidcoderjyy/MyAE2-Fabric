package com.stupidcoderx.modding.datagen.model.elements;

import com.google.common.base.Preconditions;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CubeProperty implements TransformProperty<CubeProperty>{
    final float[] area = new float[6];

    public CubeProperty set(float x1, float y1, float z1, float x2, float y2, float z2) {
        area[0] = x1;
        area[1] = y1;
        area[2] = z1;
        area[3] = x2;
        area[4] = y2;
        area[5] = z2;
        return this;
    }

    @Override
    public CubeProperty rotate(int dim, int count) {
        Preconditions.checkArgument(dim >= 0 && dim < 3);
        float r = count * Mth.HALF_PI;
        Quaternionf rotator = new Quaternionf();
        switch (dim) {
            case 0 -> rotator.rotationX(r);
            case 1 -> rotator.rotationY(r);
            case 2 -> rotator.rotationZ(r);
        }
        Vector3f p1 = new Vector3f(area[0], area[1], area[2]).rotate(rotator);
        Vector3f p2 = new Vector3f(area[3], area[4], area[5]).rotate(rotator);
        area[0] = p1.x;
        area[1] = p1.y;
        area[2] = p1.z;
        area[3] = p2.x;
        area[4] = p2.y;
        area[5] = p2.z;
        return this;
    }

    @Override
    public CubeProperty shift(float px, float py, float pz) {
        area[0] += px;
        area[1] += py;
        area[2] += pz;
        area[3] += px;
        area[4] += py;
        area[5] += pz;
        return this;
    }
}
