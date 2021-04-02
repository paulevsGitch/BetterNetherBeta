package paulevs.bnb.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;
import paulevs.bnb.BetterNetherBeta;

public class ResourceUtil {
	public static List<String> getResourceFiles(String path) {
		List<String> filenames = Lists.newArrayList();
		
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
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
		}
		else {
			int cut = path.length() - 1;
			File dir = new File("./mods");
			for (File mod: dir.listFiles()) {
				if (mod.isFile() && mod.getName().endsWith(".jar")) {
					try {
						ZipFile zipFile = new ZipFile(mod);
						Enumeration<? extends ZipEntry> entries = zipFile.entries();
						while(entries.hasMoreElements()){
						    ZipEntry entry = entries.nextElement();
						    if (entry.getName().equals("fabric.mod.json")) {
						    	JsonObject json = JsonUtil.loadJson(zipFile.getInputStream(entry));
						    	if (!json.get("id").getAsString().equals(BetterNetherBeta.MOD_ID)) {
						    		break;
						    	}
						    }
						    if (("/" + entry.getName()).startsWith(path)) {
						    	filenames.add(entry.getName().substring(cut));
						    }
						}
						zipFile.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				if (!filenames.isEmpty()) {
					break;
				}
			}
			
		}
		
		return filenames;
	}

	public static InputStream getResourceAsStream(String resource) {
		InputStream in = getContextClassLoader().getResourceAsStream(resource);
		return in == null ? ResourceUtil.class.getResourceAsStream(resource) : in;
	}

	private static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static URL getURL(String path) {
		return ResourceUtil.class.getClassLoader().getResource(path);
	}
}
