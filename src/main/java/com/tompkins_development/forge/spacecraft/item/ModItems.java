package com.tompkins_development.forge.spacecraft.item;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.item.custom.SmallOxygenTankItem;
import com.tompkins_development.forge.spacecraft.item.custom.SpaceSuitV1ArmorItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SpacecraftMod.MOD_ID);

    public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_TITANIUM = ITEMS.register("raw_titanium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> OXYGEN_TANK_SMALL = ITEMS.register("oxygen_tank_small",
            () -> new SmallOxygenTankItem());

    public static final RegistryObject<ArmorItem> SPACE_HELMET_V1 = ITEMS.register("space_helmet_v1",
            () -> new SpaceSuitV1ArmorItem(ArmorTiers.TITANIUM, Type.HELMET, new Item.Properties()));
    public static final RegistryObject<ArmorItem> SPACE_CHESTPLATE_V1 = ITEMS.register("space_chestplate_v1",
            () -> new SpaceSuitV1ArmorItem(ArmorTiers.TITANIUM, Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<ArmorItem> SPACE_LEGS_V1 = ITEMS.register("space_legs_v1",
            () -> new SpaceSuitV1ArmorItem(ArmorTiers.TITANIUM, Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<ArmorItem> SPACE_BOOTS_V1 = ITEMS.register("space_boots_v1",
            () -> new SpaceSuitV1ArmorItem(ArmorTiers.TITANIUM, Type.BOOTS, new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static class ArmorTiers {
        public static final ArmorMaterial TITANIUM = new ModArmorMaterial(
                "titanium",
                20,
                new int[] {10,13, 15, 5},
                300,
                SoundEvents.ARMOR_EQUIP_IRON,
                0.0f,
                0.0f,
                () -> Ingredient.of(ModItems.TITANIUM_INGOT.get()));
    }
}