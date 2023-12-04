package com.stupidcoderx.modding.datagen.blockstate;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariantsBlockStateBuilder implements IBlockStateBuilder{
    private int maxTypeId = 0;
    final List<VariantType> idToTypes = new ArrayList<>();
    final Map<String, VariantType> nameToTypes = new HashMap<>();
    private IStateModelCondition condition;

    public VariantsBlockStateBuilder variant(String name, List<String> options) {
        Preconditions.checkArgument(options.size() > 0, "empty options");
        VariantType type = new VariantType(maxTypeId++, name, options);
        idToTypes.add(type);
        nameToTypes.put(name, type);
        return this;
    }

    public VariantsBlockStateBuilder variantFace() {
        return variant("face", List.of("north", "south", "top", "bottom", "east", "west"));
    }

    public VariantsBlockStateBuilder condition(IStateModelCondition p) {
        this.condition = p;
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject rootObj = new JsonObject();
        JsonObject variantsObj = new JsonObject();
        rootObj.add("variants", variantsObj);
        VariantsState state = new VariantsState(this, new String[maxTypeId]);
        if (maxTypeId == 0) {
            addStateBranch(variantsObj, state);
        } else {
            recurseTest(variantsObj, state, 0);
        }
        idToTypes.clear();
        nameToTypes.clear();
        maxTypeId = 0;
        return rootObj;
    }

    private void recurseTest(JsonObject variantsObj, VariantsState state, int curType) {
        boolean recurse = curType + 1 < maxTypeId;
        for (String option : idToTypes.get(curType).options) {
            state.properties[curType] = option;
            if (recurse) {
                recurseTest(variantsObj, state, curType + 1);
            }
            addStateBranch(variantsObj, state);
        }
    }

    private void addStateBranch(JsonObject variantsObj, VariantsState state) {
        variantsObj.add(state.toString(), condition.getModel(state).toJson());
    }
}
