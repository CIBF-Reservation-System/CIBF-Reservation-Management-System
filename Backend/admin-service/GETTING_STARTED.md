# 🚀 QUICK START GUIDE - Admin Service
**For:** Backend Engineer  
**Current Stage:** Stage 1 Complete → Ready for Stage 2

---

## ✅ STAGE 1 - COMPLETED

### What Was Done:
1. ✅ All Maven dependencies configured (pom.xml)
2. ✅ Security configuration (JWT + CORS)
3. ✅ ModelMapper configuration (DTO mapping)
4. ✅ OpenAPI configuration (Swagger docs)
5. ✅ Feign client configuration (inter-service calls)
6. ✅ Global exception handler
7. ✅ Application properties configured
8. ✅ Database configuration FIXED (admin-service)

---

## 🧪 HOW TO VERIFY STAGE 1

### Option 1: Run Verification Script (Recommended)
```cmd
cd "E:\ENGINEERING\FOE-UOR\SEM 8\Software Architechture\Project\CIBF-Reservation-Management-System\Backend\admin-service"
STAGE_1_VERIFICATION.bat
```

This will check:
- MySQL connection ✓
- Database schema exists ✓
- Maven setup ✓
- Project compilation ✓

### Option 2: Manual Verification

#### Step 1: Check MySQL Connection
```cmd
mysql -u root -proot -e "SELECT 'Connected' AS Status;"
```

#### Step 2: Verify Database Schema
```cmd
mysql -u root -proot -e "SHOW DATABASES LIKE 'admin-service';"
```

If not exists, create it:
```cmd
mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS `admin-service`;"
```

#### Step 3: Compile the Project
```cmd
mvnw.cmd clean compile
```

#### Step 4: Start the Service
```cmd
start-with-db-test.bat
```
OR
```cmd
mvnw.cmd spring-boot:run
```

#### Step 5: Test Endpoints

**Health Check:**
```
GET http://localhost:8085/cibf/admin-service/api/v1/admin/health
```

**Swagger UI:**
```
http://localhost:8085/cibf/admin-service/swagger-ui.html
```

**API Docs:**
```
http://localhost:8085/cibf/admin-service/api-docs
```

---

## 📊 CURRENT PROJECT STATUS

### File Structure:
```
admin-service/
├── pom.xml                          ✅ All dependencies configured
├── src/main/
│   ├── java/com/cibf/adminservice/admin/
│   │   ├── AdminServiceApplication.java    ✅ Main class
│   │   ├── Config/
│   │   │   ├── SecurityConfig.java        ✅ JWT + Security
│   │   │   ├── ModelMapperConfig.java     ✅ DTO mapping
│   │   │   ├── OpenAPIConfig.java         ✅ Swagger docs
│   │   │   └── FeignClientConfig.java     ✅ Inter-service
│   │   ├── Exception/
│   │   │   ├── GlobalExceptionHandler.java ✅ Error handling
│   │   │   ├── BadRequestException.java   ✅
│   │   │   ├── ForbiddenException.java    ✅
│   │   │   ├── ResourceNotFoundException.java ✅
│   │   │   ├── ServiceUnavailableException.java ✅
│   │   │   └── UnauthorizedException.java ✅
│   │   ├── Entity/
│   │   │   └── HealthCheck.java           ✅ Demo entity
│   │   ├── Repository/
│   │   │   └── HealthCheckRepository.java ✅ Demo repo
│   │   ├── Service/
│   │   │   └── HealthCheckService.java    ✅ Demo service
│   │   ├── Controller/
│   │   │   └── HealthCheckController.java ✅ Demo controller
│   │   └── DTO/
│   │       └── HealthCheckResponseDTO.java ✅ Demo DTO
│   └── resources/
│       └── application.properties         ✅ Configured (FIXED)
└── Documentation/
    ├── IMPLEMENTATION_PLAN.md             ✅ 22-stage plan
    ├── DATABASE_SETUP_GUIDE.md            ✅ DB guide
    ├── admin-service-schema.sql           ✅ Complete DDL
    ├── QUICK_REFERENCE.md                 ✅ Quick ref
    ├── STAGE_1_STATUS_REPORT.md           ✅ Stage 1 review
    └── STAGE_1_VERIFICATION.bat           ✅ Test script
```

---

