package com.stupidcoderx.modding.datagen.model.elements;

import com.google.gson.JsonArray;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 可以储存由若干{@link Cube}组成的结构。
 */
public class Structure {
    private static int maxId = 0;
    private final float[] outline = new float[6];
    private final Set<Cube> cubes = new HashSet<>();
    private final List<Cube> active = new ArrayList<>();

    void internalAdd(Cube e) {
        cubes.add(e);
        updateOutline(e);
    }

    /**
     * 对结构整体进行平移
     * @param d 平移方向
     * @param val 平移大小
     * @return 调用者
     */
    public Structure globalShift(Direction d, float val) {
        return shift(cubes, d, val);
    }

    /**
     * 对整个结构进行挖勺操作
     * @param op 配置挖勺工作参数的逻辑
     * @return 调用者
     */
    public Structure globalScoop(Consumer<SeparationConfig<Cube>> op) {
        SeparationConfig<Cube> cfg = new SeparationConfig<>();
        op.accept(cfg);
        for (Cube e : cubes) {
            SeparationResult<Cube> result = e.separate(cfg);
            if (result.children.isEmpty()) {
                continue;
            }
            cfg.finishedAction.accept(result);
        }
        return this;
    }

    /**
     * 将结构沿某个方向平移，直到结构在该方向上的边缘与基准重合
     * @param base 基准的坐标
     * @param faces 平移的方向
     * @return 调用者
     */
    public Structure globalAlign(int base, Direction ... faces) {
        for (Direction face : faces) {
            float distance = face.isPositive ?
                    base - outline[face.dim + 3] :
                    outline[face.dim] - base;
            globalShift(face, distance);
        }
        return this;
    }

    /**
     * 在坐标系中心创建一个立方体，并作为唯一的活动立方体
     * @param name 立方体名字
     * @param length 立方体长度（x轴方向长度）
     * @param height 立方体高度（y轴方向长度）
     * @param width 立方体宽度（z轴方向长度）
     * @return 调用者
     */
    public Structure create(String name, float length, float height, float width) {
        float x = length / 2f, y = height / 2, z = width / 2;
        return create(name, -x, -y, -z, x, y, z);
    }

    /**
     * 在坐标系中心创建一个立方体，并作为唯一的活动立方体
     * @see #create(String, float, float, float)
     */
    public Structure create(float length, float height, float width) {
        return create("cube" + maxId++, length, height, width);
    }

    /**
     * 对活动立方体进行自定义处理
     * @param op 自定义操作
     * @return 调用者
     */
    public Structure process(Consumer<Cube> op) {
        active.forEach(op);
        return this;
    }

    /**
     * 创建一个立方体，并作为唯一的活动立方体
     * @param name 立方体名字
     * @param x1 起始x值（包含）
     * @param y1 起始y值（包含）
     * @param z1 起始z值（包含）
     * @param x2 终点x值（不包含）
     * @param y2 终点y值（不包含）
     * @param z2 终点z值（不包含）
     * @throws IllegalArgumentException 如果起始值大于等于终点值时抛出
     * @return 调用者
     */
    public Structure create(String name, float x1, float y1, float z1, float x2, float y2, float z2) {
        active.clear();
        Cube e = new Cube(this, name);
        e.set(x1, y1, z1, x2, y2, z2);
        active.add(e);
        internalAdd(e);
        return this;
    }

    /**
     * 创建一个立方体，并作为唯一的活动立方体
     * @see #create(String, float, float, float, float, float, float)
     */
    public Structure create(float x1, float y1, float z1, float x2, float y2, float z2) {
        return create("cube" + maxId++, x1, y1, z1, x2, y2, z2);
    }

    /**
     * 对所有活动立方体进行平移
     * @param d 方向
     * @param val 大小
     * @return 调用者
     */
    public Structure shift(Direction d, float val) {
        return shift(active, d, val);
    }

    /**
     * 对活动立方体进行挖勺操作
     * @param op 配置挖勺工作参数的逻辑
     * @return 调用者
     */
    public Structure scoop(Consumer<SeparationConfig<Cube>> op) {
        List<Cube> temp = new ArrayList<>(active);
        active.clear();
        SeparationConfig<Cube> cfg = new SeparationConfig<>();
        op.accept(cfg);
        for (Cube e : temp) {
            SeparationResult<Cube> result = e.separate(cfg);
            if (result.children.isEmpty()) {
                continue;
            }
            cubes.remove(e);
            result.children.forEach((d, childCube) -> active.add(childCube));
            cfg.finishedAction.accept(result);
        }
        cubes.forEach(this::updateOutline);
        return this;
    }

    /**
     * 查找满足特定规则的立方体，并设置为活动立方体
     * @param condition 规则
     * @return 调用者
     */
    public Structure find(Predicate<Cube> condition) {
        active.clear();
        cubes.stream().filter(condition).forEach(active::add);
        return this;
    }

    /**
     * 找到在某个方向上坐标数值最大的立方体
     * @param d 方向
     * @return 调用者
     */
    public Structure findMost(Direction d) {
        int i = d.isPositive ? d.dim + 3 : d.dim;
        return find(c -> outline[i] == c.data[i]);
    }

    /**
     * 对活动立方体进行过滤
     * @param condition 过滤条件
     * @return 调用者
     */
    public Structure filter(Predicate<Cube> condition) {
        List<Cube> worker = new ArrayList<>(active);
        active.clear();
        worker.stream().filter(condition).forEach(active::add);
        return this;
    }

    /**
     * 获得某个方向上的坐标最大值
     * @param d 方向
     * @return 坐标值
     */
    public float getOutline(Direction d) {
        return d.isPositive ? outline[d.dim + 3] : outline[d.dim];
    }

    /**
     * 将结构转化为Json
     * @return Json对象
     */
    public JsonArray toJson() {
        JsonArray elementsObj = new JsonArray();
        for (Cube c : cubes) {
            elementsObj.add(c.toJson());
        }
        return elementsObj;
    }

    /**
     * 将结构转化成等价的{@link VoxelShape}对象
     * @return 形状
     */
    public VoxelShape toVoxelShape() {
        if (cubes.isEmpty()) {
            return Shapes.empty();
        }
        VoxelShape shape = null;
        for (Cube c : cubes) {
            shape = shape == null ?
                    c.toVoxelShape() :
                    Shapes.or(shape, c.toVoxelShape());
        }
        return shape;
    }

    /**
     * 将结构重置
     */
    public void clear() {
        cubes.clear();
        active.clear();
    }

    private Structure shift(Collection<Cube> targets, Direction d, float val) {
        if (!d.isPositive) {
            val = -val;
        }
        int i = d.dim, j = d.dim + 3;
        for (Cube e : targets) {
            e.data[i] += val;
            e.data[j] += val;
        }
        outline[i] += val;
        outline[j] += val;
        return this;
    }

    private void updateOutline(Cube b) {
        for (int i = 0, j = 3 ; i < 3 ; i++, j++) {
            outline[i] = Math.min(b.data[i], outline[i]);
            outline[j] = Math.max(b.data[j], outline[j]);
        }
    }
}
