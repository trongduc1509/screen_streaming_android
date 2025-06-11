package com.duczxje.screenstreaming

import com.duczxje.screenstreaming.utils.Once
import com.duczxje.screenstreaming.utils.Result
import junit.framework.TestCase.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsUnitTest {
    @Test
    fun once_test() {
        val once = Once("Hello World")

        assertEquals(once.get(), "Hello World")
        assertEquals(once.get(), null)
    }

    @Test
    fun result_success() {
        val result = Result.success("Hello World")

        assertEquals(result.data, "Hello World")
    }

    @Test
    fun result_failure() {
        val result = Result.error(Exception("Error"))

        assertEquals(result.throwable.message, "Error")
    }
}