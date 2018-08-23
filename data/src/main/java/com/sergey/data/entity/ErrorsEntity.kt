package com.sergey.data.entity

data class  ErrorsEntity(
	val errors: Map<String, List<String>>
)

data class  ErrorsMessageEntity(
		val message: String
)
