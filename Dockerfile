# --- ЭТАП 1: Сборка ---
FROM gradle:9.2.0-jdk21-alpine AS build

WORKDIR /app

# Копируем содержимое папки app (там лежит gradlew, build.gradle.kts и src)
COPY app/ /app/

# Собираем приложение
RUN gradle installDist -x test

# --- ЭТАП 2: Запуск ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Копируем готовую сборку. Теперь мы знаем, что папка называется "app"
COPY --from=build /app/build/install/app /app/

# Открываем порт
EXPOSE 7070

# Запускаем приложение. Скрипт тоже называется "app"
CMD ["./bin/app"]