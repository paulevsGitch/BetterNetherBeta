package paulevs.bnb.mixin.client;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Portal;
import net.minecraft.block.Rail;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.QuadPoint;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TileRenderer;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.BlockWithWorldRenderer;
import net.modificationstation.stationloader.api.client.model.CustomCuboidRenderer;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.api.client.model.CustomTexturedQuad;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.util.BlockFaces;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.block.NetherBlock;
import paulevs.bnb.block.model.OBJBlockModel;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.interfaces.RenderTypePerMeta;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.ClientUtil;

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
	@Shadow
	private boolean field_85;
	
	/**
	 * Destruction stage, if = -1 then normal block render, otherwise used as index
	 */
	@Shadow
	private int field_83;
	
	/**
	 * Main method to render block in the world
	 */
	@Inject(method = "method_57", at = @At("HEAD"), cancellable = true)
	private void renderBlock(BlockBase block, int x, int y, int z, CallbackInfoReturnable<Boolean> info) {
		if (block instanceof Portal) {
			BlockUtil.setLightPass(true);
			bnb_vanillaBlockRenderTyped(block, x, y, z, block.method_1621());
			BlockUtil.setLightPass(false);
			info.setReturnValue(true);
		}
		else if (block instanceof BlockWithLight) {
			BlockUtil.setBreakStage(field_83);
			if (field_83 > -1) {
				boolean result = bnb_renderModel(block, x, y, z);
				info.setReturnValue(result);
			}
			else {
				boolean result1 = bnb_renderModel(block, x, y, z);
				BlockUtil.setLightPass(true);
				boolean result2 = bnb_renderModel(block, x, y, z);
				BlockUtil.setLightPass(false);
				info.setReturnValue(result1 | result2);
			}
		}
		else if (block instanceof BlockModelProvider) {
			Minecraft minecraft = ClientUtil.getMinecraft();
			Level level = minecraft.level;
			CustomModel model = ((BlockModelProvider) block).getCustomWorldModel(level, x, y, z, level.getTileMeta(x, y, z));
			if (model instanceof OBJBlockModel) {
				BlockUtil.setBreakStage(field_83);
				boolean result = bnb_renderModel(block, x, y, z);
				info.setReturnValue(result);
			}
		}
		else if (block instanceof RenderTypePerMeta) {
			Minecraft minecraft = ClientUtil.getMinecraft();
			Level level = minecraft.level;
			int renderType = ((RenderTypePerMeta) block).getTypeByMeta(level.getTileMeta(x, y, z));
			boolean result = bnb_vanillaBlockRenderTyped(block, x, y, z, renderType);
			info.setReturnValue(result);
		}
	}
	
	private boolean bnb_vanillaBlockRender(BlockBase block, int x, int y, int z) {
		int renderType = block.method_1621();
		return bnb_vanillaBlockRenderTyped(block, x, y, z, renderType);
	}
	
	private boolean bnb_vanillaBlockRenderTyped(BlockBase block, int x, int y, int z, int renderType) {
		block.method_1616(this.field_82, x, y, z);
		if (renderType == 0) {
			return this.method_76(block, x, y, z);
		} else if (renderType == 4) {
			return this.method_75(block, x, y, z);
		} else if (renderType == 13) {
			return this.method_77(block, x, y, z);
		} else if (renderType == 1) {
			return this.method_73(block, x, y, z);
		} else if (renderType == 6) {
			return this.method_74(block, x, y, z);
		} else if (renderType == 2) {
			return this.method_62(block, x, y, z);
		} else if (renderType == 3) {
			return this.method_70(block, x, y, z);
		} else if (renderType == 5) {
			return this.method_71(block, x, y, z);
		} else if (renderType == 8) {
			return this.method_72(block, x, y, z);
		} else if (renderType == 7) {
			return this.method_80(block, x, y, z);
		} else if (renderType == 9) {
			return this.method_44((Rail) block, x, y, z);
		} else if (renderType == 10) {
			return this.method_79(block, x, y, z);
		} else if (renderType == 11) {
			return this.method_78(block, x, y, z);
		} else if (renderType == 12) {
			return this.method_68(block, x, y, z);
		} else if (renderType == 14) {
			return this.method_81(block, x, y, z);
		} else if (renderType == 15) {
			return this.method_82(block, x, y, z);
		} else if (renderType == 16) {
			return this.method_59(block, x, y, z, false);
		} else {
			return renderType == 17 && this.method_64(block, x, y, z, true);
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
		if (!ClientUtil.isFancyGraphics()) {
			return bnb_vanillaBlockRender(block, x, y, z);
		}
		else if (block instanceof BlockModelProvider) {
			Minecraft minecraft = ClientUtil.getMinecraft();
			Level level = minecraft.level;
			CustomModel model = ((BlockModelProvider) block).getCustomWorldModel(level, x, y, z, level.getTileMeta(x, y, z));
			if (model != null) {
				boolean isOBJ = model instanceof OBJBlockModel;
				Tessellator tessellator = Tessellator.INSTANCE;
				TextureRegistry lastRegistry = TextureRegistry.currentRegistry();
				Integer lastTex = lastRegistry == null ? 0 : lastRegistry.currentTexture();
				CustomCuboidRenderer[] cuboids = model.getCuboids();
				
				if (!isOBJ) {
					tessellator.draw();
					TextureRegistry.unbind();
				}

				for (int indexCuboid = 0; indexCuboid < cuboids.length; ++indexCuboid) {
					CustomCuboidRenderer cuboid = cuboids[indexCuboid];
					CustomTexturedQuad[] quads = cuboid.getCubeQuads();

					for (int indexQuad = 0; indexQuad < quads.length; ++indexQuad) {
						CustomTexturedQuad texturedQuad = quads[indexQuad];
						if (texturedQuad.getTexture() != null) {
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.textureManager.getTextureId("/assets/" + cuboid.getModID() + "/" + StationLoader.INSTANCE.getData().getId() + "/models/textures/" + texturedQuad.getTexture() + ".png"));
						}
						
						if (isOBJ) {
							int atlasID = ((OBJBlockModel) model).getAtlasID(indexQuad);
							if (atlasID != lastTex) {
								tessellator.draw();
								lastTex = atlasID;
								lastRegistry.bindAtlas(minecraft.textureManager, lastTex);
								tessellator.start();
							}
						}
						else {
							tessellator.start();
						}
						
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
							float var9, var10, var11, var12;
							
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

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = colorR;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = colorG;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = colorB;
								
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

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = colorR;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = colorG;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = colorB;
								
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

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = colorR * 0.8F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = colorG * 0.8F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = colorB * 0.8F;
								
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

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = colorR * 0.8F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = colorG * 0.8F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = colorB * 0.8F;
								
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

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = colorR * 0.6F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = colorG * 0.6F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = colorB * 0.6F;
								
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

								this.field_56 = this.field_57 = this.field_58 = this.field_59 = colorR * 0.6F;
								this.field_60 = this.field_61 = this.field_62 = this.field_63 = colorG * 0.6F;
								this.field_64 = this.field_65 = this.field_66 = this.field_68 = colorB * 0.6F;
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
								(float) x + (float) quadPoint.pointVector.x * 0.0625F,
								(float) y + (float) quadPoint.pointVector.y * 0.0625F,
								(float) z + (float) quadPoint.pointVector.z * 0.0625F,
								quadPoint.field_1147, quadPoint.field_1148
							);
						}

						if (!isOBJ) {
							tessellator.draw();
						}
					}
				}

				if (!isOBJ) {
					lastRegistry.bindAtlas(minecraft.textureManager, lastTex);
					tessellator.start();
				}
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
	
	@ModifyVariable(method = {"method_56(Lnet/minecraft/block/BlockBase;IDDD)V" }, index = 10, at = @At(value = "CONSTANT", args = {"intValue=15" }, ordinal = 0, shift = Shift.BEFORE, by = 2))
	private int bnb_getCropsTextureID(int texID, BlockBase block, int meta, double x, double y, double z) {
		return block.method_1626(field_82, MathHelper.floor(x), MathHelper.floor(y + 0.0625), MathHelper.floor(z), 0);
	}
	
	@ModifyVariable(method = {"method_47(Lnet/minecraft/block/BlockBase;IDDD)V" }, index = 10, at = @At(value = "CONSTANT", args = {"intValue=15" }, ordinal = 0, shift = Shift.BEFORE, by = 2))
	private int bnb_getCrossTextureID(int texID, BlockBase block, int meta, double x, double y, double z) {
		return block instanceof NetherBlock ? block.method_1626(field_82, MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z), 0) : texID;
	}
	
	@Inject(method = "method_42", at = @At("HEAD"), cancellable = true)
	private static void bnb_render3DItem(int i, CallbackInfoReturnable<Boolean> info) {
		if (i < 0) {
			info.setReturnValue(false);
			info.cancel();
		}
	}
	
	@Shadow
	private float method_43(int i, int j, int k, Material arg) { return 0; }
	
	@Shadow
	public void method_46(BlockBase arg, double d, double d1, double d2, int i) {}
	
	/*@Inject(method = "method_75", at = @At("HEAD"), cancellable = true)
	private void bnb_fixFluidTexture(BlockBase arg, int i, int j, int k, CallbackInfoReturnable<Boolean> info) {
		int texture = arg.getTextureForSide(0, 0);
		TextureRegistry lastRegistry = TextureRegistry.currentRegistry();
		Integer lastTex = lastRegistry == null ? 0 : lastRegistry.currentTexture();
		int atlasID = texture >> 8;
		if (atlasID != lastTex) {
			Tessellator.INSTANCE.draw();
			lastRegistry.bindAtlas(ClientUtil.getMinecraft().textureManager, atlasID);
			Tessellator.INSTANCE.start();
			texture &= 255;
			
			Tessellator var5 = Tessellator.INSTANCE;
			int var6 = arg.getColor(this.field_82, i, j, k);
			float var7 = (float)(var6 >> 16 & 255) / 255.0F;
			float var8 = (float)(var6 >> 8 & 255) / 255.0F;
			float var9 = (float)(var6 & 255) / 255.0F;
			boolean var10 = arg.method_1618(this.field_82, i, j + 1, k, 1);
			boolean var11 = arg.method_1618(this.field_82, i, j - 1, k, 0);
			
			boolean[] var12 = new boolean[] {
				arg.method_1618(this.field_82, i, j, k - 1, 2),
				arg.method_1618(this.field_82, i, j, k + 1, 3),
				arg.method_1618(this.field_82, i - 1, j, k, 4),
				arg.method_1618(this.field_82, i + 1, j, k, 5)
			};
			
			if (!var10 && !var11 && !var12[0] && !var12[1] && !var12[2] && !var12[3]) {
				info.setReturnValue(false);
				return;
			}
			else {
				int var13 = 0;
				float var14 = 0.5F;
				float var15 = 1.0F;
				float var16 = 0.8F;
				float var17 = 0.6F;
				double var18 = 0.0D;
				double var20 = 1.0D;
				Material var22 = arg.material;
				int var23 = this.field_82.getTileMeta(i, j, k);
				float var24 = this.method_43(i, j, k, var22);
				float var25 = this.method_43(i, j, k + 1, var22);
				float var26 = this.method_43(i + 1, j, k + 1, var22);
				float var27 = this.method_43(i + 1, j, k, var22);
				if (this.field_85 || var10) {
					var13 = 1;
					float var29 = (float) Fluid.method_1223(this.field_82, i, j, k, var22);
					int var30 = (texture & 15) << 4;
					int var31 = texture & 240;
					double var32 = ((double)var30 + 8.0D) / 256.0D;
					double var34 = ((double)var31 + 8.0D) / 256.0D;
					if (var29 < -999.0F) {
						var29 = 0.0F;
					} else {
						var32 = (double)((float)(var30 + 16) / 256.0F);
						var34 = (double)((float)(var31 + 16) / 256.0F);
					}
					
					float var36 = MathHelper.sin(var29) * 8.0F / 256.0F;
					float var37 = MathHelper.cos(var29) * 8.0F / 256.0F;
					float var38 = arg.method_1604(this.field_82, i, j, k);
					var5.colour(var15 * var38 * var7, var15 * var38 * var8, var15 * var38 * var9);
					var5.vertex((double)(i + 0), (double)((float)j + var24), (double)(k + 0), var32 - (double)var37 - (double)var36, var34 - (double)var37 + (double)var36);
					var5.vertex((double)(i + 0), (double)((float)j + var25), (double)(k + 1), var32 - (double)var37 + (double)var36, var34 + (double)var37 + (double)var36);
					var5.vertex((double)(i + 1), (double)((float)j + var26), (double)(k + 1), var32 + (double)var37 + (double)var36, var34 + (double)var37 - (double)var36);
					var5.vertex((double)(i + 1), (double)((float)j + var27), (double)(k + 0), var32 + (double)var37 - (double)var36, var34 - (double)var37 - (double)var36);
				}
				
				if (this.field_85 || var11) {
					float var28 = arg.method_1604(this.field_82, i, j - 1, k);
					var5.colour(var14 * var28, var14 * var28, var14 * var28);
					this.method_46(arg, (double)i, (double)j, (double)k, arg.getTextureForSide(0));
					var13 = 1;
				}
				
				for(int var28 = 0; var28 < 4; ++var28) {
					int var29 = i;
					int var31 = k;
					if (var28 == 0) {
						var31 = k - 1;
					}
					
					if (var28 == 1) {
						++var31;
					}
					
					if (var28 == 2) {
						var29 = i - 1;
					}
					
					if (var28 == 3) {
						++var29;
					}
					
					int var32 = arg.getTextureForSide(var28 + 2, var23);
					int var33 = (var32 & 15) << 4;
					int var34 = var32 & 240;
					
					if (this.field_85 || var12[var28]) {
						float var35, var39, var40, var36, var37, var38;
						
						if (var28 == 0) {
							var35 = var24;
							var36 = var27;
							var37 = i;
							var39 = (i + 1);
							var38 = k;
							var40 = k;
						}
						else if (var28 == 1) {
							var35 = var26;
							var36 = var25;
							var37 = (i + 1);
							var39 = i;
							var38 = (k + 1);
							var40 = (k + 1);
						}
						else if (var28 == 2) {
							var35 = var25;
							var36 = var24;
							var37 = i;
							var39 = i;
							var38 = (k + 1);
							var40 = k;
						}
						else {
							var35 = var27;
							var36 = var26;
							var37 = (i + 1);
							var39 = (i + 1);
							var38 = k;
							var40 = (k + 1);
						}
						
						double var41 = ((float) (var33 + 0) / 256.0F);
						double var43 = ((double) (var33 + 16) - 0.01D) / 256.0D;
						double var45 = (((float) var34 + (1.0F - var35) * 16.0F) / 256.0F);
						double var47 = (((float) var34 + (1.0F - var36) * 16.0F) / 256.0F);
						double var49 = ((double) (var34 + 16) - 0.01D) / 256.0D;
						float var51 = arg.method_1604(this.field_82, var29, j, var31);
						if (var28 < 2) {
							var51 = var51 * var16;
						} else {
							var51 = var51 * var17;
						}
						
						var5.colour(var15 * var51 * var7, var15 * var51 * var8, var15 * var51 * var9);
						var5.vertex(var37, (j + var35), var38, var41, var45);
						var5.vertex(var39, (j + var36), var40, var43, var47);
						var5.vertex(var39, (j + 0), var40, var43, var49);
						var5.vertex(var37, (j + 0), var38, var41, var49);
					}
				}
				
				arg.minY = var18;
				arg.maxY = var20;
				info.setReturnValue(var13 > 0);
			}
		}
	}*/
}