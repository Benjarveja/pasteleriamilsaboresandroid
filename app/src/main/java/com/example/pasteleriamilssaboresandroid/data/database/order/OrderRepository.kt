package com.example.pasteleriamilssaboresandroid.data.database.order

import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderDao: OrderDao) {
    suspend fun insert(order: Order) {
        orderDao.insert(order)
    }

    fun getOrdersByUser(userId: Int): Flow<List<Order>> {
        return orderDao.getOrdersByUser(userId)
    }
}
