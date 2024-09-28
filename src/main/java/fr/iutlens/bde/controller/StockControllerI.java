package fr.iutlens.bde.controller;

import fr.iutlens.bde.GestionApplication;
import fr.iutlens.bde.model.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur pour la gestion du stock dans l'application.
 * Cette classe implémente l'interface MenuController.
 */
public class StockControllerI implements StockController {

    /**
     * Liste affichant les produits en stock.
     */
    @FXML
    private ListView<Product> list;

    /**
     * Étiquette affichant le résultat des actions effectuées.
     */
    @FXML
    private Label resultLabel;

    /**
     * Étiquette affichant le montant du panier.
     */
    @FXML
    private Label amountBasket;

    /**
     * Champ de texte pour saisir la réduction générale.
     */
    @FXML
    private TextField discountTextField;

    /**
     * Stage de l'application.
     */
    private Stage stage;

    /**
     * Objet de gestion des fonctionnalités de l'application.
     */
    private Gestion gestion;

    /**
     * Effectue la vente des produits dans le panier.
     */
    @FXML
    public void sell() {
        gestion.sellBasket();
        FillStockControllerI.printResult(resultLabel, "J'aime le bénéfice", "green");
    }

    /**
     * Retourne au menu principal.
     */
    @FXML
    public void backToMenu() {
        MenuControllerI.backToMenu(stage, gestion);
    }

