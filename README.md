# Selin



[![pipeline status](https://gitlab.com/thpham/selin/badges/master/pipeline.svg)](https://gitlab.com/thpham/selin/commits/master)
[![coverage report](https://gitlab.com/thpham/selin/badges/master/coverage.svg)](https://gitlab.com/thpham/selin/commits/master)


Right now it's just a POC, work in progress.

## Features

- Spring container creating the verticles
- a standard verticle with Vert.x web for the REST API
- Spring backend services (with declarative transaction management and JPA repositories)
- a worker verticle exposing the Spring services over the event bus through service proxies


Run the project with in memory H2 database

``̀ 
$ mvn clean spring-boot:run
``̀ 

Create a docker image and start compose with postgresql/adminer

``̀ 
$ mvn clean package
$ docker-compose up
``̀ 

```
$ curl -v -X POST -H 'Content-Type: application/json' --data '{"name": "staging", "creator":"Thomas Pham"}' http://localhost:8080/environment 

$ curl -v http://localhost:8080/environments
```


Generate the keystore

```
$ keytool -genkey -keyalg RSA -keystore keystore.jks  -deststoretype pkcs12 -keysize 2048 -validity 1095 -dname CN=localhost -keypass secret -storepass secret
```


Access the WebShell through `http://localhost:2222/shell.html`

