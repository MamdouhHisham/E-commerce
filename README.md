# Ecommerce Microservices Project

**Note: This project is currently under active development. The APIs and architecture are subject to change.**

## Project Architecture

This project implements a microservices architecture for a modern ecommerce platform. Each service is designed to be a self-contained, independently deployable unit responsible for a specific business capability. This approach promotes scalability, resilience, and maintainability.

The services communicate with each other via REST APIs. In the future, will use a message broker for asynchronous communication

## Technologies Used

*   **Backend:** Java 17, Spring Boot 3
*   **Data:** Spring Data JPA, MySQL
*   **Web:** Spring Web for RESTful APIs
*   **Validation:** Spring Validation
*   **API Communication:** Spring Cloud OpenFeign (for synchronous REST calls)
*   **Image Storage:** Cloudinary
*   **Developer Tools:** Lombok
*   **Build Tool:** Maven

## Services

### Current Services

*   **Product Service:** Manages all aspects of products, including categories and product images.
*   **Inventory Service:** Tracks stock for each product.

### Future Services

As the project evolves, the following services will be added:

*   **Order Service:** Handles order creation, processing, and history.
*   **Payment Service:** Integrates with payment gateways to process transactions.
*   **User Service:** Manages user authentication, profiles, and authorization.
*   **Email Service:** Sends transactional emails for events like order confirmation

---

## API Endpoints

### Product Service (`http://localhost:8081`)

| Method | Endpoint | Description | Request Body/Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/product/{id}` | Get a single product by its ID. | `id` (path variable) |
| `GET` | `/api/product` | Get a list of all products. | |
| `POST` | `/api/product` | Add a new product. | `image` (multipart), `product` (JSON part) |
| `PUT` | `/api/product/{id}` | Update an existing product. | `id` (path), `image` (multipart, optional), `product` (JSON part) |
| `DELETE`| `/api/product/{id}` | Delete a product. | `id` (path variable) |
| `GET` | `/api/category/{id}` | Get a single category by its ID. | `id` (path variable) |
| `GET` | `/api/category` | Get a list of all categories. | |
| `POST` | `/api/category` | Add a new category. | `Category` (JSON body) |
| `PUT` | `/api/category/{id}` | Update an existing category. | `id` (path), `Category` (JSON body) |
| `DELETE`| `/api/category/{id}` | Delete a category. | `id` (path variable) |

### Inventory Service (`http://localhost:8082`)

| Method | Endpoint | Description | Request Body/Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/inventory/{id}` | Get inventory details by ID. | `id` (path variable) |
| `POST` | `/api/inventory` | Add a new product to the inventory. | `skuCode` (param), `quantity` (param) |
| `PUT` | `/api/inventory/quantity/{skuCode}` | Update the stock quantity for a product. | `skuCode` (path), `quantity` (param) |
| `PUT` | `/api/inventory/sku/{skuCode}` | Update the SKU code for a product. | `skuCode` (path), `newSkuCode` (param) |
| `DELETE`| `/api/inventory/{id}` | Delete an inventory record. | `id` (path variable) |
