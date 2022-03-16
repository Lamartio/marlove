package io.lamart.mocky

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MockyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

    }

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory =
        MockyViewModelProviderFactory(
            defaultFactory = super.getDefaultViewModelProviderFactory(),
            getLogic = { mockyApplication.logic }
        )

}