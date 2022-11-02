package net.jackanape.brewmod.block;

import net.jackanape.brewmod.BrewMod;
import net.jackanape.brewmod.block.custom.*;
import net.jackanape.brewmod.block.fluid.ModFluids;
import net.jackanape.brewmod.item.ModCreativeModeTab;
import net.jackanape.brewmod.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BrewMod.MOD_ID);

    public static final RegistryObject<Block> JUMPY_BLOCK = registerBlock("jumpy_block",
            () -> new JumpyBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops()), ModCreativeModeTab.BREW_TAB);

    public static final RegistryObject<Block> ZIRCON_LAMP = registerBlock("zircon_lamp",
            () -> new ZirconLampBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(ZirconLampBlock.LIT) ? 15 : 0)), ModCreativeModeTab.BREW_TAB);

    public static final RegistryObject<Block> ZIRCON_BLOCK = registerBlock("zircon_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops()), ModCreativeModeTab.BREW_TAB);

    public static final RegistryObject<Block> ZIRCON_ORE = registerBlock("zircon_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)), ModCreativeModeTab.BREW_TAB);

    public static final RegistryObject<Block> DEEPSLATE_ZIRCON_ORE = registerBlock("deepslate_zircon_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)), ModCreativeModeTab.BREW_TAB);

    public static final RegistryObject<LiquidBlock> SOAP_WATER_BLOCK = BLOCKS.register("soap_water_block",
            () -> new LiquidBlock(ModFluids.SOURCE_SOAP_WATER, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<Block> PLANT_POT = registerBlock("plantpot",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.WOOD)), ModCreativeModeTab.BREW_TAB);



    /* ##########  Brew Mod  ########## */

    /* minerals */
    public static final RegistryObject<Block> CHROMIUM_ORE = registerBlock("chromium_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(3f, 10f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL)), ModCreativeModeTab.BREW_TAB);

    public static final RegistryObject<Block> DEEPSLATE_CHROMIUM_ORE = registerBlock("deepslate_chromium_ore",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(3f, 10f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL)), ModCreativeModeTab.BREW_TAB);

    public static final RegistryObject<Block> BARLEY_CROP = BLOCKS.register("barley_crop",
            () -> new BarleyCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    public static final RegistryObject<LiquidBlock> BEER_WATER_BLOCK = BLOCKS.register("beer_water_block",
            () -> new LiquidBlock(ModFluids.SOURCE_BEER_WATER, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<Block> BAR_BLOCK = registerBlock("bar_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.WOOD)
                    .strength(3f, 10f)
                    .requiresCorrectToolForDrops().sound(SoundType.WOOD)), ModCreativeModeTab.BREW_TAB);

    /* Custom Blocks */
    public static final RegistryObject<Block> WOODEN_BARREL = registerBlock("wooden_barrel",
            () -> new WoodenBarrelBlock(BlockBehaviour.Properties.of(Material.WOOD)
                    .strength(3f).requiresCorrectToolForDrops().noOcclusion()), ModCreativeModeTab.BREW_TAB);

    public static final RegistryObject<Block> CHROME_BOTTLER = registerBlock("chrome_bottler",
            () -> new ChromeBottlerBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(6f).requiresCorrectToolForDrops().noOcclusion()), ModCreativeModeTab.BREW_TAB);

    /* Barkeep POI */
    public static final RegistryObject<Block> BARKEEP_WORK_BLOCK = registerBlock("barkeep_work_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.WOOD)
                    .strength(3f, 10f)
                    .requiresCorrectToolForDrops().sound(SoundType.WOOD)), ModCreativeModeTab.BREW_TAB);

    //


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);

        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
}
