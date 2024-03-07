package paulevs.bnb.achievement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.client.gui.screen.achievement.AchievementPage;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.BNB;

import java.util.Random;

public class BNBAchievementPage extends AchievementPage {
	private static BNBAchievementPage instance;
	private int orichalcumTexture;
	
	public BNBAchievementPage(Identifier id) {
		super(id);
		instance = this;
	}
	
	@Override
	public int getBackgroundTexture(Random random, int column, int row, int randomizedRow, int currentTexture) {
		int rand = Math.abs((int) MathHelper.hashCode(column, 5, row)) & 31;
		return switch (rand) {
			case 0 -> orichalcumTexture;
			default -> Block.NETHERRACK.texture;
		};
	}
	
	@Environment(EnvType.CLIENT)
	public void updateTextures(ExpandableAtlas terrain) {
		orichalcumTexture = terrain.addTexture(BNB.id("block/orichalcum_ore")).index;
	}
	
	public static BNBAchievementPage getInstance() {
		return instance;
	}
}
