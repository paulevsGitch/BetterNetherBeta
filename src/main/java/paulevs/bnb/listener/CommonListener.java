package paulevs.bnb.listener;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.achievement.Achievement;
import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.BiomeSource;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.achievement.AchievementRegisterEvent;
import net.modificationstation.stationapi.api.event.block.BlockEvent.BeforePlacedByItem;
import net.modificationstation.stationapi.api.event.block.entity.BlockEntityRegisterEvent;
import net.modificationstation.stationapi.api.event.entity.EntityRegister;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.event.world.gen.WorldGenEvent.ChunkDecoration;
import net.modificationstation.stationapi.api.recipe.FuelRegistry;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.BNB;
import paulevs.bnb.achievement.BNBAchievementPage;
import paulevs.bnb.achievement.BNBAchievements;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.MossCoverBlock;
import paulevs.bnb.block.ShardsBlock;
import paulevs.bnb.block.entity.CocoonSpawnerBlockEntity;
import paulevs.bnb.block.entity.NetherrackFurnaceBlockEntity;
import paulevs.bnb.block.entity.SpinningWheelBlockEntity;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.entity.CrimsonSpiderEntity;
import paulevs.bnb.entity.ObsidianBoatEntity;
import paulevs.bnb.entity.PirozenSpiderEntity;
import paulevs.bnb.entity.PoisonSpiderEntity;
import paulevs.bnb.item.BNBItems;
import paulevs.bnb.packet.BNBWeatherPacket;
import paulevs.bnb.world.biome.BNBBiomes;

public class CommonListener {
	@EventListener
	public void onBlockRegister(BlockRegistryEvent event) {
		BNBBlocks.init();
		Block.PORTAL.setLightEmittance(1F);
	}
	
	@EventListener
	public void onItemRegister(ItemRegistryEvent event) {
		BNBItems.init();
	}
	
	@EventListener
	public void onBiomeRegister(BiomeRegisterEvent event) {
		BNBBiomes.init();
	}
	
	@EventListener
	public void onBlockEntityRegister(BlockEntityRegisterEvent event) {
		event.register(NetherrackFurnaceBlockEntity.class, "bnb_netherrack_furnace");
		event.register(CocoonSpawnerBlockEntity.class, "bnb_cocoon_spawner");
		event.register(SpinningWheelBlockEntity.class, "bnb_spinning_wheel");
	}
	
	@EventListener
	public void onEntityRegister(EntityRegister event) {
		event.register(CrimsonSpiderEntity.class, "bnb_falurian_spider");
		event.register(PirozenSpiderEntity.class, "bnb_pirozen_spider");
		event.register(PoisonSpiderEntity.class, "bnb_chlorophate_spider");
		event.register(ObsidianBoatEntity.class, "bnb_obsidian_boat");
	}
	
	@EventListener
	public void registerAchievements(AchievementRegisterEvent event) {
		BNBAchievementPage page = new BNBAchievementPage(BNB.id("achievements"));
		event.achievements.addAll(BNBAchievements.ACHIEVEMENTS);
		page.addAchievements(BNBAchievements.ACHIEVEMENTS.toArray(Achievement[]::new));
		BNBAchievements.ACHIEVEMENTS.forEach(Stat::register);
	}
	
	@EventListener
	public void onRecipesRegister(RecipeRegisterEvent event) {
		if (event.recipeId != RecipeRegisterEvent.Vanilla.SMELTING.type()) return;
		for (Block block : BNBBlocks.BLOCKS_WITH_ITEMS) {
			if (block.material == BNBBlockMaterials.NETHER_LOG) {
				FuelRegistry.addFuelItem(block.asItem(), block.isFullCube() ? 300 : 100);
			}
			if (block.material == BNBBlockMaterials.NETHER_PLANT || block.material == BNBBlockMaterials.NETHER_LEAVES) {
				FuelRegistry.addFuelItem(block.asItem(), block.isFullCube() ? 100 : 50);
			}
		}
	}
	
	@EventListener
	public void beforeItemPlace(BeforePlacedByItem event) {
		if (!(event.block instanceof ShardsBlock shards)) return;
		if (!shards.isSupport(event.world, event.x, event.y, event.z, event.side.getOpposite())) {
			event.placeFunction = () -> false;
		}
	}
	
	@EventListener
	public void registerPackets(PacketRegisterEvent event) {
		Registry.register(PacketTypeRegistry.INSTANCE, BNBWeatherPacket.ID, BNBWeatherPacket.TYPE);
	}
	
	@EventListener
	public void registerDecoration(ChunkDecoration decoration) {
		if (decoration.world.dimension.id != -1) return;
		int minX = decoration.x | 8;
		int minZ = decoration.z | 8;
		int maxX = minX + 16;
		int maxZ = minZ + 16;
		
		final BlockState netherrack = Block.NETHERRACK.getDefaultState();
		final BlockState mossyNetherrack = BNBBlocks.MOSSY_NETHERRACK.getDefaultState();
		
		for (int x = minX; x < maxX; x++) {
			for (int z = minZ; z < maxZ; z++) {
				Chunk chunk = decoration.world.getChunkFromCache(x >> 4, z >> 4);
				int cx = x & 15;
				int cz = z & 15;
				for (int y = 94; y < 256; y++) {
					BlockState state = chunk.getBlockState(cx, y, cz);
					if (state.isOf(BNBBlocks.NETHERRACK_MYCORRUM)) {
						fillCube(decoration.world, x, y, z, netherrack, mossyNetherrack, BNBBlocks.NETHER_MOSS_COVER);
					}
				}
			}
		}
	}
	
