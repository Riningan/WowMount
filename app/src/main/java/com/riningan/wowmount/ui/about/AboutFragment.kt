package com.riningan.wowmount.ui.about

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.riningan.frarg.annotations.ArgumentedFragment
import com.riningan.wowmount.BuildConfig
import com.riningan.wowmount.R
import com.riningan.wowmount.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.*


@ArgumentedFragment
class AboutFragment : BaseFragment(), AboutView {
    @InjectPresenter
    lateinit var mPresenter: AboutPresenter

    @ProvidePresenter
    fun providePresenter() = AboutPresenter(kodein)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tlbAbout.setNavigationOnClickListener { mPresenter.onBackClick() }

        tvAboutVersion.text = getString(R.string.about_version, BuildConfig.VERSION_NAME)

        tvAboutAuthor.text = SpannableString(getString(R.string.about_author, getString(R.string.about_author_email))).apply {
            val start = length - 1 - getString(R.string.about_author_email).length
            val end = length - 1
            setSpan(ForegroundColorSpan(Color.BLUE), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setSpan(UnderlineSpan(), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

//        tvAboutCode.text = SpannableStringBuilder(getString(R.string.about_code))
//                .bold { underline { color(Color.BLUE) { append(getString(R.string.about_code_url)) } } }
        tvAboutCode.text = SpannableString(getString(R.string.about_code) + getString(R.string.about_code_url)).apply {
            //            setSpan(ForegroundColorSpan(Color.BLUE), getString(R.string.about_code).length, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
//            setSpan(UnderlineSpan(), getString(R.string.about_code).length, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(StyleSpan(Typeface.BOLD), getString(R.string.about_code).length, length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}