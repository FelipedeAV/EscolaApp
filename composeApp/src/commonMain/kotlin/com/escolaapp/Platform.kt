package com.escolaapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform