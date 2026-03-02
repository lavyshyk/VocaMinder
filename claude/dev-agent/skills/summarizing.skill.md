---
name: summarizing
description: Code summarization, documentation generation, and explaining complex code. Triggers when user asks to explain, summarize, document, or understand existing code.
---

# Summarizing Skill

Explain and document code effectively.

## Code Summary Format

### Quick Summary
```
**What it does:** [One sentence description]
**Key components:** [Main classes/functions]
**Dependencies:** [What it needs]
**Returns/Output:** [What it produces]
```

### Detailed Summary
```
## Overview
[2-3 sentences about purpose]

## Architecture
[Layer/pattern explanation]

## Key Classes
- **ClassName**: What it does
- **AnotherClass**: What it does

## Flow
1. Step 1
2. Step 2
3. Step 3

## Dependencies
- Library 1 (version)
- Library 2 (version)

## Usage Example
[Code snippet]

## Notes
- Important considerations
- Gotchas
- Future improvements
```

## Documentation Generation

### KDoc for Classes

```kotlin
/**
 * Manages product list state and handles user interactions.
 *
 * This store follows MVI pattern with unidirectional data flow.
 * State updates are immutable and thread-safe using StateFlow.
 *
 * @property getProductsUseCase Use case for fetching products
 * @property scope Coroutine scope for async operations
 *
 * @see ProductListState
 * @see ProductListIntent
 */
class ProductListStore(
    private val getProductsUseCase: GetProductsUseCase,
    private val scope: CoroutineScope
) { }
```

### KDoc for Functions

```kotlin
/**
 * Loads products from repository and updates state.
 *
 * This method:
 * 1. Sets loading state
 * 2. Calls use case
 * 3. Updates state with results or error
 *
 * @throws NetworkException if no internet connection
 */
private fun loadProducts() { }
```

## Code Explanation Patterns

### For Complex Logic

```kotlin
// WHAT: Calculate discounted price with tiered discounts
// WHY: Business rule - higher quantities get better discounts
// HOW: 
// - 1-10 items: 5% off
// - 11-50 items: 10% off
// - 51+ items: 15% off
fun calculateDiscount(quantity: Int, price: Double): Double {
    val discountRate = when {
        quantity <= 10 -> 0.05
        quantity <= 50 -> 0.10
        else -> 0.15
    }
    return price * quantity * (1 - discountRate)
}
```

### For Algorithms

```kotlin
/**
 * Binary search implementation for sorted product list.
 * 
 * Time complexity: O(log n)
 * Space complexity: O(1)
 * 
 * Algorithm:
 * 1. Start with entire array
 * 2. Compare middle element with target
 * 3. If match, return index
 * 4. If target < middle, search left half
 * 5. If target > middle, search right half
 * 6. Repeat until found or exhausted
 */
```

---

**Remember:** Good docs save time later!
