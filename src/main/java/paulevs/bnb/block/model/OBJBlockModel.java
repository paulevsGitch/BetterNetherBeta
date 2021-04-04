package paulevs.bnb.block.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.render.QuadPoint;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationloader.api.client.model.CustomCuboidRenderer;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import net.modificationstation.stationloader.api.common.util.BlockFaces;
import net.modificationstation.stationloader.impl.client.model.CustomTexturedQuad;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.util.ResourceUtil;

public class OBJBlockModel implements CustomModel {
	private OBJCuboidRenderer[] emissive;
	private OBJCuboidRenderer[] cuboids;
	private final String[] textures;
	private Integer[] materials;
	private int[] atlasIDemissive;
	private int[] atlasIDsolid;
	
	public OBJBlockModel(String modelPath, String... textures) {
		this(modelPath, 16, 8, 8, 8, null, textures);
	}
	
	public OBJBlockModel(String modelPath, float scale, float offsetX, float offsetY, float offsetZ, BlockFaces mainFace, String... textures) {
		this.textures = textures;
		this.cuboids = new OBJCuboidRenderer[] {
			new OBJCuboidRenderer(makeQuads(modelPath, scale, offsetX, offsetY, offsetZ, mainFace))
		};
	}
	
	private CustomTexturedQuad[] makeQuads(String modelPath, float scale, float offsetX, float offsetY, float offsetZ, BlockFaces mainFace) {
		List<List<Integer>> groups = new ArrayList<List<Integer>>();
		List<Float> vertex = new ArrayList<Float>();
		List<Float> uvs = new ArrayList<Float>();
		List<Integer> mat = new ArrayList<Integer>();
		int material = -1;
		
		try {
			InputStream input = ResourceUtil.getResourceAsStream(modelPath);
			if (input != null) {
				InputStreamReader streamReader = new InputStreamReader(input);
				BufferedReader reader = new BufferedReader(streamReader);
				String string;

				while ((string = reader.readLine()) != null) {
					if (string.startsWith("usemtl")) {
						material ++;
					}
					else if (string.startsWith("vt")) {
						String[] uv = string.split(" ");
						uvs.add(Float.parseFloat(uv[1]));
						uvs.add(Float.parseFloat(uv[2]));
					}
					else if (string.startsWith("v")) {
						String[] vert = string.split(" ");
						for (int i = 1; i < 4; i++)
							vertex.add(Float.parseFloat(vert[i]));
					}
					else if (string.startsWith("f")) {
						List<Integer> list = new ArrayList<Integer>();
						List<Integer> uvList = new ArrayList<Integer>();
						String[] members = string.split(" ");
						if (members.length < 5) {
							System.out.println("Only qads in OBJ are supported!");
						}
						else {
							for (int i = 1; i < 5; i++) {
								String member = members[i];

								if (member.contains("/")) {
									String[] sub = member.split("/");
									list.add(Integer.parseInt(sub[0]) - 1); // Vertex
									uvList.add(Integer.parseInt(sub[1]) - 1); // UV
								}
								else {
									list.add(Integer.parseInt(member) - 1); // Vertex
								}
							}
						}
						mat.add(material < 0 ? 0 : material);
						list.addAll(uvList);
						groups.add(list);
					}
				}

				reader.close();
				streamReader.close();
				input.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		int index = 0;
		boolean hasUV = !uvs.isEmpty();
		materials = mat.toArray(new Integer[] {});
		CustomTexturedQuad[] quads = new CustomTexturedQuad[groups.size()];
		for (List<Integer> quad : groups) {
			QuadPoint[] points = new QuadPoint[4];
			for (int j = 0; j < 4; j++) {
				int vi = quad.get(j) * 3;
				float x = vertex.get(vi) * scale + offsetX;
				float y = vertex.get(vi + 1) * scale + offsetY;
				float z = vertex.get(vi + 2) * scale + offsetZ;
				float u = 0;
				float v = 0;
				if (hasUV) {
					vi = quad.get(j + 4) << 1;
					u = (uvs.get(vi)) / 16F;
					v = (1F - uvs.get(vi + 1)) / 16F;
				}
				points[j] = new QuadPoint(x, y, z, u, v);
			}
			
			BlockFaces face = mainFace;
			if (face == null) {
				float ax = (float) points[1].pointVector.x - (float) points[0].pointVector.x;
				float ay = (float) points[1].pointVector.y - (float) points[0].pointVector.y;
				float az = (float) points[1].pointVector.z - (float) points[0].pointVector.z;
				float bx = (float) points[2].pointVector.x - (float) points[0].pointVector.x;
				float by = (float) points[2].pointVector.y - (float) points[0].pointVector.y;
				float bz = (float) points[2].pointVector.z - (float) points[0].pointVector.z;
				
				float l = MathHelper.sqrt(ax * ax + ay * ay + az * az);
				ax /= l;
				ay /= l;
				az /= l;
				
				l = MathHelper.sqrt(bx * bx + by * by + bz * bz);
				bx /= l;
				by /= l;
				bz /= l;
				
				float nx = (ay * bz) - (az * by);
				float ny = (az * bx) - (ax * bz);
				float nz = (ax * by) - (ay * bx);
				ax = MathHelper.abs(nx);
				ay = MathHelper.abs(ny);
				az = MathHelper.abs(nz);
				float max = MHelper.max(ax, ay, az);
				
				if (max == ax) {
					face = nx > 0 ? BlockFaces.EAST : BlockFaces.WEST;
				}
				else if (max == ay) {
					face = ny > 0 ? BlockFaces.UP : BlockFaces.DOWN;
				}
				else {
					face = nz > 0 ? BlockFaces.SOUTH : BlockFaces.NORTH;
				}
			}
			
			quads[index++] = new CustomFacedTexturedQuad(points, face);
		}
		
		return quads;
	}

	@Override
	public CustomCuboidRenderer[] getCuboids() {
		return BlockUtil.isLightPass() ? emissive : cuboids;
	}
	
	private int calcAtlasID(int texture) {
		return textures.length > 0 ? texture / TextureRegistry.currentRegistry().texturesPerFile() : 0;
	}
	
	public void updateUVs() {
		int index = 0;
		int textures[] = new int[this.textures.length << 1];
		int offset = this.textures.length;
		
		for (int i = 0; i < this.textures.length; i++) {
			textures[i + offset] = TextureListener.getEmissiveBlockTexture(this.textures[i]);
			textures[i] = TextureListener.getSolidBlockTexture(this.textures[i]);
		}
		
		int posX;
		int posY;
		float startU;
		float startV;
		int textureIndex;
		
		List<CustomTexturedQuad> emissiveQuads = new ArrayList<CustomTexturedQuad>();
		List<Integer> emissiveAtlasses = new ArrayList<Integer>();
		atlasIDsolid = new int[cuboids[0].getCubeQuads().length];
		
		for (CustomCuboidRenderer renderer: cuboids) {
			for (net.modificationstation.stationloader.api.client.model.CustomTexturedQuad quad: renderer.getCubeQuads()) {
				int material = materials[index];
				if (material >= this.textures.length) {
					material = 0;
				}
				int texture = textures[material];
				int emissive = textures[material + offset];
				atlasIDsolid[index] = calcAtlasID(texture);
				index++;
				
				if (emissive != -1) {
					textureIndex = emissive & 255;
					posX = textureIndex & 15;
					posY = textureIndex >> 4;
					startU = posX / 16F;
					startV = posY / 16F;
					QuadPoint[] copy = new QuadPoint[quad.getQuadPoints().length];
					for (int i = 0; i < copy.length; i++) {
						QuadPoint src = quad.getQuadPoints()[i];
						copy[i] = new QuadPoint(src.pointVector, src.field_1147, src.field_1148);
					}
					CustomFacedTexturedQuad eQuad = new CustomFacedTexturedQuad(copy, quad.getSide());
					for (QuadPoint point: eQuad.getQuadPoints()) {
						point.field_1147 += startU;
						point.field_1148 += startV;
					}
					emissiveQuads.add(eQuad);
					emissiveAtlasses.add(calcAtlasID(emissive));
				}
				
				textureIndex = texture & 255;
				posX = textureIndex & 15;
				posY = textureIndex >> 4;
				startU = posX / 16F;
				startV = posY / 16F;
				for (QuadPoint point: quad.getQuadPoints()) {
					point.field_1147 += startU;
					point.field_1148 += startV;
				}
			}
		}
		
		if (emissiveQuads.isEmpty()) {
			emissive = new OBJCuboidRenderer[0];
			atlasIDemissive = new int[0];
		}
		else {
			emissive = new OBJCuboidRenderer[] { new OBJCuboidRenderer(emissiveQuads.toArray(new CustomTexturedQuad[] {})) };
			atlasIDemissive = new int[emissiveAtlasses.size()];
			for (int i = 0; i < atlasIDemissive.length; i++) {
				atlasIDemissive[i] = emissiveAtlasses.get(i);
			}
		}
	}
	
	public int getAtlasID(int quadIndex) {
		return BlockUtil.isLightPass() ? atlasIDemissive[quadIndex] : atlasIDsolid[quadIndex];
	}
}
