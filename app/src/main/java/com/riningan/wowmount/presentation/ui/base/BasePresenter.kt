package com.riningan.wowmount.presentation.ui.base

import com.arellomobile.mvp.MvpPresenter
import com.riningan.util.Logger
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


open class BasePresenter<T : BaseView> : MvpPresenter<T>() {
    private var compositeDisposable: CompositeDisposable? = null


    open fun clearSubscriptions() {
        Logger.forThis(this).debug()
        compositeDisposable?.dispose()
        compositeDisposable = null
    }


    protected fun Disposable.attach() {
        Logger.forThis(this@BasePresenter).debug()
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(this)
    }
}