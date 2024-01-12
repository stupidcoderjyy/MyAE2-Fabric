package com.stupidcoderx.modding.datagen.model.elements;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActionRecord {
    private boolean recording;
    private boolean running;
    private final List<IAction> actions = new ArrayList<>();
    private boolean hasRotation = false;
    private boolean hasShift = false;
    private int rotDim, rotCount;
    private final float[] shiftOffsets = new float[3];
    private final Structure parent;

    ActionRecord(Structure parent) {
        this.parent = parent;
    }

    void startRecord() {
        actions.clear();
        recording = true;
    }

    void endRecord() {
        recording = false;
    }

    int rotDim() {
        return rotDim;
    }

    int rotCount() {
        return hasRotation ? rotCount : 0;
    }

    float shiftX() {
        return hasShift ? shiftOffsets[0] : 0;
    }

    float shiftY() {
        return hasShift ? shiftOffsets[1] : 0;
    }

    float shiftZ() {
        return hasShift ? shiftOffsets[2] : 0;
    }

    void recordAndRun(IAction a) {
        if (recording && !running) {
            actions.add(a);
        }
        a.run(parent.ctx);
    }

    public void setRotation(int rotDim, int rotCount) {
        this.rotDim = rotDim;
        this.rotCount = rotCount;
        this.hasRotation = true;
    }

    public void setShift(float shiftX, float shiftY, float shiftZ) {
        this.shiftOffsets[0] = shiftX;
        this.shiftOffsets[1] = shiftY;
        this.shiftOffsets[2] = shiftZ;
        this.hasShift = true;
    }

    void runRecord(@NotNull ActionContext ctx) {
        Preconditions.checkState(!recording, "can not run actions when recording");
        running = true;
        actions.forEach(a -> a.run(ctx));
        running = false;
    }
}
