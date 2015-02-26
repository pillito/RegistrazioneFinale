package it.IS.client.Layouts;

import it.IS.client.OrganizerService;
import it.IS.client.OrganizerServiceAsync;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class CreaEventoLayout extends Composite implements ClickHandler{
	
	private OrganizerServiceAsync organizerService = GWT.create(OrganizerService.class);
	private ClickHandler handler = this;
	private VerticalPanel vP, panelEsterno;
	private FlexTable tablePrincipal, fTInterna;
	private Button aggiungiDataB, aggiungiOraB, salvaB, annullaB;
	private DateTimeFormat dateFormat;
	private DateBox dateBox;
	private ListBox listBox;
	private ArrayList<String> dataList;
	ArrayList<ArrayList<String>> oraList; // l'elemento in posizione 0 corrisponde alla data in posizione 0 di 'dataList' e così via.
	
	// costruttore della classe
	public CreaEventoLayout(){
		panelEsterno = new VerticalPanel();
		panelEsterno.setWidth("52em");
		
		// tabella contenente la data e gli orari.
		fTInterna = new FlexTable();
		aggiungiRiga(fTInterna);

		aggiungiDataB = new Button("Nuova Data");
		aggiungiDataB.addStyleName("aggiungiDataButton"); // CSS .gwt-Button-aggiungiDataButton
		aggiungiDataB.addClickHandler(handler);
		
		vP = new VerticalPanel();
		annullaB = new Button("Annulla");
		annullaB.setSize("80px", "25px");
		annullaB.addStyleName("AnnullaEventoButton");
		annullaB.addClickHandler(handler);
		salvaB = new Button("Salva");
		salvaB.setSize("80px", "25px");
		salvaB.addStyleName("SalvaEventoButton");
		salvaB.addClickHandler(handler);
		vP.setSpacing(5);
		vP.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		vP.add(salvaB);
		vP.add(annullaB);
	
		tablePrincipal = new FlexTable();
		FlexCellFormatter cellFormatterTP = tablePrincipal.getFlexCellFormatter();
	    tablePrincipal.addStyleName("flexTablePrincipal-creaEventoLayout"); // CSS .flexTablePrincipal td
	    tablePrincipal.setWidth("52m"); //520px
	    tablePrincipal.setCellSpacing(5);
	    tablePrincipal.setCellPadding(3);
	    // effettuo dei cambiamenti ad alcune singole celle
	    cellFormatterTP.setVerticalAlignment(1, 3, HasVerticalAlignment.ALIGN_TOP);
	    cellFormatterTP.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	    cellFormatterTP.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	    cellFormatterTP.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	    cellFormatterTP.setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	    cellFormatterTP.setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	    cellFormatterTP.setVerticalAlignment(4, 1, HasVerticalAlignment.ALIGN_TOP);

	    // Aggiungo gli elementi alla tabella.
	    tablePrincipal.setHTML(0, 1, "<b><font color='black' size='4'>Crea un nuovo evento </font></b>");
	    tablePrincipal.setHTML(1, 0, "<b><font face='verdana'> Titolo </font></b>");
	    tablePrincipal.setHTML(2, 0, "<b><font face='verdana'> Luogo dove si svolge </font></b>");
	    tablePrincipal.setHTML(3, 0, "<b><font face='verdana'> Descrizione </font></b>");
	    tablePrincipal.setHTML(5, 0, "<br><b><font face='verdana'> Scegliere la data e gli orari </font></b></br>" +
	    				"<br><font color='red'>Aggiungere un'altra data solo dopo aver inserito gli orari della precedente</font></br>");
	    tablePrincipal.setWidget(1, 1, new TextBox());
	    tablePrincipal.setWidget(2, 1, new TextBox());
	    tablePrincipal.setWidget(3, 1, new RichTextArea());
	    tablePrincipal.setWidget(4, 1, aggiungiDataB);
	    tablePrincipal.setWidget(5, 1, fTInterna);
	    tablePrincipal.setWidget(1, 3, vP);

	    panelEsterno.add(tablePrincipal);
	    initWidget(panelEsterno);
	}
	
	/*
	 * Metodi vari
	 */
	// metodo utilizzato per l'aggiunta delle date
	private void aggiungiRiga(FlexTable ft){
		int rows = ft.getRowCount();
		dateFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
		dateBox = new DateBox();
		dateBox.setValue(new Date()); // per default viene inserita la data odierna
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		aggiungiOraB = new Button("+ ora");
		aggiungiOraB.setSize("50px", "25px");
		aggiungiOraB.addStyleName("aggiungiOraButton"); // CSS .gwt-Button-aggiungiOraButton/ Finestra poup
		aggiungiOraB.addClickHandler(handler);
       	aggiungiOraB.addStyleName("aggiungiOraButton"); // CSS .gwt-Button-aggiungiOraButton
				
		ft.setWidget(rows, 0, dateBox);
		ft.setWidget(rows, 1, aggiungiOraB);	
	}
	// aggiungo gli orari in FTInterna
	private void aggiungiColonna(FlexTable ft){
		listBox = new ListBox();
	    // estraggo gli orari per comboBox
	    for(int h = 00; h < 24; h++){
	    	int m = 00;
	    	while( m < 56){
	    		listBox.addItem(Integer.toString(h) + ":" + Integer.toString(m));
	    		m = m+05;
	    	}    		
	    }
	    listBox.setSelectedIndex(0);
	    listBox.addClickHandler(handler);
		
		int row = ft.getRowCount()-1;
		int columns = ft.getCellCount(row);
		ft.setWidget(row,columns, listBox);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		Button control = (Button)event.getSource();
		if(control == aggiungiDataB ){
			aggiungiRiga(fTInterna);
		}
		if(control == aggiungiOraB){
			// aggiungo una colonna alla volta
			aggiungiColonna(fTInterna);
		}
		if(control == annullaB){
			fTInterna.removeAllRows();
			aggiungiRiga(fTInterna);
			tablePrincipal.setWidget(1, 1, new TextBox());
		    tablePrincipal.setWidget(2, 1, new TextBox());
		    tablePrincipal.setWidget(3, 1, new RichTextArea());
		    tablePrincipal.setWidget(5, 1, fTInterna);
		}
		if(control == salvaB){
			// Estraggo tutti i dati!
			String titoloEvento = ((TextBox)tablePrincipal.getWidget(1,1)).getText();
			String luogoEvento = ((TextBox)tablePrincipal.getWidget(2,1)).getText();
			String descrizioneEvento = ((RichTextArea)tablePrincipal.getWidget(3,1)).getText();
			
			DateTimeFormat fmt = DateTimeFormat.getFormat("dd/MM/yyyy");
			dataList = new ArrayList<String>(); // contiene le date
			oraList = new ArrayList<ArrayList<String>>(); // ogni posizione contiene una lista di orari corrispondente ad una singola data
			ArrayList <String> orariList = new ArrayList<String>(); // contiene gli orari riferiti ad una sola data
			
			// estraggo le date con i loro rispettivi orari
			for(int i = 0; i < fTInterna.getRowCount(); i++){
				Date d = ((DateBox)fTInterna.getWidget(i, 0)).getValue();
				String dataStringa = fmt.format(d);
				// controllo che la lista delle date non contenga doppi
				if((dataList.isEmpty() &&  fTInterna.getCellCount(i) >= 3) || (!dataList.contains(dataStringa) && 3 <= fTInterna.getCellCount(i))){
						dataList.add(dataStringa); // inserisco la data dentro alla lista
						orariList = new ArrayList<String>(); // creo una lista che conterrà gli orari per 'dataStringa'
						// estraggo gli orari per quella data
						for(int y = 2; y < fTInterna.getCellCount(i); y++){
							ListBox lB = (ListBox)fTInterna.getWidget(i, y);
							String oraInput = lB.getValue(lB.getSelectedIndex()); // estraggo l'orario delezionato
							// controllo che la lista degli orari per una sola data non contenga doppi
							if(orariList.isEmpty() || !orariList.contains(oraInput)){
								orariList.add(oraInput);
							}
							// non verranno prese in considerazione gli orari uguali per ogni data
							lB.clear();
						}
						if(!orariList.isEmpty()){
							oraList.add(orariList); // aggiungo la lista ad un contenitore.
						}
					}
				else{
					// ripulisco le liste perchè verranno riempite al prossimo ciclo for!
					dataList.clear();
					oraList.clear();				
					break;
				}
				
			}
			// in caso che nel for non ci sia stata l'aggiunta corretta delle informazioni alle varie liste o il tiolo dell'evento è vuoto
			if(dataList.isEmpty() && oraList.isEmpty() || titoloEvento.isEmpty()){
				if(!titoloEvento.isEmpty()){
					PopupPanel ppP = new PopupPanel(true);
					ppP.setWidget(new HTML("<br><font color='red' size='5'><b> Problemi nel inserimento delle date</font></b></br>" +
							"<br><font color=red> Reinserire le date con almeno un orario, non sono ammesse date ripetute.</font></br>"));
					ppP.center();
					ppP.show();
					fTInterna.removeAllRows();
					aggiungiRiga(fTInterna);
					tablePrincipal.setWidget(5, 1, fTInterna);
				}
				else{
					PopupPanel ppP2 = new PopupPanel(true);
					ppP2.setWidget(new HTML("<b><font color='red' size='5'>Inserire un titolo all'evento</font></b>"));
					ppP2.center();
					ppP2.show();
				}
				
			}
			else{
				
				// tutto viene eseguito correttamente inserisco dentro al DB.
				organizerService.creaEvento( titoloEvento, luogoEvento, descrizioneEvento, dataList, oraList,
						new AsyncCallback<Boolean>(){
						@Override
							public void onFailure(Throwable caught) {
								PopupPanel popup = new PopupPanel(true);
								popup.setWidget(new HTML("<font color='red'>Impossibile inserire l'evento: Errore nella connessione con il server</font>"));
								popup.center();					
							}
							@Override
							public void onSuccess(Boolean result) {
								if(result == true){
									PopupPanel popup = new PopupPanel(true);
									popup.setWidget(new HTML("<font color='green' size='5'><b>Evento inserito correttamente</b></font>"));
									popup.center();
								}
							}

						});
				
				//resetto tutto allo stato iniziale dovrei inizializzare pure gli arrayList
				fTInterna.removeAllRows();
				aggiungiRiga(fTInterna);
				tablePrincipal.setWidget(1, 1, new TextBox());
				tablePrincipal.setWidget(2, 1, new TextBox());
				tablePrincipal.setWidget(3, 1, new RichTextArea());
				tablePrincipal.setWidget(5, 1, fTInterna);
			  }
			}
		}
}
	

