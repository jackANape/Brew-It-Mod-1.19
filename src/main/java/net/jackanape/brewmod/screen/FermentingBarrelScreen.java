package net.jackanape.brewmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.jackanape.brewmod.BrewMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FermentingBarrelScreen extends AbstractContainerScreen<FermentingBarrelMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(BrewMod.MOD_ID, "textures/gui/fermenting_barrel_gui.png");

    public FermentingBarrelScreen(FermentingBarrelMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = this.getGuiLeft();
        int y = this.getGuiTop();

        this.blit(pPoseStack, x, y, 0, 0, this.getXSize(), this.getYSize());

    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
        this.font.draw(poseStack, "Time Left: " + menu.getTimerMinutes()
                + menu.getTimerSeconds(), (float) this.titleLabelX + 85, (float) this.titleLabelY + 4, 0x000000);
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);

    }
}
