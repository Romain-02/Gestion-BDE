package fr.iutlens.bde.controller;

import fr.iutlens.bde.model.AccountingController;
import fr.iutlens.bde.model.Gestion;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Classe contrôleur pour la gestion de la comptabilité.
 * Cette classe implémente l'interface AccountingController.
 */
public class AccountingControllerI implements AccountingController {

    /**
     * Stage principal de l'application.
     */
    private Stage stage;

    /**
     * Instance de la classe Gestion pour la gestion des données.
     */
    private Gestion gestion;

    /**
     * Retourne au menu principal en changeant la scène actuelle.
     */
    @FXML
    public void backToMenu() {
        MenuControllerI.backToMenu(stage, gestion);
    }

    /**
     * Définit l'instance de la classe Gestion.
     *
     * @param gestion l'instance de Gestion à utiliser.
     */
    public void setGestion(Gestion gestion) {
        this.gestion = gestion;
    }

    /**
     * Définit le stage principal de l'application.
     *
     * @param stage le stage principal à utiliser.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}