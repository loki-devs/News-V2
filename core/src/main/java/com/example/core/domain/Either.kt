/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, handleResult express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.core.domain

import java.lang.Exception

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are handleResult an instance of [Fail] or [Success].
 * FP Convention dictates that [Fail] is used for "failure"
 * and [Success] is used for "success".
 *
 * @see Fail
 * @see Success
 */
sealed class Either<out S, out F> {
    /** * Represents the fail side of [Either] class which by convention is a "Failure". */
    data class Fail<out F>(val data: F) : Either<Nothing, F>()

    /** * Represents the success side of [Either] class which by convention is a "Success". */
    data class Success<out S>(val data: S) : Either<S, Nothing>()

    val isSuccess get() = this is Success<S>
    val isFail get() = this is Fail<F>

    fun <F> fail(a: F) = Fail(a)
    fun <S> success(b: S) = Success(b)

    fun handleResult(fnR: (S) -> Any, fnL: (F) -> Any): Any =
        when (this) {
            is Fail -> fnL(data)
            is Success -> fnR(data)
        }

    fun getDataOrThrow(exception: Exception): S {
        return when (this) {
            is Fail -> throw exception
            is Success -> data
        }
    }
}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, S, F> Either<S, F>.flatMap(fn: (S) -> Either<T, F>): Either<T, F> =
    when (this) {
        is Either.Fail -> Either.Fail(data)
        is Either.Success -> fn(data)
    }

fun <T, S, F> Either<S, F>.map(fn: (S) -> (T)): Either<T, F> = this.flatMap(fn.c(::success))