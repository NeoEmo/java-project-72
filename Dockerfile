# --- ЭТАП 1: Сборка ---
FROM gradle:9.2.0-jdk21-alpine AS build

WORKDIR /app

# Node.js и npm нужны для сборки Tailwind CSS
RUN apk add --no-cache nodejs npm

# Копируем содержимое app/ (gradlew, build.gradle.kts, src, package.json и т.д.)
COPY app/ /app/

# Ставим npm-зависимости и собираем main.css
RUN npm ci && npm run build:css

# Собираем приложение. main.css уже лежит в src/main/resources/static/css/
RUN gradle installDist -x test

# --- ЭТАП 2: Запуск ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/build/install/app /app/

EXPOSE 7070
CMD ["./bin/app"]