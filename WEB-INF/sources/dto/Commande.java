package dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Cette classe représente une commande de pizzas.
 * 
 * @author samy.vancalster.etu@unvi-lille.fr
 */
public class Commande {

    private int cno;

    private String name;

    private LocalDate date;

    private List<Pizza> panier;

    /**
     * Constructeur de la classe Commande.
     * 
     * @param id            L'identifiant de la commande.
     * @param userName      Le nom de l'utilisateur qui a passé la commande.
     * @param date     La date de la commande.
     * @param panier La liste des pizzas commandées.
     */
    public Commande(int id, String name, LocalDate date, List<Pizza> panier) {
        this.cno = id;
        this.name = name;
        this.date = date;
        this.panier = panier;
    }

    /**
     * Retourne l'identifiant de la commande.
     * 
     * @return L'identifiant de la commande.
     */
    public int getId() {
        return cno;
    }

    /**
     * Définit l'identifiant de la commande.
     * 
     * @param id L'identifiant de la commande.
     */
    public void setCno(int id) {
        this.cno = id;
    }

    /**
     * Retourne le nom de l'utilisateur qui a passé la commande.
     * 
     * @return Le nom de l'utilisateur.
     */
    public String getName() {
        return name;
    }

    /**
     * Définit le nom de l'utilisateur qui a passé la commande.
     * 
     * @param userName Le nom de l'utilisateur.
     */
    public void setName(String userName) {
        this.name = userName;
    }

    /**
     * Retourne la date et l'heure de la commande.
     * 
     * @return La date et l'heure de la commande.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Définit la date et l'heure de la commande.
     * 
     * @param orderDate La date et l'heure de la commande.
     */
    public void setDate(LocalDate orderDate) {
        this.date = orderDate;
    }

    /**
     * Retourne la liste des pizzas commandées.
     * 
     * @return La liste des pizzas commandées.
     */
    public List<Pizza> getPanier() {
        return panier;
    }

    /**
     * Définit la liste des pizzas commandées.
     * 
     * @param orderedPizzas La liste des pizzas commandées.
     */
    public void setPanier(List<Pizza> orderedPizzas) {
        this.panier = orderedPizzas;
    }

    /**
     * Calcule le prix total de la commande.
     * 
     * @return Le prix total de la commande.
     */
    public double getFinalPrice() {
        double totalPrice = 0;
        for (Pizza pizza : panier) {
            totalPrice += pizza.getFinalPrice();
        }
        return totalPrice;
    }

    /**
     * Retourne une représentation textuelle des détails de la commande.
     * 
     * @return Une chaîne de caractères représentant les détails de la commande.
     */
    @Override
    public String toString() {
        return "Command{" +
                "id=" + cno +
                ", userName='" + name + '\'' +
                ", orderDate=" + date +
                ", orderedPizzas=" + panier +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cno;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Commande other = (Commande) obj;
        return cno != other.cno;
    }

}
