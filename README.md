# Todo List REST API

A REST API for managing todos with CRUD operations, database integration, and JWT authentication.

## Features
- Register/Login with JWT
- Create, Read, Update, Delete Todos
- MySQL/PostgreSQL/MongoDB support

## Tech Stack
- Java 21, Spring Boot
- MySQL/PostgreSQL/MongoDB
- Spring Security, JWT

## Setup
1. Clone repository
2. Update `application.yml` for your DB
3. Run: `mvn spring-boot:run`

## API Endpoints
| Method | Endpoint       | Description          | Auth |
|--------|---------------|----------------------|------|
| POST   | /auth/register| Register new user    | No   |
| POST   | /auth/login   | Login & get JWT      | No   |
| GET    | /todos        | Get all todos        | Yes  |
| POST   | /todos        | Create new todo      | Yes  |

## License
MIT
