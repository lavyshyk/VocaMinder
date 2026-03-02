---
name: networking
description: Network layer implementation with Ktor or Retrofit, API integration, error handling, and offline support. Triggers when user mentions API, network, HTTP, REST, GraphQL, Ktor, Retrofit, or backend integration.
---

# Networking Skill

API integration and network layer implementation.

## Ktor Client Setup

### Version: 2.3.11

### Basic Client Configuration

```kotlin
// data/network/HttpClientFactory.kt
object HttpClientFactory {
    
    fun create(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
            
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
            
            defaultRequest {
                url("https://api.example.com/")
                header("Content-Type", "application/json")
            }
        }
    }
}
```

### With Authentication

```kotlin
fun createAuthenticatedClient(tokenProvider: () -> String?): HttpClient {
    return HttpClient(Android) {
        install(Auth) {
            bearer {
                loadTokens {
                    val token = tokenProvider()
                    token?.let {
                        BearerTokens(accessToken = it, refreshToken = "")
                    }
                }
            }
        }
        
        install(ContentNegotiation) {
            json()
        }
    }
}
```

## Remote Data Source

```kotlin
// data/source/remote/ProductRemoteDataSource.kt
interface ProductRemoteDataSource {
    suspend fun getProducts(): Result<List<ProductDto>>
    suspend fun getProduct(id: String): Result<ProductDto>
    suspend fun createProduct(product: ProductDto): Result<ProductDto>
}

class ProductRemoteDataSourceImpl(
    private val client: HttpClient
) : ProductRemoteDataSource {
    
    override suspend fun getProducts(): Result<List<ProductDto>> = runCatching {
        client.get("products") {
            parameter("limit", 20)
        }.body()
    }
    
    override suspend fun getProduct(id: String): Result<ProductDto> = runCatching {
        client.get("products/$id").body()
    }
    
    override suspend fun createProduct(product: ProductDto): Result<ProductDto> = runCatching {
        client.post("products") {
            setBody(product)
        }.body()
    }
}
```

## Error Handling

```kotlin
sealed class NetworkError : Exception() {
    data class HttpError(val code: Int, val message: String) : NetworkError()
    data class NetworkException(override val message: String) : NetworkError()
    data class UnknownError(override val cause: Throwable) : NetworkError()
}

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Result<T> = runCatching {
    apiCall()
}.recoverCatching { error ->
    throw when (error) {
        is ClientRequestException -> {
            NetworkError.HttpError(
                code = error.response.status.value,
                message = error.message
            )
        }
        is ServerResponseException -> {
            NetworkError.HttpError(
                code = error.response.status.value,
                message = "Server error"
            )
        }
        is UnresolvedAddressException -> {
            NetworkError.NetworkException("No internet connection")
        }
        else -> NetworkError.UnknownError(error)
    }
}
```

## DTOs (Data Transfer Objects)

```kotlin
@Serializable
data class ProductDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("price") val price: Double,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class ApiResponse<T>(
    @SerialName("data") val data: T,
    @SerialName("message") val message: String? = null,
    @SerialName("success") val success: Boolean = true
)

@Serializable
data class ErrorResponse(
    @SerialName("error") val error: String,
    @SerialName("code") val code: Int
)
```

## Pagination

```kotlin
@Serializable
data class PaginatedResponse<T>(
    @SerialName("data") val data: List<T>,
    @SerialName("page") val page: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_items") val totalItems: Int
)

suspend fun getProductsPage(page: Int): Result<PaginatedResponse<ProductDto>> = runCatching {
    client.get("products") {
        parameter("page", page)
        parameter("per_page", 20)
    }.body()
}
```

## File Upload

```kotlin
suspend fun uploadImage(file: File): Result<String> = runCatching {
    val response: ApiResponse<UploadDto> = client.post("upload") {
        setBody(
            MultiPartFormDataContent(
                formData {
                    append("image", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                    })
                }
            )
        )
    }.body()
    
    response.data.url
}
```

## WebSocket

