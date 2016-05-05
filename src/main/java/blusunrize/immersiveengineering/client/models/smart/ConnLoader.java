package blusunrize.immersiveengineering.client.models.smart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;

public class ConnLoader implements ICustomModelLoader
{
	public static final String RESOURCE_LOCATION = "models/block/smartmodel/";
	public static Map<String, ImmutableMap<String, String>> textureReplacements = new HashMap<>();
	public static Map<String, ResourceLocation> baseModels = new HashMap<>();
	static
	{
		baseModels.put("conn_lv", new ResourceLocation("immersiveengineering:block/connector/connectorLV.obj"));
		baseModels.put("rel_lv", new ResourceLocation("immersiveengineering:block/connector/connectorLV.obj"));
		textureReplacements.put("rel_lv", ImmutableMap.of("#immersiveengineering:blocks/connector_connectorLV",
				"immersiveengineering:blocks/connector_relayLV"));

		baseModels.put("conn_mv", new ResourceLocation("immersiveengineering:block/connector/connectorMV.obj"));
		baseModels.put("rel_mv", new ResourceLocation("immersiveengineering:block/connector/connectorMV.obj"));
		textureReplacements.put("rel_mv", ImmutableMap.of("#immersiveengineering:blocks/connector_connectorMV",
				"immersiveengineering:blocks/connector_relayMV"));

		baseModels.put("conn_hv", new ResourceLocation("immersiveengineering:block/connector/connectorHV.obj"));
		baseModels.put("rel_hv", new ResourceLocation("immersiveengineering:block/connector/relayHV.obj"));

		baseModels.put("conn_struct",
				new ResourceLocation("immersiveengineering:block/connector/connectorStructural.obj"));

		baseModels.put("breaker_off",
				new ResourceLocation("immersiveengineering:block/connector/breakerSwitch_off.obj"));
		baseModels.put("breaker_on", new ResourceLocation("immersiveengineering:block/connector/breakerSwitch_on.obj"));

		baseModels.put("eMeter", new ResourceLocation("immersiveengineering:block/connector/eMeter.obj"));
		baseModels.put("redstoneBreaker",
				new ResourceLocation("immersiveengineering:block/connector/redstoneBreaker.obj"));

		baseModels.put("transformer_hv_left",
				new ResourceLocation("immersiveengineering:block/connector/transformer_hv_left.obj"));
		baseModels.put("transformer_hv_right",
				new ResourceLocation("immersiveengineering:block/connector/transformer_hv_right.obj"));
		baseModels.put("transformer_mv_left",
				new ResourceLocation("immersiveengineering:block/connector/transformer_mv_left.obj"));
		baseModels.put("transformer_mv_right",
				new ResourceLocation("immersiveengineering:block/connector/transformer_mv_right.obj"));
		
		baseModels.put("eLantern",
				new ResourceLocation("immersiveengineering:block/metalDevice/eLantern.obj"));
		baseModels.put("eLantern_on",
				new ResourceLocation("immersiveengineering:block/metalDevice/eLantern.obj"));
		textureReplacements.put("eLantern_on",
				ImmutableMap.of("#immersiveengineering:blocks/metalDevice1_electricLantern",
						"immersiveengineering:blocks/metalDevice1_electricLantern_on"));
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		ConnModelReal.cache.clear();
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return modelLocation.getResourceDomain().equalsIgnoreCase(ImmersiveEngineering.MODID.toLowerCase())
				&& modelLocation.getResourcePath().contains(RESOURCE_LOCATION);
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws IOException
	{
		String resourcePath = modelLocation.getResourcePath();
		int pos = resourcePath.indexOf("conn_");
		if (pos >= 0)
		{
			pos += 5;// length of "conn_"
			String name = resourcePath.substring(pos);
			ResourceLocation r = baseModels.get(name);
			if (r != null)
			{
				if (textureReplacements.containsKey(name))
					return new ConnModelBase(r, textureReplacements.get(name));
				else
					return new ConnModelBase(r);
			}
		}
		return ModelLoaderRegistry.getMissingModel();
	}

	private class ConnModelBase implements IModel
	{
		ResourceLocation base;
		ImmutableMap<String, String> texReplace;

		public ConnModelBase(ResourceLocation b, ImmutableMap<String, String> t)
		{
			base = b;
			texReplace = t;
		}

		public ConnModelBase(ResourceLocation b)
		{
			this(b, ImmutableMap.of("", ""));
		}

		@Override
		public Collection<ResourceLocation> getDependencies()
		{
			return ImmutableList.of(base);
		}

		@Override
		public Collection<ResourceLocation> getTextures()
		{
			try
			{
				List<ResourceLocation> ret = new ArrayList<>(ModelLoaderRegistry.getModel(base).getTextures());
				for (String tex:texReplace.values())
					ret.add(new ResourceLocation(tex));
				ret.add(new ResourceLocation(ImmersiveEngineering.MODID.toLowerCase() + ":blocks/wire"));
				return ret;
			} catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}

		@Override
		public IFlexibleBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
		{
			try
			{
				IModel model = ((OBJModel) ModelLoaderRegistry.getModel(base)).retexture(texReplace);
				return new ConnModelReal(model.bake(state, Attributes.DEFAULT_BAKED_FORMAT, bakedTextureGetter));
			} catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}

		@Override
		public IModelState getDefaultState()
		{
			return null;
		}

	}
}
