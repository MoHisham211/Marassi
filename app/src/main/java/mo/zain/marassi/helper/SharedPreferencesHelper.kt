package mo.zain.marassi.helper
import UserData
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object SharedPreferencesHelper {
    private const val PREF_NAME = "UserData"
    private const val KEY_USER_DATA = "userData"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserData(context: Context, userData: UserData) {
        val editor = getSharedPreferences(context).edit()
        val gson = Gson()
        val userDataJson = gson.toJson(userData)
        editor.putString(KEY_USER_DATA, userDataJson)
        editor.apply()
    }

    fun getUserData(context: Context): UserData? {
        val userDataJson = getSharedPreferences(context).getString(KEY_USER_DATA, null)
        val gson = Gson()
        return gson.fromJson(userDataJson, UserData::class.java)
    }
}