```kotlin
class ChatWebSocket(private val client: HttpClient) {
    
    fun connect(): Flow<ChatMessage> = flow {
        client.webSocket("wss://api.example.com/chat") {
            // Send messages
            send(Frame.Text("Hello"))
            
            // Receive messages
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val message = Json.decodeFromString<ChatMessage>(frame.readText())
                    emit(message)
                }
            }
        }
    }
}
```

## Retry Logic

```kotlin
suspend fun <T> retryApiCall(
    times: Int = 3,
    initialDelay: Long = 100,
    maxDelay: Long = 1000,
    factor: Double = 2.0,
    block: suspend () -> T
): Result<T> {
    var currentDelay = initialDelay
    repeat(times - 1) {
        val result = runCatching { block() }
        if (result.isSuccess) return result
        
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return runCatching { block() }
}
```

## Network Monitor

```kotlin
interface NetworkMonitor {
    val isConnected: Flow<Boolean>
    fun isCurrentlyConnected(): Boolean
}

class NetworkMonitorImpl(context: Context) : NetworkMonitor {
    
    private val connectivityManager = context.getSystemService<ConnectivityManager>()
    
    override val isConnected: Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }
            
            override fun onLost(network: Network) {
                trySend(false)
            }
        }
        
        connectivityManager?.registerDefaultNetworkCallback(callback)
        
        awaitClose {
            connectivityManager?.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
    
    override fun isCurrentlyConnected(): Boolean {
        val network = connectivityManager?.activeNetwork
        return network != null
    }
}
```

## Repository Integration

```kotlin
class ProductRepositoryImpl(
    private val remoteDataSource: ProductRemoteDataSource,
    private val localDataSource: ProductLocalDataSource,
    private val networkMonitor: NetworkMonitor,
    private val mapper: ProductMapper
) : ProductRepository {
    
    override suspend fun getProducts(): Result<List<Product>> = runCatching {
        // Try cache first
        val cached = localDataSource.getAll()
        if (cached.isNotEmpty() && !networkMonitor.isCurrentlyConnected()) {
            return@runCatching cached.map(mapper::toDomain)
        }
        
        // Fetch from network
        if (networkMonitor.isCurrentlyConnected()) {
            remoteDataSource.getProducts()
                .onSuccess { dtos ->
                    val entities = dtos.map(mapper::toEntity)
                    localDataSource.saveAll(entities)
                }
                .getOrThrow()
                .map(mapper::toDomain)
        } else {
            // No network, use cache
            cached.map(mapper::toDomain)
        }
    }
}
```

## GraphQL (Optional)

```kotlin
@Serializable
data class GraphQLRequest(
    val query: String,
    val variables: Map<String, String>? = null
)

@Serializable
data class GraphQLResponse<T>(
    val data: T,
    val errors: List<GraphQLError>? = null
)

suspend inline fun <reified T> executeGraphQL(
    client: HttpClient,
    query: String,
    variables: Map<String, String>? = null
): Result<T> = runCatching {
    val response: GraphQLResponse<T> = client.post("graphql") {
        setBody(GraphQLRequest(query, variables))
    }.body()
    
    response.errors?.let { errors ->
        throw Exception(errors.joinToString { it.message })
    }
    
    response.data
}
```

## Best Practices

✅ **Do:**
- Use HTTPS always
- Implement retry logic for transient errors
- Cache responses when appropriate
- Handle no internet gracefully
- Use proper timeouts
- Validate SSL certificates
- Log network errors (without sensitive data)
- Use DTOs for network layer

❌ **Don't:**
- Log sensitive data (tokens, passwords)
- Hardcode API URLs
- Ignore SSL errors
- Use HTTP in production
- Forget timeout configuration
- Expose network errors to UI directly
- Make synchronous network calls

## Security

```kotlin
// Certificate Pinning (Production)
install(HttpClient) {
    engine {
        config {
            certificatePinner {
                add("api.example.com", "sha256/AAAAAAA...")
            }
        }
    }
}

// Secure storage for tokens
fun saveToken(token: String) {
    // Use Android Keystore
    val encryptedPrefs = EncryptedSharedPreferences.create(...)
    encryptedPrefs.edit {
        putString("auth_token", token)
    }
}
```

---

**Remember:** Network is unreliable - handle errors gracefully!
