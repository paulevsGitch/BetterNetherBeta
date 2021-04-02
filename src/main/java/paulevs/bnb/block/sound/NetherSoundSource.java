package paulevs.bnb.block.sound;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.class_267;
import paulevs.bnb.util.ResourceUtil;

public class NetherSoundSource {
	private final Map<String, List<class_267>> sounds = Maps.newHashMap();
	private Random random = new Random();
	
	public class_267 getSound(String name) {
		List<class_267> list = sounds.get(name);
		if (list == null) {
			list = Lists.newArrayList();
			sounds.put(name, list);
			String path = "assets/" + name.replace('.', '/');
			List<String> resources = ResourceUtil.getResourceFiles(path);
			for (String file: resources) {
				System.out.println(file);
				URL url = ResourceUtil.getURL(path + "/" + file);
				String subname = path.substring(path.lastIndexOf('/') + 1) + "/" + file;
				class_267 sound = new class_267(subname, url);
				list.add(sound);
			}
		}
		return list.isEmpty() ? null : list.get(random.nextInt(list.size()));
	}
}
