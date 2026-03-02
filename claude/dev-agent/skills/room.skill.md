---
name: room
description: Room database implementation, local data storage, migrations, and offline-first patterns. Triggers when user mentions database, Room, SQLite, local storage, caching, or offline data.
---

# Room Database Skill

Local database implementation with Room.

## Version: 2.6.1

## Database Setup

```kotlin
// data/database/AppDatabase.kt
@Database(
    entities = [
        ProductEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    
    companion object {
        private const val DATABASE_NAME = "app_database.db"
        
        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration() // Dev only!
            .build()
        }
    }
}
```

## Entity

```kotlin
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
```

## DAO

```kotlin
@Dao
interface ProductDao {
    
    @Query("SELECT * FROM products ORDER BY created_at DESC")
    fun observeAll(): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getById(id: String): ProductEntity?
    
    @Query("SELECT * FROM products WHERE category = :category")
    suspend fun getByCategory(category: String): List<ProductEntity>
    
    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%'")
    suspend fun search(query: String): List<ProductEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)
    
    @Update
    suspend fun update(product: ProductEntity)
    
    @Delete
    suspend fun delete(product: ProductEntity)
    
    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteById(id: String)
    
    @Query("DELETE FROM products")
    suspend fun deleteAll()
}
```

## Relationships

### One-to-Many

```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String
)

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    val total: Double,
    val createdAt: Long
)

data class UserWithOrders(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val orders: List<OrderEntity>
)

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserWithOrders(userId: String): UserWithOrders?
}
```

### Many-to-Many

```kotlin
@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey val id: String,
    val name: String
)

@Entity(
    tableName = "product_tags",
    primaryKeys = ["product_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductTagCrossRef(
    @ColumnInfo(name = "product_id") val productId: String,
    @ColumnInfo(name = "tag_id") val tagId: String
)

data class ProductWithTags(
    @Embedded val product: ProductEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ProductTagCrossRef::class,
            parentColumn = "product_id",
            entityColumn = "tag_id"
        )
    )
    val tags: List<TagEntity>
)
```

## Type Converters

```kotlin
class Converters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
    
    @TypeConverter
    fun toStringList(list: List<String>): String {
        return Json.encodeToString(list)
    }
}

@Database(
    // ...
    converters = [Converters::class]
)
abstract class AppDatabase : RoomDatabase()
```

## Migrations

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add new column
        database.execSQL(
            "ALTER TABLE products ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0"
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create new table
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS categories (
                id TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                created_at INTEGER NOT NULL
            )
        """)
    }
}

fun createDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database.db"
    )
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
    .build()
}
```

## Local Data Source

```kotlin
class ProductLocalDataSource(
    private val dao: ProductDao
) {
    fun observeAll(): Flow<List<ProductEntity>> {
        return dao.observeAll()
    }
    
    suspend fun getById(id: String): ProductEntity? {
        return dao.getById(id)
    }
    
    suspend fun getAll(): List<ProductEntity> {
        return dao.observeAll().first()
    }
    
    suspend fun save(product: ProductEntity) {
        dao.insert(product)
    }
    
    suspend fun saveAll(products: List<ProductEntity>) {
        dao.insertAll(products)
    }
    
    suspend fun delete(id: String) {
        dao.deleteById(id)
    }
    
    suspend fun clear() {
        dao.deleteAll()
    }
    
    suspend fun search(query: String): List<ProductEntity> {
        return dao.search(query)
    }
}
```

## Offline-First Pattern

```kotlin
class ProductRepositoryImpl(
    private val remoteDataSource: ProductRemoteDataSource,
    private val localDataSource: ProductLocalDataSource,
    private val mapper: ProductMapper,
    private val networkMonitor: NetworkMonitor
) : ProductRepository {
    
    // Single source of truth
    override fun observeProducts(): Flow<List<Product>> {
        return localDataSource.observeAll()
            .map { entities -> entities.map(mapper::toDomain) }
            .onStart {
                // Trigger refresh in background
                refreshInBackground()
            }
    }
    
    private fun refreshInBackground() {
        scope.launch {
            if (networkMonitor.isCurrentlyConnected()) {
                remoteDataSource.getProducts()
                    .onSuccess { dtos ->
                        val entities = dtos.map(mapper::toEntity)
                        localDataSource.saveAll(entities)
                    }
            }
        }
    }
    
    override suspend fun refresh(): Result<Unit> = runCatching {
        if (!networkMonitor.isCurrentlyConnected()) {
            throw NetworkException("No internet")
        }
        
        val dtos = remoteDataSource.getProducts().getOrThrow()
        val entities = dtos.map(mapper::toEntity)
        
        // Replace all data
        localDataSource.clear()
        localDataSource.saveAll(entities)
    }
}
```

## Paging

```kotlin
@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    suspend fun getPage(limit: Int, offset: Int): List<ProductEntity>
}

class PaginatedProductSource(
    private val dao: ProductDao,
    private val pageSize: Int = 20
) {
    suspend fun loadPage(page: Int): List<ProductEntity> {
        val offset = page * pageSize
        return dao.getPage(limit = pageSize, offset = offset)
    }
}
```

## Full-Text Search

```kotlin
@Entity(tableName = "products_fts")
@Fts4(contentEntity = ProductEntity::class)
data class ProductFts(
    val name: String,
    val description: String
)

@Dao
interface ProductDao {
    @Query("""
        SELECT p.* FROM products p
        JOIN products_fts fts ON p.id = fts.rowid
        WHERE products_fts MATCH :query
    """)
    suspend fun searchFullText(query: String): List<ProductEntity>
}
```

## Transaction

```kotlin
@Dao
interface ProductDao {
    @Transaction
    suspend fun replaceAll(products: List<ProductEntity>) {
        deleteAll()
        insertAll(products)
    }
}
```

## Testing

```kotlin
@RunWith(AndroidJUnit4::class)
class ProductDaoTest {
    
    private lateinit var database: AppDatabase
    private lateinit var dao: ProductDao
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        
        dao = database.productDao()
    }
    
    @After
    fun teardown() {
        database.close()
    }
    
    @Test
    fun insertAndRetrieve() = runTest {
        val product = ProductEntity(
            id = "1",
            name = "Test",
            price = 10.0,
            // ...
        )
        
        dao.insert(product)
        
        val retrieved = dao.getById("1")
        assertEquals(product, retrieved)
    }
}
```

## Best Practices

✅ **Do:**
- Use Flow for reactive queries
- Write migrations for schema changes
- Use transactions for multiple operations
- Index frequently queried columns
- Use Type Converters for complex types
- Test DAOs with in-memory database
- Use proper foreign key constraints

❌ **Don't:**
- Use fallbackToDestructiveMigration in production
- Perform database operations on main thread
- Store large blobs in database
- Forget to close database in tests
- Ignore migration warnings
- Use mutable data structures in entities

---

**Remember:** Database is your offline source of truth!
