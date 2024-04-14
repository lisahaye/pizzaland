[<- Retour](./../README.md)

# Documentation de l'API Ingredient

Cette documentation décrit les requêtes possibles et les retours attendus de l'API `Ingredient`.

| URI                           | Opération     |     Requête     | Réponse                                              | Code de retour                           |
|:------------------------      |:---------:    |:---------------:|:-----------------------------------------------------|------------------------------------------|
| /ingredients                  |`GET`          |                 |Liste des ingrédients                                 |`HTTP 200 OK`, `HTTP 404 Not Found`       |
| /ingredients/{id}             |`GET`          |                 |Un ingrédient                                         |`HTTP 200 OK`, `HTTP 404 Not Found`       |   
| /ingredients/{id}/{attribut}  |`GET`          |                 |L'attribut de l'ingrédient                            |`HTTP 200 OK`, `HTTP 404 Not Found`       |
| /ingredients                  |`POST`         | Ingrédient      |                                                      |`HTTP 201 Created`, `HTTP 400 Bad Request`|
| /ingredients/{id}             |`DELETE`       |                 |                                                      |`HTTP 200 OK`, `HTTP 404 Not Found`       |
| /ingredients                  |`PATCH`        | Ingrédient      |                                                      |`HTTP 200 OK`, `HTTP 404 Not Found`, `HTTP 400 Bad Request`|

## Méthodes d'appel

La classe `IngredientAPI` étend la classe abstraite `API` et définit quatres méthodes `doGet`, `doPatch`, `doPost`, `doDelete` et un constructeur par défaut. Ces méthodes correspondent aux méthodes HTTP GET, PATCH, POST et DELETE respectivement.

## Corps des requêtes

### I1

Un ingrédient est constitué d'un identifiant, d'un nom et d'un prix.

Voici sa représentation JSON :

```JSON
{
  "id": 21,
  "name": "Parmesan",
  "price": 2
}
```

### I2

Pour la création d'un ingrédient, on a juste besoin de son nom et de son prix car l'id est généré automatiquement.

Voici sa représentation JSON :
```JSON
{
  "name": "Parmesan",
  "price": 2
}
```


## Requêtes GET

### Obtenir tous les ingrédients

```bash
GET /ingredients
```

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` Si des ingrédients existent dans la base de données
- `HTTP 404 Not Found` Et une liste vide.

Exemple de retour : 
```json
[
  {
    "id": 1,
    "name": "Sauce tomate",
    "price": 1.5
  },
  {
    "id": 2,
    "name": "Mozzarella",
    "price": 2
  }
]
```


### Obtenir un ingrédient par son ID

```bash
GET /ingredients/{id}
```

Exemple de retours : 
```json
{
  "id": 1,
  "name": "Sauce tomate",
  "price": 1.5
}
```


Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` Si des ingrédients existent dans la base de données
- `HTTP 404 Not Found` Et une liste vide.


### Obtenir une propriété d'un ingrédient par son ID

```bash
GET /ingredients/{id}/{attribute}
```

Exemple de retours :
```json
"Sauce tomate"
```

Paramètres :
- `{id}` : L'ID de l'ingrédient à modifier
- `{attribut}` : Liste des attributs possibles :
    - `id`, `nom`, `prix`

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` si des ingrédients existent dans la base de données
- `HTTP 404 Not Found` et une liste vide.

## Requêtes PATCH

### Mettre à jour une propriété d'un ingrédient par son ID

```bash
PATCH /ingredients/{id}
```

Body de la rêquete
```json
{
    "attribute": "value"
}
```

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : Change l'attribut spécifié
- `HTTP 404 Not Found` : L'ingrédient est introuvable
- `HTTP 400 Bad Request` : Attribut inconnu

Exemple d'attributs :
- `name` : Au format d'une chaîne de caractères
- `price` : Au format d'un nombre à virgule

## Requêtes POST

### Ajouter un nouvel ingrédient

```bash
POST /ingredients
```

Body de la rêquete
```json
{
    "name": "string",
    "price": <double>
}
```

Cette requête renvoie un statut de réponse : 
- `HTTP 201 Created` : Ajoute un nouvel ingrédient dans la base de données
- `HTTP 400 Bad Request` : Problème dans le body de la rêquete, il manque peut-être des attributs à définir

## Requêtes DELETE

### Supprimer un ingrédient par son ID

```bash
DELETE /ingredients/{id}
```

Paramètres :
- `{id}` : L'ID de la pizza

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : Supprime l'ingrédient en fonction de l'ID
- `HTTP 404 Not Found` : L'ingrédient n'a pas été trouvé dans la base de données
