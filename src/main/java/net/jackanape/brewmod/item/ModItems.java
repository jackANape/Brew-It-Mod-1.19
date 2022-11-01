package net.jackanape.brewmod.item;

import net.jackanape.brewmod.BrewMod;
import net.jackanape.brewmod.block.ModBlocks;
import net.jackanape.brewmod.block.fluid.ModFluids;
import net.jackanape.brewmod.item.custom.EightBallItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
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

    public static final RegistryObject<Item> EIGHT_BALL = ITEMS.register("eight_ball",
            () -> new EightBallItem(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB).stacksTo(1)));

    public static final RegistryObject<Item> SOAP_WATER_BUCKET = ITEMS.register("soap_water_bucket",
            () -> new BucketItem(ModFluids.SOURCE_SOAP_WATER,
                    new Item.Properties().tab(ModCreativeModeTab.BREW_TAB).craftRemainder(Items.BUCKET).stacksTo(1)));


    /* ##########  Brew Mod  ########## */

    /* minerals */
    public static final RegistryObject<Item> RAW_CHROMIUM = ITEMS.register("raw_chromium",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> CHROMIUM_INGOT = ITEMS.register("chromium_ingot",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    /* misc */
    public static final RegistryObject<Item> BEER_BUCKS = ITEMS.register("beer_bucks",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    /* utensils */
    public static final RegistryObject<Item> EMPTY_PINT_GLASS = ITEMS.register("empty_pint_glass",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB).stacksTo(64)));


    public static final RegistryObject<Item> BEER_CUBE_EMPTY = ITEMS.register("beer_cube_empty",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));


    /* produce */
    public static final RegistryObject<Item> BEER_WATER_BUCKET = ITEMS.register("beer_water_bucket",
            () -> new BucketItem(ModFluids.SOURCE_BEER_WATER,
                    new Item.Properties().tab(ModCreativeModeTab.BREW_TAB).craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final RegistryObject<Item> LAGER_PINT = ITEMS.register("lager_pint",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB).stacksTo(1)
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(2f).build())));

    public static final RegistryObject<Item> WORT = ITEMS.register("wort",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> BEER_CUBE = ITEMS.register("beer_cube",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> HOPS = ITEMS.register("hops",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));

    public static final RegistryObject<Item> BARLEY_SEEDS = ITEMS.register("barley_seeds",
            () -> new ItemNameBlockItem(ModBlocks.BARLEY_CROP.get(),
                    new Item.Properties().tab(ModCreativeModeTab.BREW_TAB).stacksTo(64)));

    public static final RegistryObject<Item> BARLEY = ITEMS.register("barley",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB).stacksTo(64)));

    public static final RegistryObject<Item> MILLED_BARLEY = ITEMS.register("milled_barley",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BREW_TAB)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
