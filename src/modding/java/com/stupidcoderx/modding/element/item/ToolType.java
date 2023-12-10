package com.stupidcoderx.modding.element.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class ToolType implements Tier {
    private int uses, level, enchantmentValue;
    private float speed, attackDamageBonus;
    private Supplier<Ingredient> repairIngredient;

    public ToolType(Tier parent) {
        this.uses = parent.getUses();
        this.speed = parent.getSpeed();
        this.attackDamageBonus = parent.getAttackDamageBonus();
        this.level = parent.getLevel();
        this.enchantmentValue = parent.getEnchantmentValue();
        this.repairIngredient = parent::getRepairIngredient;
    }
    
    public ToolType uses(int val) {
        this.uses = val;
        return this;
    }
    
    public ToolType level(int val) {
        this.level = val;
        return this;
    }

    public ToolType attackDamageBonus(int val) {
        this.attackDamageBonus = val;
        return this;
    }

    public ToolType enchantmentValue(int val) {
        this.enchantmentValue = val;
        return this;
    }

    public ToolType speed(float val) {
        this.speed = val;
        return this;
    }

    public ToolType repairIngredient(Supplier<ItemLike> val) {
        this.repairIngredient = () -> Ingredient.of(val.get());
        return this;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
}
