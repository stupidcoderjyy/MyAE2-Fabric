package com.stupidcoderx.modding.element.block;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import java.util.Arrays;
import java.util.Stack;
import java.util.function.Consumer;

class BlockPropertyManager {
    private final Stack<BlockProperty> properties = new Stack<>();

    @SafeVarargs
    final void pushProp(boolean inherit, boolean removeOnGet, Consumer<Properties> ... modifiers) {
        BlockProperty parentBp = (inherit && !properties.empty()) ? properties.peek() : null;
        BlockProperty bp = new BlockProperty(removeOnGet, parentBp);
        bp.modifiers.addAll(Arrays.asList(modifiers));
        properties.push(bp);
    }

    void popProp() {
        properties.pop();
    }

    void dupProp() {
        properties.push(properties.peek().dup());
    }

    Properties build() {
        if (properties.empty()) {
            return Properties.of();
        }
        BlockProperty bp = properties.peek();
        if (bp.removeOnGet) {
            properties.pop();
        }
        return bp.get();
    }
}
