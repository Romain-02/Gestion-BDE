package fr.iutlens.bde.model;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant la gestion sérialisable.
 * Cette classe est utilisée pour sérialiser et désérialiser l'état de la gestion.
 */
public class SerializableGestion implements Serializable {

    /**
     * Liste des produits sérialisables en stock.
     */
    private final List<SerializableProduct> stock;

    /**
     * Montant en caisse.
     */
    private final double cashRegister;

    /**
     * Montant sur le compte bancaire.
     */
    private final double bankAccount;

    /**
     * Liste des débiteurs avec leur montant dû.
     */
    private final List<Pair<String, Double>> debtors;

    /**
     * Historique des transactions de trésorerie.
     */
    private final List<Transaction> treasuryHistory;

    /**
     * Historique des ventes.
     */
    private final List<Sell> sellsHistory;

    /**
     * Constructeur avec tous les attributs spécifiés.
     *
     * @param stock La liste des produits sérialisables en stock
     * @param cashRegister Le montant en caisse
     * @param bankAccount Le montant sur le compte bancaire
     * @param debtors La liste des débiteurs avec leur montant dû
     * @param treasuryHistory L'historique des transactions de trésorerie
     * @param sellsHistory L'historique des ventes
     */
    public SerializableGestion(List<SerializableProduct> stock, double cashRegister, double bankAccount, List<Pair<String, Double>> debtors, List<Transaction> treasuryHistory, List<Sell> sellsHistory) {
        this.stock = stock;
        this.cashRegister = cashRegister;
        this.bankAccount = bankAccount;
        this.debtors = debtors;
        this.treasuryHistory = treasuryHistory;
        this.sellsHistory = sellsHistory;
    }

    /**
     * Constructeur prenant un objet Gestion pour initialiser les attributs.
     *
     * @param gestion L'objet Gestion à sérialiser
     */
    public SerializableGestion(Gestion gestion) {
        this(SerializableProduct.serializeProducts(gestion.getStock().getProducts()), gestion.getCashRegister(), gestion.getBankAccount(), new ArrayList<>(gestion.getDebtors()), new ArrayList<>(gestion.getTreasuryHistory()), new ArrayList<>(gestion.getSellsHistory()));
    }

    /**
     * Retourne la liste des produits sérialisables en stock.
     *
     * @return La liste des produits sérialisables en stock
     */
    public List<SerializableProduct> getStock() {
        return stock;
    }

    /**
     * Retourne le montant en caisse.
     *
     * @return Le montant en caisse
     */
    public double getCashRegister() {
        return cashRegister;
    }

    /**
     * Retourne le montant sur le compte bancaire.
     *
     * @return Le montant sur le compte bancaire
     */
    public double getBankAccount() {
        return bankAccount;
    }

    /**
     * Retourne la liste des débiteurs avec leur montant dû.
     *
     * @return La liste des débiteurs avec leur montant dû
     */
    public List<Pair<String, Double>> getDebtors() {
        return debtors;
    }

    /**
     * Retourne l'historique des transactions de trésorerie.
     *
     * @return L'historique des transactions de trésorerie
     */
    public List<Transaction> getTreasuryHistory() {
        return treasuryHistory;
    }

    /**
     * Retourne l'historique des ventes.
     *
     * @return L'historique des ventes
     */
    public List<Sell> getSellsHistory() {
        return sellsHistory;
    }
}
