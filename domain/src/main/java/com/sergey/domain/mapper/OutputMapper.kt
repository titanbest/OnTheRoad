package com.sergey.domain.mapper

interface OutputMapper<in From, out To> {
    fun transformFromDomain(item: From): To
}