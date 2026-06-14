package com.escolaapp

import com.escolaapp.core.domain.model.Role
import com.escolaapp.core.domain.model.Role.TEACHER
import com.escolaapp.core.session.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SessionManagerTest {

    @Test
    fun `save should persist user data`() {
        val manager = SessionManager()
        manager.save("token123", 1, "João", "joao@email.com", TEACHER)

        assertEquals("token123", manager.token)
        assertEquals(1, manager.userId)
        assertEquals("João", manager.name)
        assertEquals("joao@email.com", manager.email)
        assertEquals(TEACHER, manager.role)
    }

    @Test
    fun `clear should reset all fields`() {
        val manager = SessionManager()
        manager.save("token123", 1, "João", "joao@email.com", TEACHER)
        manager.clear()

        assertEquals("", manager.token)
        assertEquals(0, manager.userId)
        assertEquals("", manager.name)
        assertEquals("", manager.email)
        assertEquals(Role.GUARDIAN, manager.role)
        assertEquals(0, manager.studentId)
    }

    @Test
    fun `isLoggedIn should return true when token is not blank`() {
        val manager = SessionManager()
        manager.token = "some-token"
        assertTrue(manager.isLoggedIn)
    }

    @Test
    fun `isLoggedIn should return false when token is blank`() {
        val manager = SessionManager()
        manager.token = ""
        assertFalse(manager.isLoggedIn)
    }
}