/*
 * BluSunrize
 * Copyright (c) 2020
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 *
 */

package blusunrize.immersiveengineering.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.IETileTypes;
import blusunrize.immersiveengineering.common.blocks.generic.MiscConnectableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TransformerHVBlock extends MiscConnectableBlock<TransformerHVTileEntity>
{
	public TransformerHVBlock()
	{
		super("transformer_hv", IETileTypes.TRANSFORMER_HV);
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(IEProperties.FACING_HORIZONTAL, IEProperties.MULTIBLOCKSLAVE, IEProperties.MIRRORED, BlockStateProperties.WATERLOGGED);
	}

	@Override
	public boolean canIEBlockBePlaced(BlockState newState, BlockItemUseContext context)
	{
		return areAllReplaceable(
				context.getPos(),
				context.getPos().up(2),
				context
		);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world)
	{
		TransformerHVTileEntity te = new TransformerHVTileEntity();
		te.dummy = state.get(IEProperties.MULTIBLOCKSLAVE)?1: 0;
		return te;
	}
}
