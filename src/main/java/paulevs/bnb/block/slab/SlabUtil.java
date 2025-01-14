package paulevs.bnb.block.slab;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import paulevs.bnb.BNB;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SlabUtil {
	private static final boolean HAS_VBE = FabricLoader.getInstance().isModLoaded("vbe");
	private static Block halfSlab;
	private static Block fullSlab;
	
	public static Block makeHalfSlab(String name, Block source) {
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
	@SuppressWarnings({"JavaReflectionInvocation", "JavaReflectionMemberAccess"})
	private static void makeVBESlabs(String name, Block source) {
		try {
			Class<?> slabClass = Class.forName("paulevs.vbe.block.VBEHalfSlabBlock");
			Constructor<?> constructor = slabClass.getConstructor(String.class, Block.class);
			halfSlab = (Block) constructor.newInstance(name, source);
			
			slabClass = Class.forName("paulevs.vbe.block.VBEFullSlabBlock");
			constructor = slabClass.getConstructor(String.class, Block.class);
			fullSlab = (Block) constructor.newInstance(name, source);
			
			halfSlab.getClass().getDeclaredMethod("setFullBlock").invoke(halfSlab, fullSlab);
			fullSlab.getClass().getDeclaredMethod("setFullBlock").invoke(fullSlab, halfSlab);
		}
		catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			//noinspection CallToPrintStackTrace
			e.printStackTrace();
			makeBNBSlabs(name, source);
		}
	}
}
