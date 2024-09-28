package fr.iutlens.bde.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import javafx.util.Pair;

/**
 * Classe de gestion de la trésorerie et des transactions.
 * Gère différents aspects du BDE Info
 */
public class Gestion {

    /**
     * Stock des produits.
     */
    private final Stock stock;

    /**
     * Panier de produits.
     */
    private final Stock.Basket basket;

    /**
     * Propriété pour le montant de la caisse.
     */
    private final DoubleProperty cashRegister;

    /**
     * Propriété pour le montant du compte bancaire.
     */
    private final DoubleProperty bankAccount;

    /**
     * Liste observable des débiteurs.
     */
    private final ObservableList<Pair<String, Double>> debtors;

    /**
     * Historique observable des transactions de trésorerie.
     */
    private final ObservableList<Transaction> treasuryHistory;

    /**
     * Historique observable des ventes.
     */
    private final ObservableList<Sell> sellsHistory;

    /**
     * Constructeur de la classe Gestion avec des paramètres initiaux complets.
     *
     * @param stock Liste des produits en stock.
     * @param cashRegister Montant initial de la caisse.
     * @param bankAccount Montant initial du compte bancaire.
     * @param debtors Liste des débiteurs.
     * @param treasuryHistory Historique des transactions de trésorerie.
     * @param sellsHistory Historique des ventes.
     */
    public Gestion(List<Product> stock, double cashRegister, double bankAccount, List<Pair<String, Double>> debtors, List<Transaction> treasuryHistory, List<Sell> sellsHistory) {
        this.stock = new Stock(stock);
        this.basket = this.stock.new Basket();
        this.cashRegister = new SimpleDoubleProperty(cashRegister);
        this.bankAccount = new SimpleDoubleProperty(bankAccount);
        this.debtors = FXCollections.observableArrayList(debtors);
        this.treasuryHistory = FXCollections.observableArrayList(treasuryHistory);
        this.sellsHistory = FXCollections.observableArrayList(sellsHistory);
    }

    /**
     * Constructeur de la classe Gestion avec des paramètres initiaux pour le stock, la caisse et le compte bancaire.
     *
     * @param stock Liste des produits en stock.
     * @param cashRegister Montant initial de la caisse.
     * @param bankAccount Montant initial du compte bancaire.
     */
    public Gestion(List<Product> stock, double cashRegister, double bankAccount) {
        this.stock = new Stock(stock);
        this.basket = this.stock.new Basket();
        this.cashRegister = new SimpleDoubleProperty(cashRegister);
        this.bankAccount = new SimpleDoubleProperty(bankAccount);
        this.debtors = FXCollections.observableArrayList(new Pair<>("", 0.0));
        this.treasuryHistory = FXCollections.observableArrayList(new Transaction(Transaction.Type.CASH_DEPOSIT, 0));
        this.sellsHistory = FXCollections.observableArrayList(new Sell());
    }

    /**
     * Constructeur par défaut de la classe Gestion.
     */
    public Gestion() {
        this(new ArrayList<>(), 0, 0);
    }

    /**
     * Crée une instance de Gestion à partir d'une instance SerializableGestion.
     *
     * @param sGestion Instance de SerializableGestion.
     * @return Nouvelle instance de Gestion.
     */
    public static Gestion createGestion(SerializableGestion sGestion) {
        return new Gestion(Product.deserializeProducts(sGestion.getStock()), sGestion.getCashRegister(), sGestion.getBankAccount(), sGestion.getDebtors(), sGestion.getTreasuryHistory(), sGestion.getSellsHistory());
    }

    /**
     * Ajoute un débiteur avec un montant spécifique.
     *
     * @param name Nom du débiteur.
     * @param amount Montant dû.
     */
    public void addDebtor(String name, double amount) {
        if (debtors.get(0).getKey().isEmpty()) debtors.remove(0);
        Pair<String, Double> p = getDebtor(name);
        if (p != null) {
            amount = roundAmount(amount + p.getValue());
            debtors.remove(p);
        }
        if (amount >= 1000) amount = 999.99;
        debtors.add(new Pair<>(name, amount));
    }

