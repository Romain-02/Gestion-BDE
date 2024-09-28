package fr.iutlens.bde.model;

/**
 * Interface définissant les méthodes pour le contrôleur des ventes.
 */
public interface SellsController {

    /**
     * Retourne à la vue du stock.
     */
    void backToStock();

    /**
     * Définit l'objet de gestion pour le contrôleur.
     *
     * @param gestion l'objet de gestion à définir
     */
    void setGestion(Gestion gestion);
}