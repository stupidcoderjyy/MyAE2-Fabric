package com.stupidcoderx.modding.datagen.model.elements;

import java.util.Arrays;

public abstract class SeparateObject<T extends SeparateObject<T>> {
    final int dim;
    final float[] data;

    SeparateObject(int dimension) {
        this.dim = dimension;
        this.data = new float[dimension * 2];
    }

    abstract T copy();

    abstract void onSeparated(Direction direction, T result, float[] range);

    T copyData(T target) {
        System.arraycopy(data, 0, target.data, 0, data.length);
        return target;
    }

    SeparationResult<T> separate(SeparationConfig config) {
        SeparationResult<T> result = new SeparationResult<>();
        float[] range = Arrays.copyOf(config.range, 6);
        if (isSeparated(range)) {
            return result;
        }
        shrink(range);
        for (int m = 0 ; m < dim ; m++) {
            int i = config.dimSeq[m], j = i + 3;
            if (range[i] > data[i]) {
                Direction d = Direction.get(false, i);
                T c = copy();
                c.data[j] = range[i];
                result.children.put(d, c);
                onSeparated(d, c, range);
                data[i] = range[i];
            }
            if (range[j] < data[j]) {
                Direction d = Direction.get(true, i);
                T c = copy();
                c.data[i] = range[j];
                result.children.put(d, c);
                onSeparated(d, c, range);
                data[j] = range[j];
            }
        }
        return result;
    }

    private void shrink(float[] r) {
        for (int i = 0, j = dim; i < dim; i ++, j++) {
            r[i] = Math.max(r[i], data[i]);
            r[j] = Math.min(r[j], data[j]);
        }
    }

    private boolean isSeparated(float[] r) {
        for (int i = 0, j = dim; i < dim; i ++, j ++) {
            if (data[i] >= r[j] || data[j] <= r[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
