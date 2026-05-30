package io.github.atwa.komposed

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual val defaultIoDispatcher: CoroutineDispatcher = Dispatchers.IO
