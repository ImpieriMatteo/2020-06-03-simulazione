package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.MatchPlayer;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public void listAllPlayers(Map<Integer, Player> idMapPlayer){
		String sql = "SELECT * FROM Players";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				idMapPlayer.put(player.getPlayerID(), player);
			}
			conn.close();
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getAllPlayersPerXGoals(Double xGoals, Map<Integer, Player> idMapPlayer) {
		String sql = "SELECT p.PlayerID AS id, p.Name AS Name, (SELECT SUM(a2.Goals) "
				+ "		  												FROM actions a2 "
				+ "		  												WHERE a2.PlayerID = p.PlayerID)/COUNT(*) AS x "
				+ "FROM actions a, players p "
				+ "WHERE a.PlayerID = p.PlayerID "
				+ "GROUP BY p.PlayerID "
				+ "HAVING (SELECT SUM(a2.Goals) "
				+ "		  FROM actions a2 "
				+ "		  WHERE a2.PlayerID = p.PlayerID)/COUNT(*)>?";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, xGoals);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("id"), res.getString("Name"));
				player.setMediaGoal(res.getDouble("x"));
				
				idMapPlayer.put(player.getPlayerID(), player);
			}
			conn.close();
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void getAllMatchTitPerPlayer(Map<Integer, Player> idMapPlayer) {
		String sql = "SELECT a.PlayerID AS PlayerID, a.MatchID AS MatchID, a.TeamID AS TeamID, a.TimePlayed AS tempo "
				+ "FROM actions a "
				+ "WHERE a.`Starts`= 1";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			res.next();
			while (true) {
				if(!idMapPlayer.containsKey(res.getInt("PlayerID")))
					res.next();
				else {
					
					Player player = idMapPlayer.get(res.getInt("PlayerID"));
					player.setTeamID(res.getInt("TeamID"));
					while (idMapPlayer.get(res.getInt("PlayerID")).getPlayerID() == player.getPlayerID()) {
					
						MatchPlayer m = new MatchPlayer(res.getInt("MatchID"), res.getInt("tempo"));
						player.addMatchTitolare(m);
				
						if(!res.next())
							break;
						
						if(!idMapPlayer.containsKey(res.getInt("PlayerID")))
							break;
					}
				}
				
				if(res.isAfterLast())
					break;
			}
			conn.close();
			return;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
}
