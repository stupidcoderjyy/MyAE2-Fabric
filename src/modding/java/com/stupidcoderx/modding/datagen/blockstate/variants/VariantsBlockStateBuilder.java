package com.stupidcoderx.modding.datagen.blockstate.variants;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.stupidcoderx.modding.datagen.blockstate.IBuilderElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariantsBlockStateBuilder implements IBuilderElement {
    private int maxTypeId = 0;
    final List<VariantType> idToTypes = new ArrayList<>();
    final Map<String, VariantType> nameToTypes = new HashMap<>();
    private IStateModelCondition condition;

    /**
     * 添加一个状态变量
     * @param name 变量名
     * @param options 变量的所有可能值
     * @return 调用者
     */
    public VariantsBlockStateBuilder variant(String name, List<String> options) {
        Preconditions.checkArgument(options.size() > 0, "empty options");
        VariantType type = new VariantType(maxTypeId++, name, options);
        idToTypes.add(type);
        nameToTypes.put(name, type);
        return this;
    }

    /**
     * 添加面的状态变量
     * @return 调用者
     */
    public VariantsBlockStateBuilder variantFace() {
        return variant("face", List.of("east", "south", "west", "north", "top", "down"));
    }

    /**
     * 设置获取模型属性的逻辑，这决定了每个状态下的方块模型
     * @param p 根据变量值获取模型属性的逻辑
     * @return 调用者
     */
    public VariantsBlockStateBuilder condition(IStateModelCondition p) {
        this.condition = p;
        return this;
    }

    @Override
    public JsonObject toJson() {
        JsonObject rootObj = new JsonObject();
        JsonObject variantsObj = new JsonObject();
        rootObj.add("variants", variantsObj);
        VariantsState state = new VariantsState(this, new String[maxTypeId], new int[maxTypeId]);
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
        List<String> options = idToTypes.get(curType).options;
        for (int i = 0; i < options.size(); i++) {
            String option = options.get(i);
            state.properties[curType] = option;
            state.optionIndex[curType] = i;
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
