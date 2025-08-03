# Roomy - Roommate and Rental Finder Backend

A comprehensive Spring Boot 3.x backend application for a roommate and rental finder SaaS platform.

## Features

### 🔐 Authentication
- JWT-based authentication
- OTP login flow with email verification
- Secure password change functionality
- Role-based authorization (USER, ADMIN)

### 👤 User Management
- Instagram-style unique usernames
- Rich user profiles with lifestyle preferences
- Document upload for verification via Cloudinary
- Social links integration

### 🏠 Room Management
- Create, update, delete room posts
- Multi-image upload for rooms
- Advanced search and filtering
- Only verified users can post rooms

### 💬 Messaging System
- One-on-one chat between users
- Message history and unread count
- Real-time communication support

### 🛂 Admin Panel
- User verification management
- Document review and approval
- User management dashboard

## Tech Stack

- **Java 17** + **Spring Boot 3.x**
- **Spring Data JPA** + **MySQL**
- **Spring Security** + **JWT**
- **Cloudinary** for file/image uploads
- **Docker** for containerization
- **Maven** for dependency management

## Quick Start

### Prerequisites
- Java 17+
- Docker and Docker Compose
- Maven 3.6+

### 1. Start the Database
```bash
docker-compose up -d mysql
```

### 2. Configure Environment Variables
Create `.env` file or set environment variables:
```bash
# Database
DATABASE_URL=jdbc:mysql://localhost:3306/roomy_db
DB_USERNAME=roomy_user
DB_PASSWORD=roomy_password

# JWT
JWT_SECRET=your-super-secret-jwt-key-here

# Email (for OTP)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Cloudinary
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## API Documentation

### Authentication Endpoints
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login with email/password
- `POST /api/auth/send-otp` - Send OTP to email
- `POST /api/auth/verify-otp` - Verify OTP and login
- `POST /api/auth/change-password` - Change password
- `GET /api/auth/me` - Get current user info

### User Endpoints
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile
- `POST /api/users/upload-document` - Upload verification document
- `GET /api/users/{username}` - Get user by username

### Room Endpoints
- `POST /api/rooms` - Create new room
- `GET /api/rooms` - Get rooms with search/filter
- `GET /api/rooms/{id}` - Get room by ID
- `PUT /api/rooms/{id}` - Update room
- `DELETE /api/rooms/{id}` - Delete room
- `POST /api/rooms/{id}/images` - Upload room images
- `POST /api/rooms/{id}/interest` - Show interest in room
- `DELETE /api/rooms/{id}/interest` - Remove interest
- `GET /api/rooms/my-rooms` - Get user's rooms

### Message Endpoints
- `POST /api/messages` - Send message
- `GET /api/messages/conversation/{userId}` - Get conversation
- `GET /api/messages/conversations` - Get conversation partners
- `PUT /api/messages/{messageId}/read` - Mark as read
- `GET /api/messages/unread-count` - Get unread count

### Admin Endpoints
- `GET /api/admin/users/pending-verification` - Get users pending verification
- `PUT /api/admin/users/{userId}/verification` - Update verification status
- `GET /api/admin/users/{userId}` - Get user details

## Database Schema

The application uses MySQL with the following main entities:
- **User** - User profiles with lifestyle and social data
- **Room** - Room listings with amenities and preferences
- **Message** - Chat messages between users
- **RoomInterest** - User interest in specific rooms

## Security

- JWT tokens for stateless authentication
- Role-based access control
- Password encryption with BCrypt
- CORS configuration for cross-origin requests
- Input validation and sanitization

## File Uploads

Uses Cloudinary for:
- User document verification uploads
- Room image uploads
- Automatic image optimization and transformation

## Testing

Access phpMyAdmin at `http://localhost:8081` to view the database.

Default admin credentials:
- Email: admin@roomy.com
- Password: password123

## Production Deployment

1. Set up production environment variables
2. Use `application-prod.yml` profile
3. Configure SSL/TLS certificates
4. Set up monitoring and logging
5. Configure backup strategies

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.