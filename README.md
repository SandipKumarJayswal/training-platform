# Training Platform — catalog + enrollments (+ notifications)

## Build & run

    mvn clean verify
    mvn -pl app spring-boot:run

App starts on http://localhost:8080 with an in-memory H2 database; Flyway runs every
domain's migrations on startup.

## Module diagram

    common-api   <-----------------+--------------+--------------+
       ^                           |              |              |
       |                      catalog-api   enrollments-api  notifications-api
       |                           ^              ^  ^             ^
    common-core (impl)             |              |  |             |
       ^                      catalog-core         |  +-- notifications-core
       |                                           |           ^
       +---------------------- enrollments-core ---+           |
                                                                |
                              app -----------------------------+
        (single @SpringBootApplication, depends on every -api and -core)

- catalog-api / catalog-core — owns courses.
- enrollments-api / enrollments-core — owns enrollments; depends on catalog-api for the
  synchronous "does this course exist & accept enrollments" check, and reacts to
  catalog-api.CoursePublishedEvent via the shared event abstraction.
- notifications-api / notifications-core — reacts to enrollments-api.EnrollmentCreatedEvent;
  depends on enrollments-api only, never on enrollments-core.
- common-api — two generic interfaces (DomainEventPublisher, DomainEventHandler) plus a
  DomainEvent marker. Every -api module depends only on this + the JDK.
- common-core — the one in-process implementation of those interfaces.
- app — the single @SpringBootApplication, wires everything on one port.

## Decisions

1. **Generic pub/sub via common-api/common-core instead of ad-hoc Spring @EventListener per
   consumer.** Keeps every producer/consumer coding against our own DomainEventPublisher/
   DomainEventHandler contract instead of Spring's API directly, so a later move to a broker
   only touches common-core.

2. **Sync cross-domain call via an interface (CoursesApi) Spring auto-wires to a local
   @Service, not an HTTP call.** Same-JVM call today; the seam (implement the same interface
   with an HTTP client) is there for later without touching enrollments-core.

3. **Capacity check counts rows in `enrollments`, not a counter field on Course.** Capacity
   accounting is enrollments' business, not catalog's.

4. **Migrations live per-domain** (catalog-core, enrollments-core, notifications-core),
   globally version-numbered (V1/V2/V3) to avoid Flyway collisions, rather than one shared
   folder in app — keeps each domain self-contained.

5. **Package-by-subdomain** (enrollments.core.enrollment), not package-by-layer — controller,
   service, repository, entity and handler for one concept live together.

## What did I have to touch to add `notifications`?

New files only: notifications-api/, notifications-core/, and two new dependency lines in
app/pom.xml. **Zero lines changed in enrollments-core** — it just publishes
EnrollmentCreatedEvent through DomainEventPublisher; DomainEventDispatcher (already running)
discovered the new handler bean automatically.

## Skipped due to timebox

No auth, no Docker, no message broker (explicit non-goals). No pagination/validation
framework beyond basic 400/404/409 mapping. Testcontainers/Postgres not wired up — H2 is
explicitly acceptable per the brief.