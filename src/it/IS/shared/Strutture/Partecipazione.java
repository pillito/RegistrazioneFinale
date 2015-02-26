package it.IS.shared.Strutture;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Partecipazione implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = 5534643463667550835L;
	private String idPartecipazione;
	private String idDataOra;
	private String idUtente;
	private String nomeUtente;
	
	public Partecipazione(){}
	
	public Partecipazione(String id, String data_ora, String utente, String nome){
		this.idPartecipazione = id;
		this.idDataOra = data_ora;
		this.idUtente = utente;
		this.nomeUtente = nome;
	}
	
	public String getIdPartecipazione(){
		return idPartecipazione;
	}
	public String getIdDataOra(){
		return idDataOra;
	}
	public String getIdUtente(){
		return idUtente;
	}
	public String getNomeUtente(){
		return nomeUtente;
	}
	
	public void setIdDataOra(String idDO){
		this.idDataOra = idDO;
	}

}
