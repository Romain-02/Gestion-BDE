/**
 * Ce logiciel est destiné à faciliter la gestion du BDE info de l'IUT de Lens.
 *
 * (c) 2024-2025 Le BDE info - IUT Lens - Romain HANNOIR.
 * Tous droits réservés.
 */

package fr.iutlens.bde;

import fr.iutlens.bde.controller.MenuControllerI;
import fr.iutlens.bde.model.Gestion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

/**
 * La classe gestionApplication illustre le fonctionnement d'une {@link Application} JavaFX.
 *
 * @author Le BDE info
 *
 */
public class GestionApplication extends Application {

    /**
     * Son en arrière plan de l'application
     */
    static MediaPlayer backgroundSound;

    /**
     * Son en premier plan de l'application
     */
    static MediaPlayer foregroundSound;

    /**
     * Cette méthode permet d'initialiser l'affichage de la fenêtre de pour la gestion de BDE.
     *
     * @param stage La fenêtre (initialement vide) de l'application.
     */

    @Override
    public void start(Stage stage) throws IOException {
        // Il faut d'abord récupérer la description de la vue du menu (au format FXML).
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/menu.fxml"));
        Parent viewContent = fxmlLoader.load();

        //On récupère le controller
        MenuControllerI controller = fxmlLoader.getController();
        controller.setStage(stage);
        Gestion gestion = Gestion.loadGestion();
        controller.setGestion(gestion);

        // On ajoute la scène
        Scene scene = new Scene(viewContent, 1000, 800);
        stage.setScene(scene);
        stage.setResizable(false);

        // On sauvegarde la gestion quand la fenêtre se ferme
        stage.setOnCloseRequest((WindowEvent event) -> gestion.saveGestion());

        // On ajoute la music de fond
        playBackgroundSound("wiiMusicHome.mp3", 0.4);

        // On peut ensuite donner un titre à la fenêtre.
        stage.setTitle("gestionBDE!");

        // Enfin, on affiche la fenêtre.
        stage.show();
    }

    public static void playSound(String soundName, double sound){
        foregroundSound = new MediaPlayer(new Media(new File("src/main/resources/son/" + soundName).toURI().toString()));
        foregroundSound.setVolume(sound);
        foregroundSound.play();
    }

    public static void playBackgroundSound(String soundName, double sound){
        backgroundSound = new MediaPlayer(new Media(new File("src/main/resources/son/" + soundName).toURI().toString()));
        backgroundSound.setVolume(sound);
        backgroundSound.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundSound.play();
    }

    public static void main(String[] args) {
        launch();
    }
}
