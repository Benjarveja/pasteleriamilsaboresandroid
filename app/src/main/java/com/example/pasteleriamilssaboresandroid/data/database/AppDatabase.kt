package com.example.pasteleriamilssaboresandroid.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pasteleriamilssaboresandroid.data.database.order.Order
import com.example.pasteleriamilssaboresandroid.data.database.order.OrderDao
import com.example.pasteleriamilssaboresandroid.data.database.user.User
import com.example.pasteleriamilssaboresandroid.data.database.user.UserDao

@Database(entities = [User::class, Order::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
