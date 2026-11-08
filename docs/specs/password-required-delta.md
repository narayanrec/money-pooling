# Delta Spec: Require a Password When Creating a User

## 1. Current State & Code Boundaries
- **Impacted Files:** `backend/src/main/java/com/startup/usergroup/controllers/UserController.java`; `backend/src/test/java/com/startup/usergroup/controllers/UserControllerTest.java`.
- **Current Behavior:** `User.password` already has a Jakarta `@NotBlank` constraint, but `UserController.createUser` does not trigger request-body validation before hashing and saving. As a result, missing or blank passwords are not rejected through the intended validation path.
- **Preserved Constraints:** Reuse the existing `@NotBlank` constraint in `com.startup.usergroup.models.User` and the existing `spring-boot-starter-validation` dependency. Preserve the current password hashing with jBCrypt, repository-based persistence, Java 17/Spring Boot setup, and database schema.

## 2. The Functional Delta
- Activate Jakarta Bean Validation for the user-creation request so a missing, `null`, empty, or whitespace-only password is rejected with HTTP 400 before password hashing or repository persistence.
- Applying validation to `User` also activates its existing `name` and `email` constraints at this endpoint; do not add or change those constraints as part of this feature.

## 3. Implementation Checklist (Gated Steps)
- [ ] **Step 1 - Infrastructure/Configuration:** Reuse the existing validation dependency; no dependency, application configuration, or schema changes are required.
- [ ] **Step 2 - Core Logic:** Keep the existing `User.password` `@NotBlank` constraint; do not duplicate password validation or alter hashing behavior.
- [ ] **Step 3 - Interface/Controller:** Enable request-body validation on `UserController.createUser` so invalid input is rejected before the method hashes or saves the user.

## 4. Verification & Testing Criteria
- [ ] **Unit/Integration Tests:** Using the existing `spring-boot-starter-test` (JUnit Jupiter and Spring MVC test support), verify the user-creation HTTP endpoint returns 400 for missing, `null`, empty, and whitespace-only passwords, and that the repository is not called for these requests. Verify a nonblank password is accepted and the saved password remains hashed. Include coverage for the existing name/email constraints becoming active at this endpoint.
- [ ] **Performance/Safety Gates:** Confirm invalid requests do not reach password hashing or persistence; valid requests retain the existing hashing behavior. Do not change the relational schema or existing endpoint paths.
