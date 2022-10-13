package net.jackanape.brewmod.villager;

import com.google.common.collect.ImmutableSet;
import net.jackanape.brewmod.BrewMod;
import net.jackanape.brewmod.block.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, BrewMod.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, BrewMod.MOD_ID);

    //where the villager goes to get profession
    public static final RegistryObject<PoiType> JUMPY_BLOCK_POI = POI_TYPES.register("jumpy_block_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.JUMPY_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> JUMP_MASTER = VILLAGER_PROFESSIONS.register("jumpy_master",
            () -> new VillagerProfession("jumper_master", x -> x.get() == JUMPY_BLOCK_POI.get(),
                    x -> x.get() == JUMPY_BLOCK_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_ARMORER));


    /* Brew Villager */
    public static final RegistryObject<PoiType> ZIRCON_BLOCK_POI = POI_TYPES.register("zircon_block_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.ZIRCON_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> BREW_MASTER = VILLAGER_PROFESSIONS.register("brew_master",
            () -> new VillagerProfession("brew_master", x -> x.get() == ZIRCON_BLOCK_POI.get(),
                    x -> x.get() == ZIRCON_BLOCK_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_LEATHERWORKER));


    public static void registerPOIs() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class,
                    "registerBlockStates", PoiType.class).invoke(null, JUMPY_BLOCK_POI.get());

            ObfuscationReflectionHelper.findMethod(PoiType.class,
                    "registerBlockStates", PoiType.class).invoke(null, ZIRCON_BLOCK_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
            ;
        }
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }

}
