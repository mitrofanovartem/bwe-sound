# BWE Sound

Учебный музыкальный сервис с микросервисной архитектурой. Сейчас здесь только базовый каркас: локальная инфраструктура и `identity-service`.

## Структура

```text
services/
  identity-service/  # пользователи и аутентификация
frontend/            # клиентское приложение — зона работы frontend-разработчика
compose.yaml         # PostgreSQL, Redis, Kafka для разработки
```

## Запуск

```bash
docker compose up -d
mvn test
mvn -pl services/identity-service spring-boot:run
```

Проверка сервиса: `http://localhost:8081/actuator/health`.

Следующий небольшой шаг — добавить в `identity-service` регистрацию пользователя и Flyway-миграцию для таблицы пользователей.
