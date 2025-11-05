@echo off
REM Admin Service Database Connection Test Script
REM This script helps you test the MySQL connection and start the service

echo ========================================
echo   CIBF Admin Service - DB Test Script
echo ========================================
echo.

REM Step 1: Check MySQL
echo [STEP 1] Checking MySQL installation...
echo.

mysql --version >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] MySQL is installed
    mysql --version
) else (
    echo [ERROR] MySQL not found in PATH
    echo Please ensure MySQL is installed and added to system PATH
    echo Installation guide: https://dev.mysql.com/downloads/installer/
    pause
    exit /b 1
)

echo.
echo ========================================
echo.

REM Step 2: Test MySQL Connection
echo [STEP 2] Testing MySQL connection...
echo.
echo Please enter your MySQL root password when prompted:
echo.

mysql -u root -p -e "SHOW DATABASES;" >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] MySQL connection successful
) else (
    echo [ERROR] Could not connect to MySQL
    echo Please check:
    echo   1. MySQL service is running
    echo   2. Username/password is correct
    echo   3. MySQL is running on port 3306
    pause
    exit /b 1
)

echo.
echo ========================================
echo.

REM Step 3: Check/Create Database
echo [STEP 3] Checking database...
echo.
echo The database 'admin_service_db' will be created automatically by Spring Boot
echo.

REM Step 4: Start the service
echo [STEP 4] Starting Admin Service...
echo.
echo Press any key to start the service, or CTRL+C to exit
pause >nul

echo.
echo Starting service on port 8081...
echo.
echo ========================================
echo   Service Logs (Press CTRL+C to stop)
echo ========================================
echo.

call mvnw.cmd spring-boot:run

echo.
echo Service stopped.
pause

