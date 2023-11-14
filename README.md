# CS353 - Backend

## Overview
This repository contains the backend implementation for the CS353 course project. The backend is built using Java with the Spring framework and utilizes JDBC for database access. The PostgreSQL database is hosted by ElephantSQL, and Maven is used for managing project dependencies.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Dependencies](#dependencies)
  
## Prerequisites
Before you begin, ensure you have the following tools installed:
- Java Development Kit (JDK)
- Apache Maven
- PostgreSQL database connection details (URL, username, password) from ElephantSQL

## Getting Started
### Configuration
1. Clone this repository to your local machine.
   ```bash
   git clone https://github.com/CS353-Bilkent/backend.git
   cd backend
Open the src/main/resources/application.properties file and configure the database connection details.
properties

### Running the Application
Build the project using Maven.

  ```bash
  mvn clean install
  mvn spring-boot:run
  ```

The backend will be accessible at http://localhost:8080.

## Project Structure
The project follows a standard Spring Boot project structure. Key directories include:

src/main/java: Java source files
src/main/resources: Configuration files, including application properties
src/test: Unit and integration tests

## Dependencies
Spring Boot: Simplifies the development of production-ready Spring applications.
PostgreSQL JDBC Driver: Allows Java applications to connect to a PostgreSQL database.
Maven: Dependency management and build tool.
