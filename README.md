# SAÉ API REST

## Groupe
- Samy Van Calster
- Lisa Haye

## Les APIs

- [Ingrédients](./doc/Ingredients.md)
- [Pâtes](./doc/Pates.md)
- [Pizza](./doc/Pizzas.md)
- [Commande](./doc/Commandes.md)
- [Token](./doc/Tokens.md)

## Information

La première étape est de se connecter sur la base de données SQL et de lancer le script `init.SQL`.

Ensuite si il y a une erreur `Internal serveur error`. Il faut vérifier le `config.prop` qui permet la connexion à la base de données.

Si vous voulez utiliser les autres requêtes que les requêtes `GET`. Il faut utiliser le token de connexion dans la partie authentification de chaque requête par défaut le login et le mot de passe est `admin/admin` ce qui devient votre token.