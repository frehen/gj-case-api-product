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


