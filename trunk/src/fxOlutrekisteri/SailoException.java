package fxOlutrekisteri;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille
 * @author Vesa Lappalainen
 * @version 1.0
 */
public class SailoException extends Exception {
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Poikkeuksen muodostaja, jolle tuodaan poikkeuksessa k�ytett�v� viesti
	 * @param viesti poikkeuksen viesti
	 */
	public SailoException(String viesti) {
		super(viesti);
	}
}