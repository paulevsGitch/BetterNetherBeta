package paulevs.bnb.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Rail;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.QuadPoint;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TileRenderer;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.sortme.GameRenderer;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.BlockWithWorldRenderer;
import net.modificationstation.stationloader.api.client.model.CustomCuboidRenderer;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.api.client.model.CustomTexturedQuad;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.util.BlockFaces;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.util.BlockUtil;

@Mixin(value = TileRenderer.class, priority = 100)
public class TileRendererMixin {
	@Shadow
	private int field_55;
	@Shadow
	private boolean field_92;
	@Shadow
	private float field_93;
	@Shadow
	private TileView field_82;
	@Shadow
	private float field_94;
	@Shadow
	private float field_95;
	@Shadow
	private float field_96;
	@Shadow
	private float field_97;
	@Shadow
	private float field_98;
	@Shadow
	private float field_99;
	@Shadow
	private float field_100;
	@Shadow
	private float field_101;
	@Shadow
	private float field_102;
	@Shadow
	private float field_103;
	@Shadow
	private float field_104;
	@Shadow
	private float field_105;
	@Shadow
	private float field_41;
	@Shadow
	private float field_42;
	@Shadow
	private float field_56;
	@Shadow
	private float field_57;
	@Shadow
	private float field_58;
	@Shadow
	private float field_59;
	@Shadow
	private float field_60;
	@Shadow
	private float field_61;
	@Shadow
	private float field_62;
	@Shadow
	private float field_63;
	@Shadow
	private float field_64;
	@Shadow
	private float field_65;
	@Shadow
	private float field_66;
	@Shadow
	private float field_68;
	@Shadow
	private boolean field_69;
	@Shadow
	private boolean field_70;
	@Shadow
	private boolean field_71;
	@Shadow
	private boolean field_72;
	@Shadow
	private boolean field_73;
	@Shadow
	private boolean field_74;
	@Shadow
	private boolean field_75;
	@Shadow
	private boolean field_76;
	@Shadow
	private boolean field_77;
	@Shadow
	private boolean field_78;
	@Shadow
	private boolean field_79;
	@Shadow
	private boolean field_80;
	@Shadow
	private float field_43;
	@Shadow
	private float field_44;
	@Shadow
	private float field_45;
	@Shadow
	private float field_46;
	@Shadow
	private float field_47;
	@Shadow
	private float field_48;
	@Shadow
	private float field_49;
	@Shadow
	private float field_50;
	@Shadow
	private float field_51;
	@Shadow
	private float field_52;
	@Shadow
	private float field_53;
	@Shadow
	private float field_54;
	
	/**
	 * Main method to render block in the world
	 */
	@Inject(method = "method_57", at = @At("HEAD"), cancellable = true)
	private void renderBlock(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		if (block instanceof BlockWithLight) {
			boolean result1 = bnb_renderModel(block, x, y, z);
			BlockUtil.setLightPass(true);
			boolean result2 = bnb_renderModel(block, x, y, z);
			BlockUtil.setLightPass(false);
			if (result1 | result2) {
				info.setReturnValue(true);
				info.cancel();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private Minecraft bnb_getMinecraft() {
		return (Minecraft) FabricLoader.getInstance().getGameInstance();
	}
	
	/*private float bnb_getLight(BlockBase block, TileView world, int x, int y, int z) {
		return BlockUtil.isLightPass() ? 1F : block.method_1604(world, x, y, z);
	}*/
	
	private boolean bnb_vanillaBlockRender(BlockBase block, int x, int y, int z) {
		int var5 = block.method_1621();
		block.method_1616(this.field_82, x, y, z);
		if (var5 == 0) {
			return this.method_76(block, x, y, z);
		} else if (var5 == 4) {
			return this.method_75(block, x, y, z);
		} else if (var5 == 13) {
			return this.method_77(block, x, y, z);
		} else if (var5 == 1) {
			return this.method_73(block, x, y, z);
		} else if (var5 == 6) {
			return this.method_74(block, x, y, z);
		} else if (var5 == 2) {
			return this.method_62(block, x, y, z);
		} else if (var5 == 3) {
			return this.method_70(block, x, y, z);
		} else if (var5 == 5) {
			return this.method_71(block, x, y, z);
		} else if (var5 == 8) {
			return this.method_72(block, x, y, z);
		} else if (var5 == 7) {
			return this.method_80(block, x, y, z);
		} else if (var5 == 9) {
			return this.method_44((Rail) block, x, y, z);
		} else if (var5 == 10) {
			return this.method_79(block, x, y, z);
		} else if (var5 == 11) {
			return this.method_78(block, x, y, z);
		} else if (var5 == 12) {
			return this.method_68(block, x, y, z);
		} else if (var5 == 14) {
			return this.method_81(block, x, y, z);
		} else if (var5 == 15) {
			return this.method_82(block, x, y, z);
		} else if (var5 == 16) {
			return this.method_59(block, x, y, z, false);
		} else {
			return var5 == 17 ? this.method_64(block, x, y, z, true) : false;
		}
	}
	
	@Shadow
	private boolean method_44(Rail block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_59(BlockBase block, int x, int y, int z, boolean val) { return false; }
	
	@Shadow
	private boolean method_62(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_64(BlockBase block, int x, int y, int z, boolean val) { return false; }
	
	@Shadow
	private boolean method_68(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_70(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_71(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_72(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_73(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_74(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_75(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_76(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_77(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_78(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_79(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_80(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_81(BlockBase block, int x, int y, int z) { return false; }
	
	@Shadow
	private boolean method_82(BlockBase block, int x, int y, int z) { return false; }
	
	private boolean bnb_renderModel(BlockBase block, int x, int y, int z) {
		if (block instanceof BlockModelProvider) {
			Minecraft minecraft = bnb_getMinecraft();
			Level level = minecraft.level;
			CustomModel model = ((BlockModelProvider) block).getCustomWorldModel(level, x, y, z, level.getTileMeta(x, y, z));
			if (model != null) {
				Tessellator tessellator = Tessellator.INSTANCE;
				tessellator.draw();
				TextureRegistry lastRegistry = TextureRegistry.currentRegistry();
				int lastTex = lastRegistry.currentTexture();
				TextureRegistry.unbind();
				CustomCuboidRenderer[] cuboids = model.getCuboids();

				for (int indexCuboid = 0; indexCuboid < cuboids.length; ++indexCuboid) {
					CustomCuboidRenderer cuboid = cuboids[indexCuboid];
					CustomTexturedQuad[] quads = cuboid.getCubeQuads();

					for (int indexQuad = 0; indexQuad < quads.length; ++indexQuad) {
						CustomTexturedQuad texturedQuad = quads[indexQuad];
						if (texturedQuad.getTexture() != null) {
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/assets/" + cuboid.getModID() + "/" + StationLoader.INSTANCE.getData().getId() + "/models/textures/" + texturedQuad.getTexture() + ".png"));
						}

						tessellator.start();
						tessellator.colour(1.0F, 1.0F, 1.0F);
						QuadPoint[] points = texturedQuad.getQuadPoints();

						for (int indexPoint = 0; indexPoint < points.length; ++indexPoint) {
							QuadPoint quadPoint = points[indexPoint];
							int rgb = block.getColor(this.field_82, x, y, z);
							float colorR = (float) (rgb >> 16 & 255) / 255.0F;
							float colorG = (float) (rgb >> 8 & 255) / 255.0F;
							float colorB = (float) (rgb & 255) / 255.0F;
							if (GameRenderer.field_2340) {
								colorR = (colorR * 30.0F + colorG * 59.0F + colorB * 11.0F) / 100.0F;
								colorG = (colorR * 30.0F + colorG * 70.0F) / 100.0F;
								colorB = (colorR * 30.0F + colorB * 70.0F) / 100.0F;
							}

							this.field_92 = true;
							float var9 = this.field_93;
							float var10 = this.field_93;
							float var11 = this.field_93;
							float var12 = this.field_93;
							boolean var13 = true;
							boolean var14 = true;
							boolean var15 = true;
							boolean var16 = true;
							boolean var17 = true;
							boolean var18 = true;
							this.field_93 = block.method_1604(this.field_82, x, y, z);
							this.field_94 = block.method_1604(this.field_82, x - 1, y, z);
							this.field_95 = block.method_1604(this.field_82, x, y - 1, z);
							this.field_96 = block.method_1604(this.field_82, x, y, z - 1);
							this.field_97 = block.method_1604(this.field_82, x + 1, y, z);
							this.field_98 = block.method_1604(this.field_82, x, y + 1, z);
							this.field_99 = block.method_1604(this.field_82, x, y, z + 1);
							this.field_70 = BlockBase.field_1942[this.field_82.getTileId(x + 1, y + 1, z)];
							this.field_78 = BlockBase.field_1942[this.field_82.getTileId(x + 1, y - 1, z)];
							this.field_74 = BlockBase.field_1942[this.field_82.getTileId(x + 1, y, z + 1)];
							this.field_76 = BlockBase.field_1942[this.field_82.getTileId(x + 1, y, z - 1)];
							this.field_71 = BlockBase.field_1942[this.field_82.getTileId(x - 1, y + 1, z)];
							this.field_79 = BlockBase.field_1942[this.field_82.getTileId(x - 1, y - 1, z)];
							this.field_73 = BlockBase.field_1942[this.field_82.getTileId(x - 1, y, z - 1)];
							this.field_75 = BlockBase.field_1942[this.field_82.getTileId(x - 1, y, z + 1)];
							this.field_72 = BlockBase.field_1942[this.field_82.getTileId(x, y + 1, z + 1)];
							this.field_69 = BlockBase.field_1942[this.field_82.getTileId(x, y + 1, z - 1)];
							this.field_80 = BlockBase.field_1942[this.field_82.getTileId(x, y - 1, z + 1)];
							this.field_77 = BlockBase.field_1942[this.field_82.getTileId(x, y - 1, z - 1)];
							int xTemp;
							if (texturedQuad.getSide() == BlockFaces.DOWN) {
								if (this.field_55 <= 0) {
									var12 = this.field_95;
									var11 = this.field_95;
									var10 = this.field_95;
									var9 = this.field_95;
								}
								else {
									xTemp = y - 1;
									this.field_101 = block.method_1604(this.field_82, x - 1, xTemp, z);
									this.field_103 = block.method_1604(this.field_82, x, xTemp, z - 1);
									this.field_104 = block.method_1604(this.field_82, x, xTemp, z + 1);
									this.field_41 = block.method_1604(this.field_82, x + 1, xTemp, z);
									if (!this.field_77 && !this.field_79) {
										this.field_100 = this.field_101;
									}
									else {
										this.field_100 = block.method_1604(this.field_82, x - 1, xTemp, z - 1);
									}

									if (!this.field_80 && !this.field_79) {
										this.field_102 = this.field_101;
									}
									else {
										this.field_102 = block.method_1604(this.field_82, x - 1, xTemp, z + 1);
									}

									if (!this.field_77 && !this.field_78) {
										this.field_105 = this.field_41;
									}
									else {
										this.field_105 = block.method_1604(this.field_82, x + 1, xTemp, z - 1);
									}

									if (!this.field_80 && !this.field_78) {
										this.field_42 = this.field_41;
									}
									else {
										this.field_42 = block.method_1604(this.field_82, x + 1, xTemp, z + 1);
									}

									++xTemp;
									var9 = (this.field_102 + this.field_101 + this.field_104 + this.field_95) / 4.0F;
									var12 = (this.field_104 + this.field_95 + this.field_42 + this.field_41) / 4.0F;
									var11 = (this.field_95 + this.field_103 + this.field_41 + this.field_105) / 4.0F;
									var10 = (this.field_101 + this.field_100 + this.field_95 + this.field_103) / 4.0F;
								}

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var13 ? colorR : 1.0F)
										* 0.5F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var13 ? colorG : 1.0F)
										* 0.5F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var13 ? colorB : 1.0F)
										* 0.5F;
								this.field_56 *= var9;
								this.field_60 *= var9;
								this.field_64 *= var9;
								this.field_57 *= var10;
								this.field_61 *= var10;
								this.field_65 *= var10;
								this.field_58 *= var11;
								this.field_62 *= var11;
								this.field_66 *= var11;
								this.field_59 *= var12;
								this.field_63 *= var12;
								this.field_68 *= var12;
							}
							else if (texturedQuad.getSide() == BlockFaces.UP) {
								if (this.field_55 <= 0) {
									var12 = this.field_98;
									var11 = this.field_98;
									var10 = this.field_98;
									var9 = this.field_98;
								}
								else {
									xTemp = y + 1;
									this.field_44 = block.method_1604(this.field_82, x - 1, xTemp, z);
									this.field_48 = block.method_1604(this.field_82, x + 1, xTemp, z);
									this.field_46 = block.method_1604(this.field_82, x, xTemp, z - 1);
									this.field_49 = block.method_1604(this.field_82, x, xTemp, z + 1);
									if (!this.field_69 && !this.field_71) {
										this.field_43 = this.field_44;
									}
									else {
										this.field_43 = block.method_1604(this.field_82, x - 1, xTemp, z - 1);
									}

									if (!this.field_69 && !this.field_70) {
										this.field_47 = this.field_48;
									}
									else {
										this.field_47 = block.method_1604(this.field_82, x + 1, xTemp, z - 1);
									}

									if (!this.field_72 && !this.field_71) {
										this.field_45 = this.field_44;
									}
									else {
										this.field_45 = block.method_1604(this.field_82, x - 1, xTemp, z + 1);
									}

									if (!this.field_72 && !this.field_70) {
										this.field_50 = this.field_48;
									}
									else {
										this.field_50 = block.method_1604(this.field_82, x + 1, xTemp, z + 1);
									}

									--xTemp;
									var12 = (this.field_45 + this.field_44 + this.field_49 + this.field_98) / 4.0F;
									var9 = (this.field_49 + this.field_98 + this.field_50 + this.field_48) / 4.0F;
									var10 = (this.field_98 + this.field_46 + this.field_48 + this.field_47) / 4.0F;
									var11 = (this.field_44 + this.field_43 + this.field_98 + this.field_46) / 4.0F;
								}

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = var14 ? colorR : 1.0F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = var14 ? colorG : 1.0F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = var14 ? colorB : 1.0F;
								this.field_56 *= var9;
								this.field_60 *= var9;
								this.field_64 *= var9;
								this.field_57 *= var10;
								this.field_61 *= var10;
								this.field_65 *= var10;
								this.field_58 *= var11;
								this.field_62 *= var11;
								this.field_66 *= var11;
								this.field_59 *= var12;
								this.field_63 *= var12;
								this.field_68 *= var12;
							}
							else if (texturedQuad.getSide() == BlockFaces.WEST) {
								if (this.field_55 <= 0) {
									var12 = this.field_96;
									var11 = this.field_96;
									var10 = this.field_96;
									var9 = this.field_96;
								}
								else {
									xTemp = z - 1;
									this.field_51 = block.method_1604(this.field_82, x - 1, y, xTemp);
									this.field_103 = block.method_1604(this.field_82, x, y - 1, xTemp);
									this.field_46 = block.method_1604(this.field_82, x, y + 1, xTemp);
									this.field_52 = block.method_1604(this.field_82, x + 1, y, xTemp);
									if (!this.field_73 && !this.field_77) {
										this.field_100 = this.field_51;
									}
									else {
										this.field_100 = block.method_1604(this.field_82, x - 1, y - 1, xTemp);
									}

									if (!this.field_73 && !this.field_69) {
										this.field_43 = this.field_51;
									}
									else {
										this.field_43 = block.method_1604(this.field_82, x - 1, y + 1, xTemp);
									}

									if (!this.field_76 && !this.field_77) {
										this.field_105 = this.field_52;
									}
									else {
										this.field_105 = block.method_1604(this.field_82, x + 1, y - 1, xTemp);
									}

									if (!this.field_76 && !this.field_69) {
										this.field_47 = this.field_52;
									}
									else {
										this.field_47 = block.method_1604(this.field_82, x + 1, y + 1, xTemp);
									}

									++xTemp;
									var9 = (this.field_51 + this.field_43 + this.field_96 + this.field_46) / 4.0F;
									var10 = (this.field_96 + this.field_46 + this.field_52 + this.field_47) / 4.0F;
									var11 = (this.field_103 + this.field_96 + this.field_105 + this.field_52) / 4.0F;
									var12 = (this.field_100 + this.field_51 + this.field_103 + this.field_96) / 4.0F;
								}

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var15 ? colorR : 1.0F)
										* 0.8F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var15 ? colorG : 1.0F)
										* 0.8F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var15 ? colorB : 1.0F)
										* 0.8F;
								this.field_56 *= var9;
								this.field_60 *= var9;
								this.field_64 *= var9;
								this.field_57 *= var10;
								this.field_61 *= var10;
								this.field_65 *= var10;
								this.field_58 *= var11;
								this.field_62 *= var11;
								this.field_66 *= var11;
								this.field_59 *= var12;
								this.field_63 *= var12;
								this.field_68 *= var12;
							}
							else if (texturedQuad.getSide() == BlockFaces.EAST) {
								if (this.field_55 <= 0) {
									var12 = this.field_99;
									var11 = this.field_99;
									var10 = this.field_99;
									var9 = this.field_99;
								}
								else {
									++z;
									this.field_53 = block.method_1604(this.field_82, x - 1, y, z);
									this.field_54 = block.method_1604(this.field_82, x + 1, y, z);
									this.field_104 = block.method_1604(this.field_82, x, y - 1, z);
									this.field_49 = block.method_1604(this.field_82, x, y + 1, z);
									if (!this.field_75 && !this.field_80) {
										this.field_102 = this.field_53;
									}
									else {
										this.field_102 = block.method_1604(this.field_82, x - 1, y - 1, z);
									}

									if (!this.field_75 && !this.field_72) {
										this.field_45 = this.field_53;
									}
									else {
										this.field_45 = block.method_1604(this.field_82, x - 1, y + 1, z);
									}

									if (!this.field_74 && !this.field_80) {
										this.field_42 = this.field_54;
									}
									else {
										this.field_42 = block.method_1604(this.field_82, x + 1, y - 1, z);
									}

									if (!this.field_74 && !this.field_72) {
										this.field_50 = this.field_54;
									}
									else {
										this.field_50 = block.method_1604(this.field_82, x + 1, y + 1, z);
									}

									--z;
									var9 = (this.field_53 + this.field_45 + this.field_99 + this.field_49) / 4.0F;
									var12 = (this.field_99 + this.field_49 + this.field_54 + this.field_50) / 4.0F;
									var11 = (this.field_104 + this.field_99 + this.field_42 + this.field_54) / 4.0F;
									var10 = (this.field_102 + this.field_53 + this.field_104 + this.field_99) / 4.0F;
								}

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var16 ? colorR : 1.0F)
										* 0.8F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var16 ? colorG : 1.0F)
										* 0.8F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var16 ? colorB : 1.0F)
										* 0.8F;
								this.field_56 *= var9;
								this.field_60 *= var9;
								this.field_64 *= var9;
								this.field_57 *= var10;
								this.field_61 *= var10;
								this.field_65 *= var10;
								this.field_58 *= var11;
								this.field_62 *= var11;
								this.field_66 *= var11;
								this.field_59 *= var12;
								this.field_63 *= var12;
								this.field_68 *= var12;
							}
							else if (texturedQuad.getSide() == BlockFaces.SOUTH) {
								if (this.field_55 <= 0) {
									var12 = this.field_94;
									var11 = this.field_94;
									var10 = this.field_94;
									var9 = this.field_94;
								}
								else {
									xTemp = x - 1;
									this.field_101 = block.method_1604(this.field_82, xTemp, y - 1, z);
									this.field_51 = block.method_1604(this.field_82, xTemp, y, z - 1);
									this.field_53 = block.method_1604(this.field_82, xTemp, y, z + 1);
									this.field_44 = block.method_1604(this.field_82, xTemp, y + 1, z);
									if (!this.field_73 && !this.field_79) {
										this.field_100 = this.field_51;
									}
									else {
										this.field_100 = block.method_1604(this.field_82, xTemp, y - 1, z - 1);
									}

									if (!this.field_75 && !this.field_79) {
										this.field_102 = this.field_53;
									}
									else {
										this.field_102 = block.method_1604(this.field_82, xTemp, y - 1, z + 1);
									}

									if (!this.field_73 && !this.field_71) {
										this.field_43 = this.field_51;
									}
									else {
										this.field_43 = block.method_1604(this.field_82, xTemp, y + 1, z - 1);
									}

									if (!this.field_75 && !this.field_71) {
										this.field_45 = this.field_53;
									}
									else {
										this.field_45 = block.method_1604(this.field_82, xTemp, y + 1, z + 1);
									}

									++xTemp;
									var12 = (this.field_101 + this.field_102 + this.field_94 + this.field_53) / 4.0F;
									var9 = (this.field_94 + this.field_53 + this.field_44 + this.field_45) / 4.0F;
									var10 = (this.field_51 + this.field_94 + this.field_43 + this.field_44) / 4.0F;
									var11 = (this.field_100 + this.field_101 + this.field_51 + this.field_94) / 4.0F;
								}

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var17 ? colorR : 1.0F)
										* 0.6F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var17 ? colorG : 1.0F)
										* 0.6F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var17 ? colorB : 1.0F)
										* 0.6F;
								this.field_56 *= var9;
								this.field_60 *= var9;
								this.field_64 *= var9;
								this.field_57 *= var10;
								this.field_61 *= var10;
								this.field_65 *= var10;
								this.field_58 *= var11;
								this.field_62 *= var11;
								this.field_66 *= var11;
								this.field_59 *= var12;
								this.field_63 *= var12;
								this.field_68 *= var12;
							}
							else if (texturedQuad.getSide() == BlockFaces.NORTH) {
								if (this.field_55 <= 0) {
									var12 = this.field_97;
									var11 = this.field_97;
									var10 = this.field_97;
									var9 = this.field_97;
								}
								else {
									xTemp = x + 1;
									this.field_41 = block.method_1604(this.field_82, xTemp, y - 1, z);
									this.field_52 = block.method_1604(this.field_82, xTemp, y, z - 1);
									this.field_54 = block.method_1604(this.field_82, xTemp, y, z + 1);
									this.field_48 = block.method_1604(this.field_82, xTemp, y + 1, z);
									if (!this.field_78 && !this.field_76) {
										this.field_105 = this.field_52;
									}
									else {
										this.field_105 = block.method_1604(this.field_82, xTemp, y - 1, z - 1);
									}

									if (!this.field_78 && !this.field_74) {
										this.field_42 = this.field_54;
									}
									else {
										this.field_42 = block.method_1604(this.field_82, xTemp, y - 1, z + 1);
									}

									if (!this.field_70 && !this.field_76) {
										this.field_47 = this.field_52;
									}
									else {
										this.field_47 = block.method_1604(this.field_82, xTemp, y + 1, z - 1);
									}

									if (!this.field_70 && !this.field_74) {
										this.field_50 = this.field_54;
									}
									else {
										this.field_50 = block.method_1604(this.field_82, xTemp, y + 1, z + 1);
									}

									--xTemp;
									var9 = (this.field_41 + this.field_42 + this.field_97 + this.field_54) / 4.0F;
									var12 = (this.field_97 + this.field_54 + this.field_48 + this.field_50) / 4.0F;
									var11 = (this.field_52 + this.field_97 + this.field_47 + this.field_48) / 4.0F;
									var10 = (this.field_105 + this.field_41 + this.field_52 + this.field_97) / 4.0F;
								}

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = (var18 ? colorR : 1.0F) * 0.6F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = (var18 ? colorG : 1.0F) * 0.6F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = (var18 ? colorB : 1.0F) * 0.6F;
								this.field_56 *= var9;
								this.field_60 *= var9;
								this.field_64 *= var9;
								this.field_57 *= var10;
								this.field_61 *= var10;
								this.field_65 *= var10;
								this.field_58 *= var11;
								this.field_62 *= var11;
								this.field_66 *= var11;
								this.field_59 *= var12;
								this.field_63 *= var12;
								this.field_68 *= var12;
							}

							tessellator.colour(this.field_56, this.field_60, this.field_64);
							tessellator.vertex(
								(double) ((float) x + (float) quadPoint.pointVector.x * 0.0625F),
								(double) ((float) y + (float) quadPoint.pointVector.y * 0.0625F),
								(double) ((float) z + (float) quadPoint.pointVector.z * 0.0625F),
								(double) quadPoint.field_1147, (double) quadPoint.field_1148
							);
						}

						tessellator.draw();
					}
				}

				lastRegistry.bindAtlas(minecraft.textureManager, lastTex);
				tessellator.start();
				return true;
			}
		}
		else if (block instanceof BlockWithWorldRenderer) {
			((BlockWithWorldRenderer) block).renderWorld((TileRenderer) (Object) this, this.field_82, x, y, z, this.field_82.getTileMeta(x, y, z));
		}
		else {
			return bnb_vanillaBlockRender(block, x, y, z);
		}
		return false;
	}
}