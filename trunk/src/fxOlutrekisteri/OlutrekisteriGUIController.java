package fxOlutrekisteri;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import fxOlutrekisteri.SailoException;
import fxOlutrekisteri.Rekisteri;
import fxOlutrekisteri.Olut;
import fxOlutrekisteri.Muistiinpano;

/**
 * @author Toni Alho
 * @version 17.5.2017
 */
public class OlutrekisteriGUIController implements Initializable{
	
	@FXML private ComboBoxChooser<String> cbKentat;
	@FXML private Label labelVirhe;
	@FXML private ScrollPane panelOlut;
	@FXML private ListChooser<Olut> chooserOluet;
	@FXML private GridPane gridOlut;
	
	private Olut valinta = new Olut();
	private URL apuaURL;
	
	
	/**
	 * Initializer
	 * @param url url
	 * @param bundle bundle
	 */
	@Override
    public void initialize(URL url, ResourceBundle bundle) {
		alusta();
	}
	
	
// =================== UI HANDLERIT ===================
	/**
	 * Tallentaa oluen
	 */
	@FXML private void handleTallennaOlut() throws NumberFormatException {
		uusiOlut();
	}
	
	
	/**
	 * Poistaa oluen
	 */
	@FXML private void handlePoista() {
		poistaOlut();
	}

	
	/**
	 * Tallentaa muistiinpanon
	 */
	@FXML private void handleTallennaMuistiinpano() {
		uusiMuistiinpano();
	}
	
	
	/**
	 * Poistaa muistiinpanon
	 */
	@FXML private void handlePoistaMuistiinpano() {
	    poistaMuistiinpano();
	}
	
	
	/**
	 * Näyttää seuraavan muistiinpanon
	 */
	@FXML private void handleSeuraavaMuistiinpano() {
	    seuraavaMuistiinpano();
	}
	
	
	/**
	 * Olutlistaus-näkymän handler
	 */
	@FXML private void handleChooser() {
	    oluenMuistiinpanot.clear();
	    haeMuistiinpanot();
	    taytaKentat();
	}
	
	
	/**
	 * Hakupalkin handler
	 */
	@FXML private void handleHaku() {
	    hae(textHaku.getText());
	}

	
	/**
	 * Tulostaa oluen tiedot
	 */
	@FXML private void handleTulosta() {
		TulostusController tulostusCtrl = TulostusController.tulosta(null);
		tulostaValitut(tulostusCtrl.getTextArea()); 
	}
	

	/**
	 * Tallentaa ja sulkee ohjelman
	 */
	@FXML private void handleLopeta() {
		tallenna();
		Platform.exit();
	}
	
	
	/**
	 * Avaa apua -ikkunan
	 */
	@FXML private void handleApua() throws IOException{
	    apuaURL = new URL("https://tim.jyu.fi/view/kurssit/tie/ohj2/2017k/ht/tomaolal");
	    avaaApua(apuaURL);
	}
	
	
	/**
	 * Avaa ohjelman tietoja -ikkunan
	 */
	@FXML private void handleTietoja() {
		ModalController.showModal(OlutrekisteriGUIController.class.getResource("TietojaView.fxml"), "Olutrekisteri", null, "");
	}

    
// =============== =============== =============== =============== 	
// ================ EI VARSINAISTA KÄYTTÖLIITTYMÄÄ =============== 	
// =============== =============== =============== =============== 
	private Rekisteri rek;
	private String reknimi = "olutrekisteri";
	private Olut olutKohdalla;
	private TextArea areaOlut = new TextArea();
	private Muistiinpano aktiivinenMuistiinpano; // Esillä oleva muistiinpano
	private static int aktiivinenMpIndex = 0; // Aktiivisen muistiinpanon indeksi oluenMuistiinpanot listassa
	private List<Muistiinpano> oluenMuistiinpanot = new ArrayList<Muistiinpano>(); // Valitun oluen kaikki muistiinpanot
	
	// ===== Oluen tiedot kentät =====
	@FXML private TextField textNimi;
	@FXML private TextField textPanimo;
	@FXML private TextField textTyyli;
	@FXML private TextField textAlc;
	@FXML private TextField textEbu;
	@FXML private TextField textMaa;
	
	
	// ===== Muistiinpanot kentät ======
	@FXML private TextField textPvm;
	@FXML private TextField textHinta;
	@FXML private TextField textPaikka;
	@FXML private TextField textPisteet;
	@FXML private TextField textKommentti;
	
	
	// ========= Hakukenttä =======
	@FXML private TextField textHaku;
	
	
    /**
     * Tekee tarvittavat muut alustukset
     */
    protected void alusta() {    	
        chooserOluet.clear();
        chooserOluet.addSelectionListener(e -> naytaOlut());
    }
    
    
	private void setTitle(String title) {
		ModalController.getStage(chooserOluet).setTitle(title);
	}
	
	
	private static void avaaApua(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	private static void avaaApua(URL url) {
	    try {
	        avaaApua(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
	
	

    
// =============== TIEDOSTOJEN KÄSITTELY =============== 	
	/**
	 * Alustaa rekisterin lukemalla sen tiedostosta
	 * @param nimi minkä nimisestä tiedostosta luetaan
	 * @return null jos onnistuu
	 */
	protected String lueTiedosto(String nimi) {
		reknimi = nimi;
		setTitle("Rekisteri - " + reknimi);
		try {
			rek.lueTiedostosta(nimi);
			hae(0);
			return null;
		} catch (SailoException e) {
			hae(0);
			String virhe = e.getMessage();
			if (virhe != null) Dialogs.showMessageDialog(virhe);
			return virhe;
		}
	}
	
	
	/**
	 * Avaa tiedoston
	 * @return true jos onnistui
	 */
	public boolean avaa() {
		String uusiNimi = reknimi;
		if (uusiNimi == null) return false;
		lueTiedosto(uusiNimi);
		return true;
	}
	
	
	/**
	 * Tallentaa tiedot
	 * @return null jos onnistuu
	 */
	private String tallenna() {
		try {
			rek.tallenna();
			return null;
		} catch (SailoException e) {
			Dialogs.showMessageDialog("Tallennuksessa ongelma " + e.getMessage());
			return e.getMessage();
		}
	}
	
	
	/**
	 * Tarkastaa onko tallennettu
	 * @return true jos on
	 */
	public boolean voikoSulkea() {
		tallenna();
		return true;
	}
	
	
// =============== OLIOIDEN KÄSITTELY =============== 
	/**
	 * Näyttää valitun oluen tiedot
	 */
	protected void naytaOlut() {
		olutKohdalla = chooserOluet.getSelectedObject();
		
		if(olutKohdalla == null) {
			areaOlut.clear();
			return;
		}
		
		areaOlut.setText("");
		try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaOlut)) {
			tulosta(os, olutKohdalla);
		}
	}
	
	
	/**
	 * Hakee oluita hakusanalla 
	 * @param hakusana hakusana
	 */
	protected void hae(String hakusana) {
	    if (hakusana.equals("")) {
	        alusta();
	        for (int i = 0; i < rek.getOluidenLkm(); i++) {
	            Olut olut = rek.annaOlut(i);
	            chooserOluet.add(olut.getNimi(), olut);
	        }   
	    } 
	    
	    else {
	        chooserOluet.clear();
		
	        for(int i = 0; i < rek.getOluidenLkm(); i++) {
	            Olut olut = rek.annaOlut(i);
	            if (olut.getNimi().contains(hakusana)) 
	                chooserOluet.add(olut.getNimi(), olut);
	        }
	    }
	}
	
	
	/**
	 * Tuo valitun oluen tiedot tekstikenttiin
	 */
	private void taytaKentat() throws NullPointerException {
	    textNimi.setText(chooserOluet.getSelectedObject().getNimi());
	    textPanimo.setText(chooserOluet.getSelectedObject().getPanimo());
	    textTyyli.setText(chooserOluet.getSelectedObject().getTyyli());
	    textAlc.setText(Double.toString(chooserOluet.getSelectedObject().getAlc()));
	    textEbu.setText(Integer.toString(chooserOluet.getSelectedObject().getEbu()));
	    textMaa.setText(chooserOluet.getSelectedObject().getMaa());
	    try{
    	    if(aktiivinenMuistiinpano.getPvm() != " ") {
    	        textPvm.setText(aktiivinenMuistiinpano.getPvm());
    	        textHinta.setText(Double.toString(aktiivinenMuistiinpano.getHinta()));
    	        textPaikka.setText(aktiivinenMuistiinpano.getPaikka());
    	        textPisteet.setText(Double.toString(aktiivinenMuistiinpano.getPisteet()));
    	        textKommentti.setText(aktiivinenMuistiinpano.getKommentti());
    	    }
    	    else{
    	        textPvm.clear();
    	        textHinta.clear();
    	        textPaikka.clear();
    	        textPisteet.clear();
    	        textKommentti.clear();
    	    }
	    } catch (NullPointerException e) {
	        Dialogs.showMessageDialog("Virhe kenttien täytössä " + e.getMessage());
	    }
	}
	
	
	/**
	 * Hakee tietyn oluen kaikki muistiinpanot
	 * @throws NullPointerException poikkeus
	 */
	public void haeMuistiinpanot() throws NullPointerException {
	    Muistiinpano mp;
	    try{
	        aktiivinenMuistiinpano = null;
	        aktiivinenMpIndex = 0;
	        oluenMuistiinpanot.clear();
    	    for(int i = 0; i <= rek.getSuurinId(); i++) {
    	        if(rek.getMuistiinpano(i) == null)
    	            i = i+0;
    	        else {
    	            mp = rek.getMuistiinpano(i);
    	            if(mp.getOlutId() == olutKohdalla.getId()) {
    	                oluenMuistiinpanot.add(mp);
    	            }
    	        }
    	    }
    	    if(oluenMuistiinpanot.size() == 0)
    	        aktiivinenMuistiinpano = new Muistiinpano(); 
    	    else
    	        aktiivinenMuistiinpano = oluenMuistiinpanot.get(0);
	    } catch (NullPointerException e) {
	        Dialogs.showMessageDialog("Ongelma muistiinpanon hakemisessa " + e.getMessage());
	    } catch (IndexOutOfBoundsException e) {
	        Dialogs.showMessageDialog("Oluesta ei muistiinpanoja " + e.getMessage());
	    }
	}
	
	
	/**
	 * Hakee oluen tiedostosta luettaessa
	 * @param id id
	 */
    protected void hae(int id) {
        chooserOluet.clear();
        int index = 0;
        Olut oluet[] = rek.jarjesta();
        for (int i = 0; i < rek.getOluidenLkm(); i++) {
            Olut olut = oluet[i];
            if (olut.getId() == id) index = i;
            chooserOluet.add(olut.getNimi(), olut);
        }
        
        chooserOluet.getSelectionModel().select(index);
    }

	
	/**
	 * Luo uuden oluen ja lisää sen rekisteriin tai muokkaa olemassa olevaa olutta
	 */
	public void uusiOlut() {
	    if(tarkastaKentat(1) == -1) {
	        Dialogs.showMessageDialog("Tarkasta kentät");
	        return;
	    }	    
		Olut olut = new Olut();
		olut.annaNimi(lueKentta(1));
		if(chooserOluet.getSelectedObject() != null && olut.getNimi().equals(chooserOluet.getSelectedObject().getNimi()) ) {
		      for(int i = olut.getEkaKentta(); i < olut.getKenttia(); i++){
		            String kentta = lueKentta(i);
		            chooserOluet.getSelectedObject().annaArvot(i, kentta);
		        }
		}
		
		else {
		    olut.annaId();
		    for(int i = olut.getEkaKentta(); i < olut.getKenttia(); i++){
		        String kentta = lueKentta(i);
		        olut.annaArvot(i, kentta);
		    }
		
		    try{
		        rek.lisaa(olut);
		    } catch (SailoException e) {
		        Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
		        return; 
		    }
		}
		hae(olut.getId());
	}
	
	
	/**
	 * Poistaa oluen
	 * @throws NullPointerException exception
	 */
	public void poistaOlut() throws NullPointerException {
	    Olut poistettava = chooserOluet.getSelectedObject();
	    try { 
	        alusta();
	        rek.poista(poistettava);
	        for (int i = 0; i < rek.getOluidenLkm(); i++) {
	            Olut olut = rek.annaOlut(i);
	            chooserOluet.add(olut.getNimi(), olut);
	        }
	    }
	    catch (NullPointerException e) {
	        Dialogs.showMessageDialog("Ongelma rekisterin muuttamisessa " + e.getMessage());
	    }
	    hae(0);
	}
	
	
	/**
	 * Lukee kenttien sisällön
	 * @param i minkä kentän
	 * @return kentän sisältö
	 */
	public String lueKentta(int i) {
		switch(i){
			case 1: return textNimi.getText();
			case 2: return textPanimo.getText();
			case 3: return textTyyli.getText();
			case 4: return textAlc.getText();
			case 5: return textEbu.getText();
			case 6: return textMaa.getText();
			case 7: return textPvm.getText();
			case 8: return textHinta.getText();
			case 9: return textPaikka.getText();
			case 10: return textPisteet.getText();
			case 11: return textKommentti.getText();
			default: return "ei palautettava";
		}
	}
	
	
	/**
	 * Tarkastaa kenttien sisällön oikeellisuuden
	 * @param i 1 jos tarkastetaan oluen kenttiä
	 * 2 jos tarkastetaan muistiinpanon kenttiä
	 * @return 1 jos ei virheitä
	 * -1 tai -2 jos virheitä
	 */
	private int tarkastaKentat(int i) throws NumberFormatException{
	    int onkoVirhe = 1;
	    switch(i) {
    	    case 1: {
    	        try{ 
        	        if(Double.parseDouble(textAlc.getText()) < 0 || Double.parseDouble(textAlc.getText()) > 100) {
            	        onkoVirhe = -1;
            	        textAlc.setStyle("-fx-text-inner-color: red;");
            	    }
            	    else
            	        textAlc.setStyle("-fx-text-inner-color: black");
            	    
        	        if(textEbu.getText() == "") {
        	            Dialogs.showMessageDialog("Anna EBUt");
        	        }
        	        else if(Integer.parseInt(textEbu.getText()) < 0) {
                        onkoVirhe = -1;
                        textEbu.setStyle("-fx-text-inner-color: red;");
                    }
                    else
                        textEbu.setStyle("-fx-text-inner-color: black");     
    	        } catch (NumberFormatException e) {
    	            onkoVirhe = -1;
    	        }
                    
    	    }
    	    break;
    	    case 2: {
    	        try{
                    if(textPvm.getText().length() > 10 || textPvm.getText().length() < 8) {
                        onkoVirhe = -2;
                        textPvm.setStyle("-fx-text-inner-color: red;");
                    }
                    else
                        textPvm.setStyle("-fx-text-inner-color: black");
                    
                    if(Double.parseDouble(textHinta.getText()) < 0 || Double.parseDouble(textHinta.getText()) > 100) {
                        onkoVirhe = -2;
                        textHinta.setStyle("-fx-text-inner-color: red;");
                    }
                    else
                        textHinta.setStyle("-fx-text-inner-color: black");
                    
                    if(Double.parseDouble(textPisteet.getText()) < 0 || Double.parseDouble(textPisteet.getText()) > 100) {
                        onkoVirhe = -2;
                        textPisteet.setStyle("-fx-text-inner-color: red;");
                    }
                    else
                        textPisteet.setStyle("-fx-text-inner-color: black");
    	        } catch (NumberFormatException e) {
    	            onkoVirhe = -2;
    	        }
    	    }
            break;
    	    default: 
    	        onkoVirhe = 1;
	    }
        return onkoVirhe;
	}
	
	
	/**
	 * Luo uuden muistiinpanon ja lisää sen rekisteriin
	 */
	public void uusiMuistiinpano() {
		if(valinta == null) return;
		if(tarkastaKentat(2) == -2) {
		    Dialogs.showMessageDialog("Tarkasta kentät");
		    return;
		}
		Muistiinpano eka = new Muistiinpano(chooserOluet.getSelectedObject().getId());
		for (int i = 7; i < 12; i++) {
			String kentta = lueKentta(i);
			eka.annaArvot(i, kentta);
		}
		rek.lisaa(eka);
	}
	
	
	/**
	 * Poistaa näkyvillä olevan muistiinpanon
	 */
	public void poistaMuistiinpano() {
	    if(aktiivinenMuistiinpano == null) return;

        rek.poistaMuistiinpano(aktiivinenMuistiinpano);	    
        haeMuistiinpanot();
        taytaKentat();
	}
	
	
	/**
	 * Hakee seuraavan muistiinpanon ja tuo sen tekstikenttiin
	 * @throws IndexOutOfBoundsException poikkeus
	 */
	public void seuraavaMuistiinpano() throws IndexOutOfBoundsException {
	    if (oluenMuistiinpanot.size() == 0)
	        Dialogs.showMessageDialog("Ei muistiinpanoja");
	    else{
    	    try{
    	        aktiivinenMpIndex++;
    	        aktiivinenMuistiinpano = oluenMuistiinpanot.get(aktiivinenMpIndex % oluenMuistiinpanot.size());
    	        taytaKentat();
    	    } catch (IndexOutOfBoundsException e) {
    	        Dialogs.showMessageDialog("Seuraavan muistiinpanon haku ei onnistu " + e.getMessage());
    	    }
	    }
	}
	
	
	/**
	 * Valitsee käytettävän rekisteri-olion
	 * @param rek rekisteri
	 */
	public void setRekisteri(Rekisteri rek) {
		this.rek = rek;
		naytaOlut();
	}
	
	
// =============== TULOSTUS =============== 	
	/**
	 * Tulostaa oluen tiedot
	 * @param os tietovirta johon tulostetaan
	 * @param olut tulostettava olut
	 */
	public void tulosta(PrintStream os, final Olut olut) {
		os.println("-------");
		olut.tulosta(os);
		os.println("-------");
		List<Muistiinpano> mpt = rek.annaMuistiinpanot(olut.getId());
		for (Muistiinpano mp : mpt)
			mp.tulosta(os);	
	}
	
	
	/**
	 * Tulostaa listassa olevat oluet tulostuskenttään
	 * @param text alue johon tulostetaan
	 */
	public void tulostaValitut(TextArea text) {
		Olut olut = chooserOluet.getSelectedObject();
		try(PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
			if(chooserOluet.getSelectedObject().getNimi() != "null") {
				os.println(olut.getOlut());
				List<Muistiinpano> mpt = rek.annaMuistiinpanot(olut.getId());
				for (Muistiinpano mp : mpt)
					mp.tulosta(os);	
			}
			else os.print("Ei valittuja oluita");
		}
	} 
	
}
