package bawz.practice.handler.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import bawz.practice.match.preview.PreviewInventory;

public class PreviewManager {
	
	final Map<UUID, PreviewInventory> offlinePreview = new HashMap<>();
	
	public void addInventories(final UUID uuid, final PreviewInventory previewInventory) {
		if (this.offlinePreview.containsKey(uuid)) {
			this.offlinePreview.remove(uuid);
		}
		this.offlinePreview.putIfAbsent(uuid, previewInventory);
	}
}
