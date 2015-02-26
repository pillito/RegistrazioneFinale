package it.IS.client;


import it.IS.client.Layouts.EventoLayout;
import it.IS.client.Layouts.LoginLayout;
import it.IS.client.Layouts.RegistraLayout;
import it.IS.shared.Strutture.Evento;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RegistrazioneFinale implements EntryPoint, ClickHandler {
	
	private static final int REFRESH_INTERVAL = 30000; // ms
	private OrganizerServiceAsync organizerService = GWT.create(OrganizerService.class);
	private VerticalPanel panelPrincipale, panelloEventi;
	private Button bLogin, bRegistra, bLogout;
	private TabPanel tP;
	private DialogBox dialogRegistra, dialogLogin;


	@Override
	public void onModuleLoad() {
				
		panelPrincipale = new VerticalPanel();
		panelPrincipale.setSpacing(7);
		
		final Grid panel1 = new Grid(1,3);
		panel1.setWidth("1320px");
		panel1.setStyleName("gridpanelButtons");
		bLogin = new Button("Login");
		bLogin.getElement().setId("loginButton");
		bLogin.addClickHandler(this);
		bRegistra = new Button("Registrati");
		bRegistra.getElement().setId("registraButton");
		bRegistra.addClickHandler(this);
		bLogout = new Button("Logout");
		bLogout.getElement().setId("logoutButton");
		bLogout.addClickHandler(this);
		bLogout.setEnabled(false);
		bLogin.setSize("80px", "25px");
		bRegistra.setSize("80px", "25px");
		bLogout.setSize("80px", "25px");
		panel1.setWidget(0, 0, bLogin);
		panel1.setWidget(0, 1, bRegistra);
		panel1.setWidget(0, 2, bLogout);
		panel1.getCellFormatter().setHorizontalAlignment(0, 1,HasHorizontalAlignment.ALIGN_CENTER);
		panel1.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);

		final DecoratorPanel decoratore = new DecoratorPanel();
		decoratore.setStyleName("decoratorePanel");
		decoratore.add(panel1);
		decoratore.setWidth("1320px");
		
		panelloEventi = new VerticalPanel(); 
		aggiorna();
		
		tP = new TabPanel();
		tP.add(panelloEventi, "Eventi");
		tP.add(new EventoLayout(), "I miei eventi");
		tP.add(new HTML(" Non c'e nulla!"), "Disponibilita");
		tP.selectTab(0);
		tP.getTabBar().setTabEnabled(1, false);
		tP.getTabBar().setTabEnabled(2, false);
		
		tP.getTabBar().setSize("80px", "25px");
		tP.setSize("1320px", "1000px");
	
		panelPrincipale.add(decoratore);
		panelPrincipale.add(tP);
		
		RootPanel.get("container").add(panelPrincipale);
		
		// imposto un timer per aggionrare la lista di eventi automaticamente.
	    Timer refreshTimer = new Timer() {
	      @Override
	      public void run() {
	        aggiorna();
	      }
	    };
	    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		Button control = (Button)event.getSource();
		
		if(control == bLogout){
			organizerService.effettuaLogout(
					new AsyncCallback<Boolean> (){
						@Override
						public void onFailure(Throwable caught) {
							PopupPanel popup = new PopupPanel(true);
							popup.setWidget(new HTML("<font color='red'>Impossibile effettuare il logout: Errore nella connessione con il server</font>"));
							popup.center();
						}

						@Override
						public void onSuccess(Boolean result) {
							if(result == true){
								aggiorna();
								tP.clear();
								tP.add(panelloEventi, "Eventi");
								tP.add(new EventoLayout(), "I miei eventi");
								tP.add(new HTML(" Qui vanno le diisponibilit� proprie"), "Disponibilita");
								tP.selectTab(0);
								tP.getTabBar().setTabEnabled(1, false);
								tP.getTabBar().setTabEnabled(2, false);
								tP.getTabBar().setSize("80px", "25px");
								tP.setSize("1320px", "1000px");
								panelPrincipale.remove(1);
								panelPrincipale.add(tP);
								
								bLogin.setEnabled(true);
								bRegistra.setEnabled(true);
								bLogout.setEnabled(false);
							}
						}
						
					});
			
		}
		
		if(control == bRegistra){
			dialogRegistra = new DialogBox();
			dialogRegistra.setText("Organizer Service Registrazione");
			dialogRegistra.setAnimationEnabled(true);
			final Button close = new Button("Chiudi");
			close.setSize("80px", "25px");
			VerticalPanel dialogVPanel = new VerticalPanel();
			dialogVPanel.add(new RegistraLayout()); // richiamo il pannello che gestisce la registrazione.
			dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
			dialogVPanel.add(close);
			dialogRegistra.setWidget(dialogVPanel);
			dialogRegistra.center();
			close.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					dialogRegistra.hide();
					// verifico che l'utente sia registrato
					organizerService.verificaLogin(new AsyncCallback<String>(){
						@Override
						public void onFailure(Throwable caught) {
							PopupPanel popup = new PopupPanel(true);
							popup.setWidget(new HTML("<font color='red'>Errore nella connessione con il server</font>"));
							popup.center();	
						}
						@Override
						public void onSuccess(String result) {
							if(result == null){
								aggiorna();
								tP.clear();
								tP.add(panelloEventi, "Eventi");
								tP.add(new EventoLayout(), "I miei eventi");
								tP.add(new HTML(" Qui vanno le disponibilit� proprie"), "Disponibilita");
								tP.selectTab(0);
								tP.getTabBar().setTabEnabled(1, false);
								tP.getTabBar().setTabEnabled(2, false);
								tP.getTabBar().setSize("80px", "25px");
								tP.setSize("1320px", "1000px");
								panelPrincipale.remove(1);
								panelPrincipale.add(tP);
								
								bLogout.setEnabled(false);
								bLogin.setEnabled(true);
								bRegistra.setEnabled(true);
							}
							else{
								aggiorna();
								tP.clear();
								tP.add(panelloEventi, "Eventi");
								tP.add(new EventoLayout(), "I miei eventi");
								tP.add(new HTML(" Qui vanno le disponibilit� proprie"), "Disponibilita");
								tP.getTabBar().setTabEnabled(1, true);
								tP.getTabBar().setTabEnabled(2, true);
								tP.selectTab(0);
								tP.getTabBar().setSize("80px", "25px");
								tP.setSize("1320px", "1000px");
								panelPrincipale.remove(1);
								panelPrincipale.add(tP);
								
								bLogout.setEnabled(true);
								bLogin.setEnabled(false);
								bRegistra.setEnabled(false);
							}
					}
					});
				}
			});
		}
		
		if(control == bLogin){
			dialogLogin = new DialogBox();
			dialogLogin.setText("Organizer Service Registrazione");
			dialogLogin.setAnimationEnabled(true);
			final Button close = new Button("Chiudi");
			close.setSize("80px", "25px");
			VerticalPanel dialogVPanel = new VerticalPanel();
			dialogVPanel.add(new LoginLayout()); // richiamo il pannello che gestisce il Login.
			dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
			dialogVPanel.add(close);
			dialogLogin.setWidget(dialogVPanel);
			dialogLogin.center();
			close.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					dialogLogin.hide();
					// verifico che l'utente � loggato
					organizerService.verificaLogin(new AsyncCallback<String>(){
						@Override
						public void onFailure(Throwable caught) {
							PopupPanel popup = new PopupPanel(true);
							popup.setWidget(new HTML("<font color='red'>Errore nella connessione con il server</font>"));
							popup.center();	
						}
						@Override
						public void onSuccess(String result) {
							if(result == null){
								aggiorna();
								tP.clear();
								tP.add(panelloEventi, "Eventi");
								tP.add(new EventoLayout(), "I miei eventi");
								tP.add(new HTML(" Qui vanno le disponibilit� proprie"), "Disponibilita");
								tP.selectTab(0);
								tP.getTabBar().setTabEnabled(1, false);
								tP.getTabBar().setTabEnabled(2, false);
								tP.getTabBar().setSize("80px", "25px");
								tP.setSize("1320px", "1000px");
								panelPrincipale.remove(1);
								panelPrincipale.add(tP);
								
								bLogout.setEnabled(false);
								bLogin.setEnabled(true);
								bRegistra.setEnabled(true);
							}
							else{
								// riporto tutto come prima
								aggiorna();
								tP.clear();
								tP.add(panelloEventi, "Eventi");
								tP.add(new EventoLayout(), "I miei eventi");
								tP.add(new HTML(" Qui vanno le disponibilit� proprie"), "Disponibilita");
								tP.getTabBar().setTabEnabled(1, true);
								tP.getTabBar().setTabEnabled(2, true);
								tP.selectTab(0);
								tP.getTabBar().setSize("80px", "25px");
								tP.setSize("1320px", "1000px");
								panelPrincipale.remove(1);
								panelPrincipale.add(tP);
								
								bLogout.setEnabled(true);
								bLogin.setEnabled(false);
								bRegistra.setEnabled(false);
							}
					}
					});
				}
			});
			
		}
	}
	// aggiorno il pannello contenente la lista degli eventi
	public void aggiorna(){
		panelloEventi.clear();
		// riempo il panelloEvento con i vari eventi se presenti nel database.
		organizerService.getEventi(new AsyncCallback<Evento[]>(){
			@Override
			public void onFailure(Throwable caught) {
				PopupPanel popup = new PopupPanel(true);
				popup.setWidget(new HTML("<font color='red'>Impossibile ottenere gli eventi: Errore nella connessione con il server</font>"));
				popup.center();	
			}
			@Override
				public void onSuccess(Evento[] result) {
					if(result.length == 0){
							panelloEventi.add(new HTML("<b>Non ci sono eventi</b>"));
					}
					else{
						for(Evento e: result){
							panelloEventi.add(new HTML(e.getNomeEvento()));
						}	
					}
				}
				
			});
	
	}
}
