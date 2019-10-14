package fxOlutrekisteri;
	
import javafx.application.Application;
import javafx.application.Platform;
// import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
// import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author Toni Alho
 * @version 15.2.2017
 *
 */
public class OlutrekisteriMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("OlutrekisteriGUIView.fxml"));
			final Pane root = (Pane)ldr.load();
			final OlutrekisteriGUIController Ctrl = (OlutrekisteriGUIController)ldr.getController();
			
			final Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("olutrekisteri.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Olutrekisteri");
			primaryStage.setOnCloseRequest((event) -> {
				Ctrl.voikoSulkea();
			});
			
			Rekisteri rek = new Rekisteri(); 
			Ctrl.setRekisteri(rek);
			primaryStage.show();
			
			if (!Ctrl.avaa() ) Platform.exit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Käynnistetään käyttöliittymä
	 * @param args komentorivin parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}