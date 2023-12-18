package ru.otus.mongo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File

data class Data(
    val hostname: String,
    val playerInitialLives: String,
    val data: String,
    val password: String,
    val state: String
)

fun readSettings(): String =
    File("/config/game.properties").let {
        if (it.exists()) it.readText() else "no settings"
    }

fun readState(): String =
    File("/storage/state.txt").let {
        if (it.exists()) it.readText() else "no state"
    }

fun writeState(state: String) {
    File("/storage/state.txt").writeText(state)
}

@RestController
class Controller(private val userRepository: UserRepository) {
    @GetMapping
    fun hello() =
        Data(
            System.getenv("HOSTNAME") ?: "",
            System.getenv("PLAYER_INITIAL_LIVES") ?: "7",
            readSettings(),
            System.getenv("PASSWORD") ?: "123",
            readState()
        )

    @GetMapping("/set")
    fun set(@RequestParam data: String): String {
        writeState(data)
        return "OK"
    }

    @GetMapping("/insert")
    fun insert(@RequestParam name: String): String {
        val user = User(name)
        userRepository.insert(user)
        return "OK"
    }

    @GetMapping("/user")
    fun getAll(): List<User> = userRepository.findAll()
}