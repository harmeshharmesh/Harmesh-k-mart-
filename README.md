# 🛍️ Harmesh K-mart

> A full-stack e-commerce web application built with **Spring Boot, Java, JPA/Hibernate, H2 Database, Spring Security, HTML, CSS, and JavaScript**.

Harmesh K-mart is a modern online shopping platform that provides customers with a complete shopping experience — from account creation and product discovery to cart management, wishlist, checkout, order tracking, reviews, addresses, and personalized recommendations.

It also includes an **admin dashboard** for managing products, users, and store operations.

---

## ✨ Features

### 👤 User Authentication

- User registration and login
- Secure password hashing
- Session-based authentication
- Logout functionality
- User profile management
- Password change functionality
- Role-based user model

### 🛒 Shopping

- Browse products
- Product search
- Category filtering
- Product details
- Persistent shopping cart
- Add/remove products from cart
- Cart quantity management
- Wishlist functionality
- Product recommendations

### 📦 Orders

- Checkout flow
- Order creation
- Order history
- Order item management
- Order status tracking
- Order status lifecycle

### 💳 Payments

- Payment endpoint integrated into the checkout architecture
- Designed so a production payment gateway can be added later

> **Note:** The current payment implementation is intended for development/demo purposes and should be replaced with a real payment provider before production deployment.

### ⭐ Reviews & Ratings

- Product reviews
- Product ratings
- Review management
- Customer feedback associated with products

### 📍 Address Management

- Save customer addresses
- Manage addresses for checkout
- Associate addresses with customer orders

### 🛠️ Admin Dashboard

- Admin interface
- Product management
- User management
- Store administration

### 🎨 Frontend

The project includes a responsive static frontend with dedicated pages for:

- Home
- Login
- Signup
- Products
- Cart
- Wishlist
- Orders
- Profile
- Admin Dashboard

The frontend communicates with the backend through REST APIs.

---

## 🧰 Tech Stack

### Backend

| Technology             | Purpose                       |
| ---------------------- | ----------------------------- |
| **Java 17**            | Application development       |
| **Spring Boot 3.5.4**  | Backend framework             |
| **Spring Web**         | REST API development          |
| **Spring Data JPA**    | Database persistence          |
| **Hibernate**          | ORM                           |
| **Spring Security**    | Authentication/security       |
| **Jakarta Validation** | Request validation            |
| **H2 Database**        | Development database          |
| **Maven**              | Dependency & build management |

### Frontend

| Technology             | Purpose                 |
| ---------------------- | ----------------------- |
| **HTML5**              | Page structure          |
| **CSS3**               | Styling & responsive UI |
| **Vanilla JavaScript** | Frontend logic          |
| **Fetch API**          | REST API communication  |
| **SVG Icons**          | UI icons                |

---

## 🏗️ Architecture

Harmesh K-mart follows a modular Spring Boot architecture.

```text
Harmesh K-mart
│
├── Backend
│   ├── Authentication
│   ├── Users
│   ├── Products
│   ├── Categories
│   ├── Cart
│   ├── Wishlist
│   ├── Orders
│   ├── Payments
│   ├── Reviews
│   ├── Addresses
│   ├── Recommendations
│   └── Admin
│
├── Frontend
│   ├── Home
│   ├── Login
│   ├── Signup
│   ├── Products
│   ├── Cart
│   ├── Wishlist
│   ├── Orders
│   ├── Profile
│   └── Admin
│
└── Database
    └── H2
```

---

## 📁 Project Structure

```text
Harmesh-k-mart-/
│
├── data/
│   ├── harmesh-k-mart-db.mv.db
│   └── harmesh-k-mart-db.lock.db
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/ecommerce/
│       │       ├── address/
│       │       ├── admin/
│       │       ├── auth/
│       │       ├── cart/
│       │       ├── common/
│       │       ├── config/
│       │       ├── order/
│       │       ├── payment/
│       │       ├── product/
│       │       ├── recommendation/
│       │       ├── review/
│       │       ├── user/
│       │       ├── wishlist/
│       │       └── EcommerceApplication.java
│       │
│       └── resources/
│           ├── static/
│           │   ├── assets/
│           │   │   ├── css/
│           │   │   └── js/
│           │   ├── admin.html
│           │   ├── cart.html
│           │   ├── home.html
│           │   ├── index.html
│           │   ├── login.html
│           │   ├── orders.html
│           │   ├── product.html
│           │   ├── profile.html
│           │   ├── signup.html
│           │   └── wishlist.html
│           │
│           └── application.properties
│
├── pom.xml
└── README.md
```

---

## ⚙️ Requirements

Before running the project, make sure you have:

- **Java 17 or newer**
- **Apache Maven 3.8+**
- Git

Verify your installations:

```bash
java -version
mvn -version
git --version
```

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/harmeshharmesh/Harmesh-k-mart-.git
```

### 2. Enter the project directory

```bash
cd Harmesh-k-mart-
```

### 3. Build the application

```bash
mvn clean package
```

### 4. Start the application

Using Maven:

```bash
mvn spring-boot:run
```

Or run the generated JAR:

```bash
java -jar target/harmesh-k-mart-1.0.0.jar
```

### 5. Open the application

Once the server starts, open:

```text
http://localhost:8080/home.html
```

---

## 🔐 Demo Admin Account

For local development, the application includes a demo administrator account.

```text
Email:    admin@harmesh-k-mart.com
Password: Admin@123
```

> ⚠️ **Security warning:** Never use demo credentials or default passwords in a production environment. Change/remove them before deployment.

---

## 🗄️ Database

The development version uses an **H2 file-based database**.

The database is configured in:

```text
src/main/resources/application.properties
```

Current configuration:

```properties
spring.datasource.url=jdbc:h2:file:./data/harmesh-k-mart-db;AUTO_SERVER=TRUE
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### H2 Console

The H2 database console is available at:

```text
http://localhost:8080/h2-console
```

Use the configured JDBC URL:

```text
jdbc:h2:file:./data/harmesh-k-mart-db
```

---

## 🔌 REST API

The application exposes REST APIs for the main e-commerce operations.

### Authentication

```text
POST   /api/auth/signup
POST   /api/auth/login
GET    /api/auth/me
POST   /api/auth/logout
PUT    /api/auth/profile
POST   /api/auth/change-password
```

### Other API Modules

The backend is organized around dedicated modules for:

```text
/api/auth
/api/products
/api/categories
/api/cart
/api/orders
/api/payment
/api/reviews
/api/wishlist
/api/address
/api/recommendation
/api/admin
```

> Endpoint details may evolve as the application continues to develop.

---

## 🔒 Security

The application uses **Spring Security** and session-based authentication.

Authentication flow:

```text
User
 │
 ▼
Login / Signup
 │
 ▼
Spring Boot API
 │
 ▼
Password Verification
 │
 ▼
HTTP Session
 │
 ▼
Authenticated Requests
```

Passwords are encoded before being stored rather than being stored as plain text.

---

## 🛍️ Application Flow

A typical customer journey looks like this:

```text
                    ┌──────────────┐
                    │   Homepage   │
                    └──────┬───────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │ Browse Products │
                  └────────┬────────┘
                           │
                           ▼
                  ┌─────────────────┐
                  │ Product Details │
                  └────────┬────────┘
                           │
                 ┌─────────┴─────────┐
                 ▼                   ▼
             Wishlist              Cart
                                     │
                                     ▼
                                  Checkout
                                     │
                                     ▼
                                  Payment
                                     │
                                     ▼
                                  Order
                                     │
                                     ▼
                              Order Tracking
```

---

## 🧪 Development

Run the application in development mode:

```bash
mvn spring-boot:run
```

Build without running:

```bash
mvn clean package
```

Clean the Maven build:

```bash
mvn clean
```

---

## 📌 Current Development Status

### Implemented

- [x] User authentication
- [x] User profiles
- [x] Password management
- [x] Products
- [x] Categories
- [x] Product search
- [x] Category filtering
- [x] Shopping cart
- [x] Wishlist
- [x] Checkout/order creation
- [x] Order status tracking
- [x] Payment endpoint
- [x] Admin dashboard
- [x] Reviews & ratings
- [x] Address management
- [x] Recommendation endpoint
- [x] H2 persistence
- [x] Responsive static frontend

### Planned Production Improvements

- [ ] Production-grade role-based authorization
- [ ] PostgreSQL/MySQL production database
- [ ] Database migrations with Flyway or Liquibase
- [ ] Production payment gateway
- [ ] Payment webhooks
- [ ] Email notifications
- [ ] OTP/password recovery
- [ ] Cloud image storage
- [ ] Comprehensive automated tests
- [ ] DTO-based API architecture
- [ ] Dedicated service layer improvements
- [ ] Improved CSRF/session hardening
- [ ] Structured application logging
- [ ] Docker support
- [ ] CI/CD pipeline
- [ ] Production monitoring
- [ ] API documentation with OpenAPI/Swagger

---

## 🚧 Production Considerations

This repository is suitable for development and demonstration purposes.

Before deploying publicly, review:

1. **Authentication and authorization**
2. **Session security**
3. **CSRF protection**
4. **Database credentials**
5. **Admin credentials**
6. **Payment security**
7. **Input validation**
8. **CORS configuration**
9. **Error handling**
10. **Logging and monitoring**
11. **Database migrations**
12. **Secrets/environment variables**

Sensitive configuration should be moved from source-controlled configuration files into environment variables or a secure secrets manager.

---

## 🌐 Deployment

The application can be packaged as a Spring Boot executable JAR:

```bash
mvn clean package
```

The resulting artifact will be located under:

```text
target/
```

A production deployment can run the application with:

```bash
java -jar target/harmesh-k-mart-1.0.0.jar
```

For production environments, configure an external database and secure environment-specific configuration rather than relying on the development H2 setup.

---

## 🤝 Contributing

Contributions, suggestions, and improvements are welcome.

### Fork the repository

```bash
git clone https://github.com/harmeshharmesh/Harmesh-k-mart-.git
```

Create a feature branch:

```bash
git checkout -b feature/your-feature
```

Make your changes and commit:

```bash
git add .
git commit -m "Add your feature"
```

Push the branch:

```bash
git push origin feature/your-feature
```

Then open a Pull Request.

---

## 🐛 Reporting Issues

If you find a bug or have a feature request, please open an issue with:

- A clear description
- Steps to reproduce
- Expected behavior
- Actual behavior
- Relevant screenshots/logs
- Environment details

---

## 📜 License

No explicit license is currently specified for this repository.

If you intend to distribute, modify, or commercially use the project, add an appropriate open-source license such as MIT, Apache-2.0, or GPL-3.0 according to your requirements.

---

## 👨‍💻 Author

**Harmesh**

GitHub:

[harmeshharmesh](https://github.com/harmeshharmesh?utm_source=chatgpt.com)

Project:

[Harmesh K-mart Repository](https://github.com/harmeshharmesh/Harmesh-k-mart-?utm_source=chatgpt.com)

---

## ⭐ Support the Project

If you find **Harmesh K-mart** useful:

- ⭐ Star the repository
- 🐛 Report bugs
- 💡 Suggest improvements
- 🔧 Contribute code
- 📢 Share the project

---

## ❤️ Built with Spring Boot

**Harmesh K-mart** combines a Spring Boot REST backend with a lightweight frontend to provide a complete e-commerce application architecture that can be extended toward a production-ready online shopping platform.
