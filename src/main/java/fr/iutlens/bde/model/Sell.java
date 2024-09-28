package fr.iutlens.bde.model;

import javafx.util.Pair;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une vente dans le système.
 * Contient les produits vendus, le prix total et la date de la vente.
 */
public class Sell implements Serializable {

    /**
     * Liste des produits vendus sous forme de paires (nom du produit, quantité vendue).
     */
    private final List<Pair<String, Integer>> products;

    /**
     * Prix total de la vente.
     */
    private final double totalPrice;

    /**
     * Date de la vente.
     */
    private final LocalDate date;

    /**
     * Constructeur avec les paramètres traditionnels.
     *
     * @param products Liste des produits vendus
     * @param totalPrice Prix total de la vente
     * @param date Date de la vente
     */
    public Sell(ArrayList<Pair<String, Integer>> products, double totalPrice, LocalDate date) {
        this.products = products;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    /**
     * Constructeur à partir d'un objet Basket.
     * Initialise la vente avec les produits et le prix total du panier, et la date actuelle.
     *
     * @param basket Panier contenant les produits vendus
     */
    public Sell(Stock.Basket basket) {
        this(listProductToListPair(basket), basket.calculatePrice(), LocalDate.now());
    }

    /**
     * Constructeur par défaut.
     * Initialise une vente vide avec un prix total de 0 et la date actuelle.
     */
    public Sell(){
        this(new ArrayList<>(), 0, LocalDate.now());
    }

    /**
     * Méthode statique pour convertir un Basket en une liste de paires (nom du produit, quantité vendue).
     *
     * @param basket Panier à convertir
     * @return Liste de paires représentant les produits et leurs quantités
     */
    public static ArrayList<Pair<String, Integer>> listProductToListPair(Stock.Basket basket) {
        ArrayList<Pair<String, Integer>> list = new ArrayList<>();
        for (Product p : basket.getProducts()) {
            list.add(new Pair<>(p.getName(), p.getQuantity()));
        }
        return list;
    }

    /**
     * Retourne la liste des produits vendus.
     *
     * @return Liste des produits vendus
     */
    public List<Pair<String, Integer>> getProducts() {
        return products;
    }

    /**
     * Retourne le prix total de la vente.
     *
     * @return Prix total de la vente
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Retourne la date de la vente.
     *
     * @return Date de la vente
     */
    public LocalDate getDate() {
        return date;
    }
}