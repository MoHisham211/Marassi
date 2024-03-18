package mo.zain.marassi.ui.fragment.main

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import mo.zain.marassi.R


class WebViewFragment : Fragment() {



    private lateinit var webView: WebView
    private var url: String? = null
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString("url")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        webView = view.findViewById(R.id.webView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url?.let {
            webView.apply {
                loadUrl(it)
                webViewClient = WebViewClient() // Ensure links are handled within WebView
                settings.javaScriptEnabled = true // Enable JavaScript
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url) // Load the URL in the WebView

                // Check if the URL starts with the specified prefix
                if (url.startsWith("https://datamanager686.pythonanywhere.com/api/echo_get_data/")) {
                    // Post a delayed action to navigate after 2 seconds
                    handler.postDelayed({
                        findNavController().navigate(R.id.action_webViewFragment_to_requestsFragment)
                    }, 5000)
                }

                return true // Indicate that the WebView should handle the URL loading
            }
        }
    }




    companion object {
        @JvmStatic
        fun newInstance(url: String) =
            WebViewFragment().apply {
                arguments = Bundle().apply {
                    putString("url", url)
                }
            }
    }
}