/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.client.gui;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.utils.GuiHelper;
import blusunrize.immersiveengineering.client.utils.IERenderTypes;
import blusunrize.immersiveengineering.common.gui.RevolverContainer;
import blusunrize.immersiveengineering.common.items.IEItemInterfaces.IBulletContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class RevolverScreen extends IEContainerScreen<RevolverContainer>
{
	private static final ResourceLocation TEXTURE = makeTextureLocation("revolver");

	private final int[] bullets = new int[2];
	private final boolean otherRevolver;
	private final int offset;

	public RevolverScreen(RevolverContainer container, Inventory inventoryPlayer, Component title)
	{
		super(container, inventoryPlayer, title);
		ItemStack revolver = inventoryPlayer.player.getItemBySlot(container.entityEquipmentSlot);
		if(!revolver.isEmpty()&&revolver.getItem() instanceof IBulletContainer)
			this.bullets[0] = ((IBulletContainer)revolver.getItem()).getBulletCount(revolver);
		this.otherRevolver = !this.menu.secondRevolver.isEmpty();
		if(this.otherRevolver)
		{
			this.bullets[1] = ((IBulletContainer)this.menu.secondRevolver.getItem()).getBulletCount(this.menu.secondRevolver);
			this.offset = ((bullets[0] >= 18?150: bullets[0] > 8?136: 74)+(bullets[1] >= 18?150: bullets[1] > 8?136: 74)+4-176)/2;
			if(this.offset > 0)
				this.imageWidth += this.offset*2;
		}
		else
			this.offset = ((bullets[0] >= 18?150: bullets[0] > 8?136: 74)-176)/2;
	}

	@Override
	protected void renderBg(PoseStack transform, float par1, int par2, int par3)
	{
		ClientUtils.bindTexture(TEXTURE);
		this.blit(transform, leftPos+(offset > 0?offset: 0), topPos+77, 0, 125, 176, 89);

		int off = (offset < 0?-offset: 0);
		for(int hand = 0; hand < (otherRevolver?2: 1); hand++)
		{
			int side = !otherRevolver?0: (hand==0)==(ImmersiveEngineering.proxy.getClientPlayer().getMainArm()==HumanoidArm.RIGHT)?1: 0;
			this.blit(transform, leftPos+off+00, topPos+1, 00, 51, 74, 74);
			if(bullets[side] >= 18)
				this.blit(transform, leftPos+off+47, topPos+1, 74, 51, 103, 74);
			else if(bullets[side] > 8)
				this.blit(transform, leftPos+off+57, topPos+1, 57, 12, 79, 39);
			off += (bullets[side] >= 18?150: bullets[side] > 8?136: 74)+4;
		}
	}

	public static void drawExternalGUI(NonNullList<ItemStack> bullets, int bulletAmount, PoseStack transform)
	{
		MultiBufferSource.BufferSource buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		VertexConsumer builder = buffer.getBuffer(IERenderTypes.getGui(
				new ResourceLocation(ImmersiveEngineering.MODID, "textures/gui/revolver.png")));

		GuiHelper.drawTexturedColoredRect(builder, transform, 0, 1, 74, 74, 1, 1, 1, 1, 0/256f, 74/256f, 51/256f, 125/256f);
		if(bulletAmount >= 18)
			GuiHelper.drawTexturedColoredRect(builder, transform, 47, 1, 103, 74, 1, 1, 1, 1, 74/256f, 177/256f, 51/256f, 125/256f);
		else if(bulletAmount > 8)
			GuiHelper.drawTexturedColoredRect(builder, transform, 57, 1, 79, 39, 1, 1, 1, 1, 57/256f, 136/256f, 12/256f, 51/256f);
		buffer.endBatch();

		ItemRenderer ir = ClientUtils.mc().getItemRenderer();
		int[][] slots = RevolverContainer.slotPositions[bulletAmount >= 18?2: bulletAmount > 8?1: 0];
		transform.pushPose();
		transform.translate(0, 0, 10);
		RenderSystem.pushMatrix();
		RenderSystem.multMatrix(transform.last().pose());
		for(int i = 0; i < bulletAmount; i++)
		{
			ItemStack b = bullets.get(i);
			if(!b.isEmpty())
			{
				int x;
				int y;
				if(i==0)
				{
					x = 29;
					y = 3;
				}
				else if(i-1 < slots.length)
				{
					x = slots[i-1][0];
					y = slots[i-1][1];
				}
				else
				{
					int ii = i-(slots.length+1);
					x = ii==0?48: ii==1?29: ii==3?2: 10;
					y = ii==1?57: ii==3?30: ii==4?11: 49;
				}
				ir.renderAndDecorateItem(b, x, y);
			}
		}
		RenderSystem.popMatrix();
		transform.popPose();
	}
}