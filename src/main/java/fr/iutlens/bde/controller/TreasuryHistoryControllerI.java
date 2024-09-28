package fr.iutlens.bde.controller;

import fr.iutlens.bde.model.Gestion;
import fr.iutlens.bde.model.Transaction;
import fr.iutlens.bde.model.TreasuryHistoryController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Contrôleur de l'historique de la trésorerie
 * Implémente l'interface TreasuryHistoryController.
 */
public class TreasuryHistoryControllerI implements TreasuryHistoryController {

    /**
     * Liste des transactions affichées.
     */
    @FXML
    private ListView list;

    /**
     * Fenêtre de l'application.
     */
    private Stage stage;

    /**
     * Instance de la gestion des transactions.
     */
    private Gestion gestion;

    /**
     * Annule la dernière transaction.
     */
    @FXML
    public void cancelLastTransaction() {
        gestion.cancelLastTransaction();
    }

    /**
     * Retourne à la vue de la trésorerie.
     */
    @FXML
    public void backToTreasury() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/treasury.fxml"));
            Parent viewContent = fxmlLoader.load();
            TreasuryControllerI treasuryControllerI = fxmlLoader.getController();
            treasuryControllerI.setStage(stage);
            treasuryControllerI.setGestion(gestion);
            stage.setScene(new Scene(viewContent, 1000, 800));
        } catch (IOException e) {
            System.err.println("Le fichier FXML ou son contrôleur n'est pas bon");
        }
    }

    /**
     * Applique le style à la liste des transactions.
     */
    public void styleList() {
        list.setItems(gestion.getTreasuryHistory());
        list.setCellFactory(param -> new ListCell<Transaction>() {
            @Override
            protected void updateItem(Transaction transaction, boolean empty) {
                super.updateItem(transaction, empty);

                if (empty || transaction == null || transaction.getAmount() == 0) {
                    setText(null);
                    return;
                }
                setText(prepareText(transaction));
            }
        });
    }

    /**
     * Prépare le texte d'affichage pour une transaction.
     *
     * @param transaction La transaction à afficher
     * @return Le texte formaté pour la transaction
     */
    public static String prepareText(Transaction transaction) {
        String[] descriptionTransactions = new String[]{
                "Dépôt en caisse de ",
                "Dépôt sur le compte bancaire de ",
                "Retrait sur la caisse de ",
                "Retrait du compte bancaire de ",
                "Retrait de la caisse pour mettre à la banque un montant de ",
                "Retrait de la banque pour alimenter la caisse de "
        };
        String description = descriptionTransactions[transaction.getType().ordinal()];
        return description + transaction.getAmount() + " euros le " + transaction.getDate().format(DateTimeFormatter.ofPattern("d MMMM uuuu"));
    }

    /**
     * Définit l'instance de gestion des transactions.
     *
     * @param gestion L'instance de gestion
     */
    public void setGestion(Gestion gestion) {
        this.gestion = gestion;
        styleList();
    }

    /**
     * Définit la fenêtre de l'application.
     *
     * @param stage La fenêtre de l'application
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
