🔰 1️⃣ Job Overview (এই app আসলে কী করে)

UserProfileRegistration App

User নিজের profile add করতে পারবে

Profile data local storage (SQLite / Room) এ save হবে

সব profile list আকারে দেখাবে

Profile view / update / delete করা যাবে

👉 ইন্টারনেট লাগে না
👉 সব data মোবাইলের ভিতরেই থাকে

🔰 2️⃣ Architecture: MVVM কীভাবে কাজ করছে

MVVM = Model – View – ViewModel

UI (Activity)
   ↓
ViewModel
   ↓
Repository
   ↓
Room Database (SQLite)

সহজ ভাষায়

View (Activity) → শুধু UI দেখায়

ViewModel → logic ধরে

Model (Room) → data save / read করে

🔰 3️⃣ Data কোথা থেকে কোথায় যাচ্ছে (Full Data Flow)
🟢 Profile Add করার সময়
AddProfileActivity (User input)
      ↓
UserProfileViewModel.insertProfile()
      ↓
UserRepository.insert()
      ↓
UserProfileDao.insertProfile()
      ↓
Room Database (SQLite)

🟢 Profile List দেখানোর সময়
Room Database
   ↓
DAO → Repository
   ↓
ViewModel (LiveData)
   ↓
ProfileListActivity (RecyclerView)

🟢 Single Profile দেখানোর সময়
ProfileListActivity
   ↓ (Intent দিয়ে profileId পাঠানো)
SingleProfileActivity
   ↓
ViewModel → Repository → DAO → Database

🔰 4️⃣ File by File Complete Explanation
🧩 DATA LAYER
🔹 UserProfile.kt (Entity)

📌 কাজ:
SQLite table কেমন হবে সেটা define করা

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val dob: String,
    val district: String,
    val number: String
)


👉 এটা Model
👉 Database এর table এর structure

🔹 UserProfileDao.kt (DAO)

📌 কাজ:
Database-এর সাথে কথা বলা

@Dao
interface UserProfileDao {

    @Insert
    suspend fun insertProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Delete
    suspend fun deleteProfile(profile: UserProfile)

    @Query("SELECT * FROM user_profile")
    fun getAllProfiles(): LiveData<List<UserProfile>>
}


👉 CRUD operation
👉 SQL এখানে থাকে

🔹 UserProfileDatabase.kt

📌 কাজ:
Database তৈরি করা

@Database(entities = [UserProfile::class], version = 1)
abstract class UserProfileDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: UserProfileDatabase? = null

        fun getDatabase(context: Context): UserProfileDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    UserProfileDatabase::class.java,
                    "user_profile_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}


👉 SQLite database create করে
👉 App-এ একটাই instance থাকে (Singleton)

🧩 REPOSITORY LAYER
🔹 UserRepository.kt

📌 কাজ:
ViewModel আর Database এর মাঝখানে bridge

class UserRepository(private val dao: UserProfileDao) {

    val allProfiles: LiveData<List<UserProfile>> = dao.getAllProfiles()

    suspend fun insert(profile: UserProfile) {
        dao.insertProfile(profile)
    }

    suspend fun update(profile: UserProfile) {
        dao.updateProfile(profile)
    }

    suspend fun delete(profile: UserProfile) {
        dao.deleteProfile(profile)
    }
}


👉 ViewModel সরাসরি DAO call করে না
👉 Future-এ চাইলে API add করা যাবে

🧩 VIEWMODEL LAYER
🔹 UserProfileViewModel.kt

📌 কাজ:
UI logic ধরে রাখা

class UserProfileViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository: UserRepository
    val allProfiles: LiveData<List<UserProfile>>

    init {
        val dao = UserProfileDatabase
            .getDatabase(application)
            .userProfileDao()
        repository = UserRepository(dao)
        allProfiles = repository.allProfiles
    }

    fun insertProfile(profile: UserProfile) =
        viewModelScope.launch {
            repository.insert(profile)
        }

    fun updateProfile(profile: UserProfile) =
        viewModelScope.launch {
            repository.update(profile)
        }

    fun deleteProfile(profile: UserProfile) =
        viewModelScope.launch {
            repository.delete(profile)
        }
}


👉 LiveData automatically UI update করে
👉 Screen rotate হলেও data survive করে

🧩 UI LAYER (Activities)
🔹 WelcomeActivity.kt

📌 কাজ:
App start screen

btnProfileList.setOnClickListener {
    startActivity(Intent(this, ProfileListActivity::class.java))
}

🔹 ProfileListActivity.kt

📌 কাজ:
সব profile দেখানো + count দেখানো

viewModel.allProfiles.observe(this) { profiles ->
    adapter.submitList(profiles)
    textTotal.text = "Total: ${profiles.size}"
}


👉 RecyclerView ব্যবহার করা
👉 Edit / Delete button থাকে

🔹 AddProfileActivity.kt

📌 কাজ:
User input নিয়ে database-এ save করা

btnSave.setOnClickListener {
    val profile = UserProfile(
        name = name,
        email = email,
        dob = dob,
        district = district,
        number = number
    )
    viewModel.insertProfile(profile)
    finish()
}

🔹 SingleProfileActivity.kt

📌 কাজ:
একজন user এর full profile দেখানো

profileId = intent.getIntExtra("profileId", 0)

viewModel.allProfiles.observe(this) { profiles ->
    currentProfile = profiles.find { it.id == profileId } ?: return@observe
}


👉 profileId Intent থেকে আসে
👉 Database থেকে data fetch হয়

🔹 UpdateProfileActivity.kt

📌 কাজ:
Profile edit করা

val updated = currentProfile.copy(
    name = newName,
    email = newEmail
)
viewModel.updateProfile(updated)

🔰 5️⃣ Viva / Interview বলার জন্য ৩ লাইন

“This application follows MVVM architecture.
User input goes from Activity to ViewModel, then Repository, then Room Database.
LiveData is used to automatically update UI when database data changes.”
