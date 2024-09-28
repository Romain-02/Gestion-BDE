package fr.iutlens.bde.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un produit sérialisable.
 * Utilisée pour sérialiser et désérialiser les produits.
 */
public class SerializableProduct implements Serializable {

    /**
     * Nom du produit.
     */
    private final String name;

    /**
     * Prix unitaire du produit.
     */
    private final double unitPrice;

    /**
     * Quantité de produit.
     */
    private final int quantity;

    /**
     * Pourcentage de rabais pour le produit.
     */
    private final double discount;

    /**
     * Catégorie du produit.
     */
    private final Product.Category category;

    /**
     * Constructeur avec tous les attributs spécifiés.
     *
     * @param name Nom du produit
     * @param unitPrice Prix unitaire du produit
     * @param quantity Quantité de produit
     * @param discount Pourcentage de rabais pour le produit
     * @param category Catégorie du produit
     */
    public SerializableProduct(String name, double unitPrice, int quantity, double discount, Product.Category category) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = discount;
        this.category = category;
    }

    /**
     * Sérialise un produit en un produit sérialisable.
     *
     * @param product Le produit à sérialiser
     * @return Le produit sérialisé
     */
    public static SerializableProduct serializeProduct(Product product){
        return new SerializableProduct(product.getName(), product.getUnitPrice(), product.getQuantity(), product.getDiscount(), product.getCategory());
    }

    /**
     * Sérialise une liste de produits en une liste de produits sérialisables.
     *
     * @param products La liste de produits à sérialiser
     * @return La liste de produits sérialisés
     */
    public static List<SerializableProduct> serializeProducts(List<Product> products){
        List<SerializableProduct> sProducts = new ArrayList<>();
        for(Product p : products) sProducts.add(serializeProduct(p));
        return sProducts;
    }

    /**
     * Retourne le nom du produit.
     *
     * @return Le nom du produit
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne le prix unitaire du produit.
     *
     * @return Le prix unitaire du produit
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Retourne la quantité de produit.
     *
     * @return La quantité de produit
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Retourne le pourcentage de rabais pour le produit.
     *
     * @return Le pourcentage de rabais pour le produit
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Retourne la catégorie du produit.
     *
     * @return La catégorie du produit
     */
    public Product.Category getCategory() {
        return category;
    }
}