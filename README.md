# Spring Boot Healthcare API

### **Description**
A secure RESTful API for a healthcare management system. It provides functionalities for patients and doctors, including user authentication, appointment scheduling, and automated notifications.

### **Key Features**
* **User Management:** Register and authenticate users with two roles: `PATIENT` and `DOCTOR`.
* **Secure API:** Implemented JWT-based authentication and role-based authorization using Spring Security.
* **Appointment Scheduling:** Allows patients to book and manage appointments with doctors. Includes validation to prevent booking conflicts.
* **Data Persistence:** Uses a MySQL database with Spring Data JPA to store all user and appointment data.
* **Email Notifications:** Sends automated email reminders for upcoming appointments.

### **Technologies Used**
* **Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA
* **Database:** MySQL
* **API Security:** JSON Web Tokens (JWT)
* **Build Tool:** Maven

### **How to Run Locally**
1.  **Clone the repository:**
    ```
    git clone [https://github.com/Raj172004/Spring-Boot-Healthcare-API.git](https://github.com/Raj172004/Spring-Boot-Healthcare-API.git)
    ```
2.  **Database Setup:**
    * Create a MySQL database named `healthcare_db`.
    * Update the `src/main/resources/application.properties` file with your MySQL username and password.
3.  **Run the application:**
    ```
    ./mvnw spring-boot:run
    ```
