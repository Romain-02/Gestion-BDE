package fr.iutlens.bde.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Représente une transaction financière avec son type, sa date et son montant.
 * Cette classe est Serializable.
 */
public class Transaction implements Serializable {

    /**
     * Énumération définissant différents types de transactions.
     */
    public enum Type {CASH_DEPOSIT, BANK_DEPOSIT, CASH_WITHDRAW, BANK_WITHDRAW, CASH_REGISTER_TO_BANK, BANK_TO_CASH_REGISTER}

    /**
     * Type de la transaction
     */
    private final Type type;

    /**
     * Date de la transaction
     */
    private final LocalDate date;

    /**
     * Montant de la transaction
     */
    private final double amount;

    /**
     * Constructeur d'une transaction avec un type spécifié, une date et un montant.
     *
     * @param type   Le type de la transaction
     * @param date   La date de la transaction
     * @param amount Le montant de la transaction
     */
    public Transaction(Type type, LocalDate date, double amount) {
        this.type = type;
        this.date = date;
        this.amount = amount;
    }

    /**
     * Constructeur d'une transaction avec la date actuelle, un type spécifié et un montant.
     *
     * @param type   Le type de la transaction
     * @param amount Le montant de la transaction
     */
    public Transaction(Type type, double amount) {
        this(type, LocalDate.now(), amount);
    }

    /**
     * Retourne le type de la transaction.
     *
     * @return Le type de la transaction
     */
    public Type getType() {
        return type;
    }

    /**
     * Retourne la date de la transaction.
     *
     * @return La date de la transaction
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Retourne le montant de la transaction.
     *
     * @return Le montant de la transaction
     */
    public double getAmount() {
        return amount;
    }
}
