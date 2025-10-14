# Currency App (Spring Boot + Angular)

## Wymagania

- Java 17+ i Maven
- Node.js LTS: 20.19+ lub 22.12+

## Backend
- Spring Boot (Java 17)
- Start dev: `cd backend && mvn spring-boot:run`
- Swagger: `http://localhost:8080/swagger-ui.html`
- H2: `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:mem:currencydb`, user: `sa`)

## Frontend
- Angular
- Start dev: `cd frontend && npm start`
- API base: `http://localhost:8080` (dev)
