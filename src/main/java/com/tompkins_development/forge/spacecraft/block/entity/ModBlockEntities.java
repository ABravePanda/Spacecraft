package com.tompkins_development.forge.spacecraft.block.entity;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SpacecraftMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<OxygenCollectorBlockEntity>> OXYGEN_COLLECTOR = BLOCK_ENTITIES.register("oxygen_collector",
            ()-> BlockEntityType.Builder.of(OxygenCollectorBlockEntity::new, ModBlocks.OXYGEN_COLLECTOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<OxygenCableBlockEntity>> OXYGEN_CABLE = BLOCK_ENTITIES.register("oxygen_cable",
            ()-> BlockEntityType.Builder.of(OxygenCableBlockEntity::new, ModBlocks.OXYGEN_CABLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<OxygenTankBlockEntity>> OXYGEN_TANK = BLOCK_ENTITIES.register("oxygen_tank",
            ()-> BlockEntityType.Builder.of(OxygenTankBlockEntity::new, ModBlocks.OXYGEN_TANK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