    /**
     * Supprime un débiteur.
     *
     * @param item Débiteur à supprimer.
     */
    public void deleteDebtor(Pair<String, Double> item) {
        debtors.remove(item);
        if (debtors.isEmpty()) debtors.add(new Pair<>("", 0.0));
    }

    /**
     * Ajoute une transaction à l'historique des transactions.
     *
     * @param typeTransaction Type de la transaction.
     * @param value Montant de la transaction.
     */
    public void addTransaction(Transaction.Type typeTransaction, double value) {
        if (!treasuryHistory.isEmpty() && treasuryHistory.get(0).getAmount() == 0) treasuryHistory.remove(0);
        treasuryHistory.add(new Transaction(typeTransaction, value));
    }

    /**
     * Annule la dernière transaction effectuée.
     */
    public void cancelLastTransaction() {
        if (treasuryHistory.isEmpty()) return;
        Transaction transaction = treasuryHistory.get(treasuryHistory.size() - 1);
        treasuryHistory.remove(transaction);
        switch (transaction.getType()) {
            case CASH_DEPOSIT -> cashOut(transaction.getAmount());
            case CASH_WITHDRAW -> cashIn(transaction.getAmount());
            case BANK_DEPOSIT -> withdraw(transaction.getAmount());
            case BANK_WITHDRAW -> deposit(transaction.getAmount());
            case CASH_REGISTER_TO_BANK -> bankToCashRegister(transaction.getAmount());
            default -> cashRegisterToBank(transaction.getAmount());
        }
        if (treasuryHistory.isEmpty()) addTransaction(Transaction.Type.CASH_DEPOSIT, 0);
    }

    /**
     * Annule la dernière vente effectuée.
     */
    public void cancelLastSell() {
        if (sellsHistory.isEmpty()) return;
        Sell sell = sellsHistory.get(sellsHistory.size() - 1);
        sellsHistory.remove(sell);

        for (Pair<String, Integer> p : sell.getProducts())
            if(basket.getProduct(p.getKey()) != null)
                stock.getProduct(p.getKey()).incrementQuantity(p.getValue());

        cashOut(sell.getTotalPrice());
        if (sellsHistory.isEmpty()) sellsHistory.add(new Sell());
    }

    /**
     * Dépose un montant sur le compte bancaire.
     *
     * @param amount Montant à déposer.
     */
    public void deposit(double amount) {
        bankAccount.setValue(roundAmount(bankAccount.getValue() + amount));
    }

    /**
     * Retire un montant du compte bancaire.
     *
     * @param amount Montant à retirer.
     * @return Vrai si le retrait est réussi, faux sinon.
     */
    public boolean withdraw(double amount) {
        if (amount > getBankAccount()) return false;
        bankAccount.setValue(roundAmount(bankAccount.getValue() - amount));
        return true;
    }

    /**
     * Dépose un montant dans la caisse.
     *
     * @param amount Montant à déposer.
     */
    public void cashIn(double amount) {
        cashRegister.setValue(roundAmount(cashRegister.getValue() + amount));
    }

    /**
     * Retire un montant de la caisse.
     *
     * @param amount Montant à retirer.
     * @return Vrai si le retrait est réussi, faux sinon.
     */
    public boolean cashOut(double amount) {
        if (amount > getCashRegister()) return false;
        cashRegister.setValue(roundAmount(cashRegister.getValue() - amount));
        return true;
    }

    /**
     * Transfère un montant de la caisse vers le compte bancaire.
     *
     * @param amount Montant à transférer.
     * @return Vrai si le transfert est réussi, faux sinon.
     */
    public boolean cashRegisterToBank(double amount) {
        if (getCashRegister() < amount) return false;
        cashOut(amount);
        deposit(amount);
        return true;
    }

    /**
     * Transfère un montant du compte bancaire vers la caisse.
     *
     * @param amount Montant à transférer.
     * @return Vrai si le transfert est réussi, faux sinon.
     */
    public boolean bankToCashRegister(double amount) {
        if (getBankAccount() < amount) return false;
        withdraw(amount);
        cashIn(amount);
        return true;
    }

