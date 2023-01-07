package saturne.practice.handler.managers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;
import saturne.practice.Main;
import saturne.practice.ladder.Ladder;
import saturne.practice.match.MatchEntry;
import saturne.practice.profile.Profile;
import saturne.practice.profile.ProfileState;
import saturne.practice.queue.QueueEntry;
import saturne.practice.queue.QueueType;

public class QueueManager {
	
	private final Main main = Main.getInstance();
	
	public void addPlayerToQueue(final List<UUID> uuids, final Ladder ladder, final QueueType queueType) {
		for (UUID uuid : uuids) {
			if(!this.main.getQueues().containsKey(uuid)) {
	            final Profile pm = this.main.getProfiles().get(uuid);
	            this.main.getQueues().putIfAbsent(uuid, new QueueEntry(uuids, ladder, queueType));
	            pm.setProfileState(ProfileState.QUEUE);	
	            this.main.getManagerHandler().getItemManager().giveItems(Bukkit.getPlayer(uuid), "queue-items");
	            Bukkit.getPlayer(uuid).sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("messages.enter-in-queue").replace("%ladderName%", ladder.getDisplayName()).replace("%queueType%", queueType.toString().toLowerCase())));
	            this.main.getManagerHandler().getInventoryManager().refreshInventory();
			}
			if (!this.main.getQueues().isEmpty()) {
				UUID possibleUUID;
				for (final Map.Entry<UUID, QueueEntry> map : this.main.getQueues().entrySet()) {
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
	                    final List<UUID> firstList = Lists.newArrayList();
	                    final List<UUID> secondList = Lists.newArrayList();
                    	firstList.add(uuid);
                        secondList.add(possibleUUID);
                        new MatchEntry(firstList, secondList, ladder, queueType);
	                    firstList.clear();
	                    secondList.clear();
	                    this.main.getQueues().remove(uuid);
	                    this.main.getQueues().remove(possibleUUID);
	                    this.main.getManagerHandler().getInventoryManager().refreshInventory();
	                }
				}
			}	
		}
	}

}
