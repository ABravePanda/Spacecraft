package com.tompkins_development.forge.spacecraft.datagen.item;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SpacecraftMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.RAW_TITANIUM);
        simpleItem(ModItems.TITANIUM_INGOT);
        simpleItem(ModItems.OXYGEN_TANK_SMALL);
        armorItem(ModItems.SPACE_HELMET_V1);
        armorItem(ModItems.SPACE_CHESTPLATE_V1);
        armorItem(ModItems.SPACE_LEGS_V1);
        armorItem(ModItems.SPACE_BOOTS_V1);

    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(SpacecraftMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder armorItem(RegistryObject<ArmorItem> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(SpacecraftMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(SpacecraftMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}