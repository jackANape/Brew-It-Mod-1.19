package net.jackanape.brewmod.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExampleC2SPacket {

    public ExampleC2SPacket() {

    }

    public ExampleC2SPacket(FriendlyByteBuf buf) {

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

            //spawning thing can only be done on the server
            EntityType.COW.spawn(level, null, null, player.blockPosition(),
                    MobSpawnType.COMMAND, true, false);
        });

        return true;
    }

}
