package com.example.authentication3
//data class for returning the patient list for doctors;

data class PatientResponses(
    var __v: Int,
    var _id: String,
    var adherence: String,
    var email: String,
    var name: String,
    var new_user: Boolean,
    var username: String


) {
    override fun toString(): String {
        return "PatientResponses(__v=$__v, _id='$_id', adherence=$adherence, email='$email', name='$name', new_user=$new_user, username='$username')"
    }
}
