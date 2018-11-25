package com.riningan.wowmount.ui.base

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<T: BaseView> : MvpPresenter<T>() {
    private var compositeDisposable: CompositeDisposable? = null


    fun clearSubscriptions() {
        compositeDisposable?.dispose()
        compositeDisposable = null
    }

    protected fun Disposable.attach() {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(this)
    }
}