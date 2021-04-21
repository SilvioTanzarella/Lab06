package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.List;


import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private MeteoDAO dao;
	int minimo = -1;
	int count=0;
	int costo_parziale=0;
	List<String> result;

	public Model() {
		dao = new MeteoDAO();
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		return dao.getUmiditaMedia(mese);
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {
		result = new ArrayList<String>();
		List<String> parziale = new ArrayList<String>();
		cerca(parziale, 0, mese);
		String stringa = "";
		for(String s: result) {
			stringa += s+"\n";
		}
		for(int i=0;i<result.size();i++) {
			System.out.println(result.get(i)+" "+this.calcolaUmiditaGiorno(i+1, mese, result.get(i)));
		}
		return stringa;
	}
	
	
	
	private void cerca(List<String> parziale, int livello, int mese) {
		if(parziale.size() == Model.NUMERO_GIORNI_TOTALI) {
			if((this.calcolaCosto(parziale, mese)<minimo)||(minimo<0)) { 
					this.minimo = this.calcolaCosto(parziale, mese);
					result = new ArrayList<String>(parziale);
					System.out.println(result+" "+this.minimo+"\n");
			}
			return;
		}
		
			if(this.cittaValida(parziale, "Genova"))
			{
				parziale.add("Genova");
				this.cerca(parziale, livello+1, mese);
				parziale.remove(parziale.size()-1);
			}
			if(this.cittaValida(parziale, "Milano"))
			{
				parziale.add("Milano");
				this.cerca(parziale, livello+1, mese);
				parziale.remove(parziale.size()-1);
			}
			if(this.cittaValida(parziale, "Torino"))
			{
				parziale.add("Torino");
				this.cerca(parziale, livello+1, mese);
				parziale.remove(parziale.size()-1);
			}
		
	}

	public List<Integer> getMesi(){
		return dao.getMesi();
	}
	
	public int calcolaUmiditaGiorno(int livello, int mese, String localita) {
		return dao.calcolaUmiditaGiorno(livello, mese, localita);
	}
	
	private boolean cittaValida(List<String> parziale, String citta) {
		parziale.add(citta);
		int count = 0;
		int consecutivo = 0;
		for(int i=0; i<parziale.size(); i++) {
			if(parziale.get(i) == citta)
				count++;
			if(count > Model.NUMERO_GIORNI_CITTA_MAX) {
				parziale.remove(parziale.size()-1);
				return false;	
			}
			if(i>0) {
				if(parziale.get(i).equals(parziale.get(i-1))) {
					consecutivo++;
				}
				else {
					if(consecutivo<Model.NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN) {
						parziale.remove(parziale.size()-1);
						return false;
					}
					consecutivo = 0;
				}
			}
			
		}
		parziale.remove(parziale.size()-1);
		return true;
	}
	
	private int calcolaCosto(List<String> parziale, int mese) {
		int costo = 0;
		for(int i=0;i<parziale.size();i++) {
			if(i==0) {
				costo = this.calcolaUmiditaGiorno(i+1, mese, parziale.get(i));
			}
			else {
				if(parziale.get(i).equals(parziale.get(i-1))) {
					costo += this.calcolaUmiditaGiorno(i+1, mese, parziale.get(i));
				}
				else {
					costo += this.calcolaUmiditaGiorno(i+1, mese, parziale.get(i)) + Model.COST;
				}
			}
		}
		return costo;
	}
	


}
