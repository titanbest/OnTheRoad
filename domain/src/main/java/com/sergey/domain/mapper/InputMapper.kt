package com.sergey.domain.mapper

interface InputMapper<in From, out To> {
    fun transformToDomain(item: From): To
}