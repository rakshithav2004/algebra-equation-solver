# Algebra Equation Solver API

A RESTful backend application built using **Java 21 and Spring Boot** to store, retrieve, and evaluate algebraic equations using **Postfix (Reverse Polish Notation) and Expression Trees**.

## Features

- Store algebraic equations through REST APIs.
- Convert infix expressions into postfix notation using the Shunting Yard algorithm.
- Build binary expression trees from postfix expressions.
- Retrieve stored equations in readable infix notation.
- Evaluate equations by substituting variable values.
- Support arithmetic operators: `+`, `-`, `*`, `/`, `^`.
- Support parentheses, numeric constants, variables, implicit multiplication, and unary minus.
- Validate invalid expressions and missing variables.
- Handle division-by-zero and other errors with appropriate HTTP responses.
- Store equations in memory using `ConcurrentHashMap`.
- Interactive API documentation using Swagger UI.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4.0.0 | Backend framework |
| Spring Web MVC | REST API development |
| Maven | Build and dependency management |
| Jakarta Validation | Request validation |
| Swagger / OpenAPI | API documentation |
| JUnit 5 | Unit testing |
| MockMvc | Controller testing |
| Spotless | Code formatting |

## Project Architecture

```text
src
├── main
│   └── java/com/example/algebra
│       ├── config
│       │   └── OpenApiConfig.java
│       ├── controller
│       │   └── EquationController.java
│       ├── dto
│       │   ├── StoreEquationRequest.java
│       │   ├── StoreEquationResponse.java
│       │   ├── EquationResponse.java
│       │   ├── EvaluateEquationRequest.java
│       │   └── EvaluationResponse.java
│       ├── exception
│       │   ├── EquationNotFoundException.java
│       │   └── GlobalExceptionHandler.java
│       ├── model
│       │   ├── Equation.java
│       │   └── TreeNode.java
│       ├── parser
│       │   └── ExpressionParser.java
│       ├── repository
│       │   └── EquationRepository.java
│       └── service
│           └── EquationService.java
└── test
    └── java/com/example/algebra
        ├── controller
        ├── dto
        ├── exception
        ├── parser
        └── service
```

## How It Works

The application processes equations in the following steps:

1. **Tokenization:** The input equation is separated into numbers, variables, operators, and parentheses.
2. **Infix to Postfix Conversion:** The Shunting Yard algorithm converts the expression into postfix notation while considering operator precedence and associativity.
3. **Expression Tree Construction:** A tree is constructed from the postfix expression, with operators as parent nodes and operands as child nodes.
4. **Storage:** The expression tree is stored in an in-memory repository with a unique equation ID.
5. **Retrieval:** The stored tree is traversed to reconstruct the equation in infix notation.
6. **Evaluation:** The expression tree is evaluated recursively using the supplied variable values.

### Example

Input equation:

```text
3x + 2y - z
```

Postfix representation:

```text
3 x * 2 y * + z -
```

Expression tree:

```text
          -
         / \
        +   z
       / \
      *   *
     / \ / \
    3  x 2  y
```

For the variable values:

```json
{
  "x": 2,
  "y": 3,
  "z": 1
}
```

Calculation:

```text
(3 * 2) + (2 * 3) - 1 = 11
```

Result:

```text
11.0
```

## REST API Endpoints

Base URL:

```text
http://localhost:8080
```

### 1. Store an Equation

**Endpoint:**

```http
POST /api/equations/store
```

Request:

```json
{
  "equation": "3x + 2y - z"
}
```

Response (`201 Created`):

```json
{
  "message": "Equation stored successfully",
  "equationId": "generated-unique-id"
}
```

### 2. Retrieve All Equations

**Endpoint:**

```http
GET /api/equations
```

Response (`200 OK`):

```json
[
  {
    "equationId": "generated-unique-id",
    "equation": "(((3 * x) + (2 * y)) - z)"
  }
]
```

### 3. Evaluate an Equation

**Endpoint:**

```http
POST /api/equations/{equationId}/evaluate
```

Request:

```json
{
  "variables": {
    "x": 2,
    "y": 3,
    "z": 1
  }
}
```

Response (`200 OK`):

```json
{
  "equation": "(((3 * x) + (2 * y)) - z)",
  "variables": {
    "x": 2.0,
    "y": 3.0,
    "z": 1.0
  },
  "result": 11.0
}
```

## Error Handling

The application provides centralized exception handling using `GlobalExceptionHandler`.

| Scenario | HTTP Status |
|---|---|
| Invalid equation or expression | 400 Bad Request |
| Missing variable value | 400 Bad Request |
| Division by zero | 400 Bad Request |
| Invalid request body | 400 Bad Request |
| Equation not found | 404 Not Found |
| Unexpected server error | 500 Internal Server Error |

Example error response:

```json
{
  "error": "Equation not found"
}
```

## Getting Started

### Prerequisites

- Java 21
- Git
- Maven (optional, since Maven Wrapper is included)

### Clone the Repository

```bash
git clone https://github.com/rakshithav2004/algebra-equation-solver.git
```

Navigate to the project directory:

```bash
cd algebra-equation-solver
```

### Run the Application

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

## Swagger API Documentation

Once the application is running, open:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger UI allows you to explore and test all available API endpoints interactively.

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

## Running Tests

Run the complete test suite:

**Windows:**

```powershell
.\mvnw.cmd clean test
```

**Linux/macOS:**

```bash
./mvnw clean test
```

The test suite covers:

- Expression parsing and postfix conversion.
- Expression tree construction and evaluation.
- Equation storage and retrieval.
- REST controller endpoints.
- DTO validation.
- Exception handling and error responses.

The project currently has **31 passing test cases**.

## Code Formatting

Apply formatting:

```powershell
.\mvnw.cmd spotless:apply
```

Verify formatting:

```powershell
.\mvnw.cmd spotless:check
```

## Storage

This application uses an in-memory `ConcurrentHashMap` repository.

No external database is required. Stored equations are available while the application is running and are cleared when the application restarts.

## Repository

GitHub: https://github.com/rakshithav2004/algebra-equation-solver

---

**Developed as a backend engineering assignment using Java 21 and Spring Boot.**
