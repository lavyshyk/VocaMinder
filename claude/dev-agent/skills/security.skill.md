---
name: security
description: Android security best practices including data encryption, secure storage, network security, and OWASP compliance. Triggers when user mentions security, encryption, keystore, credentials, tokens, or secure storage.
---

# Security Skill

Security best practices for Android applications.

## Secure Storage

### Android Keystore (Recommended)

```kotlin
class SecureStorage(private val context: Context) {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    fun saveToken(token: String) {
        encryptedPrefs.edit {
            putString("auth_token", token)
        }
    }
    
    fun getToken(): String? {
        return encryptedPrefs.getString("auth_token", null)
    }
}
```

### Never Do This

```kotlin
// ❌ NEVER hardcode secrets
const val API_KEY = "sk_live_123456789"  // BAD!

// ❌ NEVER store in plain SharedPreferences
prefs.edit {
    putString("password", password)  // BAD!
}

// ❌ NEVER log sensitive data
Log.d("Auth", "Token: $token")  // BAD!
```

## Network Security

### HTTPS Only

```kotlin
// Force HTTPS in Ktor
HttpClient {
    engine {
        config {
            sslSocketFactory = createSecureSocketFactory()
        }
    }
}

// Network Security Config (res/xml/network_security_config.xml)
<network-security-config>
    <base-config cleartextTrafficPermitted="false" />
</network-security-config>
```

### Certificate Pinning

```kotlin
val certificatePinner = CertificatePinner.Builder()
    .add("api.example.com", "sha256/AAAAAAA...")
    .build()
```

## Input Validation

```kotlin
fun validateEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun sanitizeInput(input: String): String {
    return input.replace(Regex("[<>\"']"), "")
}
```

## Authentication

```kotlin
// Use BiometricPrompt for sensitive actions
val biometricPrompt = BiometricPrompt(
    activity,
    executor,
    object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            // Proceed with action
        }
    }
)

biometricPrompt.authenticate(promptInfo)
```

## ProGuard Rules

```proguard
# Keep security-critical classes
-keep class com.example.app.security.** { *; }

# Obfuscate everything else
-repackageclasses
```

## Security Checklist

- [ ] All network calls use HTTPS
- [ ] Sensitive data encrypted
- [ ] API keys in BuildConfig/Secrets
- [ ] Input validation on all user input
- [ ] No sensitive data in logs
- [ ] Certificate pinning in production
- [ ] ProGuard/R8 enabled
- [ ] Root detection (if needed)
- [ ] Jailbreak detection (if needed)

---

**Remember:** Security is not optional!
