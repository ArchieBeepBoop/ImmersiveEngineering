/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.blocks;

import blusunrize.immersiveengineering.common.IETileTypes;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISpawnInterdiction;
import blusunrize.immersiveengineering.common.blocks.metal.FloodlightTileEntity;
import blusunrize.immersiveengineering.common.temp.IETickableBlockEntity;
import blusunrize.immersiveengineering.common.util.SpawnInterdictionHandler;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class FakeLightBlock extends IETileProviderBlock
{
	public static final Supplier<Properties> PROPERTIES = () -> Properties.create(Material.AIR)
			.notSolid()
			.setLightLevel(b -> 15);

	public FakeLightBlock(Properties props)
	{
		super(props);
	}

	@Override
	public boolean isAir(BlockState state, IBlockReader world, BlockPos pos)
	{
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context)
	{
		return VoxelShapes.empty();
	}

	@Override
	public PushReaction getPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}

	@Override
	public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world)
	{
		return new FakeLightTileEntity();
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
	{
		return true;
	}

	public static class FakeLightTileEntity extends IEBaseTileEntity implements IETickableBlockEntity, ISpawnInterdiction
	{
		public BlockPos floodlightCoords = null;

		public FakeLightTileEntity()
		{
			super(IETileTypes.FAKE_LIGHT.get());
		}

		@Override
		public void tickServer()
		{
			if(floodlightCoords==null)
			{
				world.removeBlock(getPos(), false);
				return;
			}
			if(world.getGameTime()%256==((getPos().getX()^getPos().getZ())&255))
			{
				TileEntity tile = Utils.getExistingTileEntity(world, floodlightCoords);
				if(!(tile instanceof FloodlightTileEntity)||!((FloodlightTileEntity)tile).getIsActive())
					world.removeBlock(getPos(), false);
			}

		}

		@Override
		public double getInterdictionRangeSquared()
		{
			return 1024;
		}

		@Override
		public void remove()
		{
			SpawnInterdictionHandler.removeFromInterdictionTiles(this);
			super.remove();
		}

		@Override
		public void onChunkUnloaded()
		{
			SpawnInterdictionHandler.removeFromInterdictionTiles(this);
			super.onChunkUnloaded();
		}

		@Override
		public void onLoad()
		{
			super.onLoad();
			SpawnInterdictionHandler.addInterdictionTile(this);
		}

		@Override
		public void readCustomNBT(CompoundNBT nbt, boolean descPacket)
		{
			if(nbt.contains("floodlightCoords", NBT.TAG_COMPOUND))
				floodlightCoords = NBTUtil.readBlockPos(nbt.getCompound("floodlightCoords"));
			else
				floodlightCoords = null;
		}

		@Override
		public void writeCustomNBT(CompoundNBT nbt, boolean descPacket)
		{
			if(floodlightCoords!=null)
				nbt.put("floodlightCoords", NBTUtil.writeBlockPos(floodlightCoords));
		}
	}
}