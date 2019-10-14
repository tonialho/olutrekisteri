package fxOlutrekisteri;

import java.io.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Rekisterin oluet -luokka
 * 
 * @author Toni Alho
 * @version 17.5.2017
 */
public class Oluet implements Iterable<Olut> {
	private int maxOluita = 10;
	private int lkm = 0;
	private String kokoNimi = "";
	private String tiedostonPerusNimi = "oluet";
	private Olut alkiot[] = new Olut[maxOluita];
	
	
	// ================ ITERAATTORI ================ 
	/**
	 * Luokka oluiden iteroimiseksi
	 */
	public class OluetIterator implements Iterator<Olut> {
		private int kohdalla = 0;
		
		
		/**
		 * Onko olemassa seuraavaa olutta
		 * @return tieto, onko seuraavaa olutta
		 */
		@Override
        public boolean hasNext() {
			return kohdalla < getLkm();
		}
		
		
		/**
		 * Palauttaa seuraavan oluen
		 * @return seuraava oluet
		 * @throws NoSuchElementException jos ei ole enempää oluita
		 */
		@Override
        public Olut next() throws NoSuchElementException {
			if (!hasNext()) throw new NoSuchElementException("Ei ole");
			return annaViite(kohdalla++);
		}
		
		
		/**
		 * Ilmoittaa jos tuhoaminen ei onnistunut
		 * @throws UnsupportedOperationException exception
		 */
		@Override
        public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Ei onnistunut");
		}
	}
	
	
	/**
	 * Palauttaa iteraattorin
	 */
	@Override
    public Iterator<Olut> iterator() {
		return new OluetIterator();
	}
	
	
	
	// ================ TIEDOSTON KÄSITTELY ================ 
	/**
	 * Lukee oluet tiedostosta
	 * @param tied tiedoston nimi
	 * @throws SailoException jos lukeminen epäonnistuu
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonPerusNimi(tied);
		try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
			String rivi = fi.readLine();
			
			while ((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if("".equals(rivi) || rivi.charAt(0) == ';') continue;
				Olut olut = new Olut();
				olut.parse(rivi);
				lisaaOlut(olut);
				olut.idPlus(olut.getId());
			}
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
	}	
	
	
	/**
	 * Tallentaa oluet tiedostoon
	 * @throws SailoException jos epäonnistuu
	 */
	public void tallenna() throws SailoException {
		
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete();
		ftied.renameTo(fbak);
		
		try(PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
		    fo.println(";");
			for (Olut olut : this) {
				fo.println(olut.toString());
			}
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelma");
		} catch (NullPointerException e) {
		    throw new SailoException("Toinen virhe oluet luokasta, mutta kaikki toimii " + ftied.getName());
		}
	}
	
	
	/**
	 * Lukee oluet tiedostosta
	 * @throws SailoException jos lukeminen epäonnistuu
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonPerusNimi());
	}
	
	
	/**
	 * Asettaa tiedoston perusnimen
	 * @param nimi tallunnustiedoston nimi
	 */
	public void setTiedostonPerusNimi(String nimi) {
		tiedostonPerusNimi = nimi;
	}
	
	
	/**
	 * Palauttaa rekistrin koko nimen
	 * @return rekistrin koko nimi
	 */
	public String getKokoNimi() {
		return kokoNimi;
	}
	
	
	/**
	 * Palauttaa tallennustiedoston nimen
	 * @return tiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return tiedostonPerusNimi;
	}
	
	
	/**
	 * Palauttaa tallennustiedoston nimen
	 * @return tiedoston nimi
	 */
	public String getTiedostonNimi() {
		return getTiedostonPerusNimi() + ".dat";
	}
	
	
	/**
	 * Palauttaa backupin nimen
	 * @return backupin nimi
	 */
	public String getBakNimi() {
		return tiedostonPerusNimi + ".bak";
	}
	
	
	// ================ OLUET-OLION KÄSITTELY ================ 
	/**
	 * Oletusmuodostaja
	 */
	public Oluet() {
		//
	}

	
	/**
	 * Lisää uuden jäsenen tietorakenteeseen ja ottaa sen omistukseensa
	 * @param olut lisättävän oluen viite
	 * @throws IndexOutOfBoundsException poikkeus
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Oluet oluet = new Olut();
     * Olut karhu1 = new Olut(), karhu2 = new Olut();
     * oluet.lisaaOlut(karhu1); oluet.getLkm() === 1;
     * oluet.lisaaOlut(karhu2); oluet.getLkm() === 2;
     * jasenet.annaViite(0) === karhu1;
     * jasenet.annaViite(1) === karhu2;
     * </pre>
     */
	public void lisaaOlut(Olut olut) {	
	    if (lkm >= alkiot.length)
	        alkiot = Arrays.copyOf(alkiot, lkm+20);
	    alkiot[lkm] = olut;
	    lkm++;
	}
	
	
	/**
	 * Poistaa oluen tietorakenteesta
	 * @param olut poistettava olut
	 */
	public void poistaOlut(Olut olut) {
	    int indeksi = etsiOluenIndeksi(olut.getId());
	    if (indeksi < 0) 
	        return;
        lkm--;
	    for(int i = indeksi; i < lkm; i++){
	        alkiot[i] = alkiot[i+1];
	    }
	    alkiot[lkm] = null;
	}
	
	
	/**
	 * Järjestää oluet aakkosjärjestykseen
	 * @return uusi taulukko, jossa oluet aakkosjärjestyksessä
	 */
	public Olut[] jarjesta() {
	    Olut oluet[] = new Olut[alkiot.length];
	    String nimet[] = new String[lkm];
	    
	    int j = 0;
	    for(Olut olut : alkiot) {
	        if(olut == null)
	            continue;
	        nimet[j] = olut.getNimi();
            j++;
	    }
	    
	    List<String> nimetList = Arrays.asList(nimet);	    
	    Collections.sort(nimetList, Collator.getInstance());
	    
	    for(int i = 0; i < nimetList.size(); i++) {
	        for(Olut olut : alkiot) {
	            if (olut == null) continue;
	            if (olut.getNimi().equals(nimetList.get(i)))
	                    oluet[i] = olut;
	        } 
	    }
	    return oluet;
	}
	
	/**
	 * Etsii oluen indeksin taulukossa
	 * @param id oluen id
	 * @return indeksi
	 */
	public int etsiOluenIndeksi(int id) {
	    for (int i = 0; i < lkm; i++)
	        if (id == alkiot[i].getId()) return i;
	    return -1;
	}
	
	
	/**
	 * Korvaa tai luo uuden oluen tietorakenteeseen
	 * @param olut lisättävä olut
	 * @throws SailoException jos tietorakenne on täynnä
	 */
	public void korvaaTaiLisaa(Olut olut) throws SailoException {
		int id = olut.getId();
		for(int i = 0; i < lkm; i++) {
			if(alkiot[i].getId() == id) {
				alkiot[i] = olut;
				return;
			}
		}
		lisaaOlut(olut);
	}
	
	
	
	// ================ HAKU-ALIOHJELMAT ================ 
	/**
	 * Palauttaa i:nnen oluen viitteen
	 * @param i monesko olut
	 * @return viite i:nteen olueen
	 * @throws IndexOutOfBoundsException jos i laiton arvo
	 */
	public Olut annaViite(int i) throws IndexOutOfBoundsException {
		if (i < 0 || lkm <= i) {
			throw new IndexOutOfBoundsException("Laiton indeksi");
		}
		return alkiot[i];
	}
	
	
	/**
	 * Palauttaa hakuehtoon vastaavien oluiden viitteet
	 * @return tietorakenne löytyneistä oluista
	 */
	public Collection<Olut> etsi() {
		Collection<Olut> loytyneet = new ArrayList<Olut>();
		for(Olut olut : this) {
			loytyneet.add(olut);
		}
		return loytyneet;
	}
		
	
	/**
	 * Palauttaa rekisterin oluiden lukumäärän
	 * @return oluiden määrä
	 */
	public int getLkm() {
		return lkm;
	}
	
	
	// ================ TESTAUS ================  
	/**
	 * Testipääohjelma
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Oluet oluet = new Oluet();
		
		Olut karhu = new Olut();
		karhu.annaId();
		karhu.testiArvotOluelle();
		Olut karhu2 = new Olut();
		karhu2.annaId();
		karhu2.testiArvotOluelle();
		
		oluet.lisaaOlut(karhu);
        oluet.lisaaOlut(karhu2);
        
        System.out.println("TEST TEST TEST TEST");
        
        for(int i = 0; i < oluet.getLkm(); i++) {
        	Olut olut = oluet.annaViite(i);
        	System.out.println("Olut nro: " + i);
        	olut.tulosta(System.out);
        }
	}

}
