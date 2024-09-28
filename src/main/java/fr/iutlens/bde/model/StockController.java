package fr.iutlens.bde.model;

/**
 * Interface définissant les méthodes pour le contrôleur de stock.
 */
public interface StockController {

    /**
     * Navigue vers l'historique du stock.
     */
    void switchToStockHistory();

    /**
     * Navigue vers le remplissage du stock.
     */
    void switchToFillStock();

    /**
     * Retourne au menu principal.
     */
    void backToMenu();

    /**
     * Vente de produits.
     */
    void sell();

    /**
     * Définit l'objet de gestion pour le contrôleur.
     *
     * @param gestion l'objet de gestion à définir
     */
    void setGestion(Gestion gestion);
}