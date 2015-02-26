package it.IS.client;

import it.IS.shared.Strutture.Commento;
import it.IS.shared.Strutture.Evento;
import java.util.ArrayList;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>OrganizerService</code>.
 */
public interface OrganizerServiceAsync {
	
	void registrazione(String nome, String username, String password,
			String email, AsyncCallback<Boolean> callback);

	void effettuaLogin(String username, String password,
			AsyncCallback<String> callback);

	void effettuaLogout(AsyncCallback<Boolean> callback);

	void verificaLogin(AsyncCallback<String> callback);

	void creaEvento(String titolo, String luogo, String descrizione,
			ArrayList<String> date, ArrayList<ArrayList<String>> orari,
			AsyncCallback<Boolean> callback);
	
	void getEventi(AsyncCallback<Evento[]> callback);
	
	void creaCommento(String testo, String idEvento,
			AsyncCallback<Commento> callback);

	void getCommenti(String eventoid, AsyncCallback<Commento[]> callback);

	void getPropriEventi(AsyncCallback<Evento[]> callback);	
}
