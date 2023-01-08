package bawz.practice.handler;

import bawz.practice.Main;
import bawz.practice.handler.listeners.EntityListener;
import bawz.practice.handler.listeners.InventoryListener;
import bawz.practice.handler.listeners.PlayerListener;
import bawz.practice.handler.listeners.ServerListener;

public class ListenerHandler {
	
	public ListenerHandler(final Main main) {
		main.registerListeners(new PlayerListener());
		main.registerListeners(new EntityListener());
		main.registerListeners(new InventoryListener());
		main.registerListeners(new ServerListener());
	}

}
