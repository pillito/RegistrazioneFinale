package it.IS.shared.Strutture;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Utente implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = 1L;
	private String idUtente, nome, username, password, email;
	
	public Utente(){}
	
	public Utente(String id, String n, String u, String p, String e){
		this.idUtente = id;
		this.nome= n;
		this.username = u;
		this.password = p;
		this.email = e;
	}
	
	public String getIdUtente(){
		return idUtente;
	}
	public String getNome(){
		return nome;
	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
	public String getEmail(){
		return email;
	}
	public String toString(){
		return idUtente + " - " + nome + " - " + username + " - " + password + " - " + email;
	}
	
}
