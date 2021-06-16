package it.polito.tdp.PremierLeague.model;

public class MatchPlayer {
	
	private Integer matchID;
	private Integer tempoGiocato;
	
	public MatchPlayer(Integer match, Integer tempoGiocato) {
		this.matchID = match;
		this.tempoGiocato = tempoGiocato;
	}
	
	public Integer getMatch() {
		return matchID;
	}
	public void setMatchID(Integer matchID) {
		this.matchID = matchID;
	}
	public Integer getTempoGiocato() {
		return tempoGiocato;
	}
	public void setTempoGiocato(Integer tempoGiocato) {
		this.tempoGiocato = tempoGiocato;
	}

}
