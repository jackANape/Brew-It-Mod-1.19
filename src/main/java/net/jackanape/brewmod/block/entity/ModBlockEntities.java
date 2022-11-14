package net.jackanape.brewmod.block.entity;

import net.jackanape.brewmod.BrewMod;
import net.jackanape.brewmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BrewMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<ChromeBottlerBlockEntity>> CHROME_BOTTLER =
            BLOCK_ENTITIES.register("chrome_bottler", () ->
                    BlockEntityType.Builder.of(ChromeBottlerBlockEntity::new,
                            ModBlocks.CHROME_BOTTLER.get()).build(null));

    public static final RegistryObject<BlockEntityType<ChromeMillBlockEntity>> CHROME_MILL =
            BLOCK_ENTITIES.register("chrome_mill", () ->
                    BlockEntityType.Builder.of(ChromeMillBlockEntity::new,
                            ModBlocks.CHROME_MILL.get()).build(null));

    public static final RegistryObject<BlockEntityType<MashTunBlockEntity>> MASH_TUN =
            BLOCK_ENTITIES.register("mash_tun", () ->
                    BlockEntityType.Builder.of(MashTunBlockEntity::new,
                            ModBlocks.MASH_TUN.get()).build(null));

    public static final RegistryObject<BlockEntityType<BrewingKettleBlockEntity>> BREWING_KETTLE =
            BLOCK_ENTITIES.register("brewing_kettle", () ->
                    BlockEntityType.Builder.of(BrewingKettleBlockEntity::new,
                            ModBlocks.BREWING_KETTLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<FermentingBarrelBlockEntity>> FERMENTING_BARREL =
            BLOCK_ENTITIES.register("fermenting_barrel", () ->
                    BlockEntityType.Builder.of(FermentingBarrelBlockEntity::new,
                            ModBlocks.FERMENTING_BARREL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
