package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private SimpleDirectedWeightedGraph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMapPlayer;
	private PremierLeagueDAO dao;
	private List<Player> bestTeam;
	private Integer bestTitolarita;

	public Model() {
		this.idMapPlayer = new HashMap<>();
		this.dao = new PremierLeagueDAO();
	}

	public String creaGrafo(Double xGoals) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao.getAllPlayersPerXGoals(xGoals, idMapPlayer);
		
		Graphs.addAllVertices(this.grafo, this.idMapPlayer.values());
		
		this.dao.getAllMatchTitPerPlayer(idMapPlayer);
		
		for(Player p1 : this.idMapPlayer.values()) {
			
			if(p1.getTeamID()!=null) {
				for(Player p2 : this.idMapPlayer.values()) {

					if(p2.getTeamID()!=null) {
						if(!p1.getTeamID().equals(p2.getTeamID())) {

							Integer peso = 0;
							for(MatchPlayer mp1 : p1.getMatchTitolare()) {

								for(MatchPlayer mp2 : p2.getMatchTitolare()) {

									if(mp1.getMatch().equals(mp2.getMatch()))
										peso += mp1.getTempoGiocato()-mp2.getTempoGiocato();
								}
							}
							if(peso>0)
								Graphs.addEdgeWithVertices(this.grafo, p1, p2, peso);
						}
					}
				}
			}
		}
		
		return String.format("Grafo creato!\n#VERTICI: %d\n#ARCHI: %d", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}

	public List<Arco> getPlayerMigliore() {
		List<Arco> result = new ArrayList<>();
		Player best = null;
		
		for(Player p : this.grafo.vertexSet()) {
			
			Set<DefaultWeightedEdge> temp = this.grafo.outgoingEdgesOf(p);
			if(temp.size()>result.size()) {
				
				result.clear();
				best = p;
				
				result.add(new Arco(best, best, 999999));
				for(DefaultWeightedEdge e : temp) 
					result.add(new Arco(best, Graphs.getOppositeVertex(this.grafo, e, p), (int)this.grafo.getEdgeWeight(e)));
			}
		}
		
		return result;
	}
	
	public List<Player> getDreamTeam(Integer k){
		this.bestTeam = new ArrayList<>();
		this.bestTitolarita = 0;
		
		List<Player> parziale = new ArrayList<>();
		List<Player> player = new ArrayList<>(this.grafo.vertexSet());
		Integer gradoTitolarita = 0;
		
		cercaDreamTeam(parziale, player, gradoTitolarita, k);
		
		return this.bestTeam;
	}

	private void cercaDreamTeam(List<Player> parziale, List<Player> player, Integer gradoTitolarita, Integer k) {
		
		if(parziale.size()==k) {
			
			if(gradoTitolarita>this.bestTitolarita) {
				this.bestTeam = new ArrayList<>(parziale);
				this.bestTitolarita = gradoTitolarita;
			}
			
			return;
		}
		
		for(Player p : player) {
			
			if(!parziale.contains(p)) {
				Integer temp = 0;
				List<Player> temp2 = new ArrayList<>();
				
				for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) 
					temp += (int)this.grafo.getEdgeWeight(e);
				
				for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) 
					temp -= (int)this.grafo.getEdgeWeight(e);
				
				parziale.add(p);
				gradoTitolarita += temp;
				
				List<Player> playerRimanenti = new ArrayList<>(this.grafo.vertexSet());
				playerRimanenti.removeAll(Graphs.successorListOf(this.grafo, p));
				
				this.cercaDreamTeam(playerRimanenti, player, gradoTitolarita, k);
				
				parziale.remove(p);
				gradoTitolarita -= temp;
			}
		}
	}
	
	public Integer getTitolaritaBest() {
		return this.bestTitolarita;
	}

}
