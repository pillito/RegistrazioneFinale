package it.IS.client.Layouts;

import it.IS.client.OrganizerService;
import it.IS.client.OrganizerServiceAsync;
import it.IS.shared.FieldVerifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RegistraLayout extends Composite implements ClickHandler {
	
	private OrganizerServiceAsync organizerService = GWT.create(OrganizerService.class);
	private VerticalPanel panelPrincipale;
	private Grid gridPanel;
	private Button registraButton;
	private TextBox nomeTB, usernameTB, emailTB;
	private PasswordTextBox passwordTB;
	private Label erNomeL, erUsernameL, erPasswordL;
	
	public RegistraLayout(){
		panelPrincipale = new VerticalPanel();
		panelPrincipale.setSpacing(7);
		
		registraButton = new Button("Registra");
		registraButton.addStyleName("RegistraButton"); // .gwt-Button-RegistraButton
		registraButton.addClickHandler(this);
		registraButton.setSize("80px", "25px");
		nomeTB = new TextBox();
		nomeTB.setFocus(true);
		usernameTB = new TextBox();
		emailTB = new TextBox();
		passwordTB = new PasswordTextBox();
		erNomeL = new Label();
		erUsernameL = new Label();
		erPasswordL = new Label();
		
		gridPanel = new Grid(7, 2);
		gridPanel.setCellSpacing(5);
		CellFormatter cellFormatter = gridPanel.getCellFormatter();
		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
	    cellFormatter.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);
	    cellFormatter.setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_LEFT);
	    cellFormatter.setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_LEFT);
		gridPanel.setWidget(0, 0, new HTML("<b>Nome</b>"));
		gridPanel.setWidget(0, 1, nomeTB);
		gridPanel.setWidget(1, 0, new Label (""));
		gridPanel.setWidget(1, 1, erNomeL);
		gridPanel.setWidget(2, 0, new HTML("<b>Username</b>"));
		gridPanel.setWidget(2, 1, usernameTB);
		gridPanel.setWidget(3, 0, new Label (""));
		gridPanel.setWidget(3, 1, erUsernameL);
		gridPanel.setWidget(4, 0, new HTML("<b>Password</b>"));
		gridPanel.setWidget(4, 1, passwordTB);
		gridPanel.setWidget(5, 0, new Label(""));
		gridPanel.setWidget(5, 1, erPasswordL);
		gridPanel.setWidget(6, 0, new HTML("<b>Email</b>"));
		gridPanel.setWidget(6, 1, emailTB);
		
		panelPrincipale.add(gridPanel);
		panelPrincipale.add(registraButton);
		panelPrincipale.setCellHorizontalAlignment(registraButton, HasHorizontalAlignment.ALIGN_RIGHT);
		
		initWidget(panelPrincipale);
		
	}

	@Override
	public void onClick(ClickEvent event) {
		Button control = (Button)event.getSource();
		
		if(control == registraButton){
			String nome = nomeTB.getText();
			if(!FieldVerifier.isValidName(nome)){
				erNomeL.setText("Per favore inserire almeno 4 caratteri");
				return;
			}
			String username = usernameTB.getText();
			if(!FieldVerifier.isValidName(username)){
				erUsernameL.setText("Per favore inserire almeno 4 caratteri");
				return;
			}
			String password = passwordTB.getText();
			if(!FieldVerifier.controllaPassword(password)){
				erPasswordL.setText("Per favore inserire almeno 8 caratteri");
				return;
			}
			String email = emailTB.getText();
			
			organizerService.registrazione(nome, username, password, email,
					new AsyncCallback<Boolean>() {
					 public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						PopupPanel popup = new PopupPanel(true);
						popup.setWidget(new HTML("<font color='red'>Impossibile inserire una nuova registrazione: Errore nella connessione con il server</font>"));
						popup.center();
						
						nomeTB.setText("");
						gridPanel.setWidget(1, 0, new Label (""));
						usernameTB.setText("");
						gridPanel.setWidget(3, 0, new Label (""));
						passwordTB.setText("");
						gridPanel.setWidget(5, 0, new Label(""));
						emailTB.setText("");
						registraButton.setEnabled(false);
					 }
					 public void onSuccess(Boolean result) {
						if(result == false){
							PopupPanel popup = new PopupPanel(true);
							popup.setWidget(new HTML("Registrazione non riuscita, cambiare username"));
							popup.center();
							
							nomeTB.setText("");
							gridPanel.setWidget(1, 0, new Label (""));
							usernameTB.setText("");
							gridPanel.setWidget(3, 0, new Label (""));
							passwordTB.setText("");
							gridPanel.setWidget(5, 0, new Label(""));
							emailTB.setText("");
							registraButton.setEnabled(true);
							
						 } else{
							 PopupPanel popup = new PopupPanel(true);
							 popup.setWidget(new HTML("<font color='green'size='5'>Benvenuto in Organizer Service!</font>"));
							 popup.center();
							 
							 nomeTB.setText("");
							 gridPanel.setWidget(1, 0, new Label (""));
							 usernameTB.setText("");
							 gridPanel.setWidget(3, 0, new Label (""));
							 passwordTB.setText("");
							 gridPanel.setWidget(5, 0, new Label(""));
							 emailTB.setText("");
							 registraButton.setEnabled(false);
							 
						 }
					 }
				});
		}
		
	}
	
}
