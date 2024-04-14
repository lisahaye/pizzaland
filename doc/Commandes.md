 [<- Retour](./../README.md)

# Documentation de l'API Commande

Cette documentation décrit les différentes requêtes possibles pour interagir avec notre API dédiée aux commandes, ainsi que les retours attendus pour chaque demande.

| URI                         | Opération |    Requête    | Réponse                   | Code de retour                                                              |
|:----------------------------|:---------:|:-------------:|:--------------------------|-----------------------------------------------------------------------------|
| /commandes                  |`GET`      |               | Liste des commandes (C1)  | `HTTP 200 OK`, `HTTP 404 Not Found`                                         |
| /commandes/{id}             |`GET`      |               | Une commande (C1)         | `HTTP 200 OK`, `HTTP 404 Not Found`                                         |   
| /commandes/{id}/{attribute} |`GET`      |               | L'attribut de la commande | `HTTP 200 OK`, `HTTP 404 Not Found`                                         |
| /commandes                  |`POST`     | Commande (C2) |                           | `HTTP 201 Created`, `HTTP 400 Bad Request`                                  |
| /commandes/{id}/{attribute} |`POST`     |               |                           | `HTTP 201 Created`, `HTTP 400 Bad Request`, `HTTP 404 Not Found`            |
| /commandes/{id}             |`DELETE`   |               |                           | `HTTP 200 OK`, `HTTP 400 Bad Request`, `HTTP 404 Not Found`                 |
| /commandes/{id}/{attribute} |`DELETE`   |               |                           | `HTTP 200 OK`, `HTTP 400 Bad Request`, `HTTP 404 Not Found`, `409 Conflict` |

## Méthodes d'appel

La classe `CommandeAPI` étend la classe abstraite `API` et définit quatres méthodes `doGet`, `doPatch`, `doPost`, `doDelete` et un constructeur par défaut. Ces méthodes correspondent aux méthodes HTTP GET, PATCH, POST et DELETE respectivement.

# Corps des requêtes

## C1

Une commande est constituée d'un identifiant, d'un nom, d'une date de livraison, d'un prix final et d'une liste de pizzas.

Voici sa représentation JSON :

```JSON
{
  "name": "Commande 1",
  "date": [
    2023,
    6,
    1
  ],
  "panier": [
    {
      "id": 1,
      "name": "Margherita",
      "pate": {
        "id": 1,
        "name": "Classique"
      },
      "price": 8.99,
      "ingredients": [
        {
          "id": 2,
          "name": "Mozzarella",
          "price": 2
        },
        {
          "id": 1,
          "name": "Sauce tomate",
          "price": 1.5
        }
      ],
      "finalPrice": 12.49
    },
    {
      "id": 2,
      "name": "Quattro Stagioni",
      "pate": {
        "id": 2,
        "name": "Fine"
      },
      "price": 10.99,
      "ingredients": [
        {
          "id": 6,
          "name": "CÅ“urs d'artichaut",
          "price": 2
        },
        {
          "id": 5,
          "name": "Olives",
          "price": 1.25
        },
        {
          "id": 4,
          "name": "Champignons",
          "price": 1.75
        },
        {
          "id": 3,
          "name": "Jambon",
          "price": 2.5
        },
        {
          "id": 2,
          "name": "Mozzarella",
          "price": 2
        },
        {
          "id": 1,
          "name": "Sauce tomate",
          "price": 1.5
        }
      ],
      "finalPrice": 21.990000000000002
    }
  ],
  "id": 1,
  "finalPrice": 34.480000000000004
}
```

## C2

Pour la création d'une commande, on a besoin de son nom, de sa date de livraison et de sa liste de pizzas car l'id et le prix final sont générés automatiquement.

