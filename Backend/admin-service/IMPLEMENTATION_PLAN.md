# CIBF Admin Service - Implementation Plan
**Project:** Colombo International Book Fair - Reservation Management System  
**Service:** Admin Service  
**Engineer:** Senior Backend Engineer (5+ Years Experience)  
**Date:** November 3, 2025  
**Database Schema:** admin-service (MySQL)  
**Base URL:** `http://localhost:8085/cibf/admin-service`

---

## 🎯 URL Standards & Conventions

### **Professional URL Patterns:**
```
Base Context Path: /cibf/admin-service
Controller Mapping: /{resource-name}
Endpoint Pattern: /{resource}/{action}
```

### **✅ CORRECT URL Examples:**
```
GET  /cibf/admin-service/admin/health
GET  /cibf/admin-service/admin/test
GET  /cibf/admin-service/users
POST /cibf/admin-service/users
GET  /cibf/admin-service/users/{id}
PUT  /cibf/admin-service/users/{id}
DELETE /cibf/admin-service/users/{id}
GET  /cibf/admin-service/stalls
GET  /cibf/admin-service/reservations
GET  /cibf/admin-service/analytics/dashboard
GET  /cibf/admin-service/audit-logs
POST /cibf/admin-service/alerts
```

### **❌ AVOID These Patterns:**
```
❌ /api/v1/admin/...  (redundant api/v1 prefix)
❌ /adminService/...   (camelCase in URLs)
❌ /admin_service/...  (snake_case in URLs)
❌ /getUsers          (verbs in URL names)
❌ /userList          (use plural nouns)
```

### **URL Naming Rules:**
1. ✅ Use **kebab-case** for multi-word resources (`/audit-logs`, `/system-health`)
2. ✅ Use **plural nouns** for collections (`/users`, `/stalls`, `/reservations`)
3. ✅ Use **HTTP methods** to indicate action (GET, POST, PUT, DELETE)
4. ✅ Keep URLs **simple and readable**
5. ✅ **No versioning** in URLs (use headers if needed)
6. ✅ **No "api" prefix** - context path already identifies the service

### **Spring Boot Actuator Endpoints:**
```
GET /cibf/admin-service/actuator/health   (Spring Boot standard)
GET /cibf/admin-service/actuator/info
GET /cibf/admin-service/actuator/metrics
```

---

## 🎯 Executive Summary

The Admin Service is the **centralized management and monitoring service** for the CIBF Reservation Management System. It provides comprehensive administrative capabilities including user management, stall oversight, reservation monitoring, system analytics, audit logging, and notification dispatching.

---

## 📊 System Analysis

### Existing Services Architecture:
1. **User Service** - Authentication, Authorization (JWT), Role Management
2. **Reservation Service** - Booking Management, Status Tracking
3. **Stall Service** - Stall CRUD (Not yet implemented)
4. **Notification Service** - Email/SMS notifications (Not yet implemented)
5. **Admin Service** - **Current Focus** (Basic health check exists)
6. **API Gateway** - Routing and centralized access
7. **Service Registry** - Eureka service discovery

### Current Admin Service State:
- ✅ Basic Spring Boot setup with JPA & MySQL
- ✅ Health check endpoint (`/api/v1/admin/health`)
- ✅ Database connection configured (admin-service-db)
- ❌ No admin-specific entities
- ❌ No user management capabilities
- ❌ No monitoring/analytics
- ❌ No audit logging
- ❌ No inter-service communication

---

## 🏗️ Architecture Design

### Layer Architecture:
```
┌─────────────────────────────────────┐
│         API Gateway Layer           │
│   (Route: /cibf/admin-service/*)    │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│        Controller Layer             │
│  - AdminUserController              │
│  - AdminStallController             │
│  - AdminReservationController       │
│  - SystemMonitoringController       │
│  - AnalyticsController              │
│  - AuditLogController               │
│  - HealthCheckController (existing) │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│         Service Layer               │
│  - AdminUserService                 │
│  - AdminStallService                │
│  - AdminReservationService          │
│  - SystemMonitoringService          │
│  - AnalyticsService                 │
│  - AuditLogService                  │
│  - NotificationDispatchService      │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│       Repository Layer              │
│  - JPA Repositories                 │
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│      MySQL Database                 │
│    Schema: admin-service            │
└─────────────────────────────────────┘
```

### Inter-Service Communication:
- **RestTemplate/WebClient** for synchronous calls to:
  - User Service (fetch user details)
  - Stall Service (fetch stall details)
  - Reservation Service (fetch reservation data)
  - Notification Service (trigger notifications)

---

## 📋 Implementation Stages

### **STAGE 1: Foundation & Configuration** ⚙️
**Duration:** 1-2 hours  
**Objective:** Setup project dependencies, configurations, and security

#### Tasks:
1. Update `pom.xml` with required dependencies:
   - Spring Cloud OpenFeign (inter-service communication)
   - Spring Security (admin authentication)
   - Spring Validation
   - ModelMapper (DTO mapping)
   - Springdoc OpenAPI (API documentation)

2. Update `application.properties`:
   - Database configuration (schema: admin-service)
   - Server port configuration
   - Eureka client registration
   - JWT secret configuration
   - Feign client configurations

3. Create configuration classes:
   - `SecurityConfig.java` - JWT-based admin authentication
   - `ModelMapperConfig.java` - Bean mapping configuration
   - `OpenAPIConfig.java` - API documentation setup
   - `FeignClientConfig.java` - Feign client setup

4. Create global exception handler:
   - `GlobalExceptionHandler.java` - Centralized error handling

**Deliverable:** Configured project with all dependencies and base configurations

---

### **STAGE 2: Database Schema & Entities** 🗄️
**Duration:** 2-3 hours  
**Objective:** Design and implement complete database schema with JPA entities

#### Tables to Create:
1. **admin_users** - Admin user accounts
2. **audit_logs** - System-wide audit trail
3. **system_alerts** - System health alerts and notifications
4. **user_management_cache** - Cached user data from user-service
5. **stall_management_cache** - Cached stall data from stall-service
6. **reservation_analytics** - Aggregated reservation statistics
7. **notification_queue** - Queue for pending notifications
8. **configuration_settings** - System configuration key-value pairs

#### Tasks:
1. Create JPA Entity classes with proper annotations
2. Define relationships and constraints
3. Add audit fields (createdAt, updatedAt, createdBy)
4. Create enum classes (AlertSeverity, LogAction, NotificationStatus)
5. Test entity generation with `spring.jpa.hibernate.ddl-auto=update`

**Deliverable:** Complete database schema with JPA entities

---

### **STAGE 3: DTOs & Validation** 📝
**Duration:** 2-3 hours  
**Objective:** Create comprehensive DTOs with validation

#### DTO Categories:
1. **Request DTOs** - Input validation
   - `CreateAdminUserRequestDTO`
   - `UpdateUserStatusRequestDTO`
   - `StallApprovalRequestDTO`
   - `ReservationActionRequestDTO`
   - `SystemAlertRequestDTO`

2. **Response DTOs** - Output formatting
   - `AdminUserResponseDTO`
   - `UserManagementResponseDTO`
   - `StallManagementResponseDTO`
   - `ReservationDetailResponseDTO`
   - `SystemHealthResponseDTO`
   - `AnalyticsSummaryResponseDTO`
   - `AuditLogResponseDTO`

3. **Internal DTOs** - Service communication
   - `UserServiceDTO` (from user-service)
   - `StallServiceDTO` (from stall-service)
   - `ReservationServiceDTO` (from reservation-service)

#### Tasks:
1. Create all DTO classes with proper validation annotations
2. Add custom validators for business rules
3. Implement proper error messages

**Deliverable:** Complete DTO layer with validation

---

### **STAGE 4: Feign Clients & Inter-Service Communication** 🔗
**Duration:** 2-3 hours  
**Objective:** Setup communication with other microservices

#### Feign Clients:
1. `UserServiceClient.java` - User service endpoints
2. `StallServiceClient.java` - Stall service endpoints
3. `ReservationServiceClient.java` - Reservation service endpoints
4. `NotificationServiceClient.java` - Notification service endpoints

#### Tasks:
1. Create Feign client interfaces with proper annotations
2. Implement fallback handlers for resilience
3. Add request/response interceptors
4. Configure timeout and retry policies
5. Test inter-service communication

**Deliverable:** Working Feign clients with fallback mechanisms

---

### **STAGE 5: Repository Layer** 💾
**Duration:** 1-2 hours  
**Objective:** Create JPA repositories with custom queries

#### Repositories:
1. `AdminUserRepository`
2. `AuditLogRepository`
3. `SystemAlertRepository`
4. `UserManagementCacheRepository`
5. `StallManagementCacheRepository`
6. `ReservationAnalyticsRepository`
7. `NotificationQueueRepository`
8. `ConfigurationSettingsRepository`

#### Tasks:
1. Create repository interfaces extending JpaRepository
2. Add custom query methods using @Query
3. Implement native queries for complex analytics
4. Add pagination and sorting support

**Deliverable:** Complete repository layer with custom queries

---

### **STAGE 6: Service Layer - Core Services** 💼
**Duration:** 4-5 hours  
**Objective:** Implement business logic for core admin operations

#### Services:
1. **AdminUserService** - Admin user CRUD and authentication
2. **AuditLogService** - Audit trail logging and retrieval
3. **ConfigurationService** - System configuration management

#### Key Features:
- Admin user registration and role assignment
- Password encryption and JWT token validation
- Automatic audit logging for all admin actions
- Configuration CRUD with validation
- Transaction management

**Deliverable:** Core admin services with business logic

---

### **STAGE 7: Service Layer - Management Services** 👥
**Duration:** 4-5 hours  
**Objective:** Implement user, stall, and reservation management

#### Services:
1. **AdminUserManagementService**
   - Get all users (from user-service)
   - View user details
   - Enable/disable user accounts
   - Assign/modify user roles
   - Search and filter users

2. **AdminStallManagementService**
   - Get all stalls (from stall-service)
   - View stall details
   - Approve/reject stall applications
   - Update stall status
   - Manage stall availability

3. **AdminReservationManagementService**
   - Get all reservations (from reservation-service)
   - View reservation details
   - Cancel reservations
   - Resolve booking conflicts
   - Generate reservation reports

#### Tasks:
1. Implement service methods with Feign client calls
2. Add caching for frequently accessed data
3. Implement search and filter logic
4. Add transaction management
5. Include proper error handling

**Deliverable:** Complete management services

---

### **STAGE 8: Service Layer - Monitoring & Analytics** 📊
**Duration:** 3-4 hours  
**Objective:** Implement system monitoring and analytics

#### Services:
1. **SystemMonitoringService**
   - Health check for all microservices
   - Database connection monitoring
   - API response time tracking
   - Resource utilization monitoring
   - Generate system alerts

2. **AnalyticsService**
   - User registration trends
   - Stall booking statistics
   - Reservation conversion rates
   - Revenue analytics
   - Popular stall categories
   - Peak booking periods
   - Generate custom reports

#### Tasks:
1. Implement scheduled health checks
2. Create analytics aggregation logic
3. Add caching for analytics data
4. Implement alert generation logic
5. Create report generation methods

**Deliverable:** Monitoring and analytics services

---

### **STAGE 9: Service Layer - Notification Dispatch** 📧
**Duration:** 2-3 hours  
**Objective:** Implement admin notification dispatch system

#### NotificationDispatchService:
- Send user account alerts
- Send booking confirmations (on behalf of users)
- Send system maintenance notifications
- Send promotional announcements
- Queue management for bulk notifications

#### Tasks:
1. Implement notification queue management
2. Create notification templates
3. Integrate with notification-service via Feign
4. Add retry mechanism for failed notifications
5. Implement bulk notification dispatch

**Deliverable:** Complete notification dispatch service

---

### **STAGE 10: Controller Layer - Part 1** 🎮
**Duration:** 3-4 hours  
**Objective:** Implement admin user and audit controllers

#### Controllers:
1. **AdminUserController** (`/cibf/admin-service/admins`)
   - POST `/register` - Create admin user
   - POST `/login` - Admin authentication
   - GET `/profile` - Get current admin profile
   - PUT `/profile` - Update admin profile
   - GET `/` - Get all admin users
   - PUT `/{id}/status` - Enable/disable admin

2. **AuditLogController** (`/cibf/admin-service/audit-logs`)
   - GET `/` - Get all audit logs (paginated)
   - GET `/{id}` - Get specific audit log
   - GET `/user/{userId}` - Get logs by user
   - GET `/action/{action}` - Get logs by action type
   - GET `/date-range` - Get logs by date range

#### Tasks:
1. Implement controller methods with proper HTTP verbs
2. Add request/response validation
3. Implement pagination and sorting
4. Add proper error responses
5. Add Swagger documentation

**Deliverable:** Admin user and audit controllers

---

### **STAGE 11: Controller Layer - Part 2** 🎮
**Duration:** 4-5 hours  
**Objective:** Implement management controllers

#### Controllers:
1. **AdminUserManagementController** (`/cibf/admin-service/users-management`)
   - GET `/` - Get all users
   - GET `/{id}` - Get user details
   - PUT `/{id}/status` - Enable/disable user
   - PUT `/{id}/role` - Update user role
   - GET `/search` - Search users
   - GET `/statistics` - User statistics

2. **AdminStallManagementController** (`/cibf/admin-service/stalls-management`)
   - GET `/` - Get all stalls
   - GET `/{id}` - Get stall details
   - PUT `/{id}/approve` - Approve stall
   - PUT `/{id}/reject` - Reject stall
   - PUT `/{id}/status` - Update stall status
   - GET `/pending-approval` - Get pending stalls
   - GET `/statistics` - Stall statistics

3. **AdminReservationManagementController** (`/cibf/admin-service/reservations-management`)
   - GET `/` - Get all reservations
   - GET `/{id}` - Get reservation details
   - PUT `/{id}/cancel` - Cancel reservation
   - GET `/conflicts` - Get booking conflicts
   - GET `/user/{userId}` - Get user reservations
   - GET `/stall/{stallId}` - Get stall reservations
   - GET `/statistics` - Reservation statistics

**Deliverable:** Complete management controllers

---

### **STAGE 12: Controller Layer - Part 3** 📊
**Duration:** 3-4 hours  
**Objective:** Implement monitoring and analytics controllers

#### Controllers:
1. **SystemMonitoringController** (`/cibf/admin-service/monitoring`)
   - GET `/health` - Overall system health
   - GET `/services` - Health of all services
   - GET `/database` - Database health
   - GET `/alerts` - Get system alerts
   - POST `/alerts` - Create system alert
   - GET `/metrics` - System metrics

2. **AnalyticsController** (`/cibf/admin-service/analytics`)
   - GET `/dashboard` - Dashboard summary
   - GET `/users/trends` - User registration trends
   - GET `/stalls/statistics` - Stall statistics
   - GET `/reservations/statistics` - Reservation statistics
   - GET `/revenue/summary` - Revenue summary
   - GET `/reports/custom` - Generate custom reports

3. **NotificationController** (`/cibf/admin-service/notifications`)
   - POST `/send` - Send single notification
   - POST `/send-bulk` - Send bulk notifications
   - GET `/queue` - Get notification queue
   - GET `/history` - Get notification history

**Deliverable:** Monitoring, analytics, and notification controllers

---

### **STAGE 13: Security Implementation** 🔒
**Duration:** 3-4 hours  
**Objective:** Implement comprehensive security

#### Security Features:
1. JWT-based authentication for admin users
2. Role-based access control (RBAC)
   - SUPER_ADMIN - Full access
   - ADMIN - Limited access
   - MODERATOR - Read-only access
3. Method-level security with @PreAuthorize
4. Request rate limiting
5. CORS configuration
6. API key validation for inter-service calls

#### Tasks:
1. Create JWT authentication filter
2. Implement JWT token generation and validation
3. Create custom UserDetailsService for admins
4. Add @PreAuthorize annotations to sensitive endpoints
5. Configure CORS for frontend access
6. Implement request logging for security audit

**Deliverable:** Secure admin service with RBAC

---

### **STAGE 14: Exception Handling & Validation** ⚠️
**Duration:** 2-3 hours  
**Objective:** Implement robust error handling

#### Exception Classes:
1. `AdminUserNotFoundException`
2. `InvalidCredentialsException`
3. `UnauthorizedAccessException`
4. `ServiceCommunicationException`
5. `DataValidationException`
6. `ResourceConflictException`

#### Tasks:
1. Create custom exception classes
2. Implement @ControllerAdvice global exception handler
3. Create standardized error response DTOs
4. Add validation error formatting
5. Implement proper HTTP status codes
6. Add detailed error messages for debugging

**Deliverable:** Comprehensive exception handling

---

### **STAGE 15: Logging & Monitoring** 📝
**Duration:** 2-3 hours  
**Objective:** Implement comprehensive logging

#### Logging Strategy:
1. Request/Response logging
2. Service method logging
3. Error logging with stack traces
4. Audit logging for admin actions
5. Performance logging (execution time)
6. Inter-service call logging

#### Tasks:
1. Configure Logback/SLF4J
2. Add logging annotations
3. Create custom logging aspects
4. Implement structured logging (JSON format)
5. Configure log levels for different environments
6. Add correlation IDs for request tracing

**Deliverable:** Complete logging infrastructure

---

### **STAGE 16: Testing - Unit Tests** 🧪
**Duration:** 4-5 hours  
**Objective:** Write comprehensive unit tests

#### Test Coverage:
1. Service layer tests (Mockito)
2. Repository tests (DataJpaTest)
3. DTO validation tests
4. Utility method tests
5. Security tests

#### Tasks:
1. Write unit tests for all service methods
2. Mock Feign client calls
3. Test validation logic
4. Test exception handling
5. Achieve 80%+ code coverage

**Deliverable:** Complete unit test suite

---

### **STAGE 17: Testing - Integration Tests** 🔬
**Duration:** 3-4 hours  
**Objective:** Write integration tests

#### Integration Tests:
1. Controller layer tests (MockMvc)
2. Database integration tests
3. Feign client integration tests
4. Security integration tests
5. End-to-end workflow tests

#### Tasks:
1. Write controller tests with MockMvc
2. Test database transactions
3. Test inter-service communication
4. Test JWT authentication flow
5. Test complete workflows

**Deliverable:** Complete integration test suite

---

### **STAGE 18: API Documentation** 📚
**Duration:** 2-3 hours  
**Objective:** Create comprehensive API documentation

#### Documentation:
1. Swagger/OpenAPI specification
2. Endpoint descriptions
3. Request/Response examples
4. Error response documentation
5. Authentication documentation

#### Tasks:
1. Add Swagger annotations to controllers
2. Configure Swagger UI
3. Add API descriptions and examples
4. Document authentication flow
5. Create Postman collection

**Deliverable:** Complete API documentation with Swagger

---

### **STAGE 19: Configuration & Environment Setup** ⚙️
**Duration:** 2-3 hours  
**Objective:** Setup for different environments

#### Environments:
1. Development (local)
2. Testing (staging)
3. Production

#### Tasks:
1. Create `application-dev.properties`
2. Create `application-test.properties`
3. Create `application-prod.properties`
4. Configure environment-specific settings
5. Create startup scripts
6. Document deployment process

**Deliverable:** Multi-environment configuration

---

### **STAGE 20: Performance Optimization** ⚡
**Duration:** 2-3 hours  
**Objective:** Optimize service performance

#### Optimization Areas:
1. Database query optimization
2. Caching implementation (Redis/Caffeine)
3. Connection pool tuning
4. Async processing for heavy operations
5. Pagination for large datasets

#### Tasks:
1. Add database indexes
2. Implement caching for frequently accessed data
3. Configure connection pool settings
4. Implement async methods for notifications
5. Add pagination to list endpoints

**Deliverable:** Optimized admin service

---

### **STAGE 21: Final Testing & Quality Assurance** ✅
**Duration:** 3-4 hours  
**Objective:** Final testing and bug fixes

