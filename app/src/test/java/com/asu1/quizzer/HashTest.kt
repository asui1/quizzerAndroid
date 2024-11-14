package com.asu1.quizzer

import com.asu1.quizzer.util.generateUniqueId
import org.junit.Assert.assertEquals
import org.junit.Test

class HashTest {

    @Test
    fun testGenerateUniqueId() {
        val email = "whwkd122@gmail.com"
        val uuid = "cc4b7bc3-c79f-5dc7-885d-d91569194880"

        val result = generateUniqueId(uuid, email)


        val expectation = "ecafccfb53a6bab5c195eb739e2fbb07ec8b1d7f2f2cd93af9292c081271c6b4"
        assertEquals(result, expectation)
    }

    @Test
    fun resultUniqueHastTest(){
        val email = "whwkd122@gmail.com"
        val uuid = "7095a219-c74c-52ce-ba3e-2cf01c7cd3ff"
        val result = generateUniqueId(uuid, email)
        assertEquals(result, "72e270f91cb915b87d7114a958a7e7e151992f094caf5799db8e1c7c1245470a")
    }
}