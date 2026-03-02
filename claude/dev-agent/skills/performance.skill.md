---
name: performance
description: Performance optimization techniques for Android apps including memory management, compose optimization, and profiling. Triggers when user mentions performance, optimization, lag, slow, memory leak, or app speed.
---

# Performance Optimization Skill

Optimize Android app performance for smooth user experience.

## Compose Performance

### Recomposition Optimization

```kotlin
// Bad: Unstable parameter
@Composable
fun ProductList(products: List<Product>) { } // Recomposes on every list change

// Good: Stable with key
@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products, key = { it.id }) { product ->
            ProductCard(product)
        }
    }
}

// Good: Immutable data
@Immutable
data class Product(val id: String, val name: String)
```

### Avoid Lambda Recreation

```kotlin
// Bad
@Composable
fun ItemList(items: List<Item>, onClick: (String) -> Unit) {
    items.forEach { item ->
        ItemCard(onClick = { onClick(item.id) }) // New lambda!
    }
}

// Good
@Composable
fun ItemList(items: List<Item>, onClick: (String) -> Unit) {
    items.forEach { item ->
        val onItemClick = remember(item.id) { { onClick(item.id) } }
        ItemCard(onClick = onItemClick)
    }
}
```

### Use derivedStateOf

```kotlin
@Composable
fun ExpensiveList(items: List<Item>, filter: String) {
    // Recomputes only when items or filter changes
    val filteredItems by remember {
        derivedStateOf {
            items.filter { it.name.contains(filter) }
        }
    }
}
```

## Memory Management

### Avoid Leaks

```kotlin
// Bad: Leaks Activity
class LeakyViewModel {
    companion object {
        var activity: Activity? = null  // Memory leak!
    }
}

// Good: Use Application Context
class SafeViewModel(
    private val appContext: Context  // Application context
)

// Good: WeakReference if needed
class CacheManager {
    private var contextRef: WeakReference<Context>? = null
}
```

### Clean Up Resources

```kotlin
@Composable
fun VideoPlayer() {
    val player = remember { ExoPlayer.Builder(context).build() }
    
    DisposableEffect(Unit) {
        onDispose {
            player.release()  // Clean up!
        }
    }
}
```

## Image Loading

```kotlin
// Use Coil with proper sizing
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .size(width = 200, height = 200)  // Resize!
        .crossfade(true)
        .build(),
    contentDescription = null
)
```

## Background Work

```kotlin
// Use WorkManager for deferred work
val workRequest = OneTimeWorkRequestBuilder<SyncWorker>()
    .setConstraints(
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    )
    .build()

WorkManager.getInstance(context).enqueue(workRequest)
```

## Profiling

```kotlin
// Use Trace for profiling
Trace.beginSection("ExpensiveOperation")
try {
    // Your code
} finally {
    Trace.endSection()
}
```

✅ Profile with Android Studio Profiler  
✅ Use LeakCanary for memory leaks  
✅ Optimize images (WebP, proper sizing)  
✅ Use Baseline Profiles  
✅ Enable R8/ProGuard  

---

**Remember:** Measure first, optimize second!
