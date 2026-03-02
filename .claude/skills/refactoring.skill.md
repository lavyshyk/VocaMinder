---
name: refactoring
description: Code refactoring strategies, improving code quality, extracting methods/classes, removing duplication, and modernizing legacy code. Triggers when user mentions refactor, improve, clean up, extract, modernize, or technical debt.
---

# Refactoring Skill

Improve code quality while maintaining functionality.

## Refactoring Patterns

### Extract Function
```kotlin
// Before
fun processOrder(order: Order) {
    val total = order.items.sumOf { it.price * it.quantity }
    val tax = total * 0.10
    val finalTotal = total + tax
    // ...
}

// After
fun processOrder(order: Order) {
    val finalTotal = calculateTotalWithTax(order)
    // ...
}

private fun calculateTotalWithTax(order: Order): Double {
    val subtotal = calculateSubtotal(order)
    val tax = calculateTax(subtotal)
    return subtotal + tax
}
```

### Extract Class
```kotlin
// Before: God class
class UserViewModel {
    fun login() { }
    fun updateProfile() { }
    fun changePassword() { }
    fun uploadAvatar() { }
    // 500+ lines
}

// After: Separated concerns
class LoginViewModel { }
class ProfileViewModel { }
class PasswordViewModel { }
class AvatarViewModel { }
```

### Replace Conditional with Polymorphism
```kotlin
// Before
fun calculateDiscount(userType: String, amount: Double): Double {
    return when (userType) {
        "premium" -> amount * 0.20
        "regular" -> amount * 0.10
        else -> 0.0
    }
}

// After
interface DiscountStrategy {
    fun calculate(amount: Double): Double
}

class PremiumDiscount : DiscountStrategy {
    override fun calculate(amount: Double) = amount * 0.20
}
```

## Refactoring Checklist

Before refactoring:
- [ ] All tests pass
- [ ] Code is committed to git
- [ ] You understand what code does
- [ ] You have time to test thoroughly

During refactoring:
- [ ] Make small, incremental changes
- [ ] Run tests after each change
- [ ] Keep commits small and focused
- [ ] Don't add features while refactoring

After refactoring:
- [ ] All tests still pass
- [ ] Code is more readable
- [ ] No behavior changes
- [ ] Performance hasn't degraded

---

**Refactor with confidence: Test → Change → Test**
