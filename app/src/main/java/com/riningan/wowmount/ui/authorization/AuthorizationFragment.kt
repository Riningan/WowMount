package com.riningan.wowmount.ui.authorization

import android.os.Build
import android.os.Bundle
import android.support.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.R
import com.riningan.wowmount.ui.base.BaseFragment
import com.riningan.wowmount.utils.SnackbarUtil
import kotlinx.android.synthetic.main.fragment_authorization.*


@ArgumentedFragment
class AuthorizationFragment : BaseFragment(), AuthorizationView {
    @InjectPresenter
    lateinit var mPresenter: AuthorizationPresenter


    @ProvidePresenter
    fun providePresenter() = AuthorizationPresenter(kodein)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = ChangeBounds()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_authorization, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cmbAuthorizationRegion.adapter = ArrayAdapter(context!!, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.authorization_servers))
        btnAuthorizationShow.setOnClickListener {
            mPresenter.onShowClick(
                    cmbAuthorizationRegion.selectedItem.toString(),
                    etAuthorizationRealm.text.toString(),
                    etAuthorizationCharacter.text.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        mPresenter.clearSubscriptions()
    }

    override fun showServerErrorDialog() {
        SnackbarUtil.showError(activity, R.string.authorization_error_empty_server)
    }

    override fun showRealmErrorDialog() {
        SnackbarUtil.showError(activity, R.string.authorization_error_empty_realm)
    }

    override fun showCharacterErrorDialog() {
        SnackbarUtil.showError(activity, R.string.authorization_error_empty_character_name)
    }

    override fun showRequestErrorDialog(message: String) {
        SnackbarUtil.showError(activity, message)
    }

    override fun lockView() {
        setViewState(false)
    }

    override fun unlockView() {
        setViewState(true)
    }


    private fun setViewState(isEnabled: Boolean) {
        cmbAuthorizationRegion.isEnabled = isEnabled
        etAuthorizationRealm.isEnabled = isEnabled
        etAuthorizationCharacter.isEnabled = isEnabled
        btnAuthorizationShow.isEnabled = isEnabled
    }
}