package paulevs.bnb.block.properties;

import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.util.StringIdentifiable;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;

public class NetherBlockProperties {
	public static final EnumProperty<VineShape> VINE_SHAPE = EnumProperty.of("shape", VineShape.class);
	public static final EnumProperty<Axis> AXIS = EnumProperty.of("axis", Axis.class);
	public static final BooleanProperty NEAR_LAVA = BooleanProperty.of("near_lava");
	
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
}
