package it.IS.client.Layouts;

/*------>		ALEJANDRO!!!		<-------
 * Per ALE:
 * 
 * Questa classe devi richiamarla quando una volta che selezioni l'evento:
 * 		Immagino che estrarrai le informazioni di quell'evento e le metterai nel pannello accanto!
 * 		Tra queste informazioni l'idEvento glielo passi quando richiamerai il layout commento:
 * 	--- new CommentoLayout(idEvento);
 * 
 * Cosi verrà visualizzato i commenti per quel particolare evento se l'utente non è loggato o visualizzerà tutto se è loggato .
 * 
 * 
 * 	Per ora ho messo che se ci sono eventi inseriti estragga solo i commenti per il primo evento inserito in assoluto
 *	e quindi se inserirai un nuovo commento, questò riguarderà sempre il primo evento inserito!
 * 
 * 		
 * 
 * NOTA:
 * Se non gli passi l'id al costruttore della classe ti darà errore!!
 * MIRACCOMANDO
 * 
 */
import it.IS.client.OrganizerService;
import it.IS.client.OrganizerServiceAsync;
import it.IS.shared.Strutture.Commento;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentaLayout extends Composite {
	
	private OrganizerServiceAsync organizerService = GWT.create(OrganizerService.class);
	private FlexTable fT; // contiene tutti i commenti.
	private VerticalPanel vP; // pannello principale
	private HorizontalPanel hP;
	private RichTextArea commentoRTA;
	private Button commentaButton; // solo l'utente registrato può commentare.
	private final String idEvento;
	
	public CommentaLayout(String id){
		this.idEvento = id;
		
		vP = new VerticalPanel();
		vP.setSpacing(3);
		
		commentoRTA = new RichTextArea();
		commentoRTA.setSize("600px", "100px");
		commentaButton = new Button("Invia");
		commentaButton.setSize("80px", "25px");
		commentaButton.addStyleName("commentaButton");
		
		hP = new HorizontalPanel();
		hP.setSpacing(5);
				
		fT = new FlexTable();
		fT.setWidth("600px");
		fT.setCellSpacing(5);
		aggiungiCommenti(idEvento);
		
		
		// verifico che l'utente sia registrato per poter inserire un commento, altrimenti si può solo visualizzare
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
							// l'utente non è loggato quindi può solo visualizzare i commenti.
							hP.clear();
							vP.add(new HTML("<b><font face='verdana'> Commenti:</font></b>"));
							vP.add(hP);
							vP.add(fT);
						}
						else{
							hP.add(commentoRTA);
							hP.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
							hP.add(commentaButton);
							vP.add(new HTML("<b><font face='verdana'> Inserisci un nuovo commento:</font></b>"));
							vP.add(hP);
							vP.add(fT);
						}
				}
				});
				
		initWidget(vP);
		
		
		commentaButton.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				String commentoS = commentoRTA.getText();
				// controllo che il commento non sia vuoto
				if(!commentoS.isEmpty()){
					// inserisco il commento dentro al DB
					organizerService.creaCommento(commentoS, idEvento, new AsyncCallback<Commento>(){
						@Override
						public void onFailure(Throwable caught) {
							PopupPanel popup = new PopupPanel(true);
							popup.setWidget(new HTML("<font color='red'>Impossibile inserire un nuovo commento: Errore nella connessione con il server</font>"));
							popup.center();	
							commentoRTA.setText("");
						}

						@Override
						public void onSuccess(Commento result) {
								if(result != null){
									// visualizzo il commento appena aggiunto
									int row = fT.getRowCount();
									// tolgo "non ci sono commenti" se aggiungo uno nuovo.
									if(fT.getCellCount(0) < 2){ fT.removeRow(0); }
									
									fT.setHTML(row, 0, "<b>"+ result.getUsernameCommentatore() +"<b>");
									fT.setHTML(row, 1, result.getMessaggio());
									commentoRTA.setText("");
								}
								else{
									// commento non inserito.
									commentoRTA.setText("");
								}
							}	
					});
					
				}				
				}
		});
		
	}
	public void aggiungiCommenti(String evId){
		organizerService.getCommenti(evId, new AsyncCallback<Commento[]>(){
			@Override
			public void onFailure(Throwable caught) {
				PopupPanel popup = new PopupPanel(true);
				popup.setWidget(new HTML("<font color='red'>Impossibile estrarre i commenti: Errore nella connessione con il server</font>"));		
			}

			@Override
			public void onSuccess(Commento[] result) {
				if(result.length == 0){
					fT.setHTML(0, 0, " Non ci sono commenti");
				}
				else{
					// ripulisco la tabella e aggiungo tutti i commenti.
					fT.clear();
					for(Commento c: result){
						int row = fT.getRowCount();
						fT.setHTML(row, 0, "<b>"+ c.getUsernameCommentatore() +"</b>");
						fT.setHTML(row, 1, c.getMessaggio());
					}
				}
			}
		});
	}
}
