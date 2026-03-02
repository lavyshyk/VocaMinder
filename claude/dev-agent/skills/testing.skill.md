---
name: testing
description: Android testing strategies including unit tests, UI tests, integration tests, and test-driven development. Triggers when user mentions tests, testing, TDD, MockK, JUnit, Espresso, Compose testing, or wants to write/run/debug tests.
---

# Android Testing Skill

Comprehensive testing strategies for Android applications.

## Test Types

### Unit Tests (domain/data layer)
```kotlin
class GetProductsUseCaseTest {
    private lateinit var useCase: GetProductsUseCase
    private lateinit var mockRepository: ProductRepository
    
    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetProductsUseCase(mockRepository)
    }
    
    @Test
    fun `when repository returns data, should return success`() = runTest {
        // Arrange
        val products = listOf(Product("1", "Test"))
        coEvery { mockRepository.getProducts() } returns Result.success(products)
        
        // Act
        val result = useCase()
        
        // Assert
        assertTrue(result.isSuccess)
        assertEquals(products, result.getOrNull())
    }
}
```

### Store Tests
```kotlin
class ProductListStoreTest {
    @Test
    fun `when Refresh intent, should update state to loading`() = runTest {
        val store = ProductListStore(mockUseCase, this)
        
        store.accept(ProductListIntent.Refresh)
        
        assertEquals(true, store.state.value.isLoading)
    }
}
```

### Compose UI Tests
```kotlin
class ProductScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun `when state has products, should display them`() {
        composeTestRule.setContent {
            ProductList(products = testProducts, onProductClick = {})
        }
        
        composeTestRule
            .onNodeWithText("Product 1")
            .assertIsDisplayed()
    }
}
```

## Testing Libraries

- **JUnit:** `junit:junit:4.13.2`
- **MockK:** `io.mockk:mockk:1.13.11`
- **Coroutines Test:** `org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1`
- **Turbine:** `app.cash.turbine:turbine:1.1.0`
- **Compose Test:** `androidx.compose.ui:ui-test-junit4:1.6.7`

## Best Practices

✅ Follow AAA pattern (Arrange, Act, Assert)  
✅ Test one thing per test  
✅ Use descriptive test names  
✅ Mock external dependencies  
✅ Test edge cases  
✅ Use `runTest` for coroutines  
✅ Clean up resources  

---

**Test everything that can break!**
