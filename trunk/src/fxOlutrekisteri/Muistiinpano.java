package fxOlutrekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Toni Alho
 * @version 26.4.2017
 */
public class Muistiinpano {
	private int id;
	private int olutId;
	private String pvm = " ";
	private String paikka = " ";
	private double hinta = 0;
	private double pisteet = 0;
	private String kommentti = " ";
	
	private static int seuraavaId = 1;
	
	
// ================ OLION KÄSITTELY ================ 
	/**
	 * Alustus
	 */
	public Muistiinpano() {
		//
	}
	
	
	/**
	 * Muistiinpanon alustus tietylle oluelle
	 * @param olutId Olut jolle muistiinpano osoitetaan
	 */
	public Muistiinpano(int olutId){
		this.olutId = olutId;
	}
	
	
	/**
	 * Antaa muistiinpanoille arvot tekstikentistä
	 * @param i kenttä
	 * @param arvo kentän sisältö
	 */
	public void annaArvot(int i, String arvo) {
		switch(i) {
			case 7: pvm = arvo;
				break;
			case 8: hinta = Double.parseDouble(arvo);
				break;
			case 9: paikka = arvo;
				break;
			case 10: pisteet = Double.parseDouble(arvo);
				break;
			case 11: kommentti = arvo;
				break;
			default: break;
		}
	}
	
	
	/**
	 * Palauttaa muistiinpanolle annettavan id:n 
	 * @return id
	 */
	public int annaId() {
		id = seuraavaId;
		seuraavaId++;
		return id;
	}
	
	
	/**
	 * Asettaa id:n 
	 * @param id id
	 */
	public void setId(int id) {
		this.id = id;
		if (id >= seuraavaId) seuraavaId = id;
	}
	
	
    /**
     * Parsesi
     * @param rivi käsiteltävä rivi
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setId (Mjonot.erota(sb, '|', id));
        olutId = Mjonot.erota(sb, '|', olutId);
        pvm = Mjonot.erota(sb, '|', pvm);
        paikka = Mjonot.erota(sb, '|', paikka);
        hinta = Mjonot.erota(sb, '|', hinta);
        pisteet = Mjonot.erota(sb, '|', pisteet);
        kommentti = Mjonot.erota(sb, '|', kommentti);
    }	
    
    
    @Override
    public String toString() {
    	return "" + getId() + "|" + olutId + "|" + pvm + "|" + paikka + "|" + hinta + "|" + pisteet + "|" + kommentti; 
    }
    
    
    @Override
    public boolean equals(Object o) {
    	if (o == null) return false;
    	return this.toString().equals(o.toString());
    }
	
    
    @Override
    public int hashCode() {
    	return id;
    }
    
	
// ================= GETTERIT ================= 
	/**
	 * Palauttaa muistiinpanon id:n
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	
	/**
	 * Palauttaa muistiinpanoon liittyvän oluen id:n
	 * @return id
	 */
	public int getOlutId() {
		return olutId;
	}
	
	
	/**
	 * Palauttaa päivämäärän
	 * @return pvm
	 */
	public String getPvm() {
	    return pvm;
	}
	
	
	/**
	 * Palauttaa hinnan
	 * @return hinta
	 */
	public double getHinta() {
	    return hinta;
	}
	
	
	/**
	 * Palauttaa paikan
	 * @return paikka
	 */
	public String getPaikka() {
	    return paikka;
	}
	
	
	/**
	 * Palauttaa pisteet
	 * @return pisteet
	 */
	public double getPisteet() {
	    return pisteet;
	}
	
	/**
	 * Palauttaa kommentin
	 * @return kommentti
	 */
	public String getKommentti() {
	    return kommentti;
	}
	
	
	/**
	 * Muuttaa muistiinpanot String muotoon
	 * @return mp muistiinpanot String muodossa
	 */
	public String getMuistiinpano() {
		String mp = (pvm + "\n" + paikka + "\nHinta" + hinta + "€" + "\nPisteet: " + pisteet + " \nKommentti: " + kommentti);
		return mp;
	}
	

    /**
     * Asettaa muistiinpanon päivämääräksi välilyönnin
     */
    public void setTyhja() {
        this.pvm = " ";
        
    }
	
// ================= TULOSTUS ================= 
	/**
	 * Tulostaa muistiinpanon
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println("OlutID " + olutId + "\nMuistiinpanon ID: " + id + "\n" + pvm + "\n" + paikka + "\nHinta: " + hinta + "€" + "\nPisteet: " + pisteet + "\nKommentti: " + kommentti + "\n");		
	}
	
	
	/**
	 * Tulostaa oluen tiedot
	 * @param os tietovirja johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}

	
// ================= TESTAUS ================= 
	/**
	 * Testaamiseen testiarvoja muistiinpanolle
	 * @param olut olut
	 */
	public void testiArvot(int olut) {
		olutId = olut;
		pvm = "13.3.2017";
		paikka = "S-Market Kuokkala";
		hinta = 1.00;
		pisteet = 2;
		kommentti = "Hyvää";
	}
	
	
	/**
	 * Testipääohjelma
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Muistiinpano eka = new Muistiinpano(1);
		eka.annaId();
		eka.testiArvot(1);
		eka.tulosta(System.out);
		Muistiinpano toka = new Muistiinpano(2);
		toka.annaId();
		toka.testiArvot(2);
		toka.tulosta(System.out);		
	}

}