# gj-case-api-product

This project contains a Case study for the API Product.

The project consists of a Spring Boot application with a REST API and an H2-database with
a Product table. The database is maintained using Flyway.

## Setup

The project uses Java 8 as the target and can be run from the command line with:

```
mvn spring-boot:run
```

To test the project, run:

```
mvn clean test
```

API documentation is available at http://localhost:8080/swagger-ui/index.html

The H2-database is available at http://localhost:8080/h2-console/

## Docker support

To build a docker image, run (from the project root):

```
docker build -t api-product .
```

To run the docker image, run:

```
docker run --name api-product -p 8080:8080 -d api-product
```

To remove running image, run:

```
docker rm -f api-product 
```

## Request examples

Get all products:

```
curl -X GET http://localhost:8080/products/ | jq
```

Get a product by id (4 in this case):

```
curl -X GET http://localhost:8080/products/4 | jq
```

Create a new product (id is generated automatically and returned in the response):

```
curl -X POST "http://localhost:8080/products/" -H "Content-Type: application/json" \
-d '{
  "category": "Helseforsikring",
  "productName": "HelseBasis",
  "imageLink": "static.gjensidige.com/",
  "numberSold": 10,
  "price": 500.0,
  "unitCost": 20.0,
  "unitPrice": 30.0
}' | jq
```

Update a product (id must be present in the request path):

```
curl -X PUT "http://localhost:8080/products/4" -H "Content-Type: application/json" \
-d '{
  "id": 4,
  "category": "HelseforsikringNy",
  "imageLink": "static.gjensidige.com/",
  "numberSold": 5001,
  "price": 501.0,
  "productName": "Helse1Ny",
  "unitCost": 75.5,
  "unitPrice": 100.5
}' | jq
```

Delete a product (4 in this case):

```
curl -X DELETE "http://localhost:8080/products/4"
```

Get financial report:

```
curl -X GET "http://localhost:8080/reports/financial"
```
