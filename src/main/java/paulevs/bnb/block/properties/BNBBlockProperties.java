package paulevs.bnb.block.properties;

import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;

public class BNBBlockProperties {
	public static final EnumProperty<DoubleShape> DOUBLE_SHAPE = EnumProperty.of("shape", DoubleShape.class);
	public static final EnumProperty<VineShape> VINE_SHAPE = EnumProperty.of("shape", VineShape.class);
	public static final EnumProperty<SlabShape> SLAB = EnumProperty.of("shape", SlabShape.class);
	public static final EnumProperty<Axis> AXIS = EnumProperty.of("axis", Axis.class);
	public static final BooleanProperty NEAR_LAVA = BooleanProperty.of("near_lava");
	public static final IntProperty STAGE_4 = IntProperty.of("stage", 0, 3);
	public static final BooleanProperty[] FACES = new BooleanProperty[6];
	
	public static BooleanProperty getByFace(Direction dir) {
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
	
	public enum SlabShape implements StringIdentifiable {
		BOTTOM("bottom"),
		TOP("top"),
		EAST("east"),
		WEST("west"),
		NORTH("north"),
		SOUTH("south"),
		FULL("full");
		
		private static final SlabShape[] VALUES = values();
		private final String name;
		
		SlabShape(String name) {
			this.name = name;
		}
		
		@Override
		public String asString() {
			return name;
		}
		
		public static SlabShape fromDirection(Direction dir) {
			return VALUES[dir.ordinal()];
		}
		
		public Direction getDirection() {
			if (this == FULL) return Direction.DOWN;
			return Direction.byId(this.ordinal());
		}
	}
	
	static {
		for (int i = 0; i < 6; i++) {
			FACES[i] = BooleanProperty.of(Direction.byId(i).getName());
		}
	}
}
