# Stage 1: Build Angular app
FROM node:20-alpine AS build
WORKDIR /app
# Install dependencies
COPY package*.json ./
RUN npm install
# Copy source code
COPY . .
# Build Angular (uses environment.prod.ts)
RUN npm run build -- --configuration production

# Stage 2: Serve with Nginx
FROM nginx:alpine
# Remove default nginx content
RUN rm -rf /usr/share/nginx/html/*
# Copy Angular 18 build output (browser directory contents)
COPY --from=build /app/dist/loan-pricing-deal/browser /usr/share/nginx/html
# SPA routing
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