    /**
     * Effectue la vente du panier.
     *
     * @return Vrai si la vente est réussie, faux sinon.
     */
    public boolean sellBasket() {
        if (!stock.sellBasket(basket)) return false;
        cashIn(basket.calculatePrice());
        if (!sellsHistory.isEmpty() && sellsHistory.get(0).getTotalPrice() == 0) sellsHistory.remove(0);
        sellsHistory.add(new Sell(basket));
        basket.clearBasket();
        return true;
    }

    /**
     * Charge une instance de Gestion à partir d'un fichier de sauvegarde.
     *
     * @return Instance de Gestion chargée.
     */
    public static Gestion loadGestion() {
        try {
            FileInputStream fileIn = new FileInputStream("/data/gestion.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            return createGestion((SerializableGestion) in.readObject());
        } catch (FileNotFoundException f) {
            System.out.println("Il n'y a pas de sauvegarde, une nouvelle a donc été créée.");
        } catch (IOException i) {
            System.out.println("Il y a un problème avec la sauvegarde.");
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return new Gestion();
    }

    /**
     * Sauvegarde l'instance actuelle de Gestion dans un fichier.
     */
    public void saveGestion() {
        System.out.println("testtttt " + new File(".").getAbsolutePath());
        try {
            new ObjectOutputStream(new FileOutputStream("/data/gestion.ser")).writeObject(new SerializableGestion(this));
        } catch (FileNotFoundException e) {
            try {
                System.out.println("testtttt " + new File(".").getAbsolutePath());
                Files.createDirectories(Paths.get("data/backup"));
                new ObjectOutputStream(new FileOutputStream("/data/gestion.ser")).writeObject(this);
                new ObjectOutputStream(new FileOutputStream("/data/backup/gestion" + LocalDate.now() + ".ser")).writeObject(this);
            } catch (IOException i) {
                System.out.println("testtttt " + new File(".").getAbsolutePath());
                System.err.println("Il y a un problème pour sauvegarder les derniers changements");
            }
        } catch (IOException i) {
            System.err.println("Il y a un problème pour sauvegarder les derniers changements");
            i.printStackTrace();
        }
    }

    /**
     * Arrondit un montant à deux décimales.
     *
     * @param amount Montant à arrondir.
     * @return Montant arrondi.
     */
    public static double roundAmount(double amount) {
        return Math.round(amount * 100) / 100.0;
    }

    /**
     * Recherche un débiteur par son nom.
     *
     * @param name Nom du débiteur.
     * @return Le débiteur correspondant ou null s'il n'est pas trouvé.
     */
    public Pair<String, Double> getDebtor(String name) {
        for (Pair p : debtors)
            if (name.equals(p.getKey())) return p;
        return null;
    }

    /**
     * Retourne le stock des produits.
     *
     * @return Stock des produits.
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * Retourne le panier de produits.
     *
     * @return Panier de produits.
     */
    public Stock.Basket getBasket() {
        return basket;
    }

    /**
     * Retourne le montant de la caisse.
     *
     * @return Montant de la caisse.
     */
    public double getCashRegister() {
        return cashRegister.getValue();
    }

    /**
     * Retourne le montant du compte bancaire.
     *
     * @return Montant du compte bancaire.
     */
    public double getBankAccount() {
        return bankAccount.getValue();
    }

    /**
     * Retourne la propriété de la caisse.
     *
     * @return Propriété de la caisse.
     */
    public DoubleProperty getCashRegisterProperty() {
        return cashRegister;
    }

    /**
     * Retourne la propriété du compte bancaire.
     *
     * @return Propriété du compte bancaire.
     */
    public DoubleProperty getBankAccountProperty() {
        return bankAccount;
    }

    /**
     * Retourne la liste observable des débiteurs.
     *
     * @return Liste observable des débiteurs.
     */
    public ObservableList<Pair<String, Double>> getDebtors() {
        return debtors;
    }

    /**
     * Retourne l'historique observable des transactions de trésorerie.
     *
     * @return Historique observable des transactions de trésorerie.
     */
    public ObservableList<Transaction> getTreasuryHistory() {
        return treasuryHistory;
    }

    /**
     * Retourne l'historique observable des ventes.
     *
     * @return Historique observable des ventes.
     */
    public ObservableList<Sell> getSellsHistory() {
        return sellsHistory;
    }
}
