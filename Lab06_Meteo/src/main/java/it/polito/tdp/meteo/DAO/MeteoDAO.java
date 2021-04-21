package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		return null;
	}
	
	
	public List<Integer> getMesi(){
		final String sql = "SELECT distinct MONTH(data) "
							+ "FROM situazione "
							+ "ORDER BY MONTH(DATA)";
		List<Integer> result = new ArrayList<Integer>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(rs.getInt("MONTH(data)"));
			}
			
			conn.close();
			return result;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public String getUmiditaMedia(int mese){
		final String sql = "SELECT Localita, AVG(Umidita)"
							+" FROM situazione WHERE MONTH(DATA)=?"
							+" GROUP BY Localita";
		String s="";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet rs = st.executeQuery();
			
			
			while(rs.next()) {
				s += rs.getString("Localita")+" "+ rs.getDouble("AVG(Umidita)")+"\n";
			}
			
			conn.close();
			return s;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public int calcolaUmiditaGiorno(int livello, int mese, String localita) {
		final String sql = "SELECT Umidita "
				+"FROM situazione "
				+"WHERE MONTH(DATA)= ? AND Localita = ? AND DAY(DATA) = ? ";
		int result = -1;
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, localita);
			st.setInt(3, livello);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result = rs.getInt("Umidita");
			}
			conn.close();
			return result;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}


}
