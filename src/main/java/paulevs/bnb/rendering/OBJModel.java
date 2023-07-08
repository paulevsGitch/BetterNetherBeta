package paulevs.bnb.rendering;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.client.render.model.Baker;
import net.modificationstation.stationapi.api.client.render.model.BasicBakedModel;
import net.modificationstation.stationapi.api.client.render.model.ModelBakeSettings;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelOverrideList;
import net.modificationstation.stationapi.api.client.render.model.json.ModelTransformation;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import paulevs.bnb.BNB;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class OBJModel implements UnbakedModel {
	private static final SpriteIdentifier MISSING = SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, Identifier.of("missingno"));
	private final List<QuadData> quads = new ArrayList<>();
	private final SpriteIdentifier particleTexture;
	
	public OBJModel(String modelName, String[] data, Map<String, SpriteIdentifier> textures, Vec3f offset) {
		particleTexture = textures.getOrDefault("particle", MISSING);
		loadData(modelName, data, textures, offset);
	}
	
	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList();
	}
	
	@Override
	public void setParents(Function<Identifier, UnbakedModel> parents) {}
	
	@Override
	public BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		ImmutableList<BakedQuad> quads = this.quads.stream().map(quad -> quad.bake(textureGetter)).collect(ImmutableList.toImmutableList());
		Builder<Direction, ImmutableList<BakedQuad>> builder = ImmutableMap.builder();
		Direction.stream().forEach(dir -> builder.put(dir, ImmutableList.of()));
		ImmutableMap<Direction, ImmutableList<BakedQuad>> faceQuads = builder.build();
		Sprite particles = textureGetter.apply(particleTexture);
		try {
			Constructor<?> constructor = BasicBakedModel.class.getDeclaredConstructors()[0];
			constructor.setAccessible(true);
			return (BakedModel) constructor.newInstance(quads, faceQuads, false, false, false, particles, ModelTransformation.NONE, ModelOverrideList.EMPTY);
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void loadData(String modelName, String[] data, Map<String, SpriteIdentifier> textures, Vec3f offset) {
		List<Float> objVertex = new ArrayList<>(12);
		List<Float> uvData = new ArrayList<>(8);
		
		List<Integer> vertexIndex = new ArrayList<>(4);
		List<Integer> uvIndex = new ArrayList<>(4);
		
		SpriteIdentifier activeMaterial = MISSING;
		
		boolean triangleWarning = false;
		boolean ngonsWarning = false;
		
		for (String line : data) {
			if (line.startsWith("usemtl ")) {
				line = line.substring(7);
				activeMaterial = textures.getOrDefault(line, MISSING);
			}
			else if (line.startsWith("vt ")) {
				String[] uv = line.split(" ");
				uvData.add(Float.parseFloat(uv[1]));
				uvData.add(Float.parseFloat(uv[2]));
			}
			else if (line.startsWith("v ")) {
				String[] vert = line.split(" ");
				for (byte i = 1; i < 4; i++) {
					objVertex.add(Float.parseFloat(vert[i]));
				}
			}
			else if (line.startsWith("f ")) {
				String[] members = line.split(" ");
				if (members.length != 5) {
					if (!triangleWarning && members.length < 5) {
						BNB.LOGGER.warn("Only quads in OBJ models are supported! Model " + modelName + " has triangles");
						triangleWarning = true;
					}
					else if (!ngonsWarning && members.length > 5) {
						BNB.LOGGER.warn("Only quads in OBJ models are supported! Model " + modelName + " has n-gons");
						ngonsWarning = true;
					}
					continue;
				}
				vertexIndex.clear();
				uvIndex.clear();
				
				for (byte i = 1; i < members.length; i++) {
					String member = members[i];
					
					if (member.contains("/")) {
						String[] sub = member.split("/");
						vertexIndex.add(Integer.parseInt(sub[0]) - 1);
						if (!sub[1].isEmpty()) {
							uvIndex.add(Integer.parseInt(sub[1]) - 1);
						}
					}
					else {
						vertexIndex.add(Integer.parseInt(member) - 1);
					}
				}
				
				int[] vertexData = new int[32];
				boolean hasUV = !uvIndex.isEmpty();
				
				for (byte i = 0; i < 4; i++) {
					int index = vertexIndex.get(i) * 3;
					int dataIndex = i << 3;
					vertexData[dataIndex++] = Float.floatToIntBits(objVertex.get(index++) + offset.getX());
					vertexData[dataIndex++] = Float.floatToIntBits(objVertex.get(index++) + offset.getY());
					vertexData[dataIndex++] = Float.floatToIntBits(objVertex.get(index) + offset.getZ());
					if (hasUV) {
						index = uvIndex.get(i) << 1;
						float u = uvData.get(index++);
						float v = 1.0F - uvData.get(index);
						vertexData[dataIndex++] = Float.floatToIntBits(u);
						vertexData[dataIndex] = Float.floatToIntBits(v);
					}
				}
				
				quads.add(new QuadData(vertexData, 0, Direction.UP, activeMaterial, false));
			}
		}
	}
	
	private static class QuadData {
		final int[] vertexData;
		final int colorIndex;
		final Direction face;
		final SpriteIdentifier texture;
		final boolean shade;
		final int[] bakedData;
		
		QuadData(int[] vertexData, int colorIndex, Direction face, SpriteIdentifier texture, boolean shade) {
			this.vertexData = vertexData;
			this.colorIndex = colorIndex;
			this.face = face;
			this.texture = texture;
			this.shade = shade;
			this.bakedData = new int[32];
			System.arraycopy(this.vertexData, 0, this.bakedData, 0, 32);
		}
		
		BakedQuad bake(Function<SpriteIdentifier, Sprite> textureGetter) {
			Sprite sprite = textureGetter.apply(texture);
			for (byte i = 0; i < 4; i++) {
				int index = (i << 3) + 3;
				
				float delta = Float.intBitsToFloat(this.vertexData[index]);
				delta = MathHelper.lerp(delta, sprite.getMinU(), sprite.getMaxU());
				this.bakedData[index] = Float.floatToIntBits(delta);
				
				index++;
				delta = Float.intBitsToFloat(this.vertexData[index]);
				delta = MathHelper.lerp(delta, sprite.getMinV(), sprite.getMaxV());
				this.bakedData[index] = Float.floatToIntBits(delta);
			}
			BakedQuad quad = new BakedQuad(bakedData, colorIndex, face, sprite, shade);
			if (texture.texture.id.endsWith("_e")) {
				EmissiveQuad.cast(quad).setEmissive(true);
			}
			return quad;
		}
	}
}
