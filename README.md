# CIBF Reservation Management System

A **microservices-based Event & Stall Management System** built with **Spring Boot, Spring Cloud, and React**. This system provides services for user authentication, stalls, reservations, notifications, and admin operations, along with an API Gateway and Service Registry.

---

## **Project Overview**

This project allows users (publishers/vendors and organizers/employees) to manage stalls, reservations, and notifications efficiently. It is structured using **microservices architecture**, ensuring scalability, maintainability, and separation of concerns.

**Key Features:**
- User registration, authentication, and authorization
- Stall management (CRUD)
- Booking and reservation management
- Notifications via email/SMS
- Admin operations for monitoring and managing users, stalls, and reservations
- API Gateway for routing and centralized access

---


---

## **Services**

| Service                  | Description                                                                 |
|---------------------------|-----------------------------------------------------------------------------|
| User Service              | Handles user registration, login, authentication, authorization and Genre    |
| Stall Service             | Manages stall creation, update, and availability                            |
| Reservation Service       | Handles booking/reservations and interacts with other services              |
| Notification Service      | Sends notifications (email/SMS) and logs notifications                     |
| Admin Service             | Manages users, stalls, reservations, and sends admin alerts                |
| API Gateway               | Routes requests from frontend to microservices, handles authentication      |


---

## **Tech Stack**

- **Backend:** Spring Boot, Spring Cloud, Spring Security, Spring Data JPA, Eureka, Spring Cloud Gateway  
- **Frontend:** React.js  
- **Database:** MySQL / H2 (for development)  
- **Build Tools:** Maven  
- **Java Version:** 17+  

---

## **Setup Instructions**

### **1. Clone the repository**
```bash
git clone https://github.com/CIBF-Reservation-System/CIBF-Reservation-Management-System.git
cd CIBF-Reservation-Management-System/Backend
```

### **2. Configure Databases**

- Update application.properties for each microservice with your database credentials.



### **3. Run API Gateway**
```bash
cd api-gateway
mvn spring-boot:run
```


### **4. Run other microservices**

- Auth-User Service
- Stall Service
- Reservation Service
- Notification Service
- Admin Service

### **5. Frontend**

- Build and serve React frontend separately:
```bash
cd Frontend
npm install
npm run dev
```




