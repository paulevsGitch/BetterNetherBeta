package paulevs.bnb;

import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.net.URL;

public class BNB {
	public static final Namespace NAMESPACE = Namespace.of("bnb");
	public static final Logger LOGGER = LogManager.getLogger();
	
	public static Identifier id(String name) {
		return NAMESPACE.id(name);
	}
	
	public static URL getURL(String path) {
		return Thread.currentThread().getContextClassLoader().getResource(path);
	}
}
