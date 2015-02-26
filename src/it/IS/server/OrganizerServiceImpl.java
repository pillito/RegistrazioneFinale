package it.IS.server;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Fun;
import org.mapdb.Fun.Tuple2;

import it.IS.client.OrganizerService;
import it.IS.shared.Strutture.Commento;
import it.IS.shared.Strutture.Data_Ora_Evento;
import it.IS.shared.Strutture.Evento;
import it.IS.shared.Strutture.Utente;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */

public class OrganizerServiceImpl extends RemoteServiceServlet implements OrganizerService {

	private static final long serialVersionUID = -441975747605549419L;

	@Override
	public boolean registrazione(String nomeI, String usernameI, String passwordI, String emailI){
		
		// Escape data from the client to avoid cross-site script vulnerabilities.
		nomeI = escapeHtml(nomeI);
		usernameI = escapeHtml(usernameI);
		passwordI = escapeHtml(passwordI);
		emailI = escapeHtml(emailI);
		Tuple2<String, String> chiaveMapLogin = new Tuple2<String, String>(usernameI, passwordI); // key di mapLogin
		
		DB db = DBMaker.newFileDB(new File("OrganizerDB")).closeOnJvmShutdown().make();
		ConcurrentNavigableMap<String, Utente> mapUtenti = db.getTreeMap(MapKey.MappaUtenti.toString());
		ConcurrentNavigableMap<Fun.Tuple2<String, String>, String> mapLogin = db.getTreeMap(MapKey.MappaLogin.toString());
		
		// controllo che mapLogin sia vuoto o non contenga la chiave per inserire un nuovo utente
		if(mapLogin.isEmpty() || !mapLogin.containsKey(chiaveMapLogin)){
			String idUtente = UUID.randomUUID().toString();
			Utente nuovoUtente = new Utente(idUtente, nomeI, usernameI, passwordI, emailI);
			
			mapUtenti.put(idUtente, nuovoUtente);
			mapLogin.put(chiaveMapLogin, idUtente);
			
			db.commit();
			db.close();
			creaSessione(idUtente);
			return true; // registrazione inserita
		}
		else{
			db.close();
			return false; // registrazione non avvenuta
		}
	}
	
	@Override
	
	public String effettuaLogin(String username, String password){
		// Escape data from the client to avoid cross-site script vulnerabilities.
		username= escapeHtml(username);
		password= escapeHtml(password);
		
		Tuple2<String, String> chiaveMapLogin = new Tuple2<String, String>(username, password);
		
		DB db = DBMaker.newFileDB(new File("OrganizerDB")).closeOnJvmShutdown().make();
		//ConcurrentNavigableMap<String, Utente> mapUtenti = db.getTreeMap(MapKey.MappaUtenti.toString());
		ConcurrentNavigableMap<Fun.Tuple2<String, String>, String> mapLogin = db.getTreeMap(MapKey.MappaLogin.toString());
		
// i seguenti metodi ripuliscono tutto, perci� permettono un solo inserimento-----------
		//mapUtenti.clear();
		//mapLogin.clear();
		 
//---------------------------------------------------------------------------
		if(mapLogin.containsKey(chiaveMapLogin)){
			String utente = mapLogin.get(chiaveMapLogin);
			String risposta = creaSessione(utente);
			db.close();
			return risposta;
		}
		else{
			db.close();
			return null;
		}	
		
	}
	
