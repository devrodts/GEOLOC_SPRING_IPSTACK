# GeoLoc Service

## Overview
GeoLoc is a RESTful microservice for IP geolocation that provides information about the geographical location of IP addresses. The application integrates with the IPStack API to fetch geolocation data and uses Redis for caching to optimize performance and reduce external API calls.

## Features
- IP Geolocation lookup
- Caching mechanism with Redis
- Data refresh capability
- RESTful API with Swagger documentation
- Containerized deployment with Docker and Docker Compose

## Architecture

### Technology Stack
- **Java 21**: Core programming language
- **Spring Boot**: Framework for creating standalone, production-grade applications
- **Spring WebFlux**: Reactive web framework for building non-blocking REST APIs
- **Redis**: In-memory data store used for caching geolocation data
- **Maven**: Build and dependency management
- **Docker**: Containerization platform
- **Swagger/OpenAPI**: API documentation

### Core Components

#### Controllers
The application exposes RESTful endpoints through the `GeoLocationController` which handles:
- GET requests for IP geolocation data
- Refresh requests to force update cached data

#### Services
The business logic is encapsulated in the `GeoLocationServiceImpl` which:
- Implements the `GeoLocationService` interface
- Manages the fetching of geolocation data from IPStack API
- Handles caching logic with Redis

#### Repositories
Data access is managed through:
- `GeoLocationRepository` interface
- `RedisGeoLocationRepository` implementation for Redis operations

#### Data Models
- `GeoLocationEntity`: Domain entity representing geolocation data
- `GeoLocationResponseDTO`: Data Transfer Object for API responses

#### Configuration
- `RedisConfig`: Configuration for Redis connection and serialization

### Design Patterns and Practices
- **Interface-based Programming**: Services and repositories use interfaces for loose coupling
- **Builder Pattern**: Used in entity and DTO creation
- **Dependency Injection**: Spring's core IoC container manages dependencies
- **Records**: Java records for immutable data classes
- **Reactive Programming**: Non-blocking API calls with WebFlux
- **Exception Handling**: Centralized exception management

## API Documentation

### Base URL
```
http://localhost:8080
```

### Endpoints Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/geolocation/ip/{ip}` | Get geolocation data for an IP address |
| GET | `/api/geolocation/ip/{ip}/refresh` | Force refresh of geolocation data for an IP address |

### Detailed Endpoint Documentation

#### Get Geolocation by IP
```
GET /api/geolocation/ip/{ip}
```

Retrieves geolocation information for a specific IP address. If the data is available in cache, it returns the cached data; otherwise, it fetches fresh data from the IPStack API.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| ip | String | Yes | The IP address to lookup (e.g., "8.8.8.8") |

**Response Status Codes:**

| Status Code | Description |
|-------------|-------------|
| 200 | Successful operation |
| 404 | Geolocation data not found |
| 500 | Internal server error |

**Response Body Example:**

```json
{
  "ip": "8.8.8.8",
  "continent": "North America",
  "continentCode": "NA",
  "country": "United States",
  "countryCode": "US",
  "region": "California",
  "regionCode": "CA",
  "city": "Mountain View",
  "zip": "94043",
  "latitude": 37.422,
  "longitude": -122.084,
  "timestamp": "2023-11-15T12:34:56.789",
  "isp": "Google LLC",
  "org": "Google LLC",
  "timezone": "America/Los_Angeles",
  "fromCache": true
}
```

#### Refresh Geolocation Data

```
GET /api/geolocation/ip/{ip}/refresh
```

Forces a refresh of geolocation data for the specified IP address by fetching it directly from the IPStack API and updating the cache.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| ip | String | Yes | The IP address to refresh (e.g., "8.8.8.8") |

**Response Status Codes:**

| Status Code | Description |
|-------------|-------------|
| 200 | Successful operation |
| 400 | Invalid IP address format |
| 500 | Internal server error |

**Response Body Example:**

```json
{
  "ip": "8.8.8.8",
  "continent": "North America",
  "continentCode": "NA",
  "country": "United States",
  "countryCode": "US",
  "region": "California",
  "regionCode": "CA",
  "city": "Mountain View",
  "zip": "94043",
  "latitude": 37.422,
  "longitude": -122.084,
  "timestamp": "2023-11-15T12:34:56.789",
  "isp": "Google LLC",
  "org": "Google LLC",
  "timezone": "America/Los_Angeles",
  "fromCache": false
}
```

### Error Responses

In case of errors, the API returns a structured error response:

```json
{
  "message": "Error description",
  "details": "Additional error details if available"
}
```

## Running Locally with Docker

### Prerequisites
- Docker and Docker Compose installed
- Git (optional, for cloning the repository)

### Steps to Run

1. Clone the repository (if you haven't already):
   ```bash
   git clone <repository-url>
   cd geoloc
   ```

2. Configure your IPStack API key:
   - Create a `.env` file in the project root with:
     ```
     IPSTACK_API_KEY=your_api_key_here
     ```
   - Or update the key directly in `docker-compose.yml`

3. Build and start the containers:
   ```bash
   docker-compose up --build
   ```

4. Access the application:
   - API: http://localhost:8080/api/geolocation/ip/{ip}
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/api-docs

## Development

### Building Locally
```bash
./mvnw clean install
```

### Running Tests
```bash
./mvnw test
```

## Docker Environment

The application is containerized using Docker with a multi-stage build process:
- First stage: Builds the application using Maven
- Second stage: Creates a lightweight runtime image

The Docker Compose configuration includes:
- Spring Boot application
- Redis container for caching
- Appropriate networking and volume configuration
- Environment variables for configuration

## License
MIT License

Copyright (c) 2023 DevRodts

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE. 
