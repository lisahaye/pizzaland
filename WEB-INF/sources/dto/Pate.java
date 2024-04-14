package dto;

/**
 * La classe Pate représente une pâte utilisée dans une recette.
 *
 * @author Samy Van Calster
 * @author Lisa Haye
 */
public class Pate {

    /** L'identifiant de la pâte */
    private int id;

    /** Le nom de la pâte */
    private String name;

    /**
     * Constructeur par défaut de la classe Pate.
     * Ce constructeur est requis pour la désérialisation JSON.
     */
    public Pate() {
    }

    /**
     * Constructeur de la classe Pate avec des paramètres.
     *
     * @param id   L'identifiant de la pâte.
     * @param name Le nom de la pâte.
     */
    public Pate(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Retourne le nom de la pâte.
     *
     * @return Le nom de la pâte.
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne l'identifiant de la pâte.
     *
     * @return L'identifiant de la pâte.
     */
    public int getId() {
        return id;
    }

    /**
     * Définit le nom de la pâte.
     *
     * @param name Le nom à définir pour la pâte.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retourne le code de hachage de la pâte.
     *
     * @return Le code de hachage de la pâte.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /**
     * Retourne une représentation textuelle de la pâte.
     *
     * @return Une représentation textuelle de la pâte.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compare la pâte avec un autre objet pour vérifier s'ils sont égaux.
     *
     * @param obj L'objet à comparer avec la pâte.
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
        Pate other = (Pate) obj;
        return id != other.id;
    }
}