	@Override
	/*
	 * return --> l'id dell'utente loggato altrimenti null.
	 */
	public String verificaLogin() {
		HttpServletRequest request = this.getThreadLocalRequest();
		//non creare una nuova -> false
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("idUtente") == null)
			return null;
		// sessione e idUtente disponibili, l'utente sembra loggato correttamente.
		else 
			return session.getAttribute("idUtente").toString();
		
	}

	@Override
	public boolean effettuaLogout() {
		HttpServletRequest request = this.getThreadLocalRequest();
		// non creare una nuova sessione -> false
		HttpSession session = request.getSession(false);
		if (session == null){
			return false; // la sessione e' vuota
		} else {
			session.invalidate();
			return true;
		}
	}

	@Override
	public Boolean creaEvento(String titolo, String luogo, String descrizione,
		ArrayList<String> date, ArrayList<ArrayList<String>> orari) {

		titolo = escapeHtml(titolo);
		luogo = escapeHtml(luogo);
		descrizione = escapeHtml(descrizione);
		for(String d: date){
			d = escapeHtml(d);
		}
		for(ArrayList<String> o: orari){
			for(String oS: o){
				oS = escapeHtml(oS);
			}
		}
		
		DB db = DBMaker.newFileDB(new File("OrganizerDB")).closeOnJvmShutdown().make();
		ConcurrentNavigableMap<String, Evento> mapEventi = db.getTreeMap(MapKey.MappaEventi.toString());
		ConcurrentNavigableMap<String, Data_Ora_Evento> mapDataOra = db.getTreeMap(MapKey.MappaData_OraEventi.toString());

		if(verificaLogin() != null){
			int id = mapEventi.keySet().size()+1;
			String idEvento = Integer.toString(id);
			String idAdmin = verificaLogin();
			Evento evento = new Evento(idEvento, idAdmin, titolo, luogo, descrizione);

			mapEventi.put(idEvento, evento);
			
			ArrayList <String> contenitore = new ArrayList<String>();// conterr� gli orari riferiti a una singola data
			for(int i = 0; i < date.size(); i++){
				String data = date.get(i); // estraggo la data
				contenitore = orari.get(i);
				for(String ora: contenitore){
					//per ogni singola ora creiamo un nuovo Data_Ora_Evento, con un suo id.
					String idDOEvento = UUID.randomUUID().toString();
					Data_Ora_Evento dOEvento = new Data_Ora_Evento(idDOEvento, idEvento, data, ora);
					mapDataOra.put(idDOEvento, dOEvento);
				}
				// ripulisco i vari contenitori che saranno nuovamente riempiti ad ogni ciclo for pi� esterno
				contenitore.clear();
				data = "";
			}
			db.commit();
			db.close();
			return true; // evento inserito correttamente
		}
		else{
			db.close();
			return false; // evento non inserito nel db
		}
	}
	/*
	 * ho problemi nella lettura, nn so perch�.
	 */
	
	@Override
	public Evento[] getEventi() {
		
		DB db = DBMaker.newFileDB(new File("OrganizerDB")).closeOnJvmShutdown().make();
		ConcurrentNavigableMap<String, Evento> mapEventi = db.getTreeMap(MapKey.MappaEventi.toString());
		
		if(!mapEventi.isEmpty()){
			ArrayList<Evento> listaReturn =new ArrayList<Evento>();
			Set<String> key = mapEventi.keySet();
			for(String s: key){
			listaReturn.add(mapEventi.get(s));
		}
			db.close();
			return listaReturn.toArray(new Evento[0]); // lista piena
		
		} else{
			db.close();
			return new Evento[0]; // lista vuota
		}
	}

	/*
	 * Ho problemi di lettura.
	 */

	@Override
	public Evento[] getPropriEventi() {
		
		DB db = DBMaker.newFileDB(new File("OrganizerDB")).closeOnJvmShutdown().make();
		ConcurrentNavigableMap<String, Evento> mapEventi = db.getTreeMap(MapKey.MappaEventi.toString());
		
		if(!mapEventi.isEmpty() && verificaLogin() != null ){
			ArrayList<Evento> eventiTot = new ArrayList<Evento>();
			Set<String> key = mapEventi.keySet();
			for(String s: key){
				eventiTot.add(mapEventi.get(s));
			}
			ArrayList<Evento> eventiPropri = new ArrayList<Evento>();
			for(Evento ev: eventiTot){
				if(ev.getIdAmmnistratore().equals(verificaLogin())){
					eventiPropri.add(ev);
				}
			}
			if(!eventiPropri.isEmpty()){
				db.close();
				return eventiPropri.toArray(new Evento[0]);
			} else{
				db.close();
				return new Evento[0];
			}
			
		}
		else{
			db.close();
			return new Evento[0];
		}
	}
	
	@Override
	public Commento creaCommento(String testo, String idEvento) {
		testo = escapeHtml(testo);
		
		DB db = DBMaker.newFileDB(new File("OrganizerDB")).closeOnJvmShutdown().make();
		ConcurrentNavigableMap<String, Commento> mapCommenti = db.getTreeMap(MapKey.MappaCommenti.toString());
		ConcurrentNavigableMap<String, Utente> mapUtenti = db.getTreeMap(MapKey.MappaUtenti.toString());
		ConcurrentNavigableMap<String, Evento> mapEventi = db.getTreeMap(MapKey.MappaEventi.toString());
		
		Set<String> keyEventi = mapEventi.keySet();
		if(keyEventi.contains(idEvento)){
		int id = mapCommenti.keySet().size() + 1;
		String idCommento = Integer.toString(id);
		String username = ((Utente)mapUtenti.get(verificaLogin())).getUsername();
		Commento commento = new Commento(idCommento, idEvento, username, testo);
		
		mapCommenti.put(idCommento, commento);
		
		db.commit();
		db.close();
		
		return commento; // commento salvato
		}
		else{
			db.close();
			return null;
		}
	}
	
	@Override
	public Commento[] getCommenti(String evento) {
		DB db = DBMaker.newFileDB(new File("OrganizerDB")).closeOnJvmShutdown().make();
		ConcurrentNavigableMap<String, Commento> mapCommenti = db.getTreeMap(MapKey.MappaCommenti.toString());
		if(!mapCommenti.isEmpty()){
		// confronto l'idEvento con tutti i valori di mapCommenti, se presente l'aggiungo alla lista.
		ArrayList<Commento> listaCommenti = new ArrayList<Commento>();
		Collection<Commento> collectionC =  mapCommenti.values();
		for(Commento c: collectionC ){
			if(c.getIdEvento().equals(evento)){
				listaCommenti.add(c);
			}
		}
		if(!listaCommenti.isEmpty()){
			db.close();
			return listaCommenti.toArray(new Commento[0]);
		}
		else{
			db.close();
			return new Commento[0];
		}
		} else{
			db.close();
			return new Commento[0];
		}
	
	}
	
//-----------------------------------------------------------------------------------------------
//metodi della classe, non condivisi con il client ----------------------------------------------
	/*
	 * Controlla che la sessione esista gi� per l'utente, se si ritorna l'oggetto sessione, 
	 * altrimenti crea una nuova sessione (true) e ritorna  il nuovo oggetto sessione.
	 * return --> l'id dell'utente appena loggato.
	 */
	public String creaSessione(String idU) {
		HttpServletRequest request = this.getThreadLocalRequest();
		//creare una nuova sessione -> true
		HttpSession session = request.getSession(true);
		session.setAttribute("idUtente", idU);
		return session.getAttribute("idUtente").toString();
	}

	/**
	 * Verifica che non vi sia codice html all'interno dei testi
	 * recuperati dai campi di testo.
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
