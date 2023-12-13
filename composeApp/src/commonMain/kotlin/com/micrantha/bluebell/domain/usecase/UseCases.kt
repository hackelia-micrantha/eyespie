package com.micrantha.bluebell.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/** Executes the use case asynchronously and returns a [Result].
 *
 * @return a [Result].
 *
 * @param context the dispatcher context to execute with
 * @param block the block to execute.
 */
suspend fun <Out> dispatchUseCase(
    context: CoroutineContext,
    always: () -> Unit = {},
    block: suspend CoroutineScope.() -> Out
): Result<Out> {
    return try {
        withContext(context) {
            Result.success(block(this))
        }
    } catch (e: Throwable) {
        Result.failure(e)
    } finally {
        always()
    }
}

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [Result<R>].
 * Handling an exception (emit [Result.Failure] to the result) is the subclasses responsibility.
 */
fun <Out> flowUseCase(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    block: () -> Flow<Out>
) = block().map { Result.success(it) }.catch { e -> emit(Result.failure(Exception(e))) }
    .flowOn(coroutineContext)