#### Testing Checklist:
- [ ] All endpoints working correctly
- [ ] Authentication and authorization working
- [ ] Inter-service communication working
- [ ] Database operations successful
- [ ] Audit logging working
- [ ] Error handling working correctly
- [ ] API documentation accurate
- [ ] Performance acceptable
- [ ] Security measures in place

#### Tasks:
1. Manual testing of all endpoints
2. Test with Postman collection
3. Test error scenarios
4. Test concurrent requests
5. Fix identified bugs
6. Code review and refactoring

**Deliverable:** Production-ready admin service

---

### **STAGE 22: Deployment & Documentation** 🚀
**Duration:** 2-3 hours  
**Objective:** Deploy and document

#### Deployment:
1. Build JAR file
2. Configure database on server
3. Deploy to server/container
4. Configure reverse proxy
5. Setup monitoring

#### Documentation:
1. Update README.md
2. Create deployment guide
3. Create API usage guide
4. Document troubleshooting steps
5. Create maintenance guide

**Deliverable:** Deployed service with complete documentation

---

## 📦 Deliverables Summary

| Stage | Deliverable | Estimated Time |
|-------|-------------|----------------|
| 1 | Configuration & Dependencies | 1-2 hours |
| 2 | Database Schema & Entities | 2-3 hours |
| 3 | DTOs & Validation | 2-3 hours |
| 4 | Feign Clients | 2-3 hours |
| 5 | Repository Layer | 1-2 hours |
| 6 | Core Services | 4-5 hours |
| 7 | Management Services | 4-5 hours |
| 8 | Monitoring & Analytics | 3-4 hours |
| 9 | Notification Dispatch | 2-3 hours |
| 10 | Controllers Part 1 | 3-4 hours |
| 11 | Controllers Part 2 | 4-5 hours |
| 12 | Controllers Part 3 | 3-4 hours |
| 13 | Security Implementation | 3-4 hours |
| 14 | Exception Handling | 2-3 hours |
| 15 | Logging & Monitoring | 2-3 hours |
| 16 | Unit Tests | 4-5 hours |
| 17 | Integration Tests | 3-4 hours |
| 18 | API Documentation | 2-3 hours |
| 19 | Environment Setup | 2-3 hours |
| 20 | Performance Optimization | 2-3 hours |
| 21 | QA & Testing | 3-4 hours |
| 22 | Deployment & Docs | 2-3 hours |

**Total Estimated Time:** 55-75 hours (7-10 working days)

---

## 🛠️ Technology Stack

- **Framework:** Spring Boot 3.5.6
- **Language:** Java 17
- **Database:** MySQL 8.0+
- **ORM:** Spring Data JPA / Hibernate
- **Security:** Spring Security + JWT
- **Inter-Service:** Spring Cloud OpenFeign
- **Validation:** Jakarta Validation API
- **Documentation:** Springdoc OpenAPI 3
- **Testing:** JUnit 5, Mockito, MockMvc
- **Build Tool:** Maven
- **Logging:** SLF4J + Logback

---

## 📋 Prerequisites

- [x] MySQL Server running
- [x] Schema "admin-service" created
- [x] JDK 17+ installed
- [x] Maven installed
- [ ] Redis (optional, for caching)
- [ ] User Service running (for testing)
- [ ] Stall Service running (for testing)
- [ ] Reservation Service running (for testing)

---

## 🚦 Getting Started

After approval, we will proceed stage-by-stage:
1. Review and approve this plan
2. Review and execute DDL scripts
3. Implement Stage 1
4. Test and commit Stage 1
5. Repeat for subsequent stages

---

## 📞 Support & Contact

For any questions or clarifications during implementation, please refer to:
- Spring Boot Documentation
- Spring Cloud Documentation
- MySQL Documentation
- Project README.md

---

**Document Version:** 1.0  
**Last Updated:** November 3, 2025  
**Status:** Pending Approval

---

## ✅ Approval

- [ ] Plan Reviewed
- [ ] Plan Approved
- [ ] DDL Scripts Reviewed
- [ ] Ready to Start Stage 1

**Approved By:** _________________  
**Date:** _________________

---

*This implementation plan is a living document and may be updated based on project requirements and feedback.*

