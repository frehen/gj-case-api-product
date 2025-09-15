# Fixes

This file contains the motivation behind the fixes for the Product API case.

## Fix 1: Typo in "numbersold" 

The ProductDTO.java contains a typo in the setter on ProductDTO.java:

```
setNumbersold -> setNumbersSold
```

This error causes the Swagger UI to present an incorrect model for the body:

```
{
  "category": "string",
  "id": 0,
  "imageLink": "string",
  "numbersold": 0,
  "numberSold": 0,
  "price": 0,
  "productName": "string",
  "unitCost": 0,
  "unitPrice": 0
}
```
## Fix 2: Failing tests

Tests `ProductControllerTest` and `ProductControllerTest` are failing. 

Fix this by implementing the DELETE method in the ProductController.java:

## Fix 3: Implement the PUT method

The PUT method in ProductController.java is not implemented.

Implement method and add tests.

Since both the request path and the request body contain the id of the product
it is important to check that these match. If there is a mismatch the REST operation
should return bad-request (400 status code).

Added controller advice to handle InvalidArgumentExceptions.
