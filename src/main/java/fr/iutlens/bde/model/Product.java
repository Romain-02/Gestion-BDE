package fr.iutlens.bde.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Produit du Stock du BDE
 * Représente un produit avec ses attributs et méthodes
 * pour le modifier.
 */
public class Product {

    /**
     * Énumération des catégories de produits.
     */
    public enum Category {SNACK, BEVERAGE, OTHER};

    /**
     * Nom du produit
     */
    private final String name;

    /**
     * Prix unitaire du produit
     */
    private final double unitPrice;

    /**
     * Prix total du produit (dépend de la quantité et des promotions)
     */
    private final DoubleProperty totalPrice;

    /**
     * Quantité du produit
     */
    private final IntegerProperty quantity;

    /**
     * Pourcentage de promotion pour le produit
     */
    private double discount;

    /**
     * Catégorie du produit
     */
    private final Category category;

    /**
     * Constructeur du produit avec les attributs spécifiés.
     *
     * @param name Nom du produit
     * @param unitPrice Prix unitaire du produit
     * @param quantity Quantité initiale du produit
     * @param discount Pourcentage de rabais pour le produit
     * @param category Catégorie du produit
     */
    public Product(String name, double unitPrice, int quantity, double discount, Category category) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalPrice = new SimpleDoubleProperty(getReducedPrice());
        this.discount = discount;
        this.category = category;
    }

    /**
     * Construit un produit avec un nom, un prix et une catégorie spécifiés.
     * Utilisé lorsque la quantité et le rabais sont par défaut (0).
     *
     * @param name Nom du produit
     * @param price Prix du produit
     * @param category Catégorie du produit
     */
    public Product(String name, double price, Category category){
        this(name, price, 0, 0, category);
    }

    /**
     * Construit un produit basé sur un produit existant et une quantité spécifiée.
     *
     * @param product Produit existant à cloner
     * @param quantity Quantité du produit
     */
    public Product(Product product, int quantity){
        this(product.name, product.unitPrice, quantity, product.discount, product.category);
    }

    /**
     * Désérialise un produit à partir d'un produit sérialisable.
     *
     * @param sProduct Produit sérialisable à désérialiser
     * @return Le produit désérialisé
     */
    public static Product deserializeProduct(SerializableProduct sProduct){
        return new Product(sProduct.getName(), sProduct.getUnitPrice(), sProduct.getQuantity(), sProduct.getDiscount(), sProduct.getCategory());
    }

    /**
     * Désérialise une liste de produits à partir d'une liste de produits sérialisables.
     *
     * @param sProducts Liste de produits sérialisables à désérialiser
     * @return Liste des produits désérialisés
     */
    public static List<Product> deserializeProducts(List<SerializableProduct> sProducts){
        List<Product> products = new ArrayList<>();
        for(SerializableProduct p : sProducts) products.add(deserializeProduct(p));
        return products;
    }

    /**
     * Met à jour le prix total du produit en fonction de la quantité et du rabais.
     */
    public void updateTotalPrice(){
        setTotalPrice(getReducedPrice());
    }

    /**
     * Incrémente la quantité du produit.
     *
     * @param quantity Quantité à ajouter
     */
    public void incrementQuantity(int quantity){
        this.quantity.setValue(getQuantity() + quantity);
        updateTotalPrice();
    }

    /**
     * Incrémente la quantité du produit de 1.
     */
    public void incrementQuantity(){
        incrementQuantity(1);
    }

    /**
     * Décrémente la quantité du produit de 1.
     *
     * @return true si la décrémentation a réussi, false sinon
     */
    public boolean decrementQuantity(){
        return decrementQuantity(1);
    }

    /**
     * Décrémente la quantité du produit de la valeur spécifiée.
     *
     * @param quantity Quantité à retirer
     * @return true si la décrémentation a réussi, false sinon
     */
    public boolean decrementQuantity(int quantity){
        if (getQuantity() < quantity) {
            updateTotalPrice();
            return false;
        }
        this.quantity.setValue(getQuantity() - quantity);
        updateTotalPrice();
        return true;
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
     * Retourne le prix total du produit.
     *
     * @return Le prix total du produit
     */
    public double getTotalPrice(){
        return totalPrice.getValue();
    }

    /**
     * Retourne la propriété du prix total du produit.
     *
     * @return La propriété du prix total du produit
     */
    public DoubleProperty getTotalPriceProperty(){
        return totalPrice;
    }

    /**
     * Retourne le prix réduit du produit en fonction de la quantité et du rabais.
     *
     * @return Le prix réduit du produit
     */
    public double getReducedPrice() {
        return unitPrice * (1 - discount) * getQuantity();
    }

    /**
     * Retourne la propriété de la quantité du produit.
     *
     * @return La propriété de la quantité du produit
     */
    public IntegerProperty getQuantityProperty(){
        return quantity;
    }

    /**
     * Retourne la quantité actuelle du produit.
     *
     * @return La quantité actuelle du produit
     */
    public int getQuantity() {
        return quantity.getValue();
    }

    /**
     * Retourne le pourcentage de rabais du produit.
     *
     * @return Le pourcentage de rabais du produit
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Retourne la catégorie du produit.
     *
     * @return La catégorie du produit
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Met à jour le prix total du produit.
     *
     * @param value Nouveau prix total du produit
     */
    public void setTotalPrice(double value){
        totalPrice.setValue(value);
    }

    /**
     * Met à jour la quantité du produit.
     *
     * @param quantity Nouvelle quantité du produit
     */
    public void setQuantity(int quantity) {
        this.quantity.setValue(quantity);
        updateTotalPrice();
    }

    /**
     * Met à jour le pourcentage de rabais du produit.
     *
     * @param discount Nouveau pourcentage de rabais du produit
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * Retourne une chaîne de caractères représentant les informations du produit.
     *
     * @return Une chaîne de caractères représentant le produit
     */
    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + unitPrice +
                ", quantity=" + quantity +
                ", discount=" + discount +
                '}';
    }

    /**
     * Vérifie si le produit est égal à un autre objet.
     *
     * @param o Objet à comparer
     * @return true si les objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name);
    }
}