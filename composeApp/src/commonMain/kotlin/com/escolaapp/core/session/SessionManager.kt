package com.escolaapp.core.session

class SessionManager {
    var token: String = ""
    var userId: Int = 0
    var name: String = ""
    var email: String = ""
    var role: String = ""

    fun save(token: String, userId: Int, name: String, email: String, role: String) {
        this.token = token
        this.userId = userId
        this.name = name
        this.email = email
        this.role = role
    }

    fun clear() {
        token = ""
        userId = 0
        name = ""
        email = ""
        role = ""
    }

    val isLoggedIn: Boolean get() = token.isNotBlank()
}
