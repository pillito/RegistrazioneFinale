package it.IS.shared.Strutture;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Data_Ora_Evento implements Serializable,IsSerializable {
	
	private static final long serialVersionUID = -2459120993978525923L;
	private String idDOEvento;
	private String idEvento;
	private String data;
	private String ora;

	public Data_Ora_Evento(){}
	
	public Data_Ora_Evento(String id, String evento, String data, String ora){
		this.idDOEvento = id;
		this.idEvento = evento;
		this.data = data;
		this.ora = ora;
	}
	
	public String getIdData_Orario(){
		return idDOEvento;
	}
	public String getIdEvento(){
		return idEvento;
	}
	public String getData(){
		return data;
	}
	public String getOra(){
		return ora;
	}

}
