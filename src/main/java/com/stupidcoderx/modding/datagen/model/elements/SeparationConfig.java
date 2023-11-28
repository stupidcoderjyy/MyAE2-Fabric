package com.stupidcoderx.modding.datagen.model.elements;

import com.google.common.base.Preconditions;

import java.util.function.Consumer;

public class SeparationConfig {
    final int[] dimSeq = new int[]{0,1,2};
    final float[] range = new float[6];
    Consumer<SeparationResult<Cube>> finishedOp = r -> {};

    SeparationConfig() {
    }

    public static final int XYZ = 0x012;
    public static final int XZY = 0x021;
    public static final int YXZ = 0x102;
    public static final int YZX = 0x120;
    public static final int ZXY = 0x201;
    public static final int ZYX = 0x210;

    /**
     * 设置切割顺序
     * @param seq 维度序列，使用{@link #XYZ},{@link #XZY},{@link #YXZ},{@link #YZX},{@link #ZXY},{@link #ZYX}
     */
    public SeparationConfig dimensionSeq(int seq) {
        int first = (seq >> 8) & 0xF;
        int second = (seq >> 4) & 0xF;
        int third = seq & 0xF;
        Preconditions.checkArgument( first <= 3);
        Preconditions.checkArgument(second <= 3);
        Preconditions.checkArgument(third <= 3);
        dimSeq[0] = first;
        dimSeq[1] = second;
        dimSeq[2] = third;
        return this;
    }

    /**
     * 设置切割范围，切割区域默认为坐标系中心的一个立方体
     * @param length 立方体长度（x轴方向长度）
     * @param height 立方体高度（y轴方向长度）
     * @param width 立方体宽度（z轴方向长度）
     * @return 调用者
     */
    public SeparationConfig range(float length, float height, float width) {
        float x = length / 2, y = height / 2, z = width / 2;
        return range(-x, -y, -z, x, y, z);
    }

    public SeparationConfig range(float x1, float y1, float z1, float x2, float y2, float z2) {
        range[0] = x1;
        range[1] = y1;
        range[2] = z1;
        range[3] = x2;
        range[4] = y2;
        range[5] = z2;
        return this;
    }

    public SeparationConfig align(Direction d, float level) {
        float distance = d.isPositive ?
                level - range[d.dim + 3] :
                level - range[d.dim];
        range[d.dim] += distance;
        range[d.dim + 3] += distance;
        return this;
    }

    public SeparationConfig shift(Direction d, float distance) {
        if (!d.isPositive) {
            distance = -distance;
        }
        range[d.dim] += distance;
        range[d.dim + 3] += distance;
        return this;
    }

    public SeparationConfig onFinished(Consumer<SeparationResult<Cube>> op) {
        this.finishedOp = op;
        return this;
    }
}
