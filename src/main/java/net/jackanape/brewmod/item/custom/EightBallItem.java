package net.jackanape.brewmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EightBallItem extends Item {

    public EightBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            //output random number
            //set cool down

            outputRandomNumber(player);
            player.getCooldowns().addCooldown(this, 20); //ticks cooldown lasts

        }

        return super.use(level, player, hand);
    }

    //this is the text shown on an item while the mouse is highlighted
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level,
                                List<Component> components, TooltipFlag flag) {

        if (Screen.hasShiftDown()) {
            components.add(Component.literal("Right click to get a random number").withStyle(ChatFormatting.AQUA));
        } else {
            components.add(Component.literal("Press SHIFT for more info").withStyle(ChatFormatting.YELLOW));
        }

        super.appendHoverText(itemStack, level, components, flag);
    }

    private void outputRandomNumber(Player player) {
        player.sendSystemMessage(Component.literal("Your Number is " + getRandomNumber()));
    }

    private int getRandomNumber() {
        return RandomSource.createNewThreadLocalInstance().nextInt(10);
    }


}
