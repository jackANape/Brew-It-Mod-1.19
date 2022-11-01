package net.jackanape.brewmod.event;


import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.jackanape.brewmod.BrewMod;
import net.jackanape.brewmod.item.ModItems;
import net.jackanape.brewmod.networking.ModMessages;
import net.jackanape.brewmod.networking.packet.ThirstDataSyncS2CPacket;
import net.jackanape.brewmod.thirst.PlayerThirst;
import net.jackanape.brewmod.thirst.PlayerThirstProvider;
import net.jackanape.brewmod.villager.ModVillagers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = BrewMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {

        //player can only buy/sell small items to get started with farming
        //lvl 1
        if (event.getType() == ModVillagers.BARKEEP.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            int villagerLevel = 1;

            //P2V - selling
            ItemStack player_empty_pint_glass = new ItemStack(ModItems.BEER_BUCKS.get(), 1); //item selling, p_41602 = quantity per sale
            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ModItems.EMPTY_PINT_GLASS.get(), 3), //giving villager item, cost
                    player_empty_pint_glass, 999999999, 8, 0.00F)); //return item, maxuses, villager xp gain, price multiplier

            //V2P - buying
            ItemStack villager_barley_seeds = new ItemStack(ModItems.BARLEY_SEEDS.get(), 3); //item buying, p_41602 = quantity per sale
            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ModItems.BEER_BUCKS.get(), 10), //giving villager item, cost
                    villager_barley_seeds, 999999999, 8, 0.00F)); //return item, maxuses, villager xp gain, price multiplier

        }

        //player can start buying beer from villager
        //lvl 2
        if (event.getType() == ModVillagers.BARKEEP.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            int villagerLevel = 2;

            //P2V - selling
            ItemStack player_lager = new ItemStack(ModItems.BEER_BUCKS.get(), 2); //item selling, p_41602 = quantity per sale
            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ModItems.LAGER_PINT.get(), 1), //giving villager item, cost
                    player_lager, 999999999, 8, 0.00F)); //return item, maxuses, villager xp gain, price multiplier

            //V2P - buying
            ItemStack villager_lager = new ItemStack(ModItems.LAGER_PINT.get(), 1); //p_41602 = quantity per sale
            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ModItems.BEER_BUCKS.get(), 5), //giving villager item, cost
                    villager_lager, 999999999, 8, 0.00F)); //return item, maxuses, villager xp gain, price multiplier

        }

        //player can start selling beer - small quantities
        //lvl 3
        if (event.getType() == ModVillagers.BARKEEP.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            int villagerLevel = 3;

            //V2P - buying
            ItemStack villager_empty_pint = new ItemStack(ModItems.EMPTY_PINT_GLASS.get(), 10); //item buying, p_41602 = quantity per sale
            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ModItems.BEER_BUCKS.get(), 13), //giving villager item, cost
                    villager_empty_pint, 999999999, 8, 0.00F)); //return item, maxuses, villager xp gain, price multiplier
        }

        //player can start selling beer - large quantities
        //lvl 4
        if (event.getType() == ModVillagers.BARKEEP.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            int villagerLevel = 4;

        }

        //lvl 5
        if (event.getType() == ModVillagers.BARKEEP.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            int villagerLevel = 5;

            //V2P - buying
            ItemStack villager_diamond = new ItemStack(Items.DIAMOND, 1); //item buying, p_41602 = quantity per sale
            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(ModItems.BEER_BUCKS.get(), 100), //giving villager item, cost
                    villager_diamond, 999999999, 8, 0.00F)); //return item, maxuses, villager xp gain, price multiplier

        }

        //empty cases
//            ItemStack villager_empty_case = new ItemStack(ModItems.EMPTY_BOTTLE_CASE.get(), 1); //item buying, p_41602 = quantity per sale
//            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
//                    new ItemStack(ModItems.BEER_BUCKS.get(), 10), //giving villager item, cost
//                    villager_empty_pint, 10, 8, 0.02F)); //return item, maxuses, villager xp gain, price multiplier

            //empty bottles
//            ItemStack villager_empty_bottles = new ItemStack(ModItems.EMPTY_GLASS_BOTTLE.get(), 10); //item buying, p_41602 = quantity per sale
//            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
//                    new ItemStack(ModItems.BEER_BUCKS.get(), 75), //giving villager item, cost
//                    villager_empty_pint, 10, 8, 0.02F)); //return item, maxuses, villager xp gain, price multiplier



    }

    //Remove later
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerThirstProvider.PLAYER_THIRST).isPresent()) {
                event.addCapability(new ResourceLocation(BrewMod.MOD_ID, "properties"), new PlayerThirstProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerThirst.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            event.player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                if (thirst.getThirst() > 0 && event.player.getRandom().nextFloat() < 0.005f) { // Once Every 10 Seconds on Avg
                    thirst.subThirst(1);
                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), ((ServerPlayer) event.player));
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), player);
                });
            }
        }
    }
}
