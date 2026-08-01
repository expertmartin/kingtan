# Stage 1: Build the Angular application
FROM node:18-alpine AS build

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the Angular app for production
RUN npm run build --prod

# Stage 2: Serve the app with Node.js (Express)
FROM node:18-alpine

# Set working directory
WORKDIR /app

# Copy package.json and package-lock.json for the server
COPY package*.json ./

# Install only production dependencies
RUN npm install --production
RUN npm install express
# RUN npm install path

# Copy the built Angular app from the build stage
COPY --from=build /app/dist/sports-store ./dist/store

# Copy the Node.js server file
COPY server.js .

# Expose port 8080
EXPOSE 80

# Start the Node.js server
CMD ["node", "server.js"]