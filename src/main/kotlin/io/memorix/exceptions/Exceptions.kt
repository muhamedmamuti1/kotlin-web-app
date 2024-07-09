package io.memorix.exceptions

class DuplicateEmailException(email: String) : Exception("Duplicate email: $email")
class InvalidUserDataException : Exception("Invalid user data")