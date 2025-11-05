@echo off
REM Quick Test Script - Tests the Admin Service endpoints

echo ========================================
echo   Admin Service Endpoint Tests
echo ========================================
echo.

REM Check if service is running
echo [TEST 1] Testing if service is running...
curl -s http://localhost:8081/api/v1/admin/test
if %errorlevel% equ 0 (
    echo.
    echo [OK] Service is responding
) else (
    echo.
    echo [ERROR] Service is not running on port 8081
    echo Please start the service first using: mvnw.cmd spring-boot:run
    pause
    exit /b 1
)

echo.
echo ========================================
echo.

REM Test database health
echo [TEST 2] Testing database connection...
echo.
curl -s http://localhost:8081/api/v1/admin/health
echo.
echo.

echo ========================================
echo Tests completed!
echo ========================================
echo.
echo If you see "HEALTHY" status above, your database is connected properly.
echo.
pause

