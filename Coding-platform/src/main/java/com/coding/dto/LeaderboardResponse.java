package com.coding.dto;



public class LeaderboardResponse {

    private int rank;
    private String username;
    private Long totalScore;

    public LeaderboardResponse(String username, Long totalScore) {
        this.username = username;
        this.totalScore = totalScore;
    }

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}
    
    

   
}
