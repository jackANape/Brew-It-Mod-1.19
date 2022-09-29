package net.jackanape.brewmod.item;

import net.jackanape.brewmod.BrewMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BrewMod.MOD_ID);

    public static final RegistryObject<Item> ZIRCON = ITEMS.register("zircon",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> RAW_ZIRCON = ITEMS.register("raw_zircon",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));



    /* ##########  Brew Mod  ########## */

    /* minerals */
    public static final RegistryObject<Item> RAW_CHROMIUM = ITEMS.register("raw_chromium",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> CHROMIUM_INGOT = ITEMS.register("chromium_ingot",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));


    /* utensils */
    public static final RegistryObject<Item> EMPTY_PINT_GLASS = ITEMS.register("empty_pint_glass",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));


    public static final RegistryObject<Item> BEER_CUBE_EMPTY = ITEMS.register("beer_cube_empty",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));


    /* produce */
    public static final RegistryObject<Item> LAGER_PINT = ITEMS.register("lager_pint",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> WORT = ITEMS.register("wort",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> BEER_CUBE = ITEMS.register("beer_cube",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> MILLED_BARLEY = ITEMS.register("milled_barley",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> HOPS = ITEMS.register("hops",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
