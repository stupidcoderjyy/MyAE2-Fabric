package com.stupidcoderx.modding.datagen.model.elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionContext {
    final float[] outline = new float[6];
    final Set<Cube> cubes = new HashSet<>();
    final List<Cube> active = new ArrayList<>();
    ICubeCreateStrategy cubeCreateStrategy = ICubeCreateStrategy.CENTER;
}