## 🎯 NEXT: STAGE 2 - ENTITY CREATION

### What Will Be Done in Stage 2:

#### 1. Create Enum Classes (Common/)
- AdminRole.java (SUPER_ADMIN, ADMIN, MODERATOR)
- ActionType.java (USER_CREATED, STALL_APPROVED, etc.)
- AlertType.java (SERVICE_DOWN, DATABASE_ERROR, etc.)
- AlertSeverity.java (LOW, MEDIUM, HIGH, CRITICAL)
- AlertStatus.java (OPEN, ACKNOWLEDGED, RESOLVED, CLOSED)
- NotificationStatus.java (PENDING, SENT, FAILED, RETRY)
- NotificationPriority.java (LOW, MEDIUM, HIGH, URGENT)

#### 2. Create Entity Classes (Entity/)
- AdminUser.java (admin accounts with UUID)
- AuditLog.java (system audit trail)
- SystemAlert.java (health alerts)
- UserManagementCache.java (cached user data)
- StallManagementCache.java (cached stall data)
- ReservationAnalytics.java (aggregated stats)
- NotificationQueue.java (notification queue)
- ConfigurationSettings.java (system config)
- AdminSession.java (session tracking)
- SystemHealthMetrics.java (health metrics)

#### 3. Key Features to Implement:
- UUID as primary key for AdminUser
- Proper JPA annotations (@Entity, @Table, @Column)
- Relationships (@ManyToOne, @OneToMany if needed)
- Audit fields (@CreatedDate, @LastModifiedDate)
- Enum mappings (@Enumerated)
- JSON column support for metadata
- Proper indexes

#### 4. Testing:
- Start the application
- Verify Hibernate creates all tables
- Check table structure in MySQL
- Verify foreign keys and indexes

### Estimated Time: 2-3 hours

---

## 🔍 STAGE 1 ISSUES FIXED

### Issue #1: Database Name Mismatch ✅ FIXED
**Before:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/admin-service-db
```

**After:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/admin-service
```

This now matches your MySQL schema name: `admin-service`

---

## 📌 IMPORTANT CONFIGURATION VALUES

| Setting | Value | Note |
|---------|-------|------|
| Service Name | admin-service | Eureka registration |
| Port | 8085 | HTTP port |
| Context Path | /cibf/admin-service | Base URL |
| Database Schema | admin-service | MySQL database |
| DB User | root | Change for production |
| DB Password | root | Change for production |
| JWT Secret | [configured] | Change for production |
| JWT Expiration | 24 hours | 86400000 ms |
| Eureka URL | http://localhost:8761/eureka/ | Service discovery |

---

## ⚠️ PRE-STAGE 2 CHECKLIST

Before starting Stage 2, ensure:

- [ ] MySQL is running
- [ ] Database `admin-service` exists
- [ ] Project compiles without errors (`mvnw.cmd clean compile`)
- [ ] You understand the entity structure from `admin-service-schema.sql`
- [ ] You have reviewed the IMPLEMENTATION_PLAN.md
- [ ] Service Registry (Eureka) is running on port 8761 (optional for now)

---

## 🆘 TROUBLESHOOTING

### Problem: MySQL Connection Failed
**Solution:**
1. Check MySQL service is running
2. Verify credentials in application.properties
3. Test with: `mysql -u root -proot`

### Problem: Port 8085 Already in Use
**Solution:**
1. Change port in application.properties
2. OR kill the process using port 8085
3. OR stop other running service

### Problem: Compilation Errors
**Solution:**
1. Check Java version: `java -version` (should be 17+)
2. Clean and rebuild: `mvnw.cmd clean install`
3. Check for IDE errors in the Problems tab

### Problem: Cannot Access Swagger UI
**Solution:**
1. Ensure service is running
2. Check URL: http://localhost:8085/cibf/admin-service/swagger-ui.html
3. Check SecurityConfig.java has permitAll for swagger paths

---

## 📞 READY FOR STAGE 2?

**Current Status:** ✅ Stage 1 Complete  
**Next Action:** Create JPA entities  
**Command to proceed:** 
```
Ready when you are! Just say "Let's start Stage 2" and I'll begin creating the entities.
```

---

**Last Updated:** November 4, 2025  
**Status:** Stage 1 ✅ COMPLETE | Ready for Stage 2 🚀

