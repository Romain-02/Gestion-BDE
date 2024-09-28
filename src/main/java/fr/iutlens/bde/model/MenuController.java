package fr.iutlens.bde.model;

/**
 * Interface définissant les méthodes pour le contrôleur de menu.
 */
public interface MenuController {

    /**
     * Change de scène en fonction du numéro d'option donné.
     *
     * @param numOption le numéro de l'option indiquant la scène à afficher
     */
    void switchScene(int numOption);

    /**
     * Définit l'objet de gestion pour le contrôleur.
     *
     * @param gestion l'objet de gestion à définir
     */
    void setGestion(Gestion gestion);
}