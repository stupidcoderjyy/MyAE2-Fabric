package com.stupidcoderx.modding.datagen.model.elements;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 可以储存由若干{@link Cube}组成的结构。结构拥有固定的坐标系，原点为workspace中
 * {@link Direction#NX}、{@link Direction#NY}和{@link Direction#NZ}
 * 对应的三个面所交的点
 */
public class Structure {
    private static int maxId = 0;
    private final float[] workspace = new float[6];
    private final float[] basePoint = new float[3];
    final ActionRecord ar = new ActionRecord(this);
    final ActionContext ctx = new ActionContext();

    /**
     * 创建一个自定义空间大小、自定义基点策略的结构。空间大小影响基点坐标计算和{@link #stackTo(Direction)}的坐标计算
     * @param basePointStrategy 基点策略。基点决定了立方体创建时的参照坐标，和{@link ICubeCreateStrategy}共同决定立方体的坐标
     * @param xLen 空间长度
     * @param yLen 空间高度
     * @param zLen 空间宽度
     */
    public Structure(IBasePointStrategy basePointStrategy, float xLen, float yLen, float zLen) {
        Preconditions.checkNotNull(basePointStrategy);
        Preconditions.checkArgument(xLen > 0);
        Preconditions.checkArgument(yLen > 0);
        Preconditions.checkArgument(zLen > 0);
        workspace[3] = xLen;
        workspace[4] = yLen;
        workspace[5] = zLen;
        basePointStrategy.setBasePoint(basePoint, xLen, yLen, zLen);
    }

    /**
     * @see Structure#Structure(IBasePointStrategy, float, float, float)
     */
    public Structure(IBasePointStrategy basePointStrategy) {
        this(basePointStrategy, 16,16,16);
    }

    /**
     * @see Structure#Structure(IBasePointStrategy, float, float, float)
     */
    public Structure() {
        this(IBasePointStrategy.CENTER);
    }

    public Structure startRecord() {
        ar.startRecord();
        return this;
    }

    public Structure stopRecord() {
        ar.endRecord();
        return this;
    }

    public ActionRecord getRecord() {
        return ar;
    }

    public Structure runRecord(Consumer<ActionRecord> cfg) {
        cfg.accept(ar);
        ar.runRecord(ctx);
        return this;
    }

    public Structure runRecord(ActionRecord record) {
        record.runRecord(ctx);
        return this;
    }

    /**
     * 对结构整体进行平移
     * @param ds 平移方向
     * @param val 平移大小
     * @return 调用者
     */
    public Structure globalShift(float val, Direction ... ds) {
        ar.recordAndRun(ctx -> {
            for (Direction d : ds) {
                d = d.clockwise(ar);
                shift0(ctx, ctx.cubes, val, d);
            }
        });
        return this;
    }

    /**
     * 对整个结构进行挖勺操作
     * @param op 配置挖勺工作参数的逻辑
     * @return 调用者
     */
    public Structure globalScoop(Consumer<ScoopConfig> op) {
        ar.recordAndRun(ctx -> {
            ScoopConfig cfg = new ScoopConfig(this.basePoint, ctx, ar);
            op.accept(cfg);
            for (Cube e : ctx.cubes) {
                ScoopResult result = e.scoop(cfg);
                if (result.children.isEmpty()) {
                    continue;
                }
                cfg.finishedAction.accept(result);
            }
        });
        return this;
    }

    /**
     * 将结构沿某个方向前后平移，直到结构在该方向上与基准重合
     * @param base 基准的坐标
     * @param faces 平移的方向
     * @return 调用者
     */
    public Structure globalAlign(float base, Direction ... faces) {
        ar.recordAndRun(ctx -> {
            for (Direction face : faces) {
                face = face.clockwise(ar);
                globalShift(-Math.abs(ctx.outline[face.index] - base), face);
            }
        });
        return this;
    }

    /**
     * 设置立方体创建的坐标策略，策略决定了立方体在空间中的坐标
     * @param strategy 创建策略
     * @return 调用者
     */
    public Structure cubeCreateStrategy(ICubeCreateStrategy strategy) {
        Preconditions.checkNotNull(strategy);
        ar.recordAndRun(ctx -> ctx.cubeCreateStrategy = strategy);
        return this;
    }

    /**
     * 根据坐标策略（默认为{@link ICubeCreateStrategy#CENTER}）创建一个立方体，并作为唯一的活动立方体
     * @param name 立方体名字
     * @param length 立方体长度（x轴方向长度）
     * @param height 立方体高度（y轴方向长度）
     * @param width 立方体宽度（z轴方向长度）
     * @see #cubeCreateStrategy(ICubeCreateStrategy)
     * @return 调用者
     */
    public Structure create(String name, float length, float height, float width) {
        ar.recordAndRun(ctx -> {
            Cube c = new Cube(this, name);
            MapProperty<Float> p = new MapProperty<>(length, height, width)
                    .rotate(ar);
            ctx.cubeCreateStrategy.set(c.data.area, basePoint, p.x(), p.y(), p.z());
            create0(ctx, c);
        });
        return this;
    }

    /**
     * 根据坐标策略（默认为{@link ICubeCreateStrategy#CENTER}）创建一个立方体，并作为唯一的活动立方体
     * @see #create(String, float, float, float)
     */
    public Structure create(float length, float height, float width) {
        return create("cube" + maxId++, length, height, width);
    }

    /**
     * 将每个活动立方体不断沿某个方向移动，直到遇到workspace边界或者其他立方体。
     * 若立方体不在workspace内部，则会把立方体反向移动，直到与workspace边界贴在一起。
     * 若立方体与某个立方体重合，则会把立方体反向移动，直到与立方体贴在一起
     * @param d 移动方向
     * @return 调用者
     */
    public Structure stackTo(Direction d) {
        ar.recordAndRun(ctx -> {
            Direction d1 = d.clockwise(ar);
            Set<Cube> targets = new HashSet<>(ctx.cubes);
            ctx.active.forEach(targets::remove);
            Direction opposite = d1.opposite();
            for (Cube c : ctx.active) {
                Cube resCube = null;
                float resPos = ctx.outline[opposite.index]; //一定是d方向上最小的
                for (Cube target : targets) {
                    //是否嵌在立方体里面
                    if (!c.isSeparated(target.data.area)) {
                        resCube = target;
                        break;
                    }
                    float targetPos = target.data.area[opposite.index];
                    float originalPos = c.data.area[d1.index];
                    //如果比自己的坐标还小，那肯定不是
                    if (d1.larger(originalPos, targetPos)) {
                        continue;
                    }
                    //只取第一个遇到的
                    if (resCube != null && d1.larger(targetPos, resPos)) {
                        continue;
                    }
                    //检测移动过程中c是否会撞到target
                    if (!collide(c, target, d1.dim)) {
                        continue;
                    }
                    resCube = target;
                    resPos = targetPos;
                }
                float original = c.data.area[d1.index];
                if (resCube != null) {
                    c.data.area[d1.index] = resCube.data.area[opposite.index];
                    c.data.area[opposite.index] += c.data.area[d1.index] - original;
                    continue;
                }
                //贴到workspace上
                c.data.area[d1.index] = workspace[d1.index];
                c.data.area[opposite.index] += (workspace[d1.index] - original);
            }
        });
        return this;
    }

    private boolean collide(Cube c, Cube target, int dim) {
        return switch (dim) {
            case 0 -> collide0(c, target, 1) && collide0(c, target, 2);
            case 1 -> collide0(c, target, 0) && collide0(c, target, 2);
            case 2 -> collide0(c, target, 0) && collide0(c, target, 1);
            default -> false;
        };
    }

    private boolean collide0(Cube c, Cube target, int dim) {
        int larger = dim + 3;
        return (c.data.area[dim] >= target.data.area[dim] && c.data.area[dim] < target.data.area[larger]) ||
                (c.data.area[larger] >= target.data.area[dim] && c.data.area[larger] < target.data.area[larger]);
    }

    /**
     * 对活动立方体进行自定义处理
     * @param op 自定义操作
     * @return 调用者
     */
    public Structure process(Consumer<Cube> op) {
        ar.recordAndRun(ctx -> ctx.active.forEach(op));
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
        ar.recordAndRun(ctx -> {
            Cube c = new Cube(this, name);
            c.set(x1, y1, z1, x2, y2, z2); //Cube内部进行旋转
            create0(ctx, c);
        });
        return this;
    }

    /**
     * 创建一个立方体，并作为唯一的活动立方体
     * @see #create(String, float, float, float, float, float, float)
     */
    public Structure create(float x1, float y1, float z1, float x2, float y2, float z2) {
        return create("cube" + maxId++, x1, y1, z1, x2, y2, z2);
    }

    private static void create0(ActionContext ctx, Cube c) {
        ctx.active.clear();
        ctx.active.add(c);
        internalAdd(ctx, c);
    }

    /**
     * 对所有活动立方体进行平移
     * @param ds 方向
     * @param val 大小
     * @return 调用者
     */
    public Structure shift(float val, Direction ... ds) {
        ar.recordAndRun(ctx -> {
            for (Direction d : ds) {
                d = d.clockwise(ar);
                shift0(ctx, ctx.active, val, d);
            }
        });
        return this;
    }

    /**
     * 将活动立方体沿某个方向前后平移，直到结构在该方向上与基准重合
     * @param base 基准的坐标
     * @param faces 平移的方向
     * @return 调用者
     */
    public Structure align(int base, Direction ... faces) {
        ar.recordAndRun(ctx -> {
            for (Direction f : faces) {
                f = f.clockwise(ar);
                shift(-Math.abs(ctx.outline[f.index] - base), f);
            }
        });
        return this;
    }

    /**
     * 对活动立方体进行挖勺操作
     * @param op 配置挖勺工作参数的逻辑
     * @return 调用者
     */
    public Structure scoop(Consumer<ScoopConfig> op) {
        ar.recordAndRun(ctx -> {
            List<Cube> temp = new ArrayList<>(ctx.active);
            ctx.active.clear();
            ScoopConfig cfg = new ScoopConfig(this.basePoint, ctx, ar);
            op.accept(cfg);
            for (Cube e : temp) {
                ScoopResult result = e.scoop(cfg);
                if (result.children.isEmpty()) {
                    continue;
                }
                ctx.cubes.remove(e);
                result.children.forEach((d, childCube) -> ctx.active.add(childCube));
                cfg.finishedAction.accept(result);
            }
            ctx.cubes.forEach(c -> updateOutline(ctx, c));
        });
        return this;
    }

    /**
     * 查找满足特定规则的立方体，并设置为活动立方体
     * @param condition 规则
     * @return 调用者
     */
    public Structure find(Predicate<Cube> condition) {
        ar.recordAndRun(ctx -> {
            ctx.active.clear();
            ctx.cubes.stream().filter(condition).forEach(ctx.active::add); //condition执行的时候Cube内部会自动进行变换
        });
        return this;
    }

    /**
     * 找到在某个方向上坐标数值最大的立方体
     * @param d 方向
     * @return 调用者
     */
    public Structure findMost(Direction d) {
        ar.recordAndRun(ctx -> {
            Direction d0 = d.clockwise(ar);
            find(c -> ctx.outline[d0.index] == c.data.area[d0.index]);
        });
        return this;
    }

    /**
     * 对活动立方体进行过滤
     * @param condition 过滤条件
     * @return 调用者
     */
    public Structure filter(Predicate<Cube> condition) {
        ar.recordAndRun(ctx -> {
            List<Cube> worker = new ArrayList<>(ctx.active);
            ctx.active.clear();
            worker.stream().filter(condition).forEach(ctx.active::add);
        });
        return this;
    }

    /**
     * 获得某个方向上的坐标最大值
     * @param d 方向
     * @return 坐标值
     */
    public float getOutline(Direction d) {
        return ctx.outline[d.clockwise(ar).index];
    }

    /**
     * 将结构转化为Json
     * @return Json对象
     */
    public JsonArray toJson() {
        JsonArray elementsObj = new JsonArray();
        for (Cube c : ctx.cubes) {
            elementsObj.add(c.toJson());
        }
        return elementsObj;
    }

    /**
     * 将结构转化成等价的{@link VoxelShape}对象
     * @return 形状
     */
    public VoxelShape toVoxelShape() {
        if (ctx.cubes.isEmpty()) {
            return Shapes.empty();
        }
        VoxelShape shape = null;
        for (Cube c : ctx.cubes) {
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
        ctx.cubes.clear();
        ctx.active.clear();
    }

    private static void shift0(ActionContext ctx, Collection<Cube> targets, float val, Direction d) {
        if (!d.isPositive) {
            val = -val;
        }
        int i = d.dim, j = d.dim + 3;
        for (Cube e : targets) {
            e.data.area[i] += val;
            e.data.area[j] += val;
        }
        ctx.outline[i] += val;
        ctx.outline[j] += val;
    }

    static void internalAdd(ActionContext ctx, Cube e) {
        ctx.cubes.add(e);
        updateOutline(ctx, e);
    }

    private static void updateOutline(ActionContext ctx, Cube b) {
        for (int i = 0, j = 3 ; i < 3 ; i++, j++) {
            ctx.outline[i] = Math.min(b.data.area[i], ctx.outline[i]);
            ctx.outline[j] = Math.max(b.data.area[j], ctx.outline[j]);
        }
    }
}
