# API Endpoints

This document provides an overview of the RESTful API endpoints for the Healthcare Management System.

## 1. Authentication

### **User Registration**
`POST /api/users/register`
* **Description:** Creates a new user with either a `PATIENT` or `DOCTOR` role.
* **Request Body (JSON):**
    ```json
    {
      "username": "newuser",
      "password": "password123",
      "role": "PATIENT",
      "email": "newuser@example.com",
      "firstName": "John",
      "lastName": "Doe"
    }
    ```

### **User Login**
`POST /api/auth/login`
* **Description:** Authenticates a user and returns a JWT token. This token must be used for all subsequent requests to protected endpoints.
* **Request Body (JSON):**
    ```json
    {
      "username": "user",
      "password": "password"
    }
    ```

## 2. Appointment Management

### **Create Appointment**
`POST /api/appointments`
* **Description:** Allows an authenticated `PATIENT` to book an appointment with a doctor.
* **Request Body (JSON):**
    ```json
    {
      "appointmentDate": "2025-10-01",
      "appointmentTime": "10:30:00",
      "reason": "Routine checkup",
      "doctorId": 1
    }
    ```

### **Get All Appointments**
`GET /api/appointments`
* **Description:** Retrieves a list of all appointments. Requires `DOCTOR` role.

## 3. Doctor Information

### **Get All Doctors**
`GET /api/doctors`
* **Description:** Retrieves a list of all registered doctors. Requires `PATIENT` role.

### **Get Doctor by ID**
`GET /api/doctors/{id}`
* **Description:** Retrieves the details for a specific doctor. Requires `PATIENT` or `DOCTOR` role.
