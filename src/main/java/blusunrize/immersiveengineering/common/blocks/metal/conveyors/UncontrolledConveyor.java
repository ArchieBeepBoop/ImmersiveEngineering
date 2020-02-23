/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package blusunrize.immersiveengineering.common.blocks.metal.conveyors;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import static blusunrize.immersiveengineering.ImmersiveEngineering.MODID;

/**
 * @author BluSunrize - 06.05.2017
 */
public class UncontrolledConveyor extends BasicConveyor
{
	public static final ResourceLocation NAME = new ResourceLocation(MODID, "uncontrolled");

	public UncontrolledConveyor(TileEntity tile)
	{
		super(tile);
	}

	@Override
	public boolean isActive()
	{
		return true;
	}
}
