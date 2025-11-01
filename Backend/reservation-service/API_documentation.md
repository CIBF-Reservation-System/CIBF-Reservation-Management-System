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
            "eventName": "Tech Conference",
            "reservationDate": "2025-10-27T10:00:00",
            "startTime": "2025-10-27T14:00:00",
            "endTime": "2025-10-27T16:00:00",
            "notes": "Need projector setup"
        }   
        response:
            201 Created
            body:
                {
                "reservationId": "2fc0f923-5079-481b-8355-35abe6cfda92",
                "userId": "550e8400-e29b-41d4-a716-446655440000",
                "stallId": "550e8400-e29b-41d4-a716-446655440001",
                "eventName": "Tech Conference",
                "reservationDate": "2025-10-27T10:00:00",
                "startTime": "2025-10-27T14:00:00",
                "endTime": "2025-10-27T16:00:00",
                "status": "PENDING",
                "notes": "Need projector setup",
                "createdAt": null,
                "updatedAt": null,
                "message": "Reservation created successfully",
                "error": null
                }

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
                "eventName": "Tech Conference",
                "reservationDate": "2025-10-27T10:00:00",
                "startTime": "2025-10-27T14:00:00",
                "endTime": "2025-10-27T16:00:00",
                "status": "PENDING",
                "notes": "Need projector setup",
                "createdAt": null,
                "updatedAt": null,
                "message": "Reservation retrieved successfully",
                "error": null
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
            "eventName": "Tech Conference",
            "reservationDate": "2025-10-27T10:00:00",
            "startTime": "2025-10-27T14:00:00",
            "endTime": "2025-10-27T16:00:00",
            "status": "PENDING",
            "notes": "Need projector setup",
            "createdAt": null,
            "updatedAt": null,
            "message": "Reservation retrieved successfully",
            "error": null
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
                "eventName": "Tech Conference",
                "reservationDate": "2025-10-27T10:00:00",
                "startTime": "2025-10-27T14:00:00",
                "endTime": "2025-10-27T16:00:00",
                "status": "PENDING",
                "notes": "Need projector setup",
                "createdAt": null,
                "updatedAt": null,
                "message": "Reservation retrieved successfully",
                "error": null
            },
            {...}
        ]

# Update reservation status
request:
    PUT http://localhost:8083/api/v1/reservations/{reservationId}/status
    headers:
        Content-Type: text/plain
    body:
        {
            "status": "APPROVED"
        }
response:
    200 OK
    body:
        {
            "reservationId": "2fc0f923-5079-481b-8355-35abe6cfda92",
            "userId": "550e8400-e29b-41d4-a716-446655440000",
            "stallId": "550e8400-e29b-41d4-a716-446655440001",
            "eventName": "Tech Conference",
            "reservationDate": "2025-10-27T10:00:00",
            "startTime": "2025-10-27T14:00:00",
            "endTime": "2025-10-27T16:00:00",
            "status": "APPROVED",
            "notes": "Need projector setup",
            "createdAt": null,
            "updatedAt": null,
            "message": "Reservation status updated successfully",
            "error": null
        }

# Cancel a reservation
request:
    PATCH http://localhost:8083/api/v1/reservations/{reservationId}/cancel
response:
    200 OK
    body:
        {
            "reservationId": "2fc0f923-5079-481b-8355-35abe6cfda92",
            "userId": "550e8400-e29b-41d4-a716-446655440000",
            "stallId": "550e8400-e29b-41d4-a716-446655440001",
            "eventName": "Tech Conference",
            "reservationDate": "2025-10-27T10:00:00",
            "startTime": "2025-10-27T14:00:00",
            "endTime": "2025-10-27T16:00:00",
            "status": "CANCELLED",
            "notes": "Need projector setup",
            "createdAt": null,
            "updatedAt": null,
            "message": "Reservation cancelled successfully",
            "error": null
        }

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

