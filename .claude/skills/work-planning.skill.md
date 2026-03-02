---
name: work-planning
description: Creating detailed work plans, breaking down features into tasks, estimating effort, and organizing development workflow. Triggers when user asks to plan work, create tasks, break down features, estimate time, or organize development steps.
---

# Work Planning Skill

Create structured development plans for Android features.

## Feature Planning Template

**Feature:** [Feature Name]

**1. Requirements Analysis**
- User stories
- Acceptance criteria
- Dependencies
- Constraints

**2. Technical Approach**
- Architecture decision (MVI, layers)
- Technology choices
- Data flow

**3. Task Breakdown**

Domain Layer:
- [ ] Create domain models
- [ ] Define repository interfaces
- [ ] Implement use cases

Data Layer:
- [ ] Create DTOs and entities
- [ ] Implement data sources
- [ ] Implement repositories
- [ ] Create mappers

Presentation Layer:
- [ ] Create component
- [ ] Create store with MVI pattern
- [ ] Create Compose UI
- [ ] Wire up navigation

Testing:
- [ ] Unit tests for use cases
- [ ] Repository tests
- [ ] Store tests
- [ ] UI tests

**4. Time Estimates**
- Domain: X hours
- Data: Y hours
- Presentation: Z hours
- Testing: W hours
- Total: Sum hours

**5. Dependencies & Blockers**
- API availability
- Design assets
- Third-party libraries

**6. Success Criteria**
- All tests passing
- No regressions
- Code review approved
- Meets acceptance criteria

---

**Use this template for every new feature!**
