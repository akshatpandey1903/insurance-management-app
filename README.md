# Insurance Management System

This is a capstone project that serves as an Insurance Management System, developed using a Java Spring Boot backend and an Angular frontend. This project follows an industry-level structure and is designed for scalability and maintainability.

## Repository Structure
```
📂 insurance-management
│── 📁 backend
│   ├── 📁 src
│   │   ├── 📂 main/java/com/insurance
│   │   │   ├── 📁 config
│   │   │   ├── 📁 controller
│   │   │   ├── 📁 dto
│   │   │   ├── 📁 entity
│   │   │   ├── 📁 exception
│   │   │   ├── 📁 repository
│   │   │   ├── 📁 security
│   │   │   ├── 📁 service
│   │   │   │   ├── 📁 impl
│   │   │   ├── 📁 util
│   │   ├── 📂 resources
│── 📁 frontend
│   ├── 📁 src
│   │   ├── 📁 app
│   │   │   ├── 📁 components
│   │   │   ├── 📁 services
│   │   │   ├── 📝 app.component.ts
│   │   ├── 📂 assets
│   ├── 📝 angular.json
│── 📁 documentation
│── 📄 README.md
│── 📄 .gitignore
│── 📄 package.json
│── 📄 build.gradle / pom.xml
```

## Technologies Used
- **Backend:** Java, Spring Boot, Hibernate, MySQL
- **Frontend:** Angular, TypeScript, HTML, CSS
- **Other Tools:** Git, IntelliJ, VS Code

## Core Services
- User Management and Authentication
- Admin Management and Reporting
- Policy and Claim Handling
- Commission Calculation
- Email Notifications
- Payment Gateway Integration
- Cloud Storage for Images and Documents

## How to Run
### Backend
1. Navigate to the backend folder:
   ```sh
   cd backend
   ```
2. Build and run using Gradle/Maven:
   ```sh
   ./gradlew bootRun  # For Gradle
   mvn spring-boot:run  # For Maven
   ```
3. API will be accessible at `http://localhost:8080`

### Frontend
1. Navigate to the frontend folder:
   ```sh
   cd frontend
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Run the Angular application:
   ```sh
   ng serve
   ```
4. Application will be accessible at `http://localhost:4200`

## Collaboration Guidelines
- Use feature branches for new functionality
- Open pull requests for code review
- Follow proper commit message format

## Future Enhancements
- Implement Email Notifications
- Add Multi-Factor Authentication
- Optimize Database Queries

---
Happy Coding! 🚀


