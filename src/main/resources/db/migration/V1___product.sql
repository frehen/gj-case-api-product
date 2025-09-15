create table product(
  id INTEGER IDENTITY PRIMARY KEY,
  product_name varchar(50),
  unit_price numeric,
  category varchar(50),
  image_link varchar(120),
  number_sold  numeric,
  unit_cost numeric
);

