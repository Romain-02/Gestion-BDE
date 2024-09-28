package fr.iutlens.bde.controller;

import fr.iutlens.bde.model.DebtorsController;
import fr.iutlens.bde.model.Gestion;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Classe contrôleur pour la gestion des débiteurs (ou fraudeurs).
 * Cette classe implémente l'interface DebtorsController.
 */
public class DebtorsControllerI implements DebtorsController {

    /**
     * ListView pour afficher les noms des débiteurs et leurs montants.
     */
    @FXML
    private ListView<Pair<String, Double>> list;

    /**
     * Champ de texte pour écrire le nom du débiteur.
     */
    @FXML
    private TextField name;

    /**
     * Champ de texte pour écrire le montant de la dette.
     */
    @FXML
    private TextField amount;

    /**
     * Stage principal de l'application.
     */
    private Stage stage;

    /**
     * Instance de la classe Gestion pour la gestion des données.
     */
    private Gestion gestion;

    /**
     * Ajoute un débiteur à la liste lorsque l'événement est déclenché.
     */
    @FXML
    private void addDebtorEvent() {
        addDebtor();
    }

    /**
     * Supprime un débiteur de la liste lorsque l'événement est déclenché.
     */
    @FXML
    private void deleteDebtorEvent() {
        deleteDebtor();
    }

    /**
     * Retourne au menu principal en changeant la scène actuelle.
     */
    @FXML
    public void backToMenu() {
        MenuControllerI.backToMenu(stage, gestion);
    }

    /**
     * Ajoute un débiteur à la liste des débiteurs.
     */
    public void addDebtor() {
        String n = name.getText();
        if (n.isEmpty()) return;
        double a;
        try {
            a = Double.parseDouble(amount.getText());
        } catch (NumberFormatException e) {
            System.err.println("Ce n'est pas un nombre");
            return;
        }
        gestion.addDebtor(n, a);
    }

    /**
     * Supprime le débiteur sélectionné de la liste des débiteurs.
     */
    public void deleteDebtor() {
        gestion.deleteDebtor(list.getSelectionModel().getSelectedItem());
    }

    /**
     * Applique un style personnalisé à la liste des débiteurs.
     */
    private void styleList() {
        String[] colors = new String[]{"lightgreen", "green", "yellow", "orange", "red"};
        list.setItems(gestion.getDebtors());
        list.setCellFactory(param -> new ListCell<Pair<String, Double>>() {
            @Override
            protected void updateItem(Pair<String, Double> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getKey().isEmpty()) {
                    setText(null);
                    return;
                }
                int nbStar = nbStar(item.getValue());
                setText(prepareText(item, nbStar));
                setStyle("-fx-text-fill: " + colors[nbStar - 1]);
                setOnMouseEntered(e -> {
                    if (!isSelected()) setStyle("fx-text-fill: #202020");
                });
                setOnMouseExited(e -> {
                    if (!isSelected()) setStyle("-fx-text-fill: " + colors[nbStar - 1]);
                });
                setOnMouseClicked(e -> {
                    if (isSelected()) setStyle("-fx-text-fill: #00b2ff");
                    else setStyle("-fx-text-fill: " + colors[nbStar - 1]);
                });
            }
        });
    }

    /**
     * Prépare le texte à afficher pour chaque débiteur.
     *
     * @param item   l'élément à afficher
     * @param nbStar le nombre d'étoiles basé sur la valeur de la dette
     * @return le texte formaté pour l'affichage
     */
    public static String prepareText(Pair<String, Double> item, int nbStar) {
        int firstSpace = 25;
        int numberCaracterName = 20;
        int secondSpace = 7;
        String name = reduceString(item.getKey(), numberCaracterName);
        return "  Nom du suspect : " + name + " ".repeat(firstSpace - name.length()) + "Endettement : " + item.getValue() + " e" + " ".repeat(secondSpace - (item.getValue() + "").length()) + " Recherche : " + "*".repeat(nbStar);
    }

    /**
     * Calcule le nombre d'étoiles basé sur la valeur de la dette.
     *
     * @param value la valeur de la dette
     * @return le nombre d'étoiles
     */
    public static int nbStar(double value) {
        if (value < 1) return 1;
        if (value < 3) return 2;
        if (value < 5) return 3;
        if (value < 10) return 4;
        return 5;
    }

    /**
     * Réduit la longueur d'une chaîne à une taille spécifiée.
     *
     * @param name la chaîne à réduire
     * @param size la taille maximale de la chaîne
     * @return la chaîne réduite
     */
    public static String reduceString(String name, int size) {
        if (name.length() <= size) return name;
        return name.substring(0, size);
    }

    /**
     * Définit l'instance de la classe Gestion.
     *
     * @param gestion l'instance de Gestion à utiliser
     */
    public void setGestion(Gestion gestion) {
        this.gestion = gestion;
        styleList();
    }

    /**
     * Définit le stage principal de l'application.
     *
     * @param stage le stage principal à utiliser
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
