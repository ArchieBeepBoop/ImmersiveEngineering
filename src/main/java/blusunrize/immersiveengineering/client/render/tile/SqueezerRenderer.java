/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.client.render.tile;

import blusunrize.immersiveengineering.common.blocks.IEBlocks.Multiblocks;
import blusunrize.immersiveengineering.common.blocks.metal.SqueezerTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.data.EmptyModelData;

public class SqueezerRenderer extends TileEntityRenderer<SqueezerTileEntity>
{
	public static DynamicModel<Direction> PISTON;

	public SqueezerRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn);
	}

	@Override
	public void render(SqueezerTileEntity te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
	{
		if(!te.formed||te.isDummy()||!te.getWorldNonnull().isBlockLoaded(te.getPos()))
			return;

		final BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
		BlockPos blockPos = te.getPos();
		BlockState state = te.getWorld().getBlockState(blockPos);
		if(state.getBlock()!=Multiblocks.squeezer.get())
			return;
		IBakedModel model = PISTON.get(te.getFacing());

		matrixStack.push();
		matrixStack.translate(.5, .5, .5);
		bufferIn = TileRenderUtils.mirror(te, matrixStack, bufferIn);
		IVertexBuilder buffer = bufferIn.getBuffer(RenderType.getSolid());

		float piston = te.animation_piston;
		//Smoothstep! TODO partial ticks?
		piston = piston*piston*(3.0f-2.0f*piston);

		matrixStack.translate(0, piston, 0);

		matrixStack.translate(-.5, -.5, -.5);
		blockRenderer.getBlockModelRenderer().renderModel(te.getWorldNonnull(), model, state, blockPos, matrixStack,
				buffer, true, te.getWorld().rand, 0, combinedOverlayIn,
				EmptyModelData.INSTANCE);

		matrixStack.pop();
	}
}