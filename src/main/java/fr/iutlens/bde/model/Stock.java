package fr.iutlens.bde.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant le stock de produits.
 * Permet de gérer les produits, les catégories et les opérations sur le stock.
 */
public class Stock {

    /**
     * Liste des produits en stock.
     */
    private final List<Product> products;

    /**
     * Liste observable des produits en stock.
     */
    private final ObservableList<Product> observableProducts;

    /**
     * Constructeur initialisant le stock avec une liste de produits.
     *
     * @param products Liste des produits à ajouter au stock
     */
    public Stock(List<Product> products) {
        this.products = products;
        this.observableProducts = productToObservableList();
    }

    /**
     * Constructeur sans paramètres initialisant un stock vide.
     */
    public Stock() {
        this(new ArrayList<>());
    }

    /**
     * Retourne la liste des produits en stock.
     *
     * @return Liste des produits en stock
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Retourne la liste observable des produits en stock.
     *
     * @return Liste observable des produits en stock
     */
    public ObservableList<Product> getObservableProducts() {
        return observableProducts;
    }

    /**
     * Retourne une liste des produits appartenant à une catégorie spécifiée.
     *
     * @param category Catégorie des produits à récupérer
     * @return Liste des produits de la catégorie spécifiée
     */
    public List<Product> getCategorizedProduct(Product.Category category) {
        List<Product> listProducts = new ArrayList<>();
        for (Product p : products) {
            if (p.getCategory() == category)
                listProducts.add(p);
        }
        return listProducts;
    }

    /**
     * Convertit les produits en une liste observable.
     *
     * @return Liste observable des produits
     */
    public ObservableList<Product> productToObservableList() {
        List<Product> listProducts = new ArrayList<>();
        listProducts.add(new Product("", 0, Product.Category.SNACK));
        listProducts.addAll(getCategorizedProduct(Product.Category.SNACK));
        listProducts.add(new Product("", 0, Product.Category.BEVERAGE));
        listProducts.addAll(getCategorizedProduct(Product.Category.BEVERAGE));
        listProducts.add(new Product("", 0, Product.Category.OTHER));
        listProducts.addAll(getCategorizedProduct(Product.Category.OTHER));
        return FXCollections.observableArrayList(listProducts);
    }

    /**
     * Retourne un produit en fonction de son nom.
     *
     * @param name Nom du produit à récupérer
     * @return Produit correspondant au nom spécifié
     */
    public Product getProduct(String name) {
        for (Product p : products)
            if (p.getName().equals(name))
                return p;
        return null;
    }

