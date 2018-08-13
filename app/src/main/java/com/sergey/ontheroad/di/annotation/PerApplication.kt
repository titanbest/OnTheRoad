package com.sergey.ontheroad.di.annotation

import javax.inject.Scope

/**
 * A scoping annotation to permit objects whose lifetime should
 * conform to the life of the application to be memorized in the
 * correct component.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerApplication