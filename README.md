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
