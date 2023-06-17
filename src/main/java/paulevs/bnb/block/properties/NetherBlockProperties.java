package paulevs.bnb.block.properties;

import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;

public class NetherBlockProperties {
	public static final EnumProperty<Axis> AXIS = EnumProperty.of("axis", Axis.class);
}
