package com.stupidcoderx.modding.util;

import com.google.common.base.Preconditions;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * 仅用于{@link com.stupidcoderx.modding.mixin.VariantDeserializerMixin}
 */
public class BlockStateDefinitionHook {
    private static final Transformation[] TRANSFORMS;

    /**
     *
     * @return 旋转后的模型变量
     */
    public static Variant rotateVariant(Variant v, int xRot, int yRot, int zRot) {
        int i = index(xRot, yRot, zRot);
        return new Variant(v.getModelLocation(),
                TRANSFORMS[i],
                v.isUvLocked(),
                v.getWeight());
    }

    static {
        //计算出所有旋转角组合对应的变换
        Quaternionf rotator = new Quaternionf();
        TRANSFORMS = new Transformation[1 << 6];    // x | y | z
        for (int x = 0; x < 360; x += 90) {
            for (int y = 0; y < 360; y += 90) {
                TRANSFORMS[index(x, y, 0)] = BlockModelRotation.by(x, y).getRotation();
                for (int z = 90; z < 360; z+= 90) {
                    rotator.rotationXYZ(
                            -x * Mth.DEG_TO_RAD,
                            -y * Mth.DEG_TO_RAD,
                            -z * Mth.DEG_TO_RAD);
                    Matrix4f rMatrix = new Matrix4f().identity().rotate(rotator);
                    TRANSFORMS[index(x, y, z)] = new Transformation(rMatrix);
                }
            }
        }
    }

    private static int index(int x, int y, int z) {
        Preconditions.checkArgument(x >= 0 && x < 360 && x % 90 == 0);
        Preconditions.checkArgument(y >= 0 && y < 360 && y % 90 == 0);
        Preconditions.checkArgument(z >= 0 && z < 360 && z % 90 == 0);
        return (x / 90) << 4 | (y / 90) << 2 | (z / 90);
    }
}
