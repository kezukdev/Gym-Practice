package bawz.practice.board;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class Adaptater {
	
	private String name;
	private boolean queueInLobby;
	public boolean isQueueInLobby() { return queueInLobby; }
	public String getName() { return name; }
	private List<Map<String, List<String>>> scoreboard = Lists.newArrayList();
	public List<Map<String, List<String>>> getScoreboard() { return scoreboard; }
	
	public Adaptater(final String name, final String type, final boolean queueInLobby, final List<String> scoreboard) {
		this.name = name;
		Integer i = type.equals("spawn") ? 0 : 1;
		if (type.equals("start-fight")) i = 2;
		if (type.equals("fight")) i = 3;
		if (type.equals("end-fight")) i = 4;
		if (type.equals("kit-editing")) i = 5;
		this.scoreboard.get(i).put(type, scoreboard);
		if (type.equals("queue")) { this.queueInLobby = queueInLobby; }
	}
	
}
