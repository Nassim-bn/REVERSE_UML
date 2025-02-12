
# UML Diagram Creator

This project is an improved version of the "UML Reverse" application, designed for creating, managing, and manipulating UML diagrams. It was developed as part of the final year project for a Bachelor's degree in Computer Science at the University of Rouen.

For more information, refer to the project report in this directory named ***RapportDiagrammesUML***, which provides more details in French.

## Table of Contents
- [UML Diagram Creator](#uml-diagram-creator)
  - [Table of Contents](#table-of-contents)
  - [Project Overview](#project-overview)
  - [Features](#features)
  - [Technologies Used](#technologies-used)
  - [Installation](#installation)
  - [Usage](#usage)
  - [Contributors](#contributors)
  - [Acknowledgments](#acknowledgments)
  - [Future Improvements](#future-improvements)

---

## Project Overview
The goal of this project is to enhance an existing UML diagram application by improving its functionalities, specifically for class diagrams. The original application was developed using Java 7 with JavaFX for the user interface. Our version focuses on better usability, new features, and improved project management.
The application supports:
- Creating and managing UML diagrams.
- Importing from PlantUML files and Java packages.
- Exporting diagrams to PlantUML, PNG, and now PDF.

---

## Features
- **Comprehensive UML Diagram Creation:** Create and manage class diagrams with intuitive graphical manipulation.
- **Manual Positioning of Relationships:** Users can manually adjust the positioning of connections between classes.
- **Enhanced User Interface:** Improved control over elements to prevent overlapping and allow flexible editing.
- **Multi-Project Management:** Manage multiple UML projects simultaneously.
- **PDF Exportation:** Export diagrams as PDF files using Apache PDFBox.

---

## Technologies Used
- **Programming Language:** Java (OpenJDK 17)
- **User Interface:** JavaFX 21.0.1
- **Dependency Management:** Maven 3.6.3
- **Development Environment:** Eclipse
- **Quality Assurance:** SonarLint for detecting vulnerabilities and improving code quality
- **UI Design Tool:** SceneBuilder
- **Version Control:** GitHub

---

## Installation
To set up the development environment, follow these steps:
1. Install OpenJDK 17.
2. Install JavaFX 21.0.1.
3. Install Maven 3.6.3.
4. Clone the project repository:
5. Import the project into Eclipse as a Maven project.
6. Build the project using Maven:
   ```bash
   mvn clean install
   ```
7. Run the application through Eclipse or using:
   ```bash
   mvn javafx:run
   ```

---

## Usage
- **Creating Diagrams:** Start a new project and create class diagrams by adding classes, attributes, and methods.
- **Positioning Relations:** Manually position relationship lines for better visual representation.
- **Exporting:** Export diagrams in PlantUML, PNG, or PDF formats for documentation or presentation purposes.

---

## Contributors
- Lylia AIT SAHEL
- Mazarine DAHMANI
- Yanis SADOUN
- Rayane ARACHE
- Nassim BENCHIKH

---

## Acknowledgments
This project was carried out under the supervision of **Mr. Stéphane HERAUVILLE** at the University of Rouen. Special thanks to previous teams for making their code available, providing a solid foundation for this project.

---

## Future Improvements
Potential enhancements to further improve the application:
- **Automatic Relation Routing:** Automatically route relationships to avoid overlapping elements.
- **Visibility Control:** Hide or show properties and methods to simplify diagram views.
- **Enhanced Multi-Project Management:** Improve usability when managing multiple UML projects simultaneously.
