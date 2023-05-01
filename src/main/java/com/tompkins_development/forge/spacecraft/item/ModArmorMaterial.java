package com.tompkins_development.forge.spacecraft.item;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public record ModArmorMaterial(String name, int durability, int[] protection, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) implements ArmorMaterial {

    private static final int[] DURABILITY_PER_SLOT = new int[] {13,15,16,11};

    @Override
    public int getDurabilityForType(ArmorItem.Type armorItem) {
        return DURABILITY_PER_SLOT[armorItem.getSlot().getIndex()] * this.durability;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type armorItem) {
        return this.protection[armorItem.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @Override
    public String getName() {
        return SpacecraftMod.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
