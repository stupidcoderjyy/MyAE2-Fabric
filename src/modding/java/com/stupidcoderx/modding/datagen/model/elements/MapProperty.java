package com.stupidcoderx.modding.datagen.model.elements;

import com.google.common.base.Preconditions;

public class MapProperty<V> implements TransformProperty<MapProperty<V>>{
    private final Object[] values = new Object[3];

    MapProperty(V v1, V v2, V v3) {
        values[0] = v1;
        values[1] = v2;
        values[2] = v3;
    }

    V get(int i) {
        Preconditions.checkArgument(i >= 0 && i < 3);
        return (V)values[i];
    }

    V x() {
        return (V)values[0];
    }

    V y() {
        return (V)values[1];
    }

    V z() {
        return (V)values[2];
    }

    MapProperty<V> set(V v1, V v2, V v3) {
        values[0] = v1;
        values[1] = v2;
        values[2] = v3;
        return this;
    }

    @Override
    public MapProperty<V> rotate(int dim, int count) {
        Object v0 = values[Direction.PX.clockwise(dim, count).dim];
        Object v1 = values[Direction.PY.clockwise(dim, count).dim];
        Object v2 = values[Direction.PZ.clockwise(dim, count).dim];
        values[0] = v0;
        values[1] = v1;
        values[2] = v2;
        return this;
    }

    @Override
    public MapProperty<V> shift(float px, float py, float pz) {
        return this;
    }
}
