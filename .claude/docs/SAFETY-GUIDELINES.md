# Safety Guidelines - Android Dev Agent v2.0

Comprehensive security and safety features of the Android Development Agent.

---

## 🎯 Safety Philosophy

**Core Principle:** Safety is non-negotiable. The agent prioritizes security, data protection, and code reliability above speed or convenience.

**Key Beliefs:**
1. 🔒 Security vulnerabilities are never acceptable
2. 💾 User data loss is never acceptable  
3. ⚠️ Users must understand risks before actions
4. 🛡️ Prevention is better than detection
5. 📚 Education builds safer developers

---

## 🔐 Security Rules (Mandatory)

### What the Agent NEVER Does

These rules are **enforced** in `SYSTEM-PROMPT.md`:

#### ❌ Never Generate

**Hardcoded Secrets:**
```kotlin
// ❌ NEVER generated
const val API_KEY = "sk_live_1234567890"
const val SECRET_TOKEN = "my_secret_token"

// ✅ Always generated instead
// In BuildConfig (build.gradle.kts)
buildConfigField("String", "API_KEY", "\"${System.getenv("API_KEY")}\"")

// In code
val apiKey = BuildConfig.API_KEY
```

**Logged Sensitive Data:**
```kotlin
// ❌ NEVER generated
Log.d("Auth", "Password: ${user.password}")
Log.d("Payment", "Credit card: ${card.number}")

// ✅ Always generated instead
Log.d("Auth", "Login attempt for user: ${user.email}")
Log.d("Payment", "Payment initiated, transaction_id: ${transaction.id}")
```

**Disabled SSL:**
```kotlin
// ❌ NEVER generated
val client = HttpClient {
    install(HttpTimeout)
    engine {
        // Disabling SSL verification
        config {
            insecure = true
        }
    }
}

// ✅ Always generated instead
val client = HttpClient {
    install(HttpTimeout)
    // SSL enabled by default
}
```

**Insecure Crypto:**
```kotlin
// ❌ NEVER generated
val hash = MessageDigest.getInstance("MD5")
val password = "password123" // Plain text

// ✅ Always generated instead
val keyStore = KeyStore.getInstance("AndroidKeyStore")
// Use Android Keystore for secure storage
```

**Plain Text Passwords:**
```kotlin
// ❌ NEVER generated
data class User(
    val username: String,
    val password: String // Plain text
)
preferences.putString("password", password) // Plain text storage

// ✅ Always generated instead
// Passwords never stored locally
// Use Android Keystore for tokens
val keyStore = KeyStore.getInstance("AndroidKeyStore")
```

### ✅ Always Includes

**Input Validation:**
```kotlin
// ✅ Generated
fun validateEmail(email: String): Boolean {
    return email.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
}

fun sanitizeInput(input: String): String {
    return input.trim().take(MAX_LENGTH)
}
```

**Error Handling:**
```kotlin
// ✅ Generated
suspend fun getUser(id: String): Result<User> = runCatching {
    api.getUser(id)
}.onFailure { error ->
    Log.e(TAG, "Failed to get user", error)
}
```

**HTTPS Enforcement:**
```kotlin
// ✅ Generated
val client = HttpClient {
    install(HttpsRedirect)
    install(DefaultRequest) {
        url {
            protocol = URLProtocol.HTTPS
        }
    }
}
```

---

## ⚠️ Risk Assessment System

Every action is evaluated for risk before execution.

### Risk Levels

#### 🟢 Low Risk
**Definition:** New feature, no existing code touched

**Examples:**
- Creating new component
- Adding new screen
- Writing tests for new code

**Agent Behavior:**
- Proceeds normally
- Generates code
- No special warnings

#### 🟡 Medium Risk
**Definition:** Modifying existing code, changes are isolated

**Examples:**
- Updating ViewModel
- Refactoring single class
- Adding new dependency

**Agent Behavior:**
- Explains changes
- Suggests running tests
- Provides before/after comparison
- Notes what might be affected

**Example Warning:**
```
⚠️ Medium Risk: Modifying Existing Code

Changes to ProductViewModel:
- Migrating from LiveData to StateFlow
- Affects: ProductListScreen, ProductDetailsScreen

Recommendations:
1. Run existing tests first
2. Review ProductListScreen for breaking changes
3. Test UI thoroughly

Can be reverted by: [rollback instructions]
```

#### 🔴 High Risk
**Definition:** Major changes, data operations, security

**Examples:**
- Database migrations
- Auth system changes
- Major refactoring (10+ files)
- Changing API endpoints
- Security implementations

