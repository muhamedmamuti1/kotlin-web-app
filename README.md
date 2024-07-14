# Kotlin Web App

This is a simple Kotlin web app that lets you create a user and search for existing users in the system.

This project contains the following components:

- Language: Kotlin (targeting jvm)- https://kotlinlang.org/
- HTTP Server: Ktor - https://ktor.io
- Database abstraction layer: Exposed Framework - https://github.com/JetBrains/Exposed
- Dependency Injection: Koin - https://insert-koin.io/
- Build tool: Gradle - https://gradle.org/

### Example APIs
```shell
@POST http://localhost:8080/users
```

```shell
@GET http://localhost:8080/users?query=jo&limit=3
```

### Payloads

#### @POST
```shell
{
    "name": "LeBron James",
    "email": "ljames@nba.com",
    "password": "Testi123@"
}
```

#### @GET
```shell
{
    "users": [
        {
            "email": "jbreuer@yahoo.com",
            "name": "Joseph Breuer"
        },
        {
            "email": "jcena@wwe.com",
            "name": "John Cena"
        }
    ],
    "total": 9
}
```

## Setup

### Starting
```shell
docker compose up -d
```

### Stopping
```shell
docker compose stop
```

## Running the application
```shell
./gradlew run
```