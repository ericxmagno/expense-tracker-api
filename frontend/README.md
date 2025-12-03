# Expense Tracker Frontend

React frontend for the Expense Tracker API.

## Features

- View expenses with pagination
- Filter expenses by category, date range
- Sort expenses by date, title, amount, or ID
- Create new expenses
- Edit existing expenses
- Delete expenses
- View monthly summary with total spent and expense count

## Development

### Prerequisites

- Node.js 20 or higher
- npm

### Setup

```bash
cd frontend
npm install
```

### Run Development Server

```bash
npm run dev
```

The app will be available at `http://localhost:3000` (or the port shown in the terminal).

The Vite dev server is configured to proxy API requests to `http://localhost:8080`.

### Build for Production

```bash
npm run build
```

This creates a `dist` folder with the production build. The Dockerfile in the backend folder will automatically build this and serve it from the Spring Boot backend.

## Docker Integration

The frontend is automatically built and served by the backend Docker container. When you run `docker-compose up`, the Dockerfile will:

1. Build the React frontend
2. Copy the built files to the backend's static resources
3. Serve the frontend from the Spring Boot application at `http://localhost:8080`

No separate frontend container is needed - the frontend is served as static files from the backend.

