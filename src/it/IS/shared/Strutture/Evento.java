package it.IS.shared.Strutture;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Evento implements Serializable, IsSerializable{
	
	private static final long serialVersionUID = 7122657743729939015L;
	private String idEvento;
	private String idAmministratore;
	private String titoloEvento;
	private String luogoEvento;
	private String descrizione;
	
	public Evento(){}
	
	public Evento(String idE, String idU, String titolo, String luogo, String descrizione){
		this.idEvento = idE;
		this.idAmministratore = idU;
		this.titoloEvento = titolo;
		this.luogoEvento = luogo;
		this.descrizione = descrizione;
	}
	
	public String getIdEvento(){
		return idEvento;
	}
	public String getIdAmmnistratore(){
		return idAmministratore;
	}
	public String getNomeEvento(){
		return titoloEvento;
	}
	public String getLuogo(){
		return luogoEvento;
	}
	public String getDescrizione(){
		return descrizione;
	}
	
	public String toString(){
		return idEvento + " - " + idAmministratore + " - " + titoloEvento + " - " + luogoEvento + " - " + descrizione;
	}

}



