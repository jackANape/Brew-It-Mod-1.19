package net.jackanape.brewmod.block.entity;

import net.jackanape.brewmod.item.ModItems;
import net.jackanape.brewmod.screen.MashTunMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ALL")
public class MashTunBlockEntity extends BlockEntity implements MenuProvider {

    public static int slotAmount = 5;

    private final ItemStackHandler itemHandler = new ItemStackHandler(slotAmount) { //change here for slot amount in gui
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200; //20 ticks per second

    public MashTunBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MASH_TUN.get(), pos, state);

        //try adding time data through this
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> MashTunBlockEntity.this.progress;
                    case 1 -> MashTunBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> MashTunBlockEntity.this.progress = value;
                    case 1 -> MashTunBlockEntity.this.maxProgress = value;
                }
            }



            @Override
            public int getCount() {
                return slotAmount;
            }
        };

    }


    @Override
    public Component getDisplayName() {
        return Component.literal("MashTun");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new MashTunMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("mash_tun.progress", this.progress);

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("mash_tun.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, MashTunBlockEntity pEntity) {
        if (level.isClientSide()) {
            return;
        }

        //20 ticks a second. maxprogress is the time length: 1200 is 1 minute
        if (hasRecipe(pEntity)) {
            pEntity.progress++;
            setChanged(level, pos, state);

            if (pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            }

        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }

    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(MashTunBlockEntity pEntity) {

        if (hasRecipe(pEntity)) {
            pEntity.itemHandler.extractItem(0, 64, false); //remove grain 1
//            pEntity.itemHandler.extractItem(1, 1, false);
//            pEntity.itemHandler.extractItem(2, 1, false);

            pEntity.itemHandler.extractItem(3, 1, false); //remove boiling water
            pEntity.itemHandler.setStackInSlot(3, new ItemStack(Items.BUCKET));

            pEntity.itemHandler.setStackInSlot(4, new ItemStack(ModItems.MASH_EXTRACT.get(),
                    pEntity.itemHandler.getStackInSlot(4).getCount() + 1));

            pEntity.resetProgress();
        }
    }

    //change items in slots from here
    private static boolean hasRecipe(MashTunBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        int grainStackToWork = 64;
        boolean milledGrain1Ready = entity.itemHandler.getStackInSlot(0).getItem() == ModItems.MILLED_BARLEY.get();
//        boolean milledGrain2Ready = entity.itemHandler.getStackInSlot(1).getItem() == ModItems.MILLED_BARLEY.get();
//        boolean milledGrain3Ready = entity.itemHandler.getStackInSlot(2).getItem() == ModItems.MILLED_BARLEY.get();
        //add the other 2 grain slots to be ready
        //stacks need to equal 64 to work

        boolean grainsReady = false;
        if(milledGrain1Ready && entity.itemHandler.getStackInSlot(0).getCount() == grainStackToWork)
        {
            grainsReady = true;
        }

        boolean boilingWaterReady = entity.itemHandler.getStackInSlot(3).getItem() == ModItems.BOILING_WATER_BUCKET.get();


        return grainsReady && boilingWaterReady;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(1).getItem() == stack.getItem() || inventory.getItem(1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }
}
