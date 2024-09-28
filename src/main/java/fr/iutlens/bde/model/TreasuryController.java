package fr.iutlens.bde.model;

/**
 * Interface définissant les méthodes pour le contrôleur de trésorerie.
 */
public interface TreasuryController {

    /**
     * Retourne au menu principal.
     */
    void backToMenu();

    /**
     * Affiche l'historique de trésorerie.
     */
    void printTreasuryHistory();

    /**
     * Effectue un encaissement.
     */
    void cashIn();

    /**
     * Effectue un décaissement.
     */
    void cashOut();

    /**
     * Effectue un dépôt.
     */
    void deposit();

    /**
     * Effectue un retrait.
     */
    void withdraw();

    /**
     * Effectue un transfert de la caisse vers la banque.
     */
    void bankToCashRegister();

    /**
     * Effectue un transfert de la banque vers la caisse.
     */
    void cashRegisterToBank();

    /**
     * Définit l'objet de gestion pour le contrôleur.
     *
     * @param gestion l'objet de gestion à définir
     */
    void setGestion(Gestion gestion);
}