    /**
     * Ajoute un produit au stock.
     *
     * @param product Produit à ajouter
     */
    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
            updateObservableList();
        }
    }

    /**
     * Supprime un produit du stock.
     *
     * @param product Produit à supprimer
     */
    public void deleteProduct(Product product) {
        products.remove(product);
        updateObservableList();
    }

    /**
     * Met à jour la liste observable des produits en stock.
     */
    public void updateObservableList() {
        observableProducts.clear();
        observableProducts.setAll(productToObservableList());
    }

    /**
     * Vérifie si un produit est contenu dans le stock.
     *
     * @param product Produit à vérifier
     * @return true si le produit est contenu dans le stock, false sinon
     */
    public boolean doesNotContainProduct(Product product) {
        Product p = getProduct(product.getName());
        return (p == null || p.getQuantity() < product.getQuantity());
    }

    /**
     * Vérifie si tous les produits d'un panier sont contenus dans le stock.
     *
     * @param basket Panier à vérifier
     * @return true si tous les produits du panier sont contenus dans le stock, false sinon
     */
    public boolean containBasket(Basket basket) {
        for (Product product : basket.products) {
            if (doesNotContainProduct(getProduct(product.getName()))) return false;
        }
        return true;
    }

    /**
     * Vend un produit en décrémentant sa quantité dans le stock.
     *
     * @param product Produit à vendre
     */
    public void sellProduct(Product product) {
        if (doesNotContainProduct(product)) return;
        getProduct(product.getName()).decrementQuantity(product.getQuantity());
        updateObservableList();
    }

    /**
     * Vend tous les produits d'un panier en décrémentant leurs quantités dans le stock.
     *
     * @param basket Panier à vendre
     * @return true si la vente a réussi, false sinon
     */
    public boolean sellBasket(Basket basket) {
        if (!containBasket(basket)) return false;
        for (Product p : basket.products)
            sellProduct(p);
        return true;
    }

    /**
     * Classe interne représentant un panier de produits.
     * Permet de gérer les produits dans un panier et de calculer le prix total.
     */
    public class Basket {

        /**
         * Liste des produits dans le panier.
         */
        private final List<Product> products;

        /**
         * Propriété du prix total du panier.
         */
        private final DoubleProperty price;

        /**
         * Pourcentage de rabais pour le panier.
         */
        private double discount;

        /**
         * Constructeur initialisant le panier avec une liste de produits et un rabais.
         *
         * @param products Liste des produits à ajouter au panier
         * @param discount Pourcentage de rabais pour le panier
         */
        public Basket(List<Product> products, double discount) {
            this.products = products;
            this.discount = discount;
            this.price = new SimpleDoubleProperty(calculatePrice());
            updateBasket();
        }

        /**
         * Constructeur sans paramètres initialisant un panier vide.
         */
        public Basket() {
            this(new ArrayList<>(), 0);
        }

        /**
         * Vérifie si le panier est vide.
         *
         * @return true si le panier est vide, false sinon
         */
        public boolean isEmpty() {
            for (Product p : products)
                if (p != null && p.getQuantity() > 0)
                    return false;
            return true;
        }

        /**
         * Met à jour le panier en ajoutant les produits qui ne sont pas encore présents.
         */
        public void updateBasket() {
            for (Product p : Stock.this.getProducts())
                if (!products.contains(p))
                    addProduct(new Product(p, 0));
        }

        /**
         * Met à jour le prix total du panier.
         */
        public void updateBasketPrice() {
            setPrice(calculatePrice());
        }

        /**
         * Vide le panier en mettant toutes les quantités de produits à 0.
         */
        public void clearBasket() {
            for (Product p : products) p.setQuantity(0);
            updateBasketPrice();
        }

        /**
         * Ajoute un produit au panier.
         *
         * @param product Produit à ajouter
         */
        public void addProduct(Product product) {
            products.add(product);
            System.out.println(products);
            updateBasketPrice();
        }

        /**
         * Incrémente la quantité d'un produit dans le panier.
         *
         * @param product Produit dont la quantité doit être incrémentée
         */
        public boolean incrementProductQuantity(Product product) {
            Product p = Stock.this.getProduct(product.getName());
            if (p == null || p.getQuantity() <= product.getQuantity()) return false;
            product.incrementQuantity();
            updateBasketPrice();
            return true;
        }

        /**
         * Décrémente la quantité d'un produit dans le panier.
         *
         * @param product Produit dont la quantité doit être décrémentée
         */
        public void decrementProductQuantity(Product product) {
            if (product == null || !product.decrementQuantity()) return;
            updateBasketPrice();
        }

        /**
         * Calcule le prix total du panier en fonction des produits et du rabais.
         *
         * @return Le prix total du panier
         */
        public double calculatePrice() {
            double price = 0;
            for (Product p : products)
                price += p.getReducedPrice();
            return price * (1 - discount);
        }

        /**
         * Retourne la liste des produits dans le panier.
         *
         * @return Liste des produits dans le panier
         */
        public List<Product> getProducts() {
            return products;
        }

        /**
         * Met à jour le pourcentage de rabais pour le panier.
         *
         * @param discount Nouveau pourcentage de rabais pour le panier
         */
        public void setDiscount(double discount){
            this.discount = discount;
            updateBasketPrice();
        }

        /**
         * Retourne le produit correspondant au nom spécifié dans le panier.
         *
         * @param name Nom du produit à récupérer
         * @return Produit correspondant au nom spécifié, ou null si aucun produit ne correspond
         */
        public Product getProduct(String name){
            for (Product p : products)
                if (p.getName().equals(name))
                    return p;
            return null;
        }

        /**
         * Retourne le prix actuel du panier.
         *
         * @return Prix actuel du panier
         */
        public double getPrice(){
            return price.getValue();
        }

        /**
         * Retourne la propriété du prix du panier.
         *
         * @return Propriété du prix du panier
         */
        public DoubleProperty getPriceProperty(){
            return price;
        }

        /**
         * Met à jour le prix du panier avec la valeur spécifiée.
         *
         * @param value Nouvelle valeur du prix du panier
         */
        public void setPrice(double value){
            price.setValue(value);
        }
    }
}
