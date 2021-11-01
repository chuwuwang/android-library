package com.sea.library.common.lifecycle

class ResultBuilder<T>() {

    var onLading: () -> Unit = { }

    var onSuccess: (data: T ? ) -> Unit = { }

    var onError: (Throwable ? ) -> Unit = { }

}