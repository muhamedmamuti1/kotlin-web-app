package io.memorix.db

import io.memorix.user.User

fun daoToModel(dao: UserDAO) = User(
    name = dao.name,
    email = dao.email,
    password = dao.password
)