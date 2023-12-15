package com.stupidcoderx.modding.datagen.model.elements;

import com.google.common.base.Preconditions;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SeparationConfig<T extends SeparateObject<T>> {
    final int[] dimSeq = new int[]{0,1,2};
    final float[] range = new float[6];
    Consumer<SeparationResult<T>> finishedAction = r -> {};
    Map<Direction, Consumer<T>> separateActions = new EnumMap<>(Direction.class);
    boolean remove = true;
    private final ICubeCreateStrategy strategy;
    private final float[] base;

    SeparationConfig(ICubeCreateStrategy strategy, float[] base) {
        this.strategy = strategy;
        this.base = base;
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
    public SeparationConfig<T> dimensionSeq(int seq) {
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
     * 设置切割范围，切割区域的坐标策略和{@link Structure}中保持一致
     * @param length 立方体长度（x轴方向长度）
     * @param height 立方体高度（y轴方向长度）
     * @param width 立方体宽度（z轴方向长度）
     * @return 调用者
     */
    public SeparationConfig<T> range(float length, float height, float width) {
        strategy.set(range, base, length, height, width);
        return this;
    }

    /**
     * 设置切割范围
     * @return 调用者
     */
    public SeparationConfig<T> range(float x1, float y1, float z1, float x2, float y2, float z2) {
        range[0] = x1;
        range[1] = y1;
        range[2] = z1;
        range[3] = x2;
        range[4] = y2;
        range[5] = z2;
        return this;
    }

    /**
     * 将切割范围沿某个方向进行对齐
     * @param d 对齐的方向
     * @param level 对齐方向上的坐标
     * @return 调用者
     */
    public SeparationConfig<T> align(Direction d, float level) {
        float distance = level - range[d.index];
        range[d.dim] += distance;
        range[d.dim + 3] += distance;
        return this;
    }

    /**
     * 设置是否删除割下来的部分
     * @param remove 删除为true，保留为false
     * @return 调用者
     */
    public SeparationConfig<T> removeMode(boolean remove) {
        this.remove = remove;
        return this;
    }

    /**
     * 将切割区域往某个方向移动一段距离
     * @param d 方向
     * @param distance 距离
     * @return 调用者
     */
    public SeparationConfig<T> shift(float distance, Direction d) {
        if (!d.isPositive) {
            distance = -distance;
        }
        range[d.dim] += distance;
        range[d.dim + 3] += distance;
        return this;
    }

    /**
     * 设置切割时执行的操作
     * @param d 切割的方向
     * @param op 完成目标方向的切割后执行的操作。若在目标方向上没有切出新的方块，则传入null
     * @return 调用者
     */
    public SeparationConfig<T> separateAction(Direction d, Consumer<T> op) {
        this.separateActions.put(d, op);
        return this;
    }

    /**
     * 设置切割时执行的操作
     * @param cond 方向过滤器
     * @param op 完成目标方向的切割后执行的操作。若在目标方向上没有切出新的方块，则传入null
     * @return 调用者
     */
    public SeparationConfig<T> separateAction(Predicate<Direction> cond, Consumer<T> op) {
        for (Direction d : Direction.values()) {
            if (cond.test(d)) {
                separateActions.put(d, op);
            }
        }
        return this;
    }

    /**
     * 设置切割完成后执行的操作
     * @param op 操作
     * @see SeparationResult
     * @return 调用者
     */
    public SeparationConfig<T> finishedAction(Consumer<SeparationResult<T>> op) {
        this.finishedAction = op;
        return this;
    }
}
