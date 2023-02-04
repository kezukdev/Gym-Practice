package bawz.practice.profile.data;

public class ProfileData {
	
	private Integer[] elos;
	public Integer[] getElos() { return elos; }
	private boolean scoreboard;
	public boolean isScoreboard() { return scoreboard; }
	public void setScoreboard(boolean scoreboard) { this.scoreboard = scoreboard; }
	
	public ProfileData(final Integer[] elos, final boolean scoreboard) {
		this.elos = elos;
		this.scoreboard = scoreboard;
	}

}
