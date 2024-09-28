package fr.iutlens.bde.controller;

import fr.iutlens.bde.model.Gestion;
import fr.iutlens.bde.model.Transaction;
import fr.iutlens.bde.model.TreasuryController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur pour la gestion de la trésorerie.
 * Cette classe implémente l'interface TreasuryController
 */
public class TreasuryControllerI implements TreasuryController {

    /**
     * Label affichant le montant en caisse.
     */
    @FXML
    private Label amountCashRegister;

    /**
     * Label affichant le montant en compte bancaire.
     */
    @FXML
    private Label amountBankAccount;

    /**
     * Champ de texte pour entrer le montant à transférer de la caisse vers la banque.
     */
    @FXML
    private TextField amountCashRegisterToBank;

    /**
     * Champ de texte pour entrer le montant à transférer de la banque vers la caisse.
     */
    @FXML
    private TextField amountBankToCashRegister;

    /**
     * Champ de texte pour entrer le montant à déposer en caisse.
     */
    @FXML
    private TextField depositCashRegister;

    /**
     * Champ de texte pour entrer le montant à déposer en banque.
     */
    @FXML
    private TextField depositBank;

    /**
     * Champ de texte pour entrer le montant à retirer de la caisse.
     */
    @FXML
    private TextField withdrawalCashRegister;

    /**
     * Champ de texte pour entrer le montant à retirer de la banque.
     */
    @FXML
    private TextField withdrawalBank;

    /**
     * Label affichant le résultat des opérations sur la caisse.
     */
    @FXML
    private Label resultLabelCashRegister;

    /**
     * Label affichant le résultat des opérations sur la banque.
     */
    @FXML
    private Label resultLabelBank;

    /**
     * La scène principale de l'application.
     */
    private Stage stage;

    /**
     * L'instance de gestion utilisée par le contrôleur.
     */
    private Gestion gestion;

    /**
     * Effectue un dépôt en caisse.
     */
    @FXML
    public void cashIn() {
        String value = depositCashRegister.getText();
        if (value.isEmpty()) return;
        clearLabels();
        try {
            gestion.cashIn(Double.parseDouble(value));
            printText(resultLabelCashRegister, "Bientôt le million (non).", "green");
            gestion.addTransaction(Transaction.Type.CASH_DEPOSIT, Double.parseDouble(value));
        } catch (NumberFormatException e) {
            printText(resultLabelCashRegister, "Tu as cru que c'était une équation ?", "red");
        }
    }

    /**
     * Effectue un retrait en caisse.
     */
    @FXML
    public void cashOut() {
        String value = withdrawalCashRegister.getText();
        if (value.isEmpty()) return;
        clearLabels();
        try {
            if (!gestion.cashOut(Double.parseDouble(value)))
                printText(resultLabelCashRegister, "Tu manques de moula, désolé.", "red");
            else {
                printText(resultLabelCashRegister, "Ça sent le ravitaillement du stock, non ?", "green");
                gestion.addTransaction(Transaction.Type.CASH_WITHDRAW, Double.parseDouble(value));
            }
        } catch (NumberFormatException e) {
            printText(resultLabelCashRegister, "Tu as cru que c'était une équation ?", "red");
        }
    }

    /**
     * Effectue un dépôt en banque.
     */
    @FXML
    public void deposit() {
        String value = depositBank.getText();
        if (value.isEmpty()) return;
        clearLabels();
        try {
            gestion.deposit(Double.parseDouble(value));
            printText(resultLabelBank, "Bientôt le million (non).", "green");
            gestion.addTransaction(Transaction.Type.BANK_DEPOSIT, Double.parseDouble(value));
        } catch (NumberFormatException e) {
            printText(resultLabelBank, "Tu as cru que c'était une équation ?", "red");
        }
    }

    /**
     * Effectue un retrait en banque.
     */
    @FXML
    public void withdraw() {
        String value = withdrawalBank.getText();
        if (value.isEmpty()) return;
        clearLabels();
        try {
            if (!gestion.withdraw(Double.parseDouble(value))) {
                printText(resultLabelBank, "Tu manques de moula, désolé.", "red");
            } else {
                printText(resultLabelBank, "Ça se fait plaisir dis donc.", "green");
                gestion.addTransaction(Transaction.Type.BANK_WITHDRAW, Double.parseDouble(value));
            }
        } catch (NumberFormatException e) {
            printText(resultLabelBank, "Tu as cru que c'était une équation ?", "red");
        }
    }

    /**
     * Effectue un transfert de la banque vers la caisse.
     */
    @FXML
    public void bankToCashRegister() {
        String value = amountBankToCashRegister.getText();
        if (value.isEmpty()) return;
        clearLabels();
        try {
            if (!gestion.bankToCashRegister(Double.parseDouble(value))) {
                printText(resultLabelBank, "Tu manques de moula, désolé.", "red");
            } else {
                printText(resultLabelBank, "La caisse s'est remplie.", "green");
                gestion.addTransaction(Transaction.Type.BANK_TO_CASH_REGISTER, Double.parseDouble(value));
            }
        } catch (NumberFormatException e) {
            printText(resultLabelBank, "Tu as cru que c'était une équation ?", "red");
        }
    }

    /**
     * Effectue un transfert de la caisse vers la banque.
     */
    @FXML
    public void cashRegisterToBank() {
        String value = amountCashRegisterToBank.getText();
        if (value.isEmpty()) return;
        clearLabels();
        try {
            if (!gestion.cashRegisterToBank(Double.parseDouble(value))) {
                printText(resultLabelCashRegister, "Tu manques de moula, désolé.", "red");
            } else {
                printText(resultLabelCashRegister, "L'argent a été blanchi.", "green");
                gestion.addTransaction(Transaction.Type.CASH_REGISTER_TO_BANK, Double.parseDouble(value));
            }
        } catch (NumberFormatException e) {
            printText(resultLabelCashRegister, "Tu as cru que c'était une équation ?", "red");
        }
    }

    /**
     * Affiche l'historique de la trésorerie.
     */
    @FXML
    public void printTreasuryHistory() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/treasuryHistory.fxml"));
            Parent viewContent = fxmlLoader.load();
            TreasuryHistoryControllerI treasuryHistoryControllerI = fxmlLoader.getController();
            treasuryHistoryControllerI.setStage(stage);
            treasuryHistoryControllerI.setGestion(gestion);
            stage.setScene(new Scene(viewContent, 1000, 800));
        } catch (IOException e) {
            System.err.println("Le fichier FXML ou son contrôleur n'est pas bon");
        }
    }

    /**
     * Retourne au menu principal.
     */
    @FXML
    public void backToMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/iutlens/bde/view/menu.fxml"));
            Parent viewContent = fxmlLoader.load();
            MenuControllerI menuControllerI = fxmlLoader.getController();
            menuControllerI.setStage(stage);
            menuControllerI.setGestion(gestion);
            stage.setScene(new Scene(viewContent, 1000, 800));
        } catch (IOException e) {
            System.err.println("Le fichier FXML menu ou son contrôleur n'est pas bon");
        }
    }

    /**
     * Efface le contenu des champs de texte et des labels.
     */
    public void clearLabels() {
        depositBank.setText("");
        depositCashRegister.setText("");
        withdrawalBank.setText("");
        withdrawalCashRegister.setText("");
        resultLabelBank.setText("");
        resultLabelCashRegister.setText("");
        amountBankToCashRegister.setText("");
        amountCashRegisterToBank.setText("");
    }

    /**
     * Affiche un texte dans un label avec une couleur spécifique.
     *
     * @param result Le label dans lequel afficher le texte.
     * @param text   Le texte à afficher.
     * @param color  La couleur du texte.
     */
    public static void printText(Label result, String text, String color) {
        result.setText(text);
        result.setStyle("-fx-text-fill: " + color);
    }

    /**
     * Initialise les labels affichant les montants en caisse et en banque.
     */
    public void initializeAmount() {
        amountCashRegister.textProperty().bind(Bindings.createStringBinding(() -> "Montant : " + gestion.getCashRegister() + " euros", gestion.getCashRegisterProperty()));
        amountBankAccount.textProperty().bind(Bindings.createStringBinding(() -> "Montant : " + gestion.getBankAccount() + " euros", gestion.getBankAccountProperty()));
    }

    /**
     * Définit l'instance de gestion utilisée par le contrôleur.
     *
     * @param gestion L'instance de gestion.
     */
    public void setGestion(Gestion gestion){
        this.gestion = gestion;
        initializeAmount();
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