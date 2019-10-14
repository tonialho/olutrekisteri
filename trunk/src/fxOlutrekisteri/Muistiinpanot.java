package fxOlutrekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Toni Alho
 * @version 26.4.2017
 */
public class Muistiinpanot implements Iterable<Muistiinpano> {
	
	private String tiedostonPerusNimi = "muistiinpanot";	
	
	/** Taulukko muistiinpanoista */
	private final Collection<Muistiinpano> alkiot = new ArrayList<Muistiinpano>();
	
	
// =================== OLIOIDEN KÄSITTELY ===================
	/**
	 * Tyhjä
	 */
	public Muistiinpanot() {
		//
	}
	
	
	/**
	 * Lisää muistiinpanon Collectioniin
	 * @param eka lisättävä muistiinpano
	 */
	public void lisaa(Muistiinpano eka) {
		eka.annaId();
		// eka.testiArvot(eka.getOlutId());
		alkiot.add(eka);
	}
	
	
	/**
	 * Poistaa muistiinpanon Collectionista
	 * @param mp poistettava muistiinpano
	 */
	public void poista(Muistiinpano mp) {
	    alkiot.remove(mp);
	}
	
	
	/**
	 * Hakee muistiinpanojen suurimman id:n
	 * @return suurin id
	 */
	public int getSuurinId() {
	    int suurin = 0;
	    
	    for(Muistiinpano mp : alkiot) {
	        if(mp.getId() > suurin)
	            suurin = mp.getId();
	    }
	    
	    return suurin;
	}

	
	/**
	 * Iteraattori muistiinpanojen läpikäymiseen
	 * @return muistiinpanoiteraattori
	 */
	@Override
	public Iterator<Muistiinpano> iterator() {
		return alkiot.iterator();
	}
	
	
	/**
	 * Hakee tietyn oluen kaikki muistiinpanot
	 * @param id oluen id
	 * @return oluen muistiinpanot
	 */
	public List<Muistiinpano> haeMuistiinpanot(int id) {
		List<Muistiinpano> loydetyt = new ArrayList<Muistiinpano>();
		for (Muistiinpano mp : alkiot)
			if(mp.getOlutId() == id) loydetyt.add(mp);
		return loydetyt;
		
	}
	
	
// =================== GETTERIT ===================
	/**
	 * Palauttaa muistiinpanojen lukumäärän
	 * @return muistiinpanojen lkm
	 */
	public int getLkm() {
		return alkiot.size();
	}
	
	
	/**
	 * Palauttaa tietyn oluen kaikki muistiinpanot String muodossa
	 * @param olutId olut
	 * @return Muistiinpanot String muodossa
	 */
	public String getMuistiinpanot(int olutId) {
		List<Muistiinpano> mpt = haeMuistiinpanot(olutId);
		StringBuilder muistiinpanot = new StringBuilder("");
		
		for (Muistiinpano mp : mpt) {
			muistiinpanot.append(mp.getMuistiinpano());
		}
		
		return muistiinpanot.toString();
	}
	
	
	/**
	 * Palauttaa tietyn muistiinpanon
	 * @param id muistiinpanon id
	 * @return muistiinpano
	 */
	public Muistiinpano getMuistiinpano(int id){
	    Muistiinpano muistiinpano = null;
	    for(Muistiinpano mp : alkiot) 
	        if(mp.getId() == id)
	            muistiinpano = mp;
	    return muistiinpano;
	}
	
// =================== TIEDOSTON KÄSITTELY ===================
	/**
	 * Lukee muistiinpanot tiedostosta
	 * @param tied tiedosto
	 * @throws SailoException exception
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonPerusNimi(tied);
		try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
			String rivi;
			while ((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ("".equals(rivi) || rivi.charAt(0) == ';') continue;
				Muistiinpano mp = new Muistiinpano();
				mp.parse(rivi);
				lisaa(mp);
			}
		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException("Ongelmia tiedoston kanssa: "  + e.getMessage());
		}
	}
	
	
	/**
	 * Lukee tiedostosta
	 * @throws SailoException exception
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonPerusNimi());
	}
	
	
	/**
	 * Tallentaa muistiinpanot tidostoon
	 * @throws SailoException exception
	 */
	public void tallenna() throws SailoException {
		// if (!muutettu) return;
		
        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Muistiinpano mp : this) {
                fo.println(mp.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
	}
	
	
	/**
	 * Asettaa tiedoston perusnimen
	 * @param tied tiedoston nimi
	 */
	public void setTiedostonPerusNimi(String tied) {
		tiedostonPerusNimi = tied;
	}
	
	
	/**
	 * Palauttaa tiedoston nimen
	 * @return tiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return tiedostonPerusNimi;
	}
	
	
	/**
	 * Palauttaa tiedoston nimen
	 * @return tiedoston nimi
	 */
	public String getTiedostonNimi() {
		return tiedostonPerusNimi + ".dat";
	}
	
	
	/**
	 * Palauttaa varmuuskopiotiedoston nimen
	 * @return tiedoston nimi
	 */
	public String getBakNimi() {
		return tiedostonPerusNimi + ".bak";
	}
	
	
// =================== TESTAUS ===================
	/**
	 * Testipääohjelma
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Muistiinpanot mpt = new Muistiinpanot();
		Muistiinpano eka = new Muistiinpano(0);
		mpt.lisaa(eka);
		Muistiinpano toka = new Muistiinpano(0);
		mpt.lisaa(toka);
		
		System.out.println("TEST TEST TEST");
		List<Muistiinpano> mpt2 = mpt.haeMuistiinpanot(0);
		
		for (Muistiinpano mp : mpt2) {
			System.out.println(mp.getOlutId() + " ");
			mp.tulosta(System.out);
		}
	}
}
