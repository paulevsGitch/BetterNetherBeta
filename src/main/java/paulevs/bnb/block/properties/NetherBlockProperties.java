package paulevs.bnb.block.properties;

import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.EnumProperty;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;

public class NetherBlockProperties {
	public static final EnumProperty<Axis> AXIS = EnumProperty.of("axis", Axis.class);
	public static final BooleanProperty NEAR_LAVA = BooleanProperty.of("near_lava");
}
