package paulevs.bnb.sound;

public class NetherSoundSource {
	/*private final Map<String, List<class_267>> sounds = Maps.newHashMap();
	private Random random = new Random();
	
	public class_267 getSound(String name) {
		List<class_267> list = sounds.get(name);
		if (list == null) {
			list = Lists.newArrayList();
			sounds.put(name, list);
			String path = "assets/" + name.replace('.', '/');
			List<String> resources = ResourceUtil.getResourceFiles(path);
			for (String file: resources) {
				URL url = ResourceUtil.getURL(path + "/" + file);
				String subname = path.substring(path.lastIndexOf('/') + 1) + "/" + file;
				class_267 sound = new class_267(subname, url);
				list.add(sound);
			}
		}
		return list.isEmpty() ? null : list.get(random.nextInt(list.size()));
	}
	
	public class_267 getSoundDirectly(String name) {
		String path = "assets/" + name.replace('.', '/') + ".ogg";
		URL url = ResourceUtil.getURL(path);
		return new class_267(path, url);
	}*/
}
