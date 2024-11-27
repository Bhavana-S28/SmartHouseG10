# Smart House.
SmartHouse is a Java-based smart home management system designed to simulate and manage energy consumption across various smart devices and energy sources. The application allows users to add, remove, and toggle smart devices while balancing energy loads across multiple sources. The application simulates real home energy sources and usage which includes City Power, Solar Panels, and possibly a Diesel Generator (DG). 
## Features
- Manage Smart Devices: Add, list, remove, and toggle smart objects.
- Energy Source Management: Dynamically balance energy loads across multiple sources.
- Load Balancing: Automatically redistribute energy consumption when demand exceeds supply.
- Logging: Generates logs to track actions and energy balancing.
## Technologies Used
- Java
- Maven
- JUnit for testing
- Logger for logging

# Installation
## Prerequisites
1. Java 8 or later
2. Apache Maven

## How to Run
### Using Maven
Clone the repository:
Block: git clone https://github.com/yourusername/SmartHouse.git
Block: cd SmartHouse

### Build the project:

bash
Copy code
mvn clean package
Run the application:

bash
Copy code
java -jar target/SmartHouse-1.0-SNAPSHOT.jar
