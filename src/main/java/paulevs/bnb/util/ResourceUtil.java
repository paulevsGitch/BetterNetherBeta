package paulevs.bnb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import com.google.common.collect.Lists;

public class ResourceUtil {
	public static List<String> getResourceFiles(String path) {
		List<String> filenames = Lists.newArrayList();
		String resource;

		try {
			InputStream in = getResourceAsStream(path);
			if (in != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
	
				while ((resource = br.readLine()) != null) {
					filenames.add(resource);
				}
				
				br.close();
				in.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return filenames;
	}

	public static InputStream getResourceAsStream(String resource) {
		final InputStream in = getContextClassLoader().getResourceAsStream(resource);
		return in == null ? ResourceUtil.class.getResourceAsStream(resource) : in;
	}

	private static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static URL getURL(String path) {
		return ResourceUtil.class.getClassLoader().getResource(path);
	}
}
