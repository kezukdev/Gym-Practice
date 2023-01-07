package saturne.practice.handler;

import saturne.practice.Main;
import saturne.practice.handler.listeners.EntityListener;
import saturne.practice.handler.listeners.InventoryListener;
import saturne.practice.handler.listeners.PlayerListener;
import saturne.practice.handler.listeners.ServerListener;

public class ListenerHandler {
	
	public ListenerHandler(final Main main) {
		main.registerListeners(new PlayerListener());
		main.registerListeners(new EntityListener());
		main.registerListeners(new InventoryListener());
		main.registerListeners(new ServerListener());
	}

}
