package fr.iutlens.bde.model;

/**
 * Interface définissant les méthodes pour le contrôleur de l'historique de trésorerie.
 */
public interface TreasuryHistoryController {

    /**
     * Retourne à la vue de la trésorerie principale.
     */
    void backToTreasury();

    /**
     * Annule la dernière transaction effectuée.
     */
    void cancelLastTransaction();

    /**
     * Définit l'objet de gestion pour le contrôleur.
     *
     * @param gestion l'objet de gestion à définir
     */
    void setGestion(Gestion gestion);
}