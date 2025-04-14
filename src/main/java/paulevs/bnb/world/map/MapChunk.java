package paulevs.bnb.world.map;

import it.unimi.dsi.fastutil.bytes.Byte2ObjectMap;
import it.unimi.dsi.fastutil.bytes.Byte2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ByteMap;
import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;

import java.util.function.Function;

public class MapChunk<T> {
	private final Reference2ByteMap<T> objToID = new Reference2ByteOpenHashMap<>();
	private final Byte2ObjectMap<T> idToObj = new Byte2ObjectOpenHashMap<>();
	private final byte[] data = new byte[4096];
	private byte id;
	
	public void set(int index, T value) {
		byte id = objToID.computeIfAbsent(value, k -> {
			byte newID = this.id++;
			idToObj.put(newID, value);
			return newID;
		});
		data[index] = id;
	}
	
	public T get(int index) {
		return idToObj.get(data[index]);
	}
	
	public void save(CompoundTag tag, Function<T, String> serializer) {
		ListTag list = new ListTag();
		tag.put("palette", list);
		objToID.forEach((value, id) -> {
			CompoundTag entry = new CompoundTag();
			entry.put("id", id);
			entry.put("value", serializer.apply(value));
			list.add(entry);
		});
		tag.put("data", data);
	}
	
	public boolean load(CompoundTag tag, Function<String, T> deserializer) {
		if (!tag.containsKey("palette") || !tag.containsKey("data")) {
			return false;
		}
		byte[] preData = tag.getByteArray("data");
		if (preData.length != 4096) return false;
		System.arraycopy(preData, 0, data, 0, 4096);
		ListTag list = tag.getListTag("palette");
		for (int i = 0; i < list.size(); i++) {
			CompoundTag entry = (CompoundTag) list.get(i);
			byte id = entry.getByte("id");
			String name = entry.getString("value");
			T value = deserializer.apply(name);
			objToID.put(value, id);
			idToObj.put(id, value);
		}
		return true;
	}
}
