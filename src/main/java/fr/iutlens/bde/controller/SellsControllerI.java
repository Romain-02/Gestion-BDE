package fr.iutlens.bde.controller;

import fr.iutlens.bde.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Contrôleur pour la gestion des ventes dans l'application.
 * Cette classe implémente l'interface MenuController.
 */
public class SellsControllerI implements SellsController {

    /**
     * Liste affichant l'historique des ventes.
     */
    @FXML
    private ListView<Sell> list;

    /**
     * Stage de l'application.
     */
    private Stage stage;

    /**
     * Objet de gestion des fonctionnalités de l'application.
     */
    private Gestion gestion;

    /**
     * Annule la dernière vente effectuée.
     */
    @FXML
    public void cancelLastSell() {
        gestion.cancelLastSell();
    }

    /**
     * Retourne à l'écran de gestion du stock.
     */
    @FXML
    public void backToStock() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/stock.fxml"));
            Parent viewContent = fxmlLoader.load();
            StockControllerI stockControllerI = fxmlLoader.getController();
            stockControllerI.setStage(stage);
            stockControllerI.setGestion(gestion);
            stage.setScene(new Scene(viewContent, 1000, 800));
        } catch (IOException e) {
            System.err.println("Le fichier FXML menu ou son controller n'est pas bon");
        }
    }

    /**
     * Configure le style et le contenu de la liste des ventes.
     */
    public void styleList() {
        list.setItems(gestion.getSellsHistory());
        list.setCellFactory(param -> new ListCell<Sell>() {
            @Override
            protected void updateItem(Sell sell, boolean empty) {
                super.updateItem(sell, empty);

                if (empty || sell == null || sell.getTotalPrice() == 0) {
                    setText(null);
                    return;
                }
                setText(prepareText(sell));
            }
        });
    }

    /**
     * Prépare le texte à afficher pour une vente donnée.
     *
     * @param sell La vente pour laquelle le texte doit être préparé.
     * @return Le texte formaté pour afficher les détails de la vente.
     */
    public static String prepareText(Sell sell) {
        int lengthLine = 91;
        String product;
        StringBuilder s = new StringBuilder();
        s.append("La vente du ").append(sell.getDate().format(DateTimeFormatter.ofPattern("d MMMM uuuu"))).append(" d'un montant de ").append(sell.getTotalPrice()).append(" euros contenait ");

        for (Pair<String, Integer> p : sell.getProducts()) {
            if (p.getValue() > 0) {
                product = p.getValue() + " " + p.getKey() + ", ";
                System.out.println(s.length() - s.lastIndexOf("\n") + product.length() + " ");

                if (s.length() - s.lastIndexOf("\n") + product.length() > lengthLine) {
                    s.append("\n");
                }
                s.append(product);
            }
        }
        return s.subSequence(0, s.length() - 2).toString();
    }

    /**
     * Définit l'objet de gestion des fonctionnalités de l'application.
     *
     * @param gestion L'objet de gestion des fonctionnalités de l'application.
     */
    public void setGestion(Gestion gestion) {
        this.gestion = gestion;
        styleList();
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