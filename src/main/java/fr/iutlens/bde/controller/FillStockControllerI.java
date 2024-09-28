package fr.iutlens.bde.controller;

import fr.iutlens.bde.model.FillStockController;
import fr.iutlens.bde.model.Gestion;
import fr.iutlens.bde.model.Product;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur pour la gestion du stock des produits.
 * Cette classe implémente l'interface FillStockController.
 */
public class FillStockControllerI implements FillStockController {

    /**
     * Liste des produits affichée.
     */
    @FXML
    private ListView list;

    /**
     * Choix de la catégorie des produits.
     */
    @FXML
    private ChoiceBox categoryChoice;

    /**
     * Label pour afficher les résultats des actions.
     */
    @FXML
    private Label resultLabel;

    /**
     * Champ de texte pour le nom du produit.
     */
    @FXML
    private TextField nameTextField;

    /**
     * Champ de texte pour le prix du produit.
     */
    @FXML
    private TextField priceTextField;

    /**
     * Stage de l'application.
     */
    private Stage stage;

    /**
     * Objet de gestion des produits.
     */
    private Gestion gestion;

    /**
     * Méthode appelée lors de l'ajout d'un produit.
     * Elle vérifie la longueur du nom et la validité du prix, puis ajoute le produit à la gestion.
     */
    @FXML
    public void addProduct() {
        if (nameTextField.getText().length() > 19) {
            printResult(resultLabel, "Le nom est trop grand", "red");
        } else {
            try {
                gestion.getStock().addProduct(new Product(
                        DebtorsControllerI.reduceString(nameTextField.getText(), 19),
                        Double.parseDouble(priceTextField.getText()),
                        Product.Category.valueOf(categoryChoice.getValue().toString())
                ));
                printResult(resultLabel, "Le nouveau produit a bien été ajouté", "green");
            } catch (NumberFormatException e) {
                printResult(resultLabel, "Tu sais pas remplir des attributs ?", "red");
            }
        }
    }

    /**
     * Méthode appelée lors de la suppression d'un produit sélectionné dans la liste.
     */
    @FXML
    public void deleteProduct() {
        try {
            gestion.getStock().deleteProduct((Product) list.getSelectionModel().getSelectedItem());
        } catch (NumberFormatException e) {
            printResult(resultLabel, "Tu veux retirer un produit au hasard?", "red");
        }
    }

    /**
     * Méthode pour revenir au menu de gestion des stocks.
     * Elle charge la vue du stock et configure le contrôleur du stock.
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
            System.err.println("Le fichier FXML menu ou son contrôleur n'est pas bon");
        }
    }

    /**
     * Méthode pour styliser la liste des produits.
     * Elle configure l'affichage des cellules de la liste.
     */
    private void styleList() {
        list.setItems(gestion.getStock().getObservableProducts());
        list.setCellFactory(param -> new ListCell<Product>() {

            @Override
            protected void updateItem(Product product, boolean empty) {

                HBox hBox = new HBox();
                super.updateItem(product, empty);

                if (empty || product == null) {
                    setText(null);
                    setGraphic(null);

                } else if (product.getUnitPrice() == 0) {
                    setText(null);
                    Label category = new Label(" " + product.getCategory() + " ");
                    category.setStyle("-fx-text-fill: red; -fx-font-size: 26px; -fx-font-family: 'Comic Sans MS';");

                    hBox.getChildren().addAll(category);
                    hBox.setStyle("-fx-alignment: center");
                    setGraphic(hBox);

                } else {
                    String color = isSelected() ? "#00b2ff" : "white";

                    Label name = createNameLabel(product, color);
                    TextField quantity = createQuantityTextField(product, color);
                    Button addButton = createQuantityButton(product, "+", quantity);
                    Button subtractButton = createQuantityButton(product, "-", quantity);

                    hBox.getChildren().addAll(name, quantity, addButton, subtractButton);
                    hBox.setStyle("-fx-alignment: center");

                    setGraphic(hBox);
                }
            }

        });
    }

    /**
     * Crée un label pour afficher le nom et le prix d'un produit.
     *
     * @param product Le produit à afficher.
     * @param color   La couleur du texte.
     * @return Le label configuré.
     */
    private Label createNameLabel(Product product, String color) {
        Label name = new Label();
        name.textProperty().bind(Bindings.createStringBinding(() ->
                product.getName() + "  :  " + String.format("%.2f", product.getUnitPrice()) + " euros     Quantité  :  "));
        name.getStylesheets().add(getClass().getResource("/css/label/stockLabel.css").toExternalForm());
        name.setStyle("-fx-text-fill: " + color);
        return name;
    }

    /**
     * Crée un champ de texte pour afficher et modifier la quantité d'un produit.
     *
     * @param product Le produit à afficher.
     * @param color   La couleur du texte.
     * @return Le champ de texte configuré.
     */
    private TextField createQuantityTextField(Product product, String color) {
        TextField quantity = new TextField(product.getQuantity() + "");
        quantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    product.setQuantity(Integer.parseInt(quantity.getText()));
                } catch (NumberFormatException e) {
                    printResult(resultLabel, "Il faut un nombre, petit fourbe.", "red");
                }
            }
        });
        quantity.getStylesheets().add(getClass().getResource("/css/textField/stockTextField.css").toExternalForm());
        quantity.setStyle("-fx-text-fill: " + color);
        return quantity;
    }

    /**
     * Crée un bouton pour modifier la quantité d'un produit.
     *
     * @param product Le produit à modifier.
     * @param sign    Le signe "+" ou "-" pour ajouter ou soustraire à la quantité.
     * @param quantity Le champ de texte de la quantité.
     * @return Le bouton configuré.
     */
    private Button createQuantityButton(Product product, String sign, TextField quantity) {
        Button button;
        if (sign.equals("+")) {
            button = new Button(" + ");
            button.getStylesheets().add(getClass().getResource("/css/button/stockButton.css").toExternalForm());
            button.setOnAction(e -> {
                product.incrementQuantity();
                quantity.setText(product.getQuantity() + "");
            });
            return button;
        }
        if (sign.equals("-")) {
            button = new Button(" - ");
            button.getStylesheets().add(getClass().getResource("/css/button/stockButton.css").toExternalForm());
            button.setOnAction(e -> {
                product.decrementQuantity();
                quantity.setText(product.getQuantity() + "");
            });
            return button;
        }
        return new Button();
    }

    /**
     * Affiche un message de résultat dans un label.
     *
     * @param result Le label où afficher le message.
     * @param text   Le texte du message.
     * @param color  La couleur du texte.
     */
    public static void printResult(Label result, String text, String color) {
        result.setText(text);
        result.setStyle("-fx-text-fill: " + color);
    }

    /**
     * Définit l'objet de gestion des produits et configure la liste des produits.
     *
     * @param gestion L'objet de gestion des produits.
     */
    public void setGestion(Gestion gestion) {
        this.gestion = gestion;
        categoryChoice.getItems().addAll(Product.Category.values());
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