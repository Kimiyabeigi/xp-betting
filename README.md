# Execution guide

### Technologies used

* h2 database
* spring boot
* spring data jpa
* lombok
* junit5 (jupiter)
* apache commons csv
* maven as a build tool
* java 1.8

### Description
For the following reasons, I decided to implement my solution as a web-based application and use a database:
* to demonstrate my knowledge of Spring framework and RDBMS
* to show my experience of MVC architecture

### Guides
H2 DB is used to avoid extra boilerplate when running the application
xp-betting.postman_collection.json file is available in (docs) path, you can import it in your own Postman and use it,
or use the following addresses in the browser.

List of APIs:
* http://localhost:8080/api/session/win-bet : This is a GET method For task 1
* http://localhost:8080/api/game/win-bet : This is a GET method For task 2
* http://localhost:8080/api/session-game/win-bet : This is a GET method For task 3
* http://localhost:8080/api/profit-player : This is a GET method For task 4

