# Price Basket Application How To Use
## Setup 
 ## 1. Check application.conf file and adjust it according to your needs. Example of catalog has been provided.
 ## 2. Running app via sbt: 
     sbt> ;clean;compile;test
     sbt> run <item1> <item2> <item3>
##### <item1> <item2> <item3> - items to buy. Example: 
    sbt> run Milk Apples Bread 
 ## 3. Running using docker in terminal:
    docker images 
    docker run --rm -it <DOCKER_IMAGE_ID> <item1> <item2> <item3>
##### <item1> <item2> <item3> - items to buy. <DOCKER_IMAGE_ID> - docker container name. Example: 
    docker run --rm -it default/shopping-basket-app Milk Apples Bread
    
## Expected result
####Output should be to the console, for example:

Subtotal: £3.10 

Apples 10% off: 10p 

Total price: £3.00 

####If no special offers are applicable the code should output:

Subtotal: £1.30

(No offers available) 

Total price: £1.30

## Modification made:
1. Added line with description of item, its price per item and quantity selected.
2. Added special offer details.

####Output should be to the console, for example:

Apples price:1.00£ qty:1

Subtotal: £3.10 

Apples 10% off: 10p 

Total price: £3.00

####If no special offers are applicable the code should output:

Milk price:1.30£ qty:1

Subtotal: £1.30

(No offers available) Total price: £1.30

## Improvements required:
  1. Increase test coverage of the application.
  2. Code review.
  3. Design document.
