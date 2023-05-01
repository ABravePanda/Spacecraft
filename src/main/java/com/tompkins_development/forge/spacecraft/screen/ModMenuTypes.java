package com.tompkins_development.forge.spacecraft.screen;

import com.tompkins_development.forge.spacecraft.SpacecraftMod;
import com.tompkins_development.forge.spacecraft.screen.oxygen.OxygenCollectorMenu;
import com.tompkins_development.forge.spacecraft.screen.oxygen.OxygenTankMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, SpacecraftMod.MOD_ID);

    public static final RegistryObject<MenuType<OxygenCollectorMenu>> OXYGEN_COLLECTOR_MENU =
            registerMenuType(OxygenCollectorMenu::new, "oxygen_collector_menu");

    public static final RegistryObject<MenuType<OxygenTankMenu>> OXYGEN_TANK_MENU =
            registerMenuType(OxygenTankMenu::new, "oxygen_tank_menu");


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
