# MyMart

Multi-seller e-commerce marketplace — Java Servlets, JDBC, Tomcat 9 (Anna University R2025, Sem 3 capstone).

**⚠️ Before you push:** replace `MyMart`, `com.myname.mymart`, and every `yourname`/`mymart` occurrence
with your own name, exactly like the spec says (e.g. Aditya → `AdityaMart`, package `com.aditya.adityamart`).
Search-and-replace across the repo (README, package folders, `pom.xml`, JDBC URLs).

## Live deployment
`<add your live cloud URL here once deployed — Section 10>`

## Tech stack

| Layer | Tech |
|---|---|
| Language | Java 17 |
| Container | Tomcat 9.0.x (Servlet 4.0, `javax.servlet.*`) |
| Build | Maven |
| DB | H2 (server mode in prod, embedded for tests) |
| Pooling | HikariCP |
| Views | JSP + JSTL, vanilla JS/fetch for AJAX |
| JSON | Gson |
| Passwords | jBCrypt |
| Tests | JUnit 5 + Mockito |
| Logging | SLF4J + Logback |
| CI | GitHub Actions (`mvn -B clean verify` on every push) |

## Architecture

```
Browser (HTML/CSS/JS + fetch)
  -> EncodingFilter, AuthFilter
  -> Servlets (controller/) — thin, no SQL
  -> Service layer (service/) — business rules
  -> DAO layer (dao/) — all SQL, PreparedStatement only
  -> HikariCP (listener/DataSourceListener)
  -> H2 database
```

Package layout: `controller/`, `service/`, `dao/`, `model/`, `filter/`, `listener/`, `util/`.
See the project spec PDF for the full ER diagram, use-case diagram, and sequence diagram (D1–D3) —
those still need to be produced separately (draw.io/PlantUML) and dropped into `/docs`.

## Feature status

| ID | Feature | Status |
|---|---|---|
| F1 | Register/login, Buyer/Seller roles, seeded Admin | ✅ |
| F2 | Seller listing CRUD | ✅ |
| F3 | Browse/search/filter | ✅ |
| F4 | Cart (add/update/remove, running total) | ✅ |
| F5 | Checkout via mock payment | ✅ (transactional) |
| F6 | Order history (buyer + seller) | ✅ |
| F7 | Admin: view users/orders, remove listings | ✅ (basic) |
| F8 | Reviews/ratings on delivered orders | ✅ |
| O2 | Order status workflow | ⚠️ schema + `updateStatus` ready; needs a servlet/UI |
| O1, O3 | Wishlist, seller dashboard | ❌ not started |
| O4 | AI chatbot | ✅ scaffold (mock provider + widget); swap in a real API key for Week 9 |

## Setup

See `CONTRIBUTING.md` for full clone-to-running-instance steps.

Quick version:
```bash
cp src/main/resources/config.properties.example src/main/resources/config.properties
mvn clean package
# deploy target/mymart.war to Tomcat 9's webapps/
```

## Security checklist status (Section 9)
- [x] All queries parameterized (PreparedStatement only, verified by convention across DAO layer)
- [x] Passwords bcrypt-hashed, never logged
- [x] Protected servlets enforce session checks via `AuthFilter`
- [x] User-supplied output rendered via `textContent`, not `innerHTML`, in JS-rendered views
- [ ] File upload validation — not applicable yet (image URLs only, no file upload implemented)
- [x] Custom error pages, no stack traces exposed
- [x] `config.properties` excluded via `.gitignore`

## What's left before your next checkpoint
1. Produce the 3 required diagrams (ER, use-case, sequence) and drop them in `/docs`.
2. Wire O2 (order status workflow UI) if you want it before Week 5.
3. Add screenshots to this README once you have a working UI to show.
4. Run Checkstyle/SpotBugs and wire them into `pom.xml` + CI (Section 12).
5. Replace the placeholder bcrypt hashes in `db/seed.sql` with real ones (`PasswordUtil` has a `main()` for this).
