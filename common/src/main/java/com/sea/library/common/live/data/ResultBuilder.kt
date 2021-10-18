package com.sea.library.common.live.data

class ResultBuilder<T>() {

    var onLading: () -> Unit = { }

    var onSuccess: (data: T ? ) -> Unit = { }

    var onError: (Throwable ? ) -> Unit = { }

}