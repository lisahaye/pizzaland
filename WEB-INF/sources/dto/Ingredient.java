package dto;

/**
 * Représente un ingrédient utilisé dans une pizza.
 * Cette classe a été conçue pour gérer les ingrédients utilisés dans la composition des pizzas.
 * Les ingrédients ont un identifiant unique, un nom et un prix.
 * Le prix par défaut est de 0 euro.
 * 
 * @author Samy Van Calster
 * @author Lisa Haye
 */
public class Ingredient {

    /** L'identifiant de l'ingrédient */
    private int id;

    /** Le nom de l'ingrédient */
    private String name;

    /** Le prix de l'ingrédient en euros */
    private double price = DEFAULT_PRICE;

    /** Le prix par défaut de l'ingrédient en euros */
    private static final int DEFAULT_PRICE = 0;

    /**
     * Constructeur par défaut de la classe Ingredient.
     * Ce constructeur est requis pour la désérialisation JSON.
     */
    public Ingredient() {
    }

    /**
     * Constructeur de la classe Ingredient avec des paramètres.
     *
     * @param id    L'identifiant de l'ingrédient.
     * @param name  Le nom de l'ingrédient.
     * @param price Le prix de l'ingrédient.
     */
    public Ingredient(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Constructeur de la classe Ingredient avec un prix par défaut.
     *
     * @param id   L'identifiant de l'ingrédient.
     * @param name Le nom de l'ingrédient.
     */
    public Ingredient(int id, String name) {
        this(id, name, DEFAULT_PRICE);
    }

    /**
     * Retourne l'identifiant de l'ingrédient.
     *
     * @return L'identifiant de l'ingrédient.
     */
    public int getId() {
        return id;
    }

    /**
     * Modifie l'identifiant de l'ingrédient.
     *
     * @param id Le nouvel identifiant de l'ingrédient.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retourne le nom de l'ingrédient.
     *
     * @return Le nom de l'ingrédient.
     */
    public String getName() {
        return name;
    }

    /**
     * Modifie le nom de l'ingrédient.
     *
     * @param name Le nouveau nom de l'ingrédient.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retourne le prix de l'ingrédient.
     *
     * @return Le prix de l'ingrédient en euros.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Modifie le prix de l'ingrédient.
     *
     * @param price Le nouveau prix de l'ingrédient en euros.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Retourne le code de hachage de l'ingrédient.
     *
     * @return Le code de hachage de l'ingrédient.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /**
     * Compare l'ingrédient avec un autre objet pour vérifier s'ils sont égaux.
     *
     * @param obj L'objet à comparer avec l'ingrédient.
     * @return true si les objets sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ingredient other = (Ingredient) obj;
        return id != other.id;
    }

    /**
     * Retourne une représentation textuelle de l'ingrédient.
     *
     * @return Une chaîne de caractères représentant l'ingrédient avec son nom et son prix.
     */
    @Override
    public String toString() {
        return name + " (" + price + "€)";
    }
}