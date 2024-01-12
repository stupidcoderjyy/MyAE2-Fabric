package com.stupidcoderx.modding.datagen.model.elements;

import com.google.common.base.Preconditions;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ScoopConfig {
    final MapProperty<Integer> dimSeq = new MapProperty<>(0, 1, 2);
    final CubeProperty range = new CubeProperty();
    Consumer<ScoopResult> finishedAction = r -> {};
    Map<Direction, Consumer<Cube>> separateActions = new EnumMap<>(Direction.class);
    boolean remove = true;
    final ActionContext ctx;
    private final ICubeCreateStrategy strategy;
    private final ActionRecord ar;
    private final float[] base;

    ScoopConfig(float[] base, ActionContext ctx, ActionRecord ar) {
        this.strategy = ctx.cubeCreateStrategy;
        this.base = base;
        this.ctx = ctx;
        this.ar = ar;
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
    public ScoopConfig dimensionSeq(int seq) {
        int first = (seq >> 8) & 0xF;
        int second = (seq >> 4) & 0xF;
        int third = seq & 0xF;
        Preconditions.checkArgument( first <= 3);
        Preconditions.checkArgument(second <= 3);
        Preconditions.checkArgument(third <= 3);
        dimSeq.set(first, second, third).rotate(ar);
        return this;
    }

    /**
     * 设置切割范围，切割区域的坐标策略和{@link Structure}中保持一致
     * @param length 立方体长度（x轴方向长度）
     * @param height 立方体高度（y轴方向长度）
     * @param width 立方体宽度（z轴方向长度）
     * @return 调用者
     */
    public ScoopConfig range(float length, float height, float width) {
        MapProperty<Float> p = new MapProperty<>(length, height, width).rotate(ar);
        strategy.set(range.area, base, p.x(), p.y(), p.z());
        return this;
    }

    /**
     * 设置切割范围
     * @return 调用者
     */
    public ScoopConfig range(float x1, float y1, float z1, float x2, float y2, float z2) {
        range.set(x1, y1, z1, x2, y2, z2).rotate(ar);
        return this;
    }

    /**
     * 将切割范围沿某个方向进行对齐
     * @param d 对齐的方向
     * @param level 对齐方向上的坐标
     * @return 调用者
     */
    public ScoopConfig align(Direction d, float level) {
        Direction d0 = d.clockwise(ar);
        float distance = level - range.area[d0.index];
        range.area[d0.dim] += distance;
        range.area[d0.dim + 3] += distance;
        return this;
    }

    /**
     * 设置是否删除割下来的部分
     * @param remove 删除为true，保留为false
     * @return 调用者
     */
    public ScoopConfig removeMode(boolean remove) {
        this.remove = remove;
        return this;
    }

    /**
     * 将切割区域往某个方向移动一段距离
     * @param d 方向
     * @param distance 距离
     * @return 调用者
     */
    public ScoopConfig shift(float distance, Direction d) {
        d = d.clockwise(ar);
        if (!d.isPositive) {
            distance = -distance;
        }
        range.area[d.dim] += distance;
        range.area[d.dim + 3] += distance;
        return this;
    }

    /**
     * 设置切割时执行的操作
     * @param d 切割的方向
     * @param op 完成目标方向的切割后执行的操作。若在目标方向上没有切出新的方块，则传入null
     * @return 调用者
     */
    public ScoopConfig separateAction(Direction d, Consumer<Cube> op) {
        d = d.clockwise(ar);
        separateActions.put(d, op);
        return this;
    }

    /**
     * 设置切割时执行的操作
     * @param cond 方向过滤器
     * @param op 完成目标方向的切割后执行的操作。若在目标方向上没有切出新的方块，则传入null
     * @return 调用者
     */
    public ScoopConfig separateAction(Predicate<Direction> cond, Consumer<Cube> op) {
        for (Direction d : Direction.values()) {
            d = d.clockwise(ar);
            if (cond.test(d)) {
                separateActions.put(d, op);
            }
        }
        return this;
    }

    /**
     * 设置切割完成后执行的操作
     * @param op 操作
     * @see ScoopResult
     * @return 调用者
     */
    public ScoopConfig finishedAction(Consumer<ScoopResult> op) {
        finishedAction = op;
        return this;
    }
}
