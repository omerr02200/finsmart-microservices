
## 🗺️ Project Roadmap
- [x] API Gateway & Service Discovery
- [x] JWT Auth & Redis Token Management
- [x] Transactional Outbox Pattern with Kafka
- [ ] Role-Based Access Control (RBAC)
- [ ] AI-Driven Expenditure Analysis (Gemini Integration)
- [ ] Unit & Integration Testing (JUnit 5 & Mockito)
- [ ] Distributed Tracing & Monitoring

### Servisler
- **auth-service:** JWT, Refresh Token ve Redis Blacklist ile güvenli kimlik doğrulama.
- **finance-service:** Harcama yönetimi ve Transactional Outbox Pattern ile veri bütünlüğü.
- **analysis-service:** Kafka üzerinden event-driven harcama analizi ve raporlama.
- **api-gateway:** Merkezi yönlendirme ve servis izolasyonu.

# Finsmart Microservices Ecosystem
Bu proje, yüksek ölçeneklebilir ve dayanaklı(resilient) bir finansal takip mimarisini sergilemek amacaıyla geliştirilmiştir.
## Öne Çıkan Teknik Özellikler
- **Global Exception Handling:** @ControllerAdvice ve @ExceptionHandler kullanılarak merkezi bir hata yönetim mekanizması kuruldu. Tüm servisler, hata durumlarında Timestamp, Message, Details ve HTTP Status içeren standart bir JSON yanıtı döner.
- **Transactional Outbox Pattern:** Veritabanı ve mesaj kuyruğu arasındaki veri tutarlılığını (consistency) garanti altına alır.
- **Event-Driven Architecture** Servisler arası iletişim Kafka üzerinden tamamen asenkron ve gevşek bağlı **(loosely coupled)** olarak kurgulanmıştır.
- **Database Per Service** Her mikroservis kendi veritabanını (finance_db, analysis_db) yöneterek veri izolasyonu sağlar.
- **Resilience Testi** Kafka geçici olarak devre dışı kalsa bile, Outbox Relay mekanizması sayesinde sistem ayağa kalktığında veri kaybı yaşanmadan iletim devam eder.
## Teknolojiler
- **Backend** Java 21, Spring Boot 3.3.5, Spring Data JPA
- **Spring Security & JWT** (Refresh Token destekli)
- **PostgreSQL** (Ana veritabanı)
- **Messaging:** Apache Kafka
- **Redis** (Token Blacklist & Caching)
- **Flyway** (Veritabanı Versiyonlama)
- **Architecture:** Microservices, Event-Driven, Outbox Pattern
- **Docker & Docker Compose**
