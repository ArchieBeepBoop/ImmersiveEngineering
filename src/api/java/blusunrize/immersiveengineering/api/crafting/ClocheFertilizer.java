/*
 * BluSunrize
 * Copyright (c) 2020
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 *
 */

package blusunrize.immersiveengineering.api.crafting;

import blusunrize.immersiveengineering.api.Lib;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collections;
import java.util.Map;

public class ClocheFertilizer extends IESerializableRecipe
{
	public static IRecipeType<ClocheFertilizer> TYPE = IRecipeType.register(Lib.MODID+":fertilizer");
	public static RegistryObject<IERecipeSerializer<ClocheFertilizer>> SERIALIZER;

	// Initialized by reload listener
	public static Map<ResourceLocation, ClocheFertilizer> fertilizerList = Collections.emptyMap();

	public final Ingredient input;
	public final float growthModifier;

	public ClocheFertilizer(ResourceLocation id, Ingredient input, float growthModifier)
	{
		super(ItemStack.EMPTY, TYPE, id);
		this.input = input;
		this.growthModifier = growthModifier;
	}

	public float getGrowthModifier()
	{
		return growthModifier;
	}

	@Override
	protected IERecipeSerializer<ClocheFertilizer> getIESerializer()
	{
		return SERIALIZER.get();
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	public static float getFertilizerGrowthModifier(ItemStack stack)
	{
		for(ClocheFertilizer e : ClocheFertilizer.fertilizerList.values())
			if(e.input.test(stack))
				return e.getGrowthModifier();
		return 0;
	}

	public static boolean isValidFertilizer(ItemStack stack)
	{
		return getFertilizerGrowthModifier(stack) > 0;
	}
}
