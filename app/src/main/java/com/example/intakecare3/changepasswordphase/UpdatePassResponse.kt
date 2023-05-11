package com.example.intakecare3.changepasswordphase

public class UpdatePassResponse {
    private lateinit var error : String
    private lateinit var message : String

    constructor(error: String, message: String) {
        this.error = error
        this.message = message
    }
}