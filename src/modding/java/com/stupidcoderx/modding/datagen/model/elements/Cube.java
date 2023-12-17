package com.stupidcoderx.modding.datagen.model.elements;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.stupidcoderx.modding.util.JsonUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;
import java.util.function.Predicate;

/**
 * 立方体，可以为其绑定材质
 */
public class Cube extends SeparateObject<Cube> {
    String name;
    final Map<Direction, Face> faces = new EnumMap<>(Direction.class);
    private boolean uvCorrect = true;
    private final List<Face> activeFaces = new ArrayList<>();
    private final Structure structure;

    Cube(Structure structure, String name) {
        super(3);
        this.structure = structure;
        this.name = name;
    }

    void set(float x1, float y1, float z1, float x2, float y2, float z2) {
        data[0] = x1;
        data[1] = y1;
        data[2] = z1;
        data[3] = x2;
        data[4] = y2;
        data[5] = z2;
    }

    /**
     * 设置立方体名称
     * @param name 名称
     * @return 调用者
     */
    public Cube name(Object name) {
        Preconditions.checkNotNull(name);
        this.name = name.toString();
        return this;
    }

    /**
     * 创建需要渲染的面，并成为活动面。如果某个方向的面已经存在，则不会创建。
     * @param ds 方向
     * @return 调用者
     */
    public Cube face(Direction ... ds) {
        activeFaces.clear();
        for (Direction d : ds) {
            Face f = faces.get(d);
            if (f == null) {
                f = new Face(d);
                faces.put(d, f);
            }
            activeFaces.add(f);
        }
        return this;
    }

    /**
     * @see #face(Direction...)
     */
    public Cube faceAll() {
        return face(Direction.values());
    }

    /**
     * @see #face(Direction...)
     */
    public Cube face(Predicate<Direction> condition) {
        return face(Arrays.stream(Direction.values()).filter(condition).toArray(Direction[]::new));
    }

    /**
     * 是否存在方向上的面
     * @param d 方向
     * @return 存在返回true
     */
    public boolean faceExits(Direction d) {
        return faces.containsKey(d);
    }

    /**
     * 方向上的面是否存在材质
     * @param d 方向
     * @return 存在返回true，当不存在对应面时返回false
     */
    public boolean faceHasTexture(Direction d) {
        Face face = faces.get(d);
        return face != null && face.texture != null;
    }

    /**
     * 方向上面的uv坐标是否合法
     * @param d 方向
     * @return uv坐标合法返回true，当不存在对应面时返回false
     */
    public boolean faceHasValidUV(Direction d) {
        Face face = faces.get(d);
        return face != null && uvCorrect;
    }

    /**
     * 设置活动面的材质变量
     * @param t 材质变量名称（如"#0"）或材质路径（如"minecraft:block/dirt"）
     * @return 调用者
     */
    public Cube texture(String t) {
        activeFaces.forEach(f -> f.texture(t));
        return this;
    }

    /**
     * 设置活动面的材质uv，仅支持等比例材质UV
     * @param x1 起始x
     * @param y1 起始y
     * @param x2 终点x
     * @param y2 终点y
     * @throws IllegalArgumentException 传入的uv不是1:1的（自己算清楚）
     * @return 调用者
     */
    public Cube uv(float x1, float y1, float x2, float y2) {
        checkUV(x1, y1, x2, y2);
        activeFaces.forEach(f -> f.uv(x1, y1, x2, y2));
        return this;
    }

    /**
     * 设置活动面的材质uv，根据面的宽度自动补全UV
     * @param x 起始x
     * @param y 起始y
     * @return 调用者
     */
    public Cube uv(float x, float y) {
        activeFaces.forEach(f -> {
            calcFaceSize(f.direction);
            f.uv(x, y, x + tempSize[0], y + tempSize[1]);
        });
        return this;
    }

    /**
     * 强制设置活动面UV，但这会导致当前立方体无法被切割
     * @param x 起始x
     * @param y 起始y
     * @param width uv宽度
     * @param height uv高度
     * @return 调用者
     */
    public Cube forceUv(float x, float y, float width, float height) {
        uvCorrect = false;
        activeFaces.forEach(f -> f.uv(x, y, x + width, y + height));
        return this;
    }

