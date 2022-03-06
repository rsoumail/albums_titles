package com.rsoumail.mymemories.view.activities


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.rsoumail.mymemories.R
import com.rsoumail.mymemories.view.fragments.BaseFragment
import com.rsoumail.mymemories.view.viewmodels.BaseViewModel

/**
 * FragmentWrapperActivity provides all fragment management functions
 * avoiding fragment management boilerplate code in activities
 */
abstract class FragmentWrapperActivity<VM : BaseViewModel, ViewBindingType : ViewBinding> : BaseActivity<VM, ViewBindingType>() {

    private var progressLayout: View? = null
    /**
     * Set a fragment as content by replace
     * if a fragment is already loaded is will be removed and replaced
     */
    fun <V:BaseViewModel , F : BaseFragment<V>> setFragment(fragment: F, fragmentId: Int = R.id.fragment_container){
        copyExtrasToArgs(fragment)
        val currentFragment = getCurrentFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment)
        }
        fragmentTransaction.replace(fragmentId, fragment,fragment.javaClass.simpleName)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_NONE)
        fragmentTransaction.commit()
    }

    /**
     * Set the fragment as content by addition
     * if a fragment is already loaded add it to the back stack
     * if there is no fragment loaded acts like [setFragment]
     */
    fun addFragment(fragment: Fragment, fragmentId: Int = R.id.fragment_container, addToBackStack: Boolean = true) {
        copyExtrasToArgs(fragment)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(fragmentId, fragment, fragment.javaClass.simpleName)
        if (supportFragmentManager.fragments.isNotEmpty() && addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    /**
     * get the current loaded fragment
     */
    private fun getCurrentFragment(fragmentId: Int = R.id.fragment_container): BaseFragment<*>? {
        return supportFragmentManager.findFragmentById(fragmentId) as BaseFragment<*>?
    }

    /**
     * convenient function to copy extras from an activity as fragment args
     */
    private fun copyExtrasToArgs(fragment: Fragment) {
        val args = Bundle()
        if (intent.extras != null) {
            args.putAll(intent.extras)
        }
        if (fragment.arguments != null) {
            args.putAll(fragment.arguments)
        }
        fragment.arguments = args
    }


    override fun initViews() {
        // Should be implemented in the parent activity
    }

    override fun initObservers() {
        // Should be implemented in the parent activity
    }


    fun clearAllFragments() {
        val fragments = supportFragmentManager.fragments
        if(fragments.isNotEmpty()){
            for (fragment in fragments) {
                supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}
