# Skill Development Guide

How to create, customize, and optimize skills for the Android Dev Agent.

---

## 🎯 What is a Skill?

A **skill** is a specialized instruction set that teaches Claude how to perform specific tasks following your patterns and conventions.

**Think of it like:**
- 📖 A recipe book for specific tasks
- 🎓 Expert training material  
- 🛠️ Technology-specific best practices
- 📋 Code templates and patterns

---

## 📁 Skill Anatomy

### Basic Structure

```markdown
---
name: skill-name
description: When to use and what it does
---

# Skill Title

Content here...
```

### Required Components

1. **YAML Frontmatter** (Required)
   - `name`: Unique identifier
   - `description`: Triggers and purpose

2. **Content** (Your choice of structure)
   - Patterns
   - Code examples
   - Best practices
   - Anti-patterns

---

## 🏗️ Creating a New Skill

### Step 1: Identify the Need

**Good reasons to create a skill:**
- ✅ You use a specific library frequently (Firebase, GraphQL)
- ✅ You have team-specific patterns
- ✅ You need domain-specific knowledge (e.g., payment processing)
- ✅ You want to enforce coding standards

**Bad reasons:**
- ❌ One-time task
- ❌ Already covered by existing skill
- ❌ Too generic (belongs in SYSTEM-PROMPT.md)

### Step 2: Plan the Skill

**Answer these questions:**

1. **What does it teach?**
   - Example: "How to integrate Firebase Auth"

2. **When should it load?**
   - Example: "When user mentions Firebase or authentication"

3. **What's the output?**
   - Example: "Complete auth setup with best practices"

4. **Who is it for?**
   - Example: "Developers integrating Firebase"

### Step 3: Create the File

```bash
# In your .claude/skills/ directory
touch .claude/skills/firebase.skill.md
```

### Step 4: Write the Frontmatter

```markdown
---
name: firebase
description: Firebase integration for Android including Auth, Firestore, Analytics, and Crashlytics. Triggers when user mentions Firebase, authentication, cloud database, analytics, or crash reporting. Use for Firebase setup, configuration, and best practices.
---
```

**Tips for good descriptions:**
- Include ALL trigger keywords
- Be specific about what it covers
- Mention when to use
- Be "pushy" - Claude undertriggers skills

### Step 5: Write the Content

See templates below for structure ideas.

---

## 📝 Skill Templates

### Template 1: Library Integration

```markdown
---
name: library-name
description: Triggers and usage
---

# Library Name Integration

Integration guide for [Library] in Android projects.

## Version

**Library:** X.Y.Z
**Min SDK:** ##

## Setup

### Add Dependencies

```kotlin
// build.gradle.kts
dependencies {
    implementation("group:artifact:version")
}
```

### Configuration

```kotlin
// Code examples
```

## Common Patterns

### Pattern 1: Basic Usage

```kotlin
// Example
```

### Pattern 2: Advanced Usage

```kotlin
// Example
```

## Best Practices

✅ **Do:**
- Point 1
- Point 2

❌ **Don't:**
- Point 1
- Point 2

## Testing

```kotlin
// Test examples
```

## Troubleshooting

**Issue:** Problem description
**Solution:** How to fix

---

**Remember:** Key takeaway
```

### Template 2: Architectural Pattern

```markdown
---
name: pattern-name
description: Pattern description and triggers
---

# Pattern Name

Description of the architectural pattern.

## When to Use

Use this pattern when:
- Condition 1
- Condition 2

## Structure

```
[Visual representation]
```

## Implementation

### Step 1: Define Models

```kotlin
// Code
```

### Step 2: Create Components

```kotlin
// Code
```

### Step 3: Wire It Up

```kotlin
// Code
```

## Complete Example

```kotlin
// Full working example
```

## Testing Strategy

```kotlin
// How to test this pattern
```

## Common Mistakes

### ❌ Mistake 1

```kotlin
// Bad code
```

### ✅ Correct

```kotlin
// Good code
```

---

**Key principle:** Main takeaway
```

### Template 3: Workflow/Process

```markdown
---
name: workflow-name
description: Process description and triggers
---

# Workflow Name

Step-by-step guide for [process].

## Overview

Brief description of what this workflow accomplishes.

## Prerequisites

- Item 1
- Item 2

## Step-by-Step Guide

### Step 1: [Action]

**What to do:**
- Task 1
- Task 2

**Example:**
```kotlin
// Code if applicable
```

### Step 2: [Action]

**What to do:**
- Task 1
- Task 2

### Step 3: [Action]

**What to do:**
- Task 1
- Task 2

## Checklist

- [ ] Task 1
- [ ] Task 2
- [ ] Task 3

## Success Criteria

- Criterion 1
- Criterion 2

---

**Remember:** Important note
```

---

## 🎨 Writing Great Skills

### Content Guidelines

**Be Specific:**
```markdown
❌ "Use proper error handling"
✅ "Wrap repository calls in runCatching { } and return Result<T>"
```

**Show, Don't Just Tell:**
```markdown
❌ "Use dependency injection"
✅ 
```kotlin
class FeatureComponent(
    private val repository: Repository // ← Inject here
) 
```
```

**Include Context:**
```markdown
❌ "Use this pattern"
✅ "Use this pattern when you need [reason]. For [other case], use [alternative]."
```

### Code Example Guidelines

**Complete, Runnable Examples:**
```kotlin
// ✅ Good - complete with imports
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val api: UserApi,
    private val db: UserDao
) {
    fun getUser(id: String): Flow<User> {
        return db.observeUser(id)
            .onStart { refreshUser(id) }
    }
    
    private suspend fun refreshUser(id: String) {
        api.getUser(id)
            .onSuccess { db.insert(it) }
    }
}

// ❌ Bad - incomplete
class Repo {
    fun get() { /* ... */ }
}
```

**Real-World Examples:**
```kotlin
// ✅ Good - realistic scenario
class ProductListStore(
    private val getProductsUseCase: GetProductsUseCase,
    private val scope: CoroutineScope
) {
    init {
        loadProducts()
    }
}

// ❌ Bad - too simple
class Store {
    val state = MutableStateFlow(State())
}
```

### Structure Guidelines

**Progressive Disclosure:**
1. Start simple
2. Add complexity gradually
3. Show complete example at end

**Example:**

```markdown
## Basic Usage

```kotlin
// Simple example
```

## With Error Handling

```kotlin
// Adds error handling
```

## Production-Ready

```kotlin
// Complete with all features
```
```

---

## 🔍 Skill Optimization

### Make It Trigger Correctly

**Problem:** Claude doesn't load your skill

**Solutions:**

1. **Expand description keywords:**
```markdown
❌ description: "GraphQL integration"
✅ description: "GraphQL API integration including Apollo Client, queries, mutations, subscriptions, caching, and error handling. Triggers when user mentions GraphQL, Apollo, queries, mutations, subscriptions, or graph APIs."
```

2. **Make it "pushy":**
```markdown
❌ "Use when working with GraphQL"
✅ "ALWAYS use this skill for any GraphQL-related tasks, including setup, queries, mutations, and troubleshooting. Also trigger when user mentions API design or wants to implement a graph-based API."
```

3. **Test triggers:**
```
# Test these phrases:
- "Set up GraphQL"         ← Should trigger
- "Create a query"         ← Might not trigger (too generic)
- "Add GraphQL query"      ← Should trigger
- "Fetch data from API"    ← Might not trigger
- "Use Apollo to get user" ← Should trigger
```

### Keep It Focused

**One skill = One technology/pattern**

✅ **Good:**
- firebase.skill.md → Firebase only
- graphql.skill.md → GraphQL only
- analytics.skill.md → Analytics only

❌ **Bad:**
- backend-services.skill.md → Firebase + GraphQL + REST
  (Too broad, hard to trigger correctly)

### Size Guidelines

**Ideal size:** 100-500 lines

**If skill is getting too large:**

Option 1: Split into multiple skills
```
payment-integration.skill.md → Too big (800 lines)

Split into:
├── stripe-payment.skill.md (300 lines)
├── paypal-payment.skill.md (250 lines)
└── payment-common.skill.md (200 lines)
```

Option 2: Use references
```markdown
# Main skill file (short)
See references/detailed-guide.md for complete examples
```

---

## 🧪 Testing Your Skill

### Test 1: Manual Testing

```bash
# In Claude.ai or Claude Code

# Test trigger
"Set up [your tech]"  # Should load your skill

# Test output quality
"Create a complete [feature] using [your tech]"
# Check if code follows your patterns

# Test edge cases
"What if [edge case] happens?"
# Check if skill has guidance
```

### Test 2: Multiple Scenarios

Create test cases:

```markdown
# Test cases for firebase.skill.md

Test 1: Basic setup
Request: "Set up Firebase"
Expected: Dependencies + config code

Test 2: Auth integration
Request: "Add Firebase Auth login"
Expected: Auth setup + login flow

Test 3: Combining features
Request: "Use Firebase Auth and Firestore"
Expected: Both skills, proper integration

Test 4: Troubleshooting
Request: "Why isn't Firebase Auth working?"
Expected: Common issues + solutions
```

### Test 3: Conflicts

Test with other skills:

```
Request: "Create product list with Firebase"
Expected skills: compose + decompose + firebase
Check: Do they conflict or complement?
```

---

## 📊 Skill Examples

### Example 1: Simple Skill

```markdown
---
name: analytics
description: Analytics tracking with Google Analytics for Firebase. Triggers when user mentions analytics, tracking, events, or user behavior monitoring.
---

# Analytics Tracking

Google Analytics for Firebase implementation.

## Setup

```kotlin
// Add dependency
implementation("com.google.firebase:firebase-analytics-ktx:21.5.0")
```

## Track Events

```kotlin
class AnalyticsTracker(
    private val analytics: FirebaseAnalytics
) {
    fun trackScreenView(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }
    
    fun trackButtonClick(buttonName: String) {
        analytics.logEvent("button_click") {
            param("button_name", buttonName)
        }
    }
}
```

## Best Practices

✅ Use predefined events when available
✅ Keep event names consistent
✅ Add context with parameters
❌ Don't track PII
❌ Don't create too many custom events

---

**Track user behavior to improve the app!**
```

### Example 2: Complex Skill

```markdown
---
name: feature-flags
description: Feature flag management using Firebase Remote Config or custom solution. Triggers when user mentions feature flags, AB testing, remote config, gradual rollout, or experimental features.
---

# Feature Flags System

Implement feature flags for controlled releases.

## Options

### Option 1: Firebase Remote Config

```kotlin
class RemoteConfigFlags(
    private val remoteConfig: FirebaseRemoteConfig
) : FeatureFlags {
    init {
        remoteConfig.fetchAndActivate()
    }
    
    override fun isEnabled(flag: String): Boolean {
        return remoteConfig.getBoolean(flag)
    }
}
```

### Option 2: Custom Solution

```kotlin
interface FeatureFlags {
    fun isEnabled(flag: String): Boolean
}

class LocalFeatureFlags : FeatureFlags {
    private val flags = mutableMapOf<String, Boolean>()
    
    override fun isEnabled(flag: String): Boolean {
        return flags[flag] ?: false
    }
}
```

## Usage in Code

```kotlin
class FeatureComponent(
    private val featureFlags: FeatureFlags
) {
    fun showNewUI(): Boolean {
        return featureFlags.isEnabled("new_ui_redesign")
    }
}
```

## Testing

```kotlin
class TestFeatureFlags : FeatureFlags {
    private val overrides = mutableMapOf<String, Boolean>()
    
    fun override(flag: String, enabled: Boolean) {
        overrides[flag] = enabled
    }
    
    override fun isEnabled(flag: String): Boolean {
        return overrides[flag] ?: false
    }
}
```

## Best Practices

✅ Use meaningful flag names
✅ Set default values
✅ Remove old flags
✅ Document flag purpose
❌ Don't use flags for permanent configuration
❌ Don't nest flags deeply

---

**Control features safely in production!**
```

---

## 🎓 Advanced Topics

### Skill Composition

Skills can reference each other:

```markdown
# networking.skill.md

For authentication, see security.skill.md
For error handling UI, see compose.skill.md
```

### Conditional Content

```markdown
## For Beginners

Basic explanation...

## For Advanced Users

Complex scenarios...
```

### Version-Specific Content

```markdown
## For Compose 1.6.x

```kotlin
// Old way
```

## For Compose 1.7+ (Recommended)

```kotlin
// New way
```
```

---

## ✅ Skill Quality Checklist

Before finalizing your skill:

### Content
- [ ] Clear, specific description
- [ ] All trigger keywords included
- [ ] Real, complete code examples
- [ ] Best practices section
- [ ] Anti-patterns section
- [ ] Testing guidance

### Structure
- [ ] Logical flow (simple → complex)
- [ ] Headers for easy navigation
- [ ] Code blocks properly formatted
- [ ] Consistent style

### Testing
- [ ] Manually tested multiple scenarios
- [ ] Triggers correctly
- [ ] No conflicts with other skills
- [ ] Output matches expectations

### Documentation
- [ ] Comments in complex code
- [ ] Explanations where needed
- [ ] Links to official docs (if applicable)
- [ ] Version numbers specified

---

## 🔄 Maintaining Skills

### When to Update

- ✅ Library version updates
- ✅ New best practices emerge
- ✅ Team patterns change
- ✅ Bugs/issues found
- ✅ User feedback

### Version Control

```bash
# Track changes
git add .claude/skills/firebase.skill.md
git commit -m "Update Firebase skill to v21.5.0"

# Tag important changes
git tag -a skills-v2.1 -m "Updated Firebase and Analytics skills"
```

### Change Log

Add to skill file:

```markdown
## Change Log

### v2.1 (2026-03-02)
- Updated to Firebase 21.5.0
- Added Crashlytics integration
- Improved error handling examples

### v2.0 (2026-01-15)
- Complete rewrite for Compose
- Added testing examples

### v1.0 (2025-10-01)
- Initial version
```

---

## 📚 Resources

### Learning More

- Study existing skills in `skills/` folder
- Read `SYSTEM-PROMPT.md` for overall context
- Check `FILE-INDEX.md` for skill overview

### Getting Help

- Test with real examples
- Ask Claude for feedback
- Share with team for review
- Iterate based on usage

### Contributing

- Share skills with community
- Publish to GitHub
- Get feedback
- Improve collaboratively

---

**Happy skill building!** 🚀
