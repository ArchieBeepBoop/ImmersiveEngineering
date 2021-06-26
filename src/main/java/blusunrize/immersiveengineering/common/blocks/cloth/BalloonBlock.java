/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.blocks.cloth;

import blusunrize.immersiveengineering.common.blocks.IETileProviderBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BalloonBlock extends IETileProviderBlock
{
	public static final Supplier<Properties> PROPERTIES = () -> Properties.create(Material.WOOL)
			.sound(SoundType.CLOTH)
			.hardnessAndResistance(0.8F)
			.setLightLevel(s -> 13)
			.notSolid();

	public BalloonBlock(Properties props)
	{
		super(props);
		setHasColours();
		setLightOpacity(0);
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder)
	{
		super.fillStateContainer(builder);
		builder.add(BlockStateProperties.WATERLOGGED);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world)
	{
		return new BalloonTileEntity();
	}

	@Override
	public void onFallenUpon(World w, BlockPos pos, Entity entity, float fallStrength)
	{
		entity.fallDistance = 0;
	}
}
