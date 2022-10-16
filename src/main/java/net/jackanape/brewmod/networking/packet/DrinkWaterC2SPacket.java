package net.jackanape.brewmod.networking.packet;

import net.jackanape.brewmod.block.ModBlocks;
import net.jackanape.brewmod.networking.ModMessages;
import net.jackanape.brewmod.thirst.PlayerThirstProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DrinkWaterC2SPacket {

    private static final String MESSAGE_DRINK_WATER = "message.brewmod.drink_water";
    private static final String MESSAGE_NO_WATER = "message.brewmod.no_water";

    public DrinkWaterC2SPacket() {

    }

    public DrinkWaterC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //here we are on the server!!
            //this can get all player info from server players
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            //check if player is near water
            //be able to drink beer from barrel

            if (hasWaterAroundThem(player, level, 2)) {
                //notify the player that water has been drunk
                player.sendSystemMessage(Component.translatable(MESSAGE_DRINK_WATER)
                        .withStyle(ChatFormatting.DARK_AQUA));

                //play the drinking sound
                level.playSound(null, player.getOnPos(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS,
                        0.5F, level.random.nextFloat() * 0.1F + 0.9F);

                //increase the water level / thirst level of player
                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    thirst.addThirst(1);

                    //output the current thirst level
                    player.sendSystemMessage(Component.literal("Current Thirst " + thirst.getThirst())
                            .withStyle(ChatFormatting.AQUA));
                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), player);
                });


            } else {
                //notify the player that there's no water
                //output the current thirst level
                player.sendSystemMessage(Component.translatable(MESSAGE_NO_WATER)
                        .withStyle(ChatFormatting.RED));

                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    //output the current thirst level
                    player.sendSystemMessage(Component.literal("Current Thirst " + thirst.getThirst())
                            .withStyle(ChatFormatting.AQUA));
                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), player);
                });
            }

        });

        return true;
    }

    //allow player to drink from barrel or open keg
    private boolean hasWaterAroundThem(ServerPlayer player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(ModBlocks.BEER_WATER_BLOCK.get())).toArray().length > 0;
    }

}
