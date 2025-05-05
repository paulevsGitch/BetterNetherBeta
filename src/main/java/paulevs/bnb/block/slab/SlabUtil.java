package paulevs.bnb.block.slab;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SlabUtil {
	private static final boolean HAS_VBE = FabricLoader.getInstance().isModLoaded("vbe");
	private static Block halfSlab;
	private static Block fullSlab;
	
	public static Block makeHalfSlab(String name, Block source) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			updateTexture(source);
		}
		if (HAS_VBE) makeVBESlabs(name, source);
		else makeBNBSlabs(name, source);
		return halfSlab;
	}
	
	public static Block getFullSlab() {
		return fullSlab;
	}
	
	private static void makeBNBSlabs(String name, Block source) {
		halfSlab = new BNBHalfSlab(BNB.id(name + "_slab_half"), source);
		fullSlab = new BNBFullSlab(BNB.id(name + "_slab_full"), source);
		((BNBHalfSlab) halfSlab).setFullBlock(fullSlab);
		((BNBFullSlab) fullSlab).setHalfBlock(halfSlab);
	}
	
	// Reason for reflection - prevent static init crash
	private static void makeVBESlabs(String name, Block source) {
		try {
			Class<?> slabClass = Class.forName("paulevs.vbe.block.VBEHalfSlabBlock");
			Constructor<?> constructor = slabClass.getConstructor(Identifier.class, Block.class);
			halfSlab = (Block) constructor.newInstance(BNB.id(name + "_slab_half"), source);
			
			slabClass = Class.forName("paulevs.vbe.block.VBEFullSlabBlock");
			constructor = slabClass.getConstructor(Identifier.class, Block.class);
			fullSlab = (Block) constructor.newInstance(BNB.id(name + "_slab_full"), source);
			fullSlab.disableAutoItemRegistration();
			
			halfSlab.getClass().getDeclaredMethod("setFullBlock", Block.class).invoke(halfSlab, fullSlab);
			fullSlab.getClass().getDeclaredMethod("setHalfBlock", Block.class).invoke(fullSlab, halfSlab);
		}
		catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			//noinspection CallToPrintStackTrace
			e.printStackTrace();
			makeBNBSlabs(name, source);
		}
	}
	
	@Environment(EnvType.CLIENT)
	private static void updateTexture(Block block) {
		BNBBlocks.UPDATE_TEXTURE_INTERFACE.add((atlas) -> {
			Identifier id = BlockRegistry.INSTANCE.getId(block);
			assert id != null;
			block.texture = atlas.addTexture(BNB.id("block/" + id.path)).index;
		});
	}
}