Voici sa représentation JSON :
```JSON
{
  "name": "Commande 1",
  "date": [
    2023,
    6,
    1
  ],
  "panier": [
    {
      "id": 1,
      "name": "Margherita",
      "pate": {
        "id": 1,
        "name": "Classique"
      },
      "price": 8.99,
      "ingredients": [
        {
          "id": 2,
          "name": "Mozzarella",
          "price": 2
        },
        {
          "id": 1,
          "name": "Sauce tomate",
          "price": 1.5
        }
      ],
      "finalPrice": 12.49
    },
    {
      "id": 2,
      "name": "Quattro Stagioni",
      "pate": {
        "id": 2,
        "name": "Fine"
      },
      "price": 10.99,
      "ingredients": [
        {
          "id": 6,
          "name": "CÅ“urs d'artichaut",
          "price": 2
        },
        {
          "id": 5,
          "name": "Olives",
          "price": 1.25
        },
        {
          "id": 4,
          "name": "Champignons",
          "price": 1.75
        },
        {
          "id": 3,
          "name": "Jambon",
          "price": 2.5
        },
        {
          "id": 2,
          "name": "Mozzarella",
          "price": 2
        },
        {
          "id": 1,
          "name": "Sauce tomate",
          "price": 1.5
        }
      ],
      "finalPrice": 21.990000000000002
    }
  ]
}
```

## Requêtes GET

### Obtenir toutes les commandes

```bash
GET /commandes
```

Exemple de retour : 
```json
[
  {
    "name": "Commande 1",
    "date": [
      2023,
      6,
      1
    ],
    "panier": [
      {
        "id": 1,
        "name": "Margherita",
        "pate": {
          "id": 1,
          "name": "Classique"
        },
        "price": 8.99,
        "ingredients": [
          {
            "id": 2,
            "name": "Mozzarella",
            "price": 2
          },
          {
            "id": 1,
            "name": "Sauce tomate",
            "price": 1.5
          }
        ],
        "finalPrice": 12.49
      },
      {
        "id": 2,
        "name": "Quattro Stagioni",
        "pate": {
          "id": 2,
          "name": "Fine"
        },
        "price": 10.99,
        "ingredients": [
          {
            "id": 6,
            "name": "CÅ“urs d'artichaut",
            "price": 2
          },
          {
            "id": 5,
            "name": "Olives",
            "price": 1.25
          },
          {
            "id": 4,
            "name": "Champignons",
            "price": 1.75
          },
          {
            "id": 3,
            "name": "Jambon",
            "price": 2.5
          },
          {
            "id": 2,
            "name": "Mozzarella",
            "price": 2
          },
          {
            "id": 1,
            "name": "Sauce tomate",
            "price": 1.5
          }
        ],
        "finalPrice": 21.990000000000002
      }
    ],
    "id": 1,
    "finalPrice": 34.480000000000004
  },
  {
    "name": "Commande 2",
    "date": [
      2023,
      6,
      2
    ],
    "panier": [
      {
        "id": 2,
        "name": "Quattro Stagioni",
        "pate": {
          "id": 2,
          "name": "Fine"
        },
        "price": 10.99,
        "ingredients": [
          {
            "id": 6,
            "name": "CÅ“urs d'artichaut",
            "price": 2
          },
          {
            "id": 5,
            "name": "Olives",
            "price": 1.25
          },
          {
            "id": 4,
            "name": "Champignons",
            "price": 1.75
          },
          {
            "id": 3,
            "name": "Jambon",
            "price": 2.5
          },
          {
            "id": 2,
            "name": "Mozzarella",
            "price": 2
          },
          {
            "id": 1,
            "name": "Sauce tomate",
            "price": 1.5
          }
        ],
        "finalPrice": 21.990000000000002
      }
    ],
    "id": 2,
    "finalPrice": 21.990000000000002
  }
]
```

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` Si des commandes existent dans la base de données.
- `HTTP 404 Not Found` Et une liste vide.

### Obtenir une commande par son ID

```bash
GET /commandes/{id}
```

Paramètres :
- `{id}` : L'ID de la commandes a afficher

Exemple de retours : 
```json
{
  "name": "Commande 1",
  "date": [
    2023,
    6,
    1
  ],
  "panier": [
    {
      "id": 1,
      "name": "Margherita",
      "pate": {
        "id": 1,
        "name": "Classique"
      },
      "price": 8.99,
      "ingredients": [
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
      ],
      "finalPrice": 12.49
    },
    {
      "id": 2,
      "name": "Quattro Stagioni",
      "pate": {
        "id": 2,
        "name": "Fine"
      },
      "price": 10.99,
      "ingredients": [
        {
          "id": 1,
          "name": "Sauce tomate",
          "price": 1.5
        },
        {
          "id": 2,
          "name": "Mozzarella",
          "price": 2
        },
        {
          "id": 3,
          "name": "Jambon",
          "price": 2.5
        },
        {
          "id": 4,
          "name": "Champignons",
          "price": 1.75
        },
        {
          "id": 5,
          "name": "Olives",
          "price": 1.25
        },
        {
          "id": 6,
          "name": "CÅ“urs d'artichaut",
          "price": 2
        }
      ],
      "finalPrice": 21.990000000000002
    }
  ],
  "id": 1,
  "finalPrice": 34.480000000000004
}
```

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` Si des commandes existent dans la base de données.
- `HTTP 404 Not Found` Et une liste vide.

