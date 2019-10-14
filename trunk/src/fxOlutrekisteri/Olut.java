package fxOlutrekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Olut-luokka
 * 
 * @author Toni Alho
 * @version 26.4.2017
 */
public class Olut {
	private int id = 0;
	private String nimi = "";
	private String panimo = "";
	private String tyyli = "";
	private double alc = 0.0;
	private int ebu = 0;
	private String maa = "";
	
	private static int seuraavaId = 1; 
	
	// =============== GETTERIT	===============
	/**
	 * Palauttaa oluen nimen
	 * @return oluen nimi
	 */
	public String getNimi() {
		return nimi;
	}
	
	
	/**
	 * Palauttaa oluen panimon
	 * @return panimo
	 */
	public String getPanimo() {
	    return panimo;
	}
	
	
	/**
	 * Palauttaa oluen tyylin
	 * @return oluen tyyli
	 */
	public String getTyyli() {
	    return tyyli;
	}
	
	
	/**
	 * Palauttaa oluen alkoholiprosentin
	 * @return alc%
	 */
	public double getAlc() {
	    return alc;
	}
	
	
	/**
	 * Palauttaa oluen EBU-lukeman
	 * @return EBU
	 */
	public int getEbu() {
	    return ebu;
	}
	
	
	/**
	 * Palauttaa oluen valmistusmaan
	 * @return maa
	 */
	public String getMaa() {
	    return maa;
	}
	
	/**
	 * Palauttaa oluen kaikki tiedot
	 * @return oluen tiedot String muodossa
	 */
	public String getOlut() {
		String olut = "Oluen id: " + id + "\n" + nimi + "\n" + tyyli + "\n" + panimo + ", " + 
					  maa + "\nAlc% " + alc + "\nEBU " + ebu + "\n";
		return olut;
	}
	
	
	/**
	 * Palauttaa oluen id:n
	 * @return oluen id
	 */
	public int getId() {
		return id;
	}
	
	
	/**
	 * Palauttaa oluen kenttien lukumäärän
	 * @return lkm
	 */
	public int getKenttia() {
		return 7;
	}
	
	
	/**
	 * Palauttaa ensimmäisen kysyttävän kentän
	 * @return kentän indeksi
	 */
	public int getEkaKentta() {
		return 1;
	}
	
	
	/**
	 * Antaa oluelle tiedot tekstikentistä
	 * @param i mikä kenttä
	 * @param arvo kentän arvo
	 */
	public void annaArvot(int i, String arvo) {
		switch(i) {
			case 0: id = Integer.parseInt(arvo); 
				break;
			case 1: nimi = arvo;
				break;
			case 2: panimo = arvo;
				break;
			case 3: tyyli = arvo;
				break;
			case 4: alc = Double.parseDouble(arvo);
				break;
			case 5: ebu = Integer.parseInt(arvo);
				break;
			case 6: maa = arvo;
				break;
			default: break;
		}
	}
	
	
	/**
	 * Palauttaa tietyn kentän sisällön merkkijonona
	 * @param kentta monennenko kentän sisältö palautetaan
	 * @return kentän sisältö
	 */
	public String getKentanSisalto(int kentta) {
		switch (kentta) {
			case 0: return "" + id;
			case 1: return nimi;
			case 2: return tyyli;
			case 3: return panimo;
			case 4: return "" + alc;
			case 5: return "" + ebu;
			case 6: return maa;
			default: return "ei palautettavaa";
			
		}
	}
	
	
	/**
	 * Antaa oluelle nimen
	 * @param nimi1 oluen nimi
	 */
	public void annaNimi(String nimi1) {
		this.nimi = nimi1;
	}
	
	
	/**
	 * Palautaa kentää koskevan kysymyksen
	 * @param kentta kenttä jonka kysymys haetaan
	 * @return kysymys
	 */
	public String getKysymys(int kentta) {
		switch (kentta) {
			case 0: return "id";        
			case 1: return "nimi";
	        case 2: return "panimo";
	        case 3: return "tyyli";
	        case 4: return "alc";
	        case 5: return "ebu";
	        case 6: return "maa";
	        default: return "vituiksmän";
		}
	}
	
	
	/**
	 * Lisää seuraavan uuden oluen id:tä yhdellä 
	 * @param id1 nykyinen viimeisin id
	 */
	public void idPlus(int id1) {
		if(id1 >= seuraavaId)
			seuraavaId = id1 + 1; 
	}
	
	
	// =============== TULOSTUS ===============
	/**
	 * Tulostaa oluen tiedot
	 * @param out tietovirta, johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(String.format("%03d", id, 3) + " " + nimi);
		out.println(tyyli);
		out.println(panimo + ", " + maa);
		out.println("Alc. " + alc + "%  EBU " + ebu + "\n");
	}
	
	
	/**
	 * Tulostaa oluen tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}
	
	
	// =============== OLUT-OLION KÄSITTELY ===============
	/**
	 * Oluen alustus 
	 */
	public Olut() {
		//
	}
	
	
	/**
	 * Lisää oluelle id:n ja laskee seuraavan id:n
	 * @return seuraava id
	 */
	public int annaId() {
		id = seuraavaId;
		seuraavaId++;
		return id;
	}
	
	
    /**
     * Antaa kentän sisällön merkkijonona
     * @param k minkä kentän sisältö
     * @return kenttä merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + id;
        case 1: return "" + nimi;
        case 2: return "" + panimo;
        case 3: return "" + tyyli;
        case 4: return "" + alc;
        case 5: return "" + ebu;
        case 6: return "" + maa;
        default: return "Äääliö";
        }
    }
	
	
	/**
	 * Asettaa tuodun merkkijonon tietyn kentän arvoksi 
	 * @param kentta monennenko kentän arvo asetetaan
	 * @param jono kenttään asetettava arvo
	 * @return null kun asettaminen onnistuu
	 */
	public String aseta(int kentta, String jono) {
		String tjono = jono.trim();
		switch (kentta) {
			case 0:
				id = Integer.parseInt(tjono);
				return null;
			case 1:
				nimi = tjono;
				return null;
			case 2:
				tyyli = tjono;
				return null;
			case 3:
				panimo = tjono;
				return null;
			case 4:
				alc = Double.parseDouble(tjono);
				return null;
			case 5:
				ebu = Integer.parseInt(tjono);
				return null;
			case 6:
				maa = tjono;
				return null;
			default: 
				return "Jotain meni pieleen";				
		}
	}
	
	
	/**
	 * Palauttaa oluen tiedot tiedostoon tallennettavassa muodossa
	 * @return oluen tiedot tolpilla eroteltuna
	 */
	@Override
    public String toString() {
		StringBuffer sb = new StringBuffer("");
		String erotin = "";
		for (int i = 0; i < getKenttia(); i++) {
			sb.append(erotin);
			sb.append(getKentanSisalto(i));
			erotin = "|";
		}
		return sb.toString();
	}
	
	
	/**
	 * Selventää oluen tiedot kun luetaan tiedostosta
	 * @param rivi rivi josta oluen tietoja luetaan
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		for (int i = 0; i < getKenttia(); i++) {
			aseta(i, Mjonot.erota(sb, '|'));
		}
	}
	
	
	// ============ TESTAUS ============	
	/**
	 * Testiarvot oluelle
	 */
	public void testiArvotOluelle() {
		nimi = "Karhu III";
		panimo = "Sinebrychoff";
		tyyli = "Vaalea Lager";
		alc = 4.7;
		ebu = 12;
		maa = "Suomi";
	}
	
	
	
	/**
	 * Pääohjelma testaamiseen
	 * @param args ei käytössä
	 */
	public static void main(String args[]) {
		Olut karhu = new Olut();
		karhu.annaId();
		karhu.testiArvotOluelle();
		karhu.tulosta(System.out);
		Olut karhu2 = new Olut();
		karhu2.annaId();
		karhu2.testiArvotOluelle();
		karhu2.tulosta(System.out);
	}
}
