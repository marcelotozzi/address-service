CEP

* [http://postmon.com.br/](http://postmon.com.br/)
* spring boot
* mysql
* mockito
* spring mockmvc
* hsqldb

### run
mvn spring-boot:run


### Busca CEP
curl -X GET "http://localhost:8080/addresses?zipcode=05110000"

### CRUD
curl -X POST  -H "Content-Type: application/json" http://localhost:8080/addresses -d '{"street":"Av. Mutinga", "number":"5452", "zipCode":"05110000", "city":"Sao Paulo", "state":"SP"}'

curl -X GET "http://localhost:8080/addresses/1"

curl -X PUT -H "Content-Type: application/json" "http://localhost:8080/addresses/1"  -d '{"street":"Avenida Mutinga", "number":"5600", "district":"jd. santo", "zipCode":"05110000", "city":"Sao Paulo", "state":"SP"}'

curl -X DELETE "http://localhost:8080/addresses/1"

