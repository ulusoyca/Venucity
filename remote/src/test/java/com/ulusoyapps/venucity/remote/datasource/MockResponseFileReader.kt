package com.ulusoyapps.venucity.remote.datasource

import java.io.InputStreamReader

// https://sachinkmr375.medium.com/unit-test-retrofit-api-calls-with-mockwebserver-bbb9f66a78a6
class MockResponseFileReader(path: String) {
    val content: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }
}
