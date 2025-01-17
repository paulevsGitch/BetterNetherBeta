package paulevs.bnb.block.property;

import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;

public class BNBBlockProperties {
	public static final EnumProperty<DoubleShape> DOUBLE_SHAPE = EnumProperty.of("shape", DoubleShape.class);
	public static final EnumProperty<Direction> DIRECTION = EnumProperty.of("direction", Direction.class);
	public static final EnumProperty<VineShape> VINE_SHAPE = EnumProperty.of("shape", VineShape.class);
	public static final EnumProperty<Axis> AXIS = EnumProperty.of("axis", Axis.class);
	public static final BooleanProperty LIT = BooleanProperty.of("lit");
	public static final BooleanProperty BERRIES = BooleanProperty.of("berries");
	public static final IntProperty STAGE_4 = IntProperty.of("stage", 0, 3);
	public static final IntProperty LEAVES_DIRECTION = IntProperty.of("direction", 0, 6);
	public static final BooleanProperty GRAPE = BooleanProperty.of("grape");
	public static final BooleanProperty[] FACES = new BooleanProperty[6];
	public static final IntProperty TYPE_16 = IntProperty.of("type", 0, 15);
	
	public static BooleanProperty getByDir(Direction dir) {
		return FACES[dir.getId()];
	}
	
	public enum VineShape implements StringIdentifiable {
		NORMAL("normal"),
		BOTTOM("bottom");
		
		final String name;
		
		VineShape(String name) {
			this.name = name;
		}
		
		@Override
		public String asString() {
			return name;
		}
	}
	
	public enum DoubleShape implements StringIdentifiable {
		TOP("top"),
		BOTTOM("bottom");
		
		final String name;
		
		DoubleShape(String name) {
			this.name = name;
		}
		
		@Override
		public String asString() {
			return name;
		}
	}
	
	static {
		for (int i = 0; i < 6; i++) {
			FACES[i] = BooleanProperty.of(Direction.byId(i).getName());
		}
	}
}
