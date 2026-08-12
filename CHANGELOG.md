# Changelog

## v0.1.0 — MVP scaffold
- Auth (register/login/logout), session regen on login, bcrypt hashing.
- Product listing CRUD (seller), browse/search (buyer).
- Cart (add/update/remove, running total).
- Checkout via mock payment, transactional (order + stock decrement + cart clear).
- Order history (buyer view + seller incoming orders).
- Admin: view users/orders, remove listings.
- Reviews/ratings gated to delivered orders.
- AI chatbot scaffold (mock provider, rate limiting, caching) — ready for a real provider swap.
- Health check endpoint, custom error pages, DAO + service unit tests, CI workflow.
