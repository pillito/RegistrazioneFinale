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
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginLayout extends Composite implements ClickHandler{
	
	private OrganizerServiceAsync organizerService = GWT.create(OrganizerService.class);

	private VerticalPanel panelPrincipale;
	private Grid gridPanel;
	private Button loginButton;
	private TextBox usernameTB;
	private PasswordTextBox passwordTB;
	private Label erUsernameL, erPasswordL;
	
	public LoginLayout(){
		
		panelPrincipale = new VerticalPanel();
		panelPrincipale.setSpacing(7);
		
		loginButton = new Button("Sign In");
		loginButton.addStyleName("LoginButtonLayout"); // .gwt-Button-RegistraButton
		loginButton.addClickHandler(this);
		loginButton.setSize("80px", "25px");
	
		usernameTB = new TextBox();
		passwordTB = new PasswordTextBox();
		
		erUsernameL = new Label();
		erPasswordL = new Label();
		
		gridPanel = new Grid(4, 2);
		gridPanel.setCellSpacing(5);	
		CellFormatter cellFormatter = gridPanel.getCellFormatter();
		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
	   	cellFormatter.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);
	    
		gridPanel.setWidget(0, 0, new HTML("<b>Username</b>"));
		gridPanel.setWidget(0, 1, usernameTB);
		gridPanel.setWidget(1, 0, new Label (""));
		gridPanel.setWidget(1, 1, erUsernameL);
	
		gridPanel.setWidget(2, 0, new HTML("<b>Password</b>"));
		gridPanel.setWidget(2, 1, passwordTB);
		gridPanel.setWidget(3, 0, new Label(""));
		gridPanel.setWidget(3, 1, erPasswordL);
		
		panelPrincipale.add(gridPanel);
		panelPrincipale.add(loginButton);
		panelPrincipale.setCellHorizontalAlignment(loginButton, HasHorizontalAlignment.ALIGN_RIGHT);
		
		initWidget(panelPrincipale);
	}

	@Override
	public void onClick(ClickEvent event) {
		
		// TODO Auto-generated method stub
		Button control = (Button)event.getSource();
		if(control == loginButton){
			
			String username = usernameTB.getText();
			if(!FieldVerifier.isValidName(username)){
				erUsernameL.setText("Per favore inserire almeno 4 caratteri");
				return;
			}
			String password = passwordTB.getText();
			if(!FieldVerifier.controllaPassword(password)){
				erPasswordL.setText("La Password è composta da almeno 8 caratteri");
				return;
			}
			
			organizerService.effettuaLogin(username, password, new AsyncCallback<String>() {

				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					PopupPanel popup = new PopupPanel(true);
					popup.setWidget(new HTML("<font color='red'>Errore nella connessione con il server</font>"));
					popup.center();
					
					usernameTB.setText("");
					gridPanel.setWidget(3, 0, new Label (""));
					passwordTB.setText("");
					gridPanel.setWidget(5, 0, new Label(""));
					loginButton.setEnabled(false);
				 }
				 public void onSuccess(String result) {
					if(result == null){
						
						PopupPanel popup = new PopupPanel(true);
						popup.setWidget(new HTML("<font size='5'><b>Username o Password errate</b></font>"));
						popup.center();
						
						usernameTB.setText("");
						gridPanel.setWidget(3, 0, new Label (""));
						passwordTB.setText("");
						gridPanel.setWidget(5, 0, new Label(""));
						loginButton.setEnabled(false);
						
					 } else{
						 
						 PopupPanel popup = new PopupPanel(true);
							popup.setWidget(new HTML("<font color ='green' size='5'><b>Login effettuato</b></font>"));
							popup.center();
							
							usernameTB.setText("");
							gridPanel.setWidget(3, 0, new Label (""));
							passwordTB.setText("");
							gridPanel.setWidget(5, 0, new Label(""));
							loginButton.setEnabled(false);
					 }
				 }
			});
			
			
		}
		
	}

}


