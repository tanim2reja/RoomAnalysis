# Room
The Room persistence library is a part of android architecture component it provides an abstraction layer over SQLite to allow for more robust database access

Room has Three major components
1. Entity
2. DAO (Database Access Object)
3. Database

Add the dependencies for the artifacts you need in the build.gradle file for your app or module:
```kotlin
dependencies {
  def room_version = "2.2.3"

  implementation "androidx.room:room-runtime:$room_version"
  annotationProcessor "androidx.room:room-compiler:$room_version" // For Kotlin use kapt instead of annotationProcessor

  // optional - Kotlin Extensions and Coroutines support for Room
  implementation "androidx.room:room-ktx:$room_version"

  // optional - RxJava support for Room
  implementation "androidx.room:room-rxjava2:$room_version"

  // optional - Guava support for Room, including Optional and ListenableFuture
  implementation "androidx.room:room-guava:$room_version"

  // Test helpers
  testImplementation "androidx.room:room-testing:$room_version"
}
```

Room allows you to create tables via an Entity. Let's do this now.
```kotlin
@Entity(tableName = "word_table")
class Word(@PrimaryKey @ColumnInfo(name = "word") val word: String)
```

`@Entity` 
class represents a SQLite table. Annotate your class declaration to indicate that it's an entity. You can specify the name of the table if you want it to be different from the name of the class. This names the table "word_table".

`@PrimaryKey`
Each entity must define at least 1 field as a primary key. Even when there is only 1 field, you still need to annotate the field with the `@PrimaryKey` annotation. Also, if you want Room to assign automatic IDs to entities, you can set the `@PrimaryKey`'s autoGenerate property. If the entity has a composite primary key, you can use the primaryKeys property of the `@Entity` annotation, like this 

```kotlin
@Entity(primaryKeys = arrayOf("id", "word"))
data class Word (@PrimaryKey @ColumnInfo(name="id") val id: Int,
                 @ColumnInfo(name="word") val word: String,
                 @ColumnInfo(name="meaning") val meaning: String)
```
If an entity has fields that you don't want to persist, you can annotate them using @Ignore                 
`@Ignore val picture: Bitmap?`

```kotlin
open class User {
    var picture: Bitmap? = null
}

@Entity(ignoredColumns = arrayOf("picture"))
data class RemoteUser(
    @PrimaryKey val id: Int,
    val hasVpn: Boolean
) : User()
```

DAO

```kotlin
@Dao
interface WordDao {

    @Query("select * from word_table order by word asc")
    fun getAscendingWords() : List<Word>

    @Query("select * from word_table order by word desc")
    fun getDescendingWords() : List<Word>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word:Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

    @RawQuery
    fun getWords(query: SupportSQLiteQuery?): List<Word>?
}

```

The `@Insert`, `@Delete` and `@Update` annotation is a special DAO method annotation where you don't have to provide any SQL!
`onConflict = OnConflictStrategy.IGNORE` The selected on conflict strategy ignores a new word if it's exactly the same as one already in the list. `ABORT` OnConflict strategy constant to abort the transaction. `REPLACE`
OnConflict strategy constant to replace the old data and continue the transaction.

Implement the Room database
Room database class must be abstract and extend RoomDatabase. Usually, you only need one instance of a Room database for the whole app.
```kotlin
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase() {

   abstract fun wordDao(): WordDao

   companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time. 
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java, 
                        "word_database"
                    ).build()
                INSTANCE = instance
                return instance
            }
        }
   }
}

```
* The database class for Room must be `abstract` and `extend` `RoomDatabase`
* annotate the class to be a Room database with `@Database` and use the annotation parameters to declare the entities that belong in the database and set the version number.

Note: When you modify the database schema, you'll need to update the version number and define a migration strategy

Why use a Repository?

A Repository manages queries and allows you to use multiple backends. In the most common example, the Repository implements the logic for deciding whether to fetch data from a network or use results cached in a local database.

```kotlin
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()
 
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}
```
The DAO is passed into the repository constructor as opposed to the whole database. There's no need to expose the entire database to the repository.

Repositories are meant to mediate between different data sources. In this simple example, you only have one data source, so the Repository doesn't do much

Room Migration ???

