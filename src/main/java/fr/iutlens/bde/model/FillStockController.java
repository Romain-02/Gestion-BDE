package fr.iutlens.bde.model;

/**
 * Interface définissant les méthodes pour le contrôleur de remplissage du stock.
 */
public interface FillStockController {

    /**
     * Retourne à l'écran de gestion du stock.
     */
    void backToStock();

    /**
     * Ajoute un produit au stock.
     */
    void addProduct();

    /**
     * Supprime un produit du stock.
     */
    void deleteProduct();

    /**
     * Définit l'objet de gestion pour le contrôleur.
     *
     * @param gestion l'objet de gestion à définir
     */
    void setGestion(Gestion gestion);
}