    /**
     * Passe à l'historique des ventes.
     */
    @FXML
    public void switchToStockHistory() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/sells.fxml"));
            Parent viewContent = fxmlLoader.load();
            SellsControllerI sellsControllerI = fxmlLoader.getController();
            sellsControllerI.setStage(stage);
            sellsControllerI.setGestion(gestion);
            stage.setScene(new Scene(viewContent, 1000, 800));
        } catch (IOException e) {
            System.err.println("Le fichier FXML ou son controller n'est pas bon");
        }
    }

    /**
     * Passe à l'écran de remplissage du stock.
     */
    @FXML
    public void switchToFillStock() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/fillStock.fxml"));
            Parent viewContent = fxmlLoader.load();
            FillStockControllerI fillStockController = fxmlLoader.getController();
            fillStockController.setStage(stage);
            fillStockController.setGestion(gestion);
            stage.setScene(new Scene(viewContent, 1000, 800));
        } catch (IOException e) {
            System.err.println("Le fichier FXML ou son controller n'est pas bon");
        }
    }

    /**
     * Configure le style et le contenu de la liste des produits en stock.
     */
    private void styleList() {
        list.setItems(FXCollections.observableArrayList(gestion.getStock().getObservableProducts()));
        list.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (empty || product == null) {
                    setText(null);
                    setGraphic(null);
                } else if (product.getUnitPrice() == 0) {
                    setText(null);
                    Label category = new Label(" " + product.getCategory() + " ");
                    category.setStyle("-fx-text-fill: red; -fx-font-size: 26px; -fx-font-family: 'Comic Sans MS';");
                    HBox hBox = new HBox(category);
                    hBox.setStyle("-fx-alignment: center");
                    setGraphic(hBox);
                } else {
                    Label name = createNameLabel(product);
                    Label textDiscount = createDiscountLabel();
                    TextField discount = createDiscountTextField(product);
                    Label priceProduct = createPriceProduct(product);
                    Button addButton = createQuantityButton(product, "+");
                    Button subtractButton = createQuantityButton(product, "-");

                    HBox hBox = new HBox(name, addButton, subtractButton, textDiscount, discount, priceProduct);
                    hBox.setStyle("-fx-alignment: center");
                    setGraphic(hBox);
                }
            }
        });
    }

    /**
     * Crée une étiquette pour afficher le nom et le prix d'un produit.
     *
     * @param product Le produit pour lequel l'étiquette doit être créée.
     * @return L'étiquette créée.
     */
    private Label createNameLabel(Product product) {
        Label name = new Label();
        name.textProperty().bind(Bindings.createStringBinding(
                () -> product.getName() + "  :  " + String.format("%.2f", product.getUnitPrice()) + " e    Quantité  :  " + gestion.getBasket().getProduct(product.getName()).getQuantity() + " / " + product.getQuantity() + " ",
                gestion.getBasket().getProduct(product.getName()).getQuantityProperty(), product.getQuantityProperty()));
        name.getStylesheets().add(getClass().getResource("/css/label/stockLabel.css").toExternalForm());
        return name;
    }

    /**
     * Crée une étiquette pour afficher le texte de réduction.
     *
     * @return L'étiquette créée.
     */
    private Label createDiscountLabel() {
        Label textDiscount = new Label("   Réduction  ");
        textDiscount.getStylesheets().add(getClass().getResource("/css/label/stockLabel.css").toExternalForm());
        return textDiscount;
    }

    /**
     * Crée un champ de texte pour saisir la réduction d'un produit.
     *
     * @param product Le produit pour lequel le champ de texte doit être créé.
     * @return Le champ de texte créé.
     */
    private TextField createDiscountTextField(Product product) {
        TextField discount = new TextField(Math.round(product.getDiscount() * 100) + "");
        discount.getStylesheets().add(getClass().getResource("/css/textField/stockTextField.css").toExternalForm());
        discount.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (!gestion.getBasket().isEmpty()) {
                    discount.setText(Math.round(gestion.getStock().getProduct(product.getName()).getDiscount() * 100) + "");
                    FillStockControllerI.printResult(resultLabel, "Il faut vendre avant de\npouvoir changer les remises", "red");
                } else if (Double.parseDouble(newValue) > 100) {
                    FillStockControllerI.printResult(resultLabel, "Tu veux faire\nfaillite ouuuuu?", "red");
                } else if (Double.parseDouble(newValue) < 0) {
                    FillStockControllerI.printResult(resultLabel, "C'est plus une réduction\nmais une augmentation là.", "red");
                } else {
                    gestion.getBasket().getProduct(product.getName()).setDiscount(Double.parseDouble(discount.getText()) / 100.0);
                }
            } catch (NumberFormatException e) {
                FillStockControllerI.printResult(resultLabel, "La promo ne doit pas\n être une équation.", "red");
            }
        });
        return discount;
    }

    /**
     * Crée une étiquette pour afficher le prix réduit d'un produit.
     *
     * @param product Le produit pour lequel l'étiquette doit être créée.
     * @return L'étiquette créée.
     */
    private Label createPriceProduct(Product product) {
        Label priceProduct = new Label();
        priceProduct.textProperty().bind(Bindings.createStringBinding(() -> " %      Montant  :  " + String.format("%.2f", gestion.getBasket().getProduct(product.getName()).getTotalPrice()), gestion.getBasket().getProduct(product.getName()).getTotalPriceProperty()));
        priceProduct.getStylesheets().add(getClass().getResource("/css/label/stockLabel.css").toExternalForm());
        return priceProduct;
    }

    /**
     * Crée un bouton pour ajuster la quantité d'un produit.
     *
     * @param product Le produit pour lequel le bouton doit être créé.
     * @param sign Le signe indiquant si la quantité doit être augmentée ou diminuée.
     * @return Le bouton créé.
     */
    private Button createQuantityButton(Product product, String sign) {
        Button button;
        if (sign.equals("+")) {
            button = new Button(" + ");
            button.getStylesheets().add(getClass().getResource("/css/button/stockButton.css").toExternalForm());
            button.setOnAction(e -> {
                gestion.getBasket().incrementProductQuantity(gestion.getBasket().getProduct(product.getName()));
            });
            return button;
        } else if (sign.equals("-")) {
            button = new Button(" - ");
            button.getStylesheets().add(getClass().getResource("/css/button/stockButton.css").toExternalForm());
            button.setOnAction(e -> gestion.getBasket().decrementProductQuantity(gestion.getBasket().getProduct(product.getName())));
            return button;
        }
        return new Button();
    }

    /**
     * Initialise le champ de texte de la réduction générale sur les produits.
     */
    private void initializeGeneralDiscountTextField(){
        discountTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if(newValue.isEmpty()) {discountTextField.setText("0"); gestion.getBasket().setDiscount(0);}
                    else if (Double.parseDouble(newValue) > 100) FillStockControllerI.printResult(resultLabel, "Tu veux faire\nfaillite ouuuuu?", "red");
                    else if (Double.parseDouble(newValue) < 0) FillStockControllerI.printResult(resultLabel,"C'est plus une reduc\nmais une augmentation la.", "red");
                    else gestion.getBasket().setDiscount(Double.parseDouble(discountTextField.getText()) / 100.0);

                }catch (NumberFormatException e){
                    FillStockControllerI.printResult(resultLabel,"La promo ne doit pas\n etre une equation.", "red");
                }
            }
        });
    }

    /**
     * Initialise l'étiquette du montant total du panier.
     */
    private void initializeAamountBasketLabel(){
        amountBasket.textProperty().bind(Bindings.createStringBinding(() -> ":  " + String.format("%.2f", gestion.getBasket().getPrice()) + " euros", gestion.getBasket().getPriceProperty()));
    }

    /**
     * Définit l'objet de gestion des fonctionnalités de l'application.
     *
     * @param gestion L'objet de gestion des fonctionnalités de l'application.
     */
    public void setGestion(Gestion gestion){
        this.gestion = gestion;
        gestion.getBasket().updateBasket();
        styleList();
        initializeAamountBasketLabel();
        initializeGeneralDiscountTextField();
    }

    /**
     * Définit le stage de l'application.
     *
     * @param stage Le stage de l'application.
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }
}
