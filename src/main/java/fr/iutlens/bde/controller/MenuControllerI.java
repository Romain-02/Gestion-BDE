package fr.iutlens.bde.controller;

import fr.iutlens.bde.model.Gestion;
import fr.iutlens.bde.model.MenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur pour le menu principal de l'application.
 * Cette classe implémente l'interface MenuController.
 */
public class MenuControllerI implements MenuController {

    /**
     * Conteneur vertical pour les éléments de l'interface.
     */
    @FXML
    private VBox vbox;

    /**
     * Stage de l'application.
     */
    private Stage stage;

    /**
     * Objet de gestion des fonctionnalités de l'application.
     */
    private Gestion gestion;

    /**
     * Méthode appelée lors d'un événement de changement de scène.
     * Elle détermine l'index du bouton cliqué et appelle la méthode switchScene avec cet index.
     *
     * @param event L'événement déclenché par l'utilisateur.
     */
    @FXML
    private void switchScene(ActionEvent event) {
        switchScene(vbox.getChildren().indexOf(event.getSource()) - 1);
    }

    /**
     * Change la scène affichée en fonction de l'index du bouton cliqué.
     *
     * @param numButton L'index du bouton cliqué.
     */
    public void switchScene(int numButton) {
        FXMLLoader fxmlLoader;
        Parent viewContent;
        try {
            switch (numButton) {
                case 0:
                    fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/stock.fxml"));
                    viewContent = fxmlLoader.load();
                    StockControllerI stockController = fxmlLoader.getController();
                    stockController.setStage(stage);
                    stockController.setGestion(gestion);
                    stage.setScene(new Scene(viewContent, 1000, 800));
                    break;
                case 1:
                    fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/treasury.fxml"));
                    viewContent = fxmlLoader.load();
                    TreasuryControllerI treasuryController = fxmlLoader.getController();
                    treasuryController.setStage(stage);
                    treasuryController.setGestion(gestion);
                    stage.setScene(new Scene(viewContent, 1000, 800));
                    break;
                case 2:
                    fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/accounting.fxml"));
                    viewContent = fxmlLoader.load();
                    AccountingControllerI accountingController = fxmlLoader.getController();
                    accountingController.setStage(stage);
                    accountingController.setGestion(gestion);
                    stage.setScene(new Scene(viewContent, 1000, 800));
                    break;
                case 3:
                    fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/debtors.fxml"));
                    viewContent = fxmlLoader.load();
                    DebtorsControllerI debtorsController = fxmlLoader.getController();
                    debtorsController.setStage(stage);
                    debtorsController.setGestion(gestion);
                    stage.setScene(new Scene(viewContent, 1000, 800));
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.err.println("Le fichier FXML ou son controller n'est pas bon");
        }
    }

    /**
     * Méthode statique pour revenir au menu principal.
     * Elle charge la vue du menu et configure le contrôleur du menu.
     *
     * @param stage   Le stage de l'application.
     * @param gestion L'objet de gestion des fonctionnalités de l'application.
     */
    public static void backToMenu(Stage stage, Gestion gestion) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MenuControllerI.class.getResource("/fr/iutlens/bde/view/menu.fxml"));
            Parent viewContent = fxmlLoader.load();
            MenuControllerI menuControllerI = fxmlLoader.getController();
            menuControllerI.setStage(stage);
            menuControllerI.setGestion(gestion);
            stage.setScene(new Scene(viewContent, 1000, 800));
        } catch (IOException e) {
            System.err.println("Le fichier FXML menu ou son controller n'est pas bon");
        }
    }

    /**
     * Définit l'objet de gestion des fonctionnalités de l'application.
     *
     * @param gestion L'objet de gestion des fonctionnalités de l'application.
     */
    public void setGestion(Gestion gestion) {
        this.gestion = gestion;
    }

    /**
     * Définit le stage de l'application.
     *
     * @param stage Le stage de l'application.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}