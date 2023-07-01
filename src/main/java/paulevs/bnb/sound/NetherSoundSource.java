package paulevs.bnb.sound;

public class NetherSoundSource {
	/*private final Map<String, List<SoundEntry>> sounds = new HashMap<>();
	private final Random random = new Random();
	
	public SoundEntry getSound(String name) {
		List<SoundEntry> list = sounds.get(name);
		if (list == null) {
			list = new ArrayList<>();
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
	
	public SoundEntry getSoundDirectly(String name) {
		String path = "assets/bnb/stationapi/sounds/" + name + ".ogg";
		URL url = Thread.currentThread().getContextClassLoader().getResource(path);
		return new SoundEntry(path, url);
	}*/
}
