# Test controller
request:
    GET http://localhost:8083/api/v1/reservations/controller_test
response:
    200 OK
    Reservation Controller is working!

# Create a new reservation
request:
    POST http://localhost:8083/api/v1/reservations
    headers:
        Content-Type: text/plain
    body:
        {
            "userId": "550e8400-e29b-41d4-a716-446655440000",
            "stallId": "550e8400-e29b-41d4-a716-446655440001",
            "businessName": "Tech Solutions",
            "email": "contact@techsolutions.com",
            "phoneNumber": "123-456-7890"
        }   
        response:
            201 Created
            body:
                {
                    "reservationId": "2fc0f923-5079-481b-8355-35abe6cfda92",
                    "userId": "550e8400-e29b-41d4-a716-446655440000",
                    "stallId": "550e8400-e29b-41d4-a716-446655440001",
                    "businessName": "Tech Solutions",
                    "email": "contact@techsolutions.com",
                    "phoneNumber": "123-456-7890",
                    "reservationDate": "2025-10-27T10:00:00"
                }
        Error Responses:
            400 Bad Request - Invalid input data
            409 Conflict - Reservation already exists
            429 Too Many Requests - User has reached the maximum number of reservations allowed for the selected date


# Get all reservations
request:
    GET http://localhost:8083/api/v1/reservations
response:
    200 OK
    body:
        [
            {
                "reservationId": "2fc0f923-5079-481b-8355-35abe6cfda92",
                "userId": "550e8400-e29b-41d4-a716-446655440000",
                "stallId": "550e8400-e29b-41d4-a716-446655440001",
                "businessName": "Tech Solutions",
                "email": "contact@techsolutions.com",
                "phoneNumber": "123-456-7890"
            },
            {...}
        ]

# Get reservation by ID
request:
    GET http://localhost:8083/api/v1/reservations/{reservationId}
response:
    200 OK
    body:
        {
            "reservationId": "2fc0f923-5079-481b-8355-35abe6cfda92",
            "userId": "550e8400-e29b-41d4-a716-446655440000",
            "stallId": "550e8400-e29b-41d4-a716-446655440001",
            "businessName": "Tech Solutions",
            "email": "contact@techsolutions.com",
            "phoneNumber": "123-456-7890"
        }

# Get reservations by User ID
request:
    GET http://localhost:8083/api/v1/reservations/user/{userId}
response:
    200 OK
    body:
        [
            {
                "reservationId": "2fc0f923-5079-481b-8355-35abe6cfda92",
                "userId": "550e8400-e29b-41d4-a716-446655440000",
                "stallId": "550e8400-e29b-41d4-a716-446655440001",
                "businessName": "Tech Solutions",
                "email": "contact@techsolutions.com",
                "phoneNumber": "123-456-7890"
            },
            {...}
        ]



# Delete a reservation
request:
    DELETE http://localhost:8083/api/v1/reservations/{reservationId}
response:
    200 OK
    body:
        {
            "message": "Reservation deleted successfully",
            "error": null
        }

