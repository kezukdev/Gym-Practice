package bawz.practice.handler.managers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;

import com.google.common.collect.Lists;

import bawz.practice.Main;
import bawz.practice.ladder.Ladder;
import bawz.practice.match.MatchEntry;
import bawz.practice.profile.Profile;
import bawz.practice.profile.ProfileState;
import bawz.practice.queue.QueueEntry;
import bawz.practice.queue.QueueType;
import net.md_5.bungee.api.ChatColor;

public class QueueManager {
	
	private Main main;
	
	private ConcurrentMap<UUID, QueueEntry> queues = new ConcurrentHashMap<>();
	public ConcurrentMap<UUID, QueueEntry> getQueues() { return queues; }
	
	public QueueManager(final Main main) { this.main = main; }
	
	public void addPlayerToQueue(final List<UUID> uuids, final Ladder ladder, final QueueType queueType) {
		for (UUID uuid : uuids) {
			if(!this.getQueues().containsKey(uuid)) {
	            final Profile pm = this.main.getManagerHandler().getProfileManager().getProfiles().get(uuid);
	            this.getQueues().putIfAbsent(uuid, new QueueEntry(uuids, ladder, queueType));
	            pm.setProfileState(ProfileState.QUEUE);	
	            this.main.getManagerHandler().getItemManager().giveItems(Bukkit.getPlayer(uuid), "queue-items");
	            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.enter-in-queue").replace("%ladderName%", ladder.getDisplayName()).replace("%queueType%", queueType.toString().toLowerCase())));
			}
			if (!this.getQueues().isEmpty()) {
				UUID possibleUUID;
				for (final Map.Entry<UUID, QueueEntry> map : this.getQueues().entrySet()) {
	                final UUID key = map.getKey();
	                final QueueEntry value = map.getValue();
	                if (uuid != key && queueType == value.getQueueType()) {
	                    if (ladder != value.getLadder()) {
	                        continue;
	                    }
	                    possibleUUID = key;
	                    if (possibleUUID == uuid) {
	                        return;
	                    }
	                    if (value.getQueueType().equals(QueueType.RANKED)) {
	                    	return;
	                    }
	                    final List<List<UUID>> list = Arrays.asList(Lists.newArrayList(uuid), Lists.newArrayList(possibleUUID));
                        new MatchEntry(list, ladder, queueType, this.main);
	                    this.getQueues().remove(uuid);
	                    this.getQueues().remove(possibleUUID);
	                }
				}
			}	
            this.main.getManagerHandler().getInventoryManager().refreshInventory();
		}
	}

}
