[<- Retour](./../README.md)

# Documentation de l'API Pâte

Cette documentation décrit les différentes requêtes possibles pour interagir avec notre API dédiée aux pâtes, ainsi que les retours attendus pour chaque demande.

| URI                     | Opération     |  Requête   | Réponse               | Code de retour                                               |
|:------------------------|:---------:    |:----------:|:----------------------|--------------------------------------------------------------|
| /pates                  |`GET`          |            | Liste des pâtes (P1)  | `HTTP 200 OK`, `HTTP 404 Not Found`                          |
| /pates/{id}             |`GET`          |            | Une pâte (P1)         | `HTTP 200 OK`, `HTTP 404 Not Found`                          |   
| /pates/{id}/{attribute} |`GET`          |            | L'attribut de la pâte | `HTTP 200 OK`, `HTTP 404 Not Found`                          |
| /pates                  |`POST`         | Pâte (P2)  |                       | `HTTP 201 Created`, `HTTP 400 Bad Request`                   |
| /pates/{id}             |`DELETE`       |            |                       | `HTTP 200 OK`, `HTTP 404 Not Found`                          |
| /pates                  |`PATCH`        | Pâte  (P2) |                       | `HTTP 200 OK`, `HTTP 404 Not Found`, `HTTP 400 Bad Request`  |

## Méthodes d'appel

La classe `IngredientAPI` étend la classe abstraite `API` et définit quatres méthodes `doGet`, `doPatch`, `doPost`, `doDelete` et un constructeur par défaut. Ces méthodes correspondent aux méthodes HTTP GET, PATCH, POST et DELETE respectivement.

## Requêtes GET

## Corps des requêtes

### P1

Une pâte est constituée d'un identifiant et d'un nom.

Voici sa représentation JSON :

```JSON
{
  "id": 1,
  "name": "Classique"
}
```

### P2

Pour la création d'une pâte, on a juste besoin de son nom car l'id est généré automatiquement.

Voici sa représentation JSON :
```JSON
{
  "name": "Classique"
}
```


### Obtenir toutes les pâtes

```bash
GET /pates
```

Exemple de retour : 
```json
[
  {
    "id": 1,
    "name": "Classique"
  },
  {
    "id": 2,
    "name": "Fine"
  }
]
```

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : Retourne une liste de pâtes dans le corps de la réponse, au format JSON.
- `HTTP 404 Not Found` : Aucune pâte n'a été trouvée dans la base de données.

### Obtenir une pâte spécifique par son ID

```bash
GET /pates/{id}
```

Paramètres :
- `{id}` : L'ID de la pâte à récupérer

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : Retourne une pâte au format JSON dans le corps de la réponse.
- `HTTP 404 Not Found` : Aucune pâte ayant cet ID n'a été trouvée dans la base de données.

### Obtenir un attribut de pâte par son ID

```bash
GET /pates/{id}/{attribute}
```

Exemple de retour : 
```json
"Classique"
```

Paramètres :
- `{id}` : L'ID de la pâte à modifier
- `{attribut}` : Liste des attributs possibles :
  - `id`, `nom`


Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : Retourne le nom de la pâte dans le corps de la réponse.
- `HTTP 404 Not Found` : Aucune pâte ayant cet ID n'a été trouvée dans la base de données.

## Requêtes POST

```bash
POST /pates
```

Paramètres requis :
- `name` : Sous forme d'une chaîne de caractères

Exemple de body : 
```json
{
  "name": "Classique"
}
```

Cette requête renvoie un statut de réponse : 
- `HTTP 201 Created` : La nouvelle pâte a été sauvegardée dans la base de données et un ID lui est attribué.
- `HTTP 400 Bad Request` : Erreur lors de la sauvegarde de la nouvelle pâte.

## Requêtes PATCH

```bash
PATCH /pates/{id}
```

Paramètres :
- `{id}` : L'ID de la pâte à mettre à jour

Exemple de body : 
```json
{
  "name": "Classique"
}
```

> Attention le nom d'une pâte doit être unique !

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : La pâte a été mise à jour dans la base de données et un ID lui reste inchangé.
- `HTTP 400 Bad Request` : Erreur lors de la mise à jour de la pâte, Par exemple une pâte portant le même nom existe déjà.
- `HTTP 404 Not Found` : Aucune pâte ayant cet ID n'a été trouvée dans la base de données.

## Requêtes DELETE

```bash
DELETE /pates/{id}
```

Paramètres :
- `{id}` : L'ID de la pâte à supprimer

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : La pâte a été supprimée de la base de données.
- `HTTP 400 Bad Request` : Erreur lors de la suppression de la pâte, exemple l'ID n'est pas un nombre . 
- `HTTP 404 Not Found` : Aucune pâte ayant cet ID n'a été trouvée dans la base de données.





