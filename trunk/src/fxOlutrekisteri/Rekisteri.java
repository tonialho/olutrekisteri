package fxOlutrekisteri;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Ohjelman oluet ja tapahtumat -luokat kokoava luokka
 * @author Toni Alho
 * @version 26.4.2017
 */
public class Rekisteri {
	private Oluet oluet = new Oluet();
	private Muistiinpanot mpt = new Muistiinpanot();
	
	
	// ================= TIEDOSTOJEN KÄSITTELY =================
	/**
	 * Asettaa tiedostojen perusnimet
	 * @param nimi uusi nimi
	 */
	public void setTiedosto(String nimi) {
		File dir = new File(nimi);
		dir.mkdirs();
		String hakemistonNimi = "";
		if(!nimi.isEmpty()) hakemistonNimi = nimi + "/";
		oluet.setTiedostonPerusNimi(hakemistonNimi + "oluet");
		mpt.setTiedostonPerusNimi(hakemistonNimi + "muistiinpanot");
	}
	
	
	/**
	 * Lukee rekisterin tiedot tiedostosta
	 * @param nimi tiedoston nimi
	 * @throws SailoException jos lukeminen epäonnistuu
	 */
	public void lueTiedostosta(String nimi) throws SailoException {
		oluet = new Oluet();
		mpt = new Muistiinpanot();
		
		setTiedosto(nimi);
		oluet.lueTiedostosta();
		mpt.lueTiedostosta();
	}
	
	
	/**
	 * Tallentaa rekisterin tiedot tiedostoon
	 * @throws SailoException jos ongelmia
	 */
	public void tallenna() throws SailoException {
		String virhe = "";
		try {
			oluet.tallenna();
		} catch (SailoException e) {
			virhe = e.getMessage();
		}
		
		try {
			mpt.tallenna();
		} catch (SailoException e) {
			virhe += e.getMessage();
		}
		if (!"".equals(virhe)) throw new SailoException(virhe);
	}
	
	
	// ================= OLIOIDEN KÄSITTELY ================= 
	/**
	 * Lisää oluen
	 * @param olut lisättävä olut
	 * @throws SailoException jos lisäys epäonnistuu
	 */
	public void lisaa(Olut olut) throws SailoException {
		oluet.lisaaOlut(olut);
	}
	
	
	/**
	 * Poistaa oluen
	 * @param olut poistettava olut
	 */
	public void poista(Olut olut) {
	    oluet.poistaOlut(olut);
	}
	
	
	/**
	 * Palauttaa oluet taulukon aakkosjärjestyksessä
	 * @return taulukko aakkosjärjestyksessä
	 */
	public Olut[] jarjesta() {
	    return oluet.jarjesta();
	}
	
	
	/**
	 * Lisää muistiinpanot
	 * @param mp lisättävä muistiinpano
	 */
	public void lisaa(Muistiinpano mp) {
		mpt.lisaa(mp);
	}
	
	
	/**
	 * Poistaa poistiinpanon
	 * @param mp poistettava muistiinpano
	 */
	public void poistaMuistiinpano(Muistiinpano mp){
	    mpt.poista(mp);
	}
	
	
	/**
	 * Lisää tai korvaa oluen tietorakenteessa
	 * @param olut lisättävä olut
	 * @throws SailoException jos tietorakenne on täynnä
	 */
	public void korvaaTaiLisaa(Olut olut) throws SailoException {
		oluet.korvaaTaiLisaa(olut);
	}
	
	
	/**
	 * Palauttaa hakuehtoa vastaavien oluiden viitteet
	 * @return vastaavat oluet
	 * @throws SailoException jos ei onnistu
	 */
	public Collection<Olut> etsi() throws SailoException {
		return oluet.etsi();
	}
	
	
	/**
	 * Palauttaa i:nnen oluen viitteen
	 * @param i oluen viite
	 * @return i:nnen oluen viite
	 * @throws IndexOutOfBoundsException jos i:nnettä olutta ei löydy
	 * @example
	 * <pre name="test">
	 *  Rekisteri rek = new Rekisteri();
	 *  Olut karhu = new Olut();
	 *	karhu.annaId();
	 *	karhu.testiArvotOluelle();
 	 *	Olut karhu2 = new Olut();
	 *	karhu2.annaId();
	 *	karhu2.testiArvotOluelle();
	 *	
	 *	rek.lisaa(karhu);
	 *	rek.lisaa(karhu2);
	 *
	 *  annaOlut(karhu) === 1;
	 *  annaOlut(karhu2) === 2;
	 * </pre>
	 */
	public Olut annaOlut(int i) throws IndexOutOfBoundsException {
		return oluet.annaViite(i);
	}
	
	
	/**
	 * Palautaa kaikki oluen muistiinpanot
	 * @param id oluen id
	 * @return tietorakenne, jossa viitteet muistiinpanoihin
	 * @example
	 * <pre name="test">
	 *  Rekisteri rek = new Rekisteri();
	 *  Olut karhu = new Olut();
	 *	karhu.annaId();
	 *	karhu.testiArvotOluelle();
 	 *	Olut karhu2 = new Olut();
	 *	karhu2.annaId();
	 *	karhu2.testiArvotOluelle();
	 *	
	 *	rek.lisaa(karhu);
	 *	rek.lisaa(karhu2);
	 *
	 *  Muistiinpano mp1 = new Muistiinpano(0);
	 *	Muistiinpano mp2 = new Muistiinpano(0);
	 *	Muistiinpano mp3 = new Muistiinpano(0);
	 *	Muistiinpano mp4 = new Muistiinpano(1);
	 *	Muistiinpano mp5 = new Muistiinpano(1);
	 *
	 *  rek.lisaa(mp1);
	 *  rek.lisaa(mp2);
	 *  rek.lisaa(mp3);
	 *  rek.lisaa(mp4);
	 *  rek.lisaa(mp5);
	 *
	 *  List<Muistiinpano> loytyneet;
	 *  loytyneet = rek.annaMuistiinpanot(karhu);
	 *  loytyneet.size() === 3;
	 *  loytyneet = rek.annaMuistiinpanot(karhu2);
	 *  loytyneet.size() === 2;
	 * </pre>
	 */
	public List<Muistiinpano> annaMuistiinpanot(int id) {
		return mpt.haeMuistiinpanot(id);
	}
	
	
	// ================= GETTERIT ================= 
	/**
	 * Palauttaa rekisterin oluiden lukumäärän
	 * @return oluiden määrä
	 */
	public int getOluidenLkm() {
		return oluet.getLkm();
	}
	
	
	/**
	 * Palauttaa tietyn muistiinpanon
	 * @param id muistiinpanon id
	 * @return muistiinpano
	 */
	public Muistiinpano getMuistiinpano(int id) {
	    return mpt.getMuistiinpano(id);
	}
	
	
	/** 
	 * Palauttaa rekisterin muistiinpanojen lukumäärän
	 * @return muistiinpanojen määrä
	 */
	public int getMuistiinpanojenLkm() {
		return mpt.getLkm();
	}
	
	
	/**
	 * Hakee muistiinpanojen suurimman id:n
	 * @return suurin id
	 */
	public int getSuurinId() {
	    return mpt.getSuurinId();
	}
	
	
	// ================= TESTAUS ================= 
	/**
	 * Testipääohjelma
	 * @param args ei käytössä
	 */
	public static void main(String args[]) {
		Rekisteri rek = new Rekisteri();
		
		try{
			Olut karhu = new Olut();
			karhu.annaId();
			karhu.testiArvotOluelle();
			Olut karhu2 = new Olut();
			karhu2.annaId();
			karhu2.testiArvotOluelle();
			
			rek.lisaa(karhu);
			rek.lisaa(karhu2);
			
			Muistiinpano mp1 = new Muistiinpano(0);
			Muistiinpano mp2 = new Muistiinpano(0);
			Muistiinpano mp3 = new Muistiinpano(0);
			Muistiinpano mp4 = new Muistiinpano(1);
			Muistiinpano mp5 = new Muistiinpano(1);
			
			/*
			mp1.annaId();
			mp2.annaId();
			mp3.annaId();
			mp4.annaId();
			mp5.annaId();
			
			mp1.testiArvot(id1);
			mp2.testiArvot(id1);
			mp3.testiArvot(id2);
			mp4.testiArvot(id2);
			mp5.testiArvot(id1); */
			
			rek.lisaa(mp1);
			rek.lisaa(mp2);
			rek.lisaa(mp3);
			rek.lisaa(mp4);
			rek.lisaa(mp5);
			
			
			System.out.println("TEST TEST TEST TEST");
			
			for (int i = 0; i < rek.getOluidenLkm(); i++) {
				Olut olut = rek.annaOlut(i);
				System.out.println("Olut paikassa: " + i);
				olut.tulosta(System.out);	
				List<Muistiinpano> loytyneet = rek.annaMuistiinpanot(0);
				for(Muistiinpano mp : loytyneet)
					mp.tulosta(System.out);
			}
			
		} catch (SailoException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
