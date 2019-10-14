package fxOlutrekisteri;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Kontrolleri tulostuksille
 * 
 * @author Toni Alho
 * @version 24.3.2017
 */
public class TulostusController implements ModalControllerInterface<String> {
	@FXML TextArea tulostusAlue;	
	
	/**
	 * Sulkee tulosteikkunan
	 */
	@FXML private void handleOK() {
		ModalController.closeStage(tulostusAlue);
	}	
	
	
	/**
	 * @return tyhj‰
	 */
	@Override
    public String getResult() {
		return null;
	}
	
	
	/**
	 * @param oletus oletusteksti
	 */
	@Override
    public void setDefault(String oletus) {
		tulostusAlue.setText(oletus);
	}
	
	
	/**
	 * Ajetaan kun dialogi on n‰ytetty 
	 */
	@Override
    public void handleShown() {
		//
	}
	
	
	/**
	 * @return Alue, johon tulostetaan
	 */
	public TextArea getTextArea() {
		return tulostusAlue;
	}
	
	/**
	 * Tulostaa oluen tiedot ja muistiinpanot tulostusikkunaan
	 * @param tuloste Tulostettava teksti
	 * @return Kontrolleri, jolta voi pyyt‰‰ lis‰‰ tietoa
	 */
	public static TulostusController tulosta(String tuloste) {
		TulostusController tulostusCtrl = (TulostusController) ModalController.showModeless(TulostusController.class.getResource("TulostusView.fxml"), "Tulostus", tuloste);
		return tulostusCtrl;
	}

}