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

## Fix 4: Financal report, Wrong request path

I assume that the financial report should be available at `/report/financial`

There is currently an error in the controller mapping:

```
    @GetMapping(name = "/financial")
    public FinancialReport getFinancialReport(){
```

Should be:

```
    @GetMapping(value = "/financial")
                ----- 
    public FinancialReport getFinancialReport(){
```

## Fix 5: Implement the Financal report

Added implementation of controller with test.

Added initial dummy implementation of ReportService with dummy test.

## Fix 6: Dockerized application

Added Dockerfile. Added build instructions to README.md. 

## Fix 7: Added missing leastSoldProduct

Discovered that the leastSoldProduct was missing from the provided FinancialReport implementation.

Added it.

### Fix 8: Fixed inconsistent naming in ProductDTO

The ProductDTO.java contains inconsistent naming in the setter methods:

```
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
```

The `case.pdf` document refers to `unit price`, so I changed the setter to `setUnitPrice`.

Note, the original code worked, because Spring maps the `unitPrice` field in the PUT request to the `priceField` 
field directly using reflection. This overwrites the `price` field in the request.

This fix removes the `price` field from the Swagger model.

I also updated the example request for POST and PUT in the `README.md` file to not use the `price` field.