	private static void fillCube(Level level, int x, int y, int z, BlockState filter, BlockState fill, MossCoverBlock moss) {
		boolean skipMoss = level.random.nextInt(32) > 0;
		
		if (!skipMoss) {
			BiomeSource source = level.getBiomeSource();
			Biome center = source.getBiome(x, z);
			boolean isSame = true;
			for (byte i = 0; i < 4; i++) {
				Direction dir = Direction.fromHorizontal(i);
				Biome side = source.getBiome(x + (dir.getOffsetX() << 2), z + (dir.getOffsetZ() << 2));
				if (side != center) {
					isSame = false;
					break;
				}
			}
			skipMoss = isSame;
		}
		
		for (byte dx = -2; dx <= 2; dx++) {
			int wx = x + dx;
			byte cx = (byte) (wx & 15);
			for (byte dz = -2; dz <= 2; dz++) {
				int wz = z + dz;
				byte cz = (byte) (wz & 15);
				Chunk chunk2 = level.getChunkFromCache(wx >> 4, wz >> 4);
				for (int dy = -1; dy <= 1; dy++) {
					int cy = y + dy;
					BlockState above = chunk2.getBlockState(cx, cy + 1, cz);
					if (!above.isAir() && above.isOpaque()) continue;
					
					if (chunk2.getBlockState(cx, cy, cz) == filter) {
						chunk2.setBlockState(cx, cy, cz, fill);
					}
					
					if (skipMoss) continue;
					
					for (byte i = 0; i < 6; i++) {
						if (level.random.nextInt(3) > 0) continue;
						Direction dir = Direction.byId(i);
						int px = wx + dir.getOffsetX();
						int py = cy + dir.getOffsetY();
						int pz = wz + dir.getOffsetZ();
						if (!level.getBlockState(px, py, pz).isAir()) continue;
						BlockState state = moss.getStructureState(level, px, py, pz);
						if (state != null) {
							level.setBlockState(px, py, pz, state);
						}
					}
				}
			}
		}
	}
	
	/*@EventListener
	public void registerGen(ChunkDecoration decoration) {
		if (decoration.world.dimension.id != -1) return;
		int px = decoration.x;
		int pz = decoration.z;
		Biome[] biomes = decoration.world.getBiomeSource().getBiomes(px, pz, 16, 16);
		int index = 0;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				Biome biome = biomes[index++];
				if (i == 0 || j == 0 || i == 15 || j == 15) continue;
				int color = biome.name.hashCode() & 15;
				decoration.world.setBlockStateWithMetadataWithNotify(
					px + i, 100, pz + j, Block.WOOL.getDefaultState(), color
				);
			}
		}
	}*/
	
	/*@SuppressWarnings("unchecked")
	@EventListener(priority = ListenerPriority.LOWEST)
	public void afterRecipeRegister(AfterBlockAndItemRegisterEvent event) {
		final int planksID = BaseBlock.LOG.asItem().id;
		Field[] fields = ShapedRecipe.class.getDeclaredFields();
		List<Recipe> recipes = new ArrayList<>();
		
		fields[0].setAccessible(true);
		fields[1].setAccessible(true);
		
		RecipeRegistry.getInstance().getRecipes().forEach(obj -> {
			if (obj instanceof ShapedRecipe recipe) {
				ItemStack[] ingredients = ((StationRecipe) recipe).getIngredients();
				boolean add = false;
				
				for (ItemStack stack : ingredients) {
					if (stack != null && stack.itemId == planksID) {
						add = true;
						break;
					}
				}
				
				if (!add) return;
				
				int width = 0;
				int height = 0;
				
				try {
					width = (int) fields[0].get(recipe);
					height = (int) fields[1].get(recipe);
				}
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				ItemStack[] newIngredients = new ItemStack[ingredients.length];
				
				for (int i = 0; i < newIngredients.length; i++) {
					ItemStack stack = ingredients[i];
					if (stack == null) continue;
					if (stack.itemId == planksID) {
						stack = new ItemStack(BNBBlocks.FALURIAN_PLANKS);
					}
					else stack = stack.copy();
					newIngredients[i] = stack;
				}
				
				System.out.println("Recipe " + width + " " + height + " " + newIngredients.length + " " + recipe.getOutput() + " " + Arrays.toString(newIngredients));
				recipes.add(new ShapedRecipe(width, height, newIngredients, recipe.getOutput().copy()));
			}
		});
		
		//RecipeRegistry.getInstance().getRecipes().addAll(recipes);
		//RecipeRegistry.getInstance().re
	}*/
}
