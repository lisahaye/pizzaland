/**
* Classe DTO (Data Transfer Object) représentant un jeton d'api.
* Contient les informations de l'utilisateur et du mot de passe permettant d'accéder à l'api.
*/
package dto;

/**
* Classe représentant un jeton d'api.
* Contient les informations de l'utilisateur et du mot de passe permettant d'accéder à l'api.
*
* @author Samy Van Calster 
*/
public class APIToken {
   /**
    * Utilisateur associé au jeton.
    */
   private String user;

   /**
    * Mot de passe associé au jeton.
    */
   private String password;

   /**
    * Initialise un nouveau jeton d'api avec l'utilisateur et le mot de passe donnés.
    *
    * @param user Utilisateur associé au jeton.
    * @param password Mot de passe associé au jeton.
    */
   public APIToken(String user, String password) {
       this.user = user;
       this.password = password;
   }

   /**
    * Retourne l'utilisateur associé au jeton.
    *
    * @return L'utilisateur associé au jeton.
    */
   public String getUser() {
       return user;
   }

   /**
    * Modifie l'utilisateur associé au jeton.
    *
    * @param user Nouvel utilisateur associé au jeton.
    */
   public void setUser(String user) {
       this.user = user;
   }

   /**
    * Retourne le mot de passe associé au jeton.
    *
    * @return Le mot de passe associé au jeton.
    */
   public String getPassword() {
       return password;
   }

   /**
    * Modifie le mot de passe associé au jeton.
    *
    * @param password Nouveau mot de passe associé au jeton.
    */
   public void setPassword(String password) {
       this.password = password;
   }

@Override
public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
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
    APIToken other = (APIToken) obj;
    if (user == null) {
        if (other.user != null)
            return false;
    } else if (!user.equals(other.user))
        return false;
    if (password == null) {
        if (other.password != null)
            return false;
    } else if (!password.equals(other.password))
        return false;
    return true;
}
}