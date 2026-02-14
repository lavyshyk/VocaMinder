package org.vocaminder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform