# Smart House.
SmartHouse is a Java-based smart home management system designed to simulate and manage energy consumption across various smart devices and energy sources. The application allows users to add, remove, and toggle smart devices while balancing energy loads across multiple sources. The application simulates real home energy sources and usage which includes City Power, Solar Panels, and possibly a Diesel Generator (DG).
## Role Distribution
This project was developed collaboratively by a team of three members, with each member focusing on specific components:
### Team Member	Responsibility
Description: 
1. *Mohammad Ali Moradi* - **SmartHouseSimulator** and **SmartObject** classes	Responsible for implementing the core simulation logic and managing smart objects within the system.
2. *Bhavana Shivaraju* - **EnergyManager** and **LogManger** classes	Designed and implemented the energy management system, including load balancing and smart object control and logging messages to file.
3. *Aftab Makbul Makandar* - **EnergySource** interface, **SolarPanel**, **CityPower**, and **DieselGenerator** classes	Developed the energy source framework and specific implementations to manage energy capacity and interaction with smart objects.


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
'git clone https://github.com/yourusername/SmartHouse.git'
'cd SmartHouse'

### Build the project:
'mvn clean package'
### Run the application:
'java -jar target/SmartHouse-1.0-SNAPSHOT.jar'

## Logs
Logs are saved in **/home/ali/Documents/smart_objects.log.** Adjust the logging path in the **LogManager** configuration if needed.
