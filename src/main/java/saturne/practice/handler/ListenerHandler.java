package saturne.practice.handler;

import saturne.practice.Main;
import saturne.practice.handler.listeners.PlayerListener;
import saturne.practice.handler.listeners.ServerListener;

public class ListenerHandler {
	
	public ListenerHandler(final Main main) {
		main.registerListeners(new PlayerListener());
		main.registerListeners(new ServerListener());
	}

}
