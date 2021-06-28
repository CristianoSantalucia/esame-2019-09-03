/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.ResourceBundle;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Portion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController
{
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtCalorie"
	private TextField txtCalorie; // Value injected by FXMLLoader

	@FXML // fx:id="txtPassi"
	private TextField txtPassi; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalisi"
	private Button btnAnalisi; // Value injected by FXMLLoader

	@FXML // fx:id="btnCorrelate"
	private Button btnCorrelate; // Value injected by FXMLLoader

	@FXML // fx:id="btnCammino"
	private Button btnCammino; // Value injected by FXMLLoader

	@FXML // fx:id="boxPorzioni"
	private ComboBox<Portion> boxPorzioni; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML void doCreaGrafo(ActionEvent event)
	{
		// controlli input
		Integer calories;
		try
		{
			calories = Integer.parseInt(this.txtCalorie.getText());
			if (calories <= 0) throw new NumberFormatException();
		}
		catch (NumberFormatException nfe)
		{
			this.txtResult.appendText("\n\nErrore, inserire un numero corretto");
			return;
		}

		// resetto testo
		this.txtResult.clear();
		this.txtResult.appendText("Creo grafo...\n");

		// creo grafo
		this.model.creaGrafo(calories);
		txtResult.appendText(String.format("\nGRAFO CREATO CON:\n#Vertici: %d\n#Archi: %d", this.model.getNumVertici(),
				this.model.getNumArchi()));

		// cliccabili
		this.boxPorzioni.setDisable(false);
		this.btnCorrelate.setDisable(false);
		this.btnCammino.setDisable(false);
		this.txtPassi.setDisable(false);

		// aggiungo risultati alla combobox
		this.boxPorzioni.getItems().clear();
		this.boxPorzioni.getItems().addAll(this.model.getVertici());
	}

	@FXML void doCorrelate(ActionEvent event)
	{
		Portion portion = this.boxPorzioni.getValue();
		if (portion == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		}

		txtResult.appendText("\n\nCORRELATE a " + portion + ":" + this.model.stampaConnessi(portion));
	}

	@FXML void doCammino(ActionEvent event)
	{
		// controlli input
		Integer passi;
		try
		{
			passi = Integer.parseInt(this.txtPassi.getText());
			if (passi <= 0) throw new NumberFormatException();
		}
		catch (NumberFormatException nfe)
		{
			this.txtResult.appendText("\n\nErrore, inserire un numero corretto");
			return;
		}
		
		Portion portion = this.boxPorzioni.getValue();
		if (portion == null)
		{
			this.txtResult.appendText("\n\nErrore, scegliere elemento dalla lista");
			return;
		}

		txtResult.appendText("\n\nCerco cammino peso massimo...\n" + this.model.cercaCamminio(portion, passi));
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize()
	{
		assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
		assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

	}

	public void setModel(Model model)
	{
		this.model = model;
	}
}
