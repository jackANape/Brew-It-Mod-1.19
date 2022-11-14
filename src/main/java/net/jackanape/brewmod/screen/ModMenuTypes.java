package net.jackanape.brewmod.screen;

import net.jackanape.brewmod.BrewMod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, BrewMod.MOD_ID);

    public static final RegistryObject<MenuType<ChromeBottlerMenu>> CHROME_BOTTLER_MENU =
            registerMenuType(ChromeBottlerMenu::new, "chrome_bottler_menu");

    public static final RegistryObject<MenuType<ChromeMillMenu>> CHROME_MILL_MENU =
            registerMenuType(ChromeMillMenu::new, "chrome_mill_menu");

    public static final RegistryObject<MenuType<MashTunMenu>> MASH_TUN_MENU =
            registerMenuType(MashTunMenu::new, "mash_tun_menu");

    public static final RegistryObject<MenuType<BrewingKettleMenu>> BREWING_KETTLE_MENU =
            registerMenuType(BrewingKettleMenu::new, "brewing_kettle_menu");

    public static final RegistryObject<MenuType<FermentingBarrelMenu>> FERMENTING_BARREL_MENU =
            registerMenuType(FermentingBarrelMenu::new, "fermenting_barrel_menu");


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
