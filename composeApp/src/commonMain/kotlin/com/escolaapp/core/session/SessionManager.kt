package com.escolaapp.core.session

import com.escolaapp.core.domain.model.Role

class SessionManager {
    var token: String = ""
    var userId: Int = 0
    var name: String = ""
    var email: String = ""
    var role: Role = Role.GUARDIAN
    var languageTag: String = "pt"

    fun save(token: String, userId: Int, name: String, email: String, role: Role) {
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
        role = Role.GUARDIAN
    }

    val isLoggedIn: Boolean get() = token.isNotBlank()
}
