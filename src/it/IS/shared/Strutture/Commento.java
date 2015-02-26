package it.IS.shared.Strutture;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;


public class Commento implements Serializable, IsSerializable {
	
	private static final long serialVersionUID = 5083585598928965587L;
	private String idCommento;
	private String idEvento;
	private String usernameCommentatore;
	private String messaggio;
	
	public Commento(){}
	
	public Commento(String id, String evento, String username, String messaggio){
		this.idCommento = id;
		this.idEvento = evento;
		this.messaggio = messaggio;
		this.usernameCommentatore = username;
	}
	
	public String getIdCommento(){
		return idCommento;
	}
	public String getIdEvento(){
		return idEvento;
	}
	public String getUsernameCommentatore(){
		return usernameCommentatore;
	}
	public String getMessaggio(){
		return messaggio;
	}
}
