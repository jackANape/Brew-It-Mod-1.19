package net.jackanape.brewmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class ZirconLampBlock extends Block {

    public static final BooleanProperty LIT = BooleanProperty.create("lit");

    public ZirconLampBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            level.setBlock(pos, state.cycle(LIT), 3);
        }

        return super.use(state, level, pos, player, hand, blockHitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
