package it.IS.client.Layouts;

/*
 * classe dove implemento gli oggetti dentro al Tab "I miei eventi". Essa conterrà:
 * pulsante per l'inserimento di eventi.
 * 
 * 
 */

import it.IS.client.OrganizerService;
import it.IS.client.OrganizerServiceAsync;
import it.IS.shared.Strutture.Evento;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EventoLayout extends Composite implements ClickHandler {

	private OrganizerServiceAsync organizerService = GWT.create(OrganizerService.class);
	private HorizontalPanel panelPrincipale;
	private VerticalPanel panelEventoSingolo, panelEventi;
	private Button creaEventoButton;
	private DialogBox dialogEvento;
	
	
	public EventoLayout(){
		
		panelPrincipale = new HorizontalPanel();
		panelPrincipale.setSpacing(3);
		
		creaEventoButton = new Button("Crea evento");
		creaEventoButton.setSize("90px", "25px");
		creaEventoButton.getElement().setId("creaEventoButton");
		creaEventoButton.addClickHandler(this);
		
		panelEventoSingolo = new VerticalPanel(); //  il panel con le info dell'evento, le disponibilità, e commenti
		panelEventoSingolo.add(new CommentaLayout("1")); // ottengo i commenti del primo evento che inserito se il db è vuoto.
		
		panelEventi = new VerticalPanel();// contiene il button add, e gli eventi presenti.
		panelEventi.setWidth("400px");
		panelEventi.setSpacing(2);
		aggiornaPanelEventi(); // riempo il panelEventi con il Button e gli eventi
		
		panelPrincipale.add(panelEventi);
		panelPrincipale.add(panelEventoSingolo);
		
		initWidget(panelPrincipale);
	}
	
	public void aggiornaPanelEventi(){
		panelEventi.clear();
		// riempo il panelEvento con i vari eventi se presenti nel database.
		organizerService.getPropriEventi( new AsyncCallback<Evento[]>(){
					@Override
					public void onFailure(Throwable caught) {
						PopupPanel popup = new PopupPanel(true);
						popup.setWidget(new HTML("<font color='red'>Impossibile ottenere i propri eventi : Errore nella connessione con il server</font>"));
						popup.center();	
					}
					@Override
					public void onSuccess(Evento[] result) {
						if(result.length == 0){
							panelEventi.add(creaEventoButton);
							panelEventi.setCellHorizontalAlignment(creaEventoButton, HasHorizontalAlignment.ALIGN_RIGHT);
							panelEventi.add(new HTML("<b>Non ci sono eventi</b>"));
					}
						else{
							panelEventi.add(creaEventoButton);
							panelEventi.setCellHorizontalAlignment(creaEventoButton, HasHorizontalAlignment.ALIGN_RIGHT);
							for(Evento e: result){
								panelEventi.add(new HTML(e.getNomeEvento()));
							}	
						}
					}	
				});
	}
	
	@Override
	public void onClick(ClickEvent event) {
		Button control = (Button)event.getSource();
	
		if(control == creaEventoButton){
			dialogEvento = new DialogBox();
			dialogEvento.setText("Organizer Service");
			final Button close = new Button("Chiudi");
			close.setSize("80px", "25px");
			VerticalPanel dialogVPanel = new VerticalPanel();
			dialogVPanel.add(new CreaEventoLayout());
			dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
			dialogVPanel.add(close);
			dialogEvento.setWidget(dialogVPanel);
			dialogEvento.center();
			close.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					dialogEvento.hide();
					// aggiorno il layout con i nuovi eventi.
					aggiornaPanelEventi();
					panelPrincipale.clear();
					panelPrincipale.add(panelEventi); // inserisco quello aggiornato
					panelPrincipale.add(panelEventoSingolo);
				}
			});
		}
	}
}