### Obtenir une propriété d'une commande par son ID

```bash
GET /commande/{id}/{attribute}
```

Paramètres :
- `{id}` : L'ID de la commandes à afficher.
- `{attribute}` : L'attribut à afficher.
    - `nom`, `date`, `panier`, `id`, `prixfinal`.

Exemple de retours :
```json
"Commande 1"
```

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` Si l'attribut a été trouvé.
- `HTTP 404 Not Found` Si la commandes n'a pas été trouvée.

## Requêtes POST

### Ajouter une nouvelle commande

```bash
POST /commandes
```

Paramètres requis :
- `name` : Sous forme d'une chaîne de caractères (Le nom doit être unique dans la base de données).
- `date` : Sous forme d'une chaîne de caractères.
- `panier` : Sous forme d'un tableau contenant les IDs des pizzas.

Exemple de body : 
```json
{
  "name": "Samy",
  "date": "2024-03-10",
  "panier": [
    {
      "id": 1
    },
    {
      "id": 2
    },
    {
      "id": 3
    }
  ]
}
```

Cette requête renvoie un statut de réponse : 
- `HTTP 201 Created` : La nouvelle commande a été sauvegardé dans la base de données et un ID lui est attribué.
- `HTTP 400 Bad Request` : Erreur lors de la sauvegarde de la nouvelle commande.

### Ajouter une pizza dans une commande (beta)

```bash
POST /commandes/{id}/{idPizza}
```

Paramètres :
- `{id}` : L'ID de la commande où ajouter la pizza.
- `{idPizza}` : L'ID de la pizza à ajouter de la commande.


Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : La commande a été supprimée de la base de données.
- `HTTP 400 Bad Request` : Erreur lors de la suppression de la commande, exemple l'ID n'est pas un nombre.
- `HTTP 404 Not Found` : Aucune commande ayant cet ID n'a été trouvée dans la base de données.
- `HTTP 409 Conflict` : La pizza a déjà été associée à la commande.


## Requêtes DELETE

### Supprimer une commande (beta)

```bash
DELETE /commandes/{id}
```
Paramètres :
- `{id}` : L'ID de la commande à supprimer

Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : La commande a été supprimée de la base de données.
- `HTTP 400 Bad Request` : Erreur lors de la suppression de la commande, exemple l'ID n'est pas un nombre.
- `HTTP 404 Not Found` : Aucune commande ayant cet ID n'a été trouvée dans la base de données.

### Supprimer une pizza dans une commande (beta)

```bash
DELETE /commandes/{id}/{idPizza}
```

Paramètres :
- `{id}` : L'ID de la commande sur laquelle il faut supprimer une pizza
- `{idPizza}` : L'ID de la pizza à supprimer


Cette requête renvoie un statut de réponse : 
- `HTTP 200 OK` : La commande a été supprimée de la base de données.
- `HTTP 400 Bad Request` : Erreur lors de la suppression de la commande, exemple l'ID n'est pas un nombre.
- `HTTP 404 Not Found` : Aucune commande ayant cet ID n'a été trouvée dans la base de données.
