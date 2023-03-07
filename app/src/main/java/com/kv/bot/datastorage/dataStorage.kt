package com.kv.bot.datastorage

import android.content.Context

object dataStorage {

    // Define a constant for the name of the shared preferences file
    private const val MY_PREF = "my_prefs"
    // Define a constant for the key of the user name data in the file
    const val USERNAME = "user_name"

    /**
     * Save a key-value pair to the app's shared preferences file.
     *
     * @param context The context of the app.
     * @param key The key to save the data under.
     * @param value The data to save.
     */
    fun saveData(context: Context, key: String, value: String) {
        // Get a reference to the shared preferences file with the specified name
        val prefs = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        // Create an editor to modify the contents of the file
        val editor = prefs.edit()
        // Put the key-value pair into the editor
        editor.putString(key, value)
        // Apply the changes to the file
        editor.apply()
    }

    /**
     * Load data from the app's shared preferences file.
     *
     * @param context The context of the app.
     * @param key The key to load the data for.
     * @param defaultValue The default value to return if the key does not exist in the file.
     * @return The data associated with the key, or the default value if the key does not exist.
     */
    fun loadData(context: Context, key: String, defaultValue: String): String {
        // Get a reference to the shared preferences file with the specified name
        val prefs = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
        // Get the value associated with the key from the file, or the default value if the key does not exist
        return prefs.getString(key, defaultValue) ?: defaultValue
    }
}