    /**
     * 将活动面的纹理逆时针旋转90°若干次
     * @param times 旋转次数
     * @return 调用者
     */
    public Cube rotate(int times) {
        activeFaces.forEach(f -> f.rotateTimes = times);
        return this;
    }

    /**
     * 移除所有的活动面
     * @return 调用者
     */
    public Cube removeFaces() {
        activeFaces.forEach(f -> faces.remove(f.direction));
        activeFaces.clear();
        return this;
    }

    public float length() {
        return data[3] - data[0];
    }

    public float height() {
        return data[4] - data[1];
    }

    public float width() {
        return data[5] - data[2];
    }

    VoxelShape toVoxelShape() {
        return Block.box(data[0], data[1], data[2], data[3], data[4], data[5]);
    }

    JsonObject toJson() {
        JsonObject elementObj = new JsonObject();
        elementObj.addProperty("name", name);
        elementObj.add("from", JsonUtil.serializeFloatArray(data, 0, 3));
        elementObj.add("to", JsonUtil.serializeFloatArray(data, 3, 6));
        JsonObject facesObj = new JsonObject();
        for (Direction d : Direction.values()) {
            Face face = faces.get(d);
            if (face == null) {
                continue;
            }
            facesObj.add(d.id, face.toJson());
        }
        elementObj.add("faces", facesObj);
        return elementObj;
    }

    private void checkUV(float x1, float y1, float x2, float y2) {
        for (Face f : activeFaces) {
            Direction d = f.direction;
            calcFaceSize(d);
            if (x2 - x1 == tempSize[0] && y2 - y1 == tempSize[1]) {
                continue;
            }
            throw new IllegalArgumentException("only accept 1:1 texture uv");
        }
    }

    private final float[] tempSize = new float[2];

    private void calcFaceSize(Direction face) {
        int ix = 0, iy = 0;
        switch (face.dim) {
            case 0 -> {
                ix = 2;
                iy = 1;
            }
            case 1 -> iy = 2;
            case 2 -> iy = 1;
        }
        tempSize[0] = data[ix + 3] - data[ix];
        tempSize[1] = data[iy + 3] - data[iy];
    }

    @Override
    Cube copy() {
        Cube e = copyData(new Cube(structure, name));
        faces.forEach((d, f) -> e.faces.put(d, f.copy()));
        structure.internalAdd(e);
        return e;
    }

    private Cube result;
    private float valBig, valSmall;

    @Override
    void onSeparated(Direction d, Cube res, float[] range) {
        Preconditions.checkState(uvCorrect, "cube with incorrect uv cannot be scooped");
        result = res;
        valBig = Math.abs(range[d.dim + 3] - data[d.dim]);
        valSmall = Math.abs(data[d.dim + 3] - range[d.dim + 3]);

        //对与direction垂直的四个面对应的uv进行切割，由于MC中各个面的材质坐标系不满足对称性，所以只能枚举
        switch (d.dim) {
            case 0 -> { //x
                set(Direction.PY, d.isPositive, 0);
                set(Direction.PZ, d.isPositive, 0);
                set(Direction.NY, d.isPositive, 0);
                set(Direction.NZ, !d.isPositive, 0);
            }
            case 1 -> { //y
                set(Direction.PX, !d.isPositive, 1);
                set(Direction.NX, !d.isPositive, 1);
                set(Direction.PZ, !d.isPositive, 1);
                set(Direction.NZ, !d.isPositive, 1);
            }
            case 2 -> { //z
                set(Direction.PY, !d.isPositive, 1);
                set(Direction.PX, d.isPositive, 0);
                set(Direction.NY, !d.isPositive, 1);
                set(Direction.NX, !d.isPositive, 0);
            }
        }
    }

    private void set(Direction d, boolean start, int dim) {
        Face orgFace = faces.get(d);
        if (orgFace == null) {
            return;
        }
        Face resFace = result.faces.get(d);
        if (resFace == null) {
            float[] uv = orgFace.uv;
            result.face(d).uv(uv[0], uv[1], uv[2], uv[3]);
            resFace = result.faces.get(d);
        }
        resFace.shrink(start, dim, valBig);
        orgFace.shrink(!start, dim, valSmall); //这样才能实现UV的递推计算
    }

    @Override
    public String toString() {
        return name + ":" + super.toString();
    }
}
