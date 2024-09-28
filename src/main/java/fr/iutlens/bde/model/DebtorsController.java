package fr.iutlens.bde.model;

/**
 * Interface définissant les méthodes pour le contrôleur des débiteurs.
 */
public interface DebtorsController {

    /**
     * Ajoute un débiteur.
     */
    void addDebtor();

    /**
     * Supprime un débiteur.
     */
    void deleteDebtor();

    /**
     * Retourne au menu principal.
     */
    void backToMenu();

    /**
     * Définit l'objet de gestion pour le contrôleur.
     *
     * @param gestion l'objet de gestion à définir
     */
    void setGestion(Gestion gestion);
}