/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Arco;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Double xGoals;
    	try {
    		xGoals = Double.parseDouble(this.txtGoals.getText());
    	}
    	catch(NumberFormatException e) {
    		this.txtResult.appendText("Numero medio di Goal inserito errato, inserisci un numero!!");
    		return;
    	}
    	
    	String result = this.model.creaGrafo(xGoals);

    	this.txtResult.appendText(result);
    	this.btnTopPlayer.setDisable(false);
    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Integer k;
    	try {
    		k = Integer.parseInt(this.txtK.getText());
    	}
    	catch(NumberFormatException e) {
    		this.txtResult.appendText("Devi inserire un Numero intero!!");
    		return;
    	}
    	
    	List<Player> result = this.model.getDreamTeam(k);
    	Integer titolarita = this.model.getTitolaritaBest();
    	
    	this.txtResult.appendText("DREAM TEAM TROVATO:\n\nTITOLARITA': "+titolarita+"\n");
    	for(Player p : result)
    		this.txtResult.appendText(p.toString()+"\n");
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	this.txtResult.clear();
    	
    	List<Arco> playerBattuti = this.model.getPlayerMigliore();
    	Collections.sort(playerBattuti);
    	
    	for(Arco a : playerBattuti) {
    		if(playerBattuti.get(0).equals(a))
    			this.txtResult.appendText(String.format("TOP PLAYER: %s\n\nAVVERSARI BATTUTI:\n", a.getSource().toString()));
    		else
    			this.txtResult.appendText(a.getDestination()+" | "+a.getPeso()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
