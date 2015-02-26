package it.IS.client;

import it.IS.shared.Strutture.Commento;
import it.IS.shared.Strutture.Evento;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("organizer")
public interface OrganizerService extends RemoteService {
	
	boolean registrazione(String nome, String username, String password, String email);
	
	String effettuaLogin(String username, String password);
	
	boolean effettuaLogout();
	
	String verificaLogin();
	
	Boolean creaEvento(String titolo, String luogo, String descrizione, ArrayList<String> date, ArrayList<ArrayList<String>> orari);
	
	Evento[] getEventi();
	
	Commento creaCommento(String testo, String idEvento);
	
	Commento[] getCommenti(String eventoid);
	
	Evento[] getPropriEventi();
}