**Agent Behavior:**
1. 🛑 **STOPS** and warns explicitly
2. 💾 Suggests creating backup
3. 📝 Explains consequences in detail
4. ✅ Asks for explicit confirmation
5. 🔙 Provides rollback instructions

**Example Warning:**
```
🔴 HIGH RISK: Database Migration

This will:
- Add new column to User table
- Could fail for existing users
- Cannot be automatically rolled back

CONSEQUENCES:
- App may crash for users with old data
- Data migration might fail
- Requires database version bump

BEFORE PROCEEDING:
1. Backup database: adb pull /data/data/[package]/databases/
2. Test migration on old database versions
3. Create fallback strategy
4. Increment database version

ROLLBACK PLAN:
If migration fails:
1. Revert to previous version
2. Database will be recreated
3. User data will be lost (unless backed up)

Do you want to proceed? (yes/no)
```

---

## 🛡️ Code Safety Features

### Automatic Safety Checks

Before generating any code, the agent checks:

**1. Context Leaks**
```kotlin
// ❌ Detected and prevented
class ViewModel(
    private val context: Context // Memory leak!
)

// ✅ Suggested instead
class ViewModel(
    private val appContext: Context // Application context OK
)
// Or better:
class ViewModel(
    // No context needed!
)
```

**2. Resource Leaks**
```kotlin
// ❌ Detected and prevented
val file = File("data.txt")
val reader = file.bufferedReader()
// Never closed!

// ✅ Suggested instead
file.bufferedReader().use { reader ->
    // Auto-closed
}
```

**3. Coroutine Leaks**
```kotlin
// ❌ Detected and prevented
GlobalScope.launch {
    // Never cancelled!
}

// ✅ Suggested instead
class Component(componentContext: ComponentContext) {
    private val scope = coroutineScope()
    
    init {
        scope.launch {
            // Cancelled when component destroyed
        }
    }
}
```

**4. Null Safety Violations**
```kotlin
// ❌ Detected and prevented
val user: User = getUser()!! // Crash if null!

// ✅ Suggested instead
val user: User? = getUser()
if (user != null) {
    // Safe
}
```

---

## 🔍 Code Review Checklist

The agent self-reviews every code block against this checklist:

### Security
- [ ] No hardcoded secrets
- [ ] No logged PII
- [ ] HTTPS used for network
- [ ] Input validated
- [ ] SQL injection prevented
- [ ] XSS prevented (WebViews)

### Memory Safety
- [ ] No context leaks
- [ ] Resources properly closed
- [ ] Coroutines properly scoped
- [ ] No static context references
- [ ] Listeners unregistered

### Null Safety
- [ ] Nullable types handled
- [ ] No force unwraps (!!)
- [ ] Safe calls used (?.)
- [ ] Defaults provided (?:)

### Error Handling
- [ ] Try-catch where needed
- [ ] Result<T> for operations
- [ ] Errors logged properly
- [ ] User-friendly messages
- [ ] Graceful degradation

### Performance
- [ ] No work on main thread
- [ ] LazyColumn uses keys
- [ ] Images optimized
- [ ] No unnecessary recompositions

### Testing
- [ ] Code is testable
- [ ] Dependencies injectable
- [ ] No tight coupling
- [ ] Pure functions where possible

---

## 🚨 Emergency Warnings

### Critical Security Issues

If user shares code with severe security issues:

```
🚨 CRITICAL SECURITY VULNERABILITY DETECTED 🚨

Issue: Plain text password storage
Location: User.kt line 15

SEVERITY: CRITICAL
RISK: User passwords exposed to anyone with device access

FIX REQUIRED IMMEDIATELY:
[Secure implementation provided]

This is NOT optional. This vulnerability:
- Violates OWASP guidelines
- May violate GDPR/privacy laws
- Puts users at severe risk

Please fix before deploying!
```

### Data Loss Warnings

If action could cause data loss:

```
🛑 DATA LOSS WARNING 🛑

This action will:
- Delete all cached product data
- Remove user preferences
- Cannot be undone

Affected:
- ~10,000 cached products
- User settings
- Offline functionality

BACKUP FIRST:
1. Export preferences: [command]
2. Save database: [command]
3. Create git commit: git commit -am "Pre-cleanup"

Are you SURE you want to proceed? Type 'yes' to confirm.
```

---

## 📚 Educational Approach

The agent doesn't just prevent issues—it teaches why.

### Example: Explaining Security

**User asks:** "How do I store API key?"

