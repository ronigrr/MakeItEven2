package com.example.makeiteven2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.makeiteven2.R

class FragmentStartScreen : Fragment() {

    companion object {
        fun newInstance() : FragmentStartScreen {
            return FragmentStartScreen()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_start_screen,container,false)


        return rootView
    }


}