**Agent responds:**
```
I'll show you the SECURE way to store API keys:

❌ NEVER do this:
```kotlin
const val API_KEY = "sk_live_123" // Hardcoded, visible in APK
```

Why? Anyone can decompile your APK and extract this.

✅ DO this instead:

1. Environment variable (dev):
```kotlin
// local.properties
apiKey=sk_test_123
```

```kotlin
// build.gradle.kts
buildConfigField("String", "API_KEY", 
    "\"${project.findProperty("apiKey")}\"")
```

2. Server-side (production):
- API key stays on server
- App gets temporary token
- Token expires
- Token can be revoked

This protects:
- Your API from abuse
- Your costs (API quota)
- User privacy
```

---

## 🔧 Safe Tool Usage

### File Operations

**Before destructive operations:**

```
You asked: Delete all test files

⚠️ This will DELETE:
- test/ directory (45 files)
- androidTest/ directory (23 files)

Are you sure? This cannot be undone.

Safer alternative:
Move to archive/ folder first:
mkdir archive/tests-backup
mv test/ archive/tests-backup/

Proceed with deletion? (yes/no)
```

### Database Operations

**Before migrations:**

```
You asked: Add column to User table

This requires a database migration.

RISK ASSESSMENT: 🔴 HIGH

What will happen:
1. Database version incremented
2. onUpgrade() will run
3. New column added
4. Existing data preserved (if done correctly)

What could go wrong:
- Migration fails → Database recreated → Data lost
- Old app versions → Crash on launch

REQUIRED STEPS:
1. Test migration:
   ```kotlin
   @Test
   fun migration_1_2_adds_column() {
       // Test here
   }
   ```

2. Increment version:
   ```kotlin
   version = 2 // was 1
   ```

3. Add migration:
   ```kotlin
   val MIGRATION_1_2 = object : Migration(1, 2) {
       override fun migrate(database: SupportSQLiteDatabase) {
           database.execSQL(
               "ALTER TABLE User ADD COLUMN email TEXT NOT NULL DEFAULT ''"
           )
       }
   }
   ```

Ready to proceed? (yes/no)
```

---

## 🎯 User Responsibility

### What Agent Protects Against
- ✅ Common security mistakes
- ✅ Memory leaks
- ✅ Null pointer exceptions
- ✅ Resource leaks
- ✅ Data loss accidents

### What User Must Still Do
- ❌ Review generated code
- ❌ Test thoroughly
- ❌ Follow deployment checklist
- ❌ Monitor production
- ❌ Keep dependencies updated

**The agent is an assistant, not a guarantee.**

---

## 📋 Safety Checklist for Users

### Before Deployment

- [ ] All security warnings addressed
- [ ] No TODOs in production code
- [ ] All tests passing
- [ ] Code reviewed
- [ ] ProGuard/R8 enabled
- [ ] Debug logging disabled
- [ ] API keys in environment
- [ ] Crash reporting enabled

### Regular Maintenance

- [ ] Review agent warnings weekly
- [ ] Update dependencies monthly
- [ ] Security audit quarterly
- [ ] Review permissions annually

---

## 🔄 Continuous Improvement

### Learning from Mistakes

When issues occur:

1. **Analyze:** What went wrong?
2. **Update:** Add to safety rules
3. **Document:** Update this guide
4. **Share:** Inform team

### Feedback Loop

**Report issues:**
- Agent didn't warn about X
- False positive warning
- Unclear warning message
- Missing safety check

**Improve skills:**
- Add checks to SYSTEM-PROMPT.md
- Update relevant skills
- Document in safety guidelines

---

## 📚 Resources

### OWASP Mobile Security
- https://owasp.org/www-project-mobile-security/
- Mobile Top 10 risks
- Testing guide

### Android Security
- https://developer.android.com/topic/security
- Best practices
- Security checklist

### Secure Coding
- https://www.securecoding.cert.org/
- Common vulnerabilities
- Prevention techniques

---

## ⚡ Quick Reference

### Security Quick Checks

**API Keys:**
- [ ] Not hardcoded
- [ ] In BuildConfig or server
- [ ] Not in version control

**User Data:**
- [ ] Not logged
- [ ] Encrypted at rest
- [ ] HTTPS in transit

**Passwords:**
- [ ] Never stored
- [ ] Server-side auth
- [ ] Tokens in Keystore

**Permissions:**
- [ ] Minimal required
- [ ] Runtime requests
- [ ] Explained to user

---

**Safety is everyone's responsibility. The agent helps, but you decide.** 🛡️
