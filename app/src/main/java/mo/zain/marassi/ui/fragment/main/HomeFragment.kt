package mo.zain.marassi.ui.fragment.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import mo.zain.marassi.R
import mo.zain.marassi.adapter.HomeAdapter
import mo.zain.marassi.model.DataX
import mo.zain.marassi.viewModel.PortsViewModel


class HomeFragment : Fragment() {
    lateinit var RV:RecyclerView
    var saveToken: SharedPreferences? = null
    private lateinit var viewModel: PortsViewModel
    private var AllPorts: ArrayList<DataX> = ArrayList()
    private var homeAdapter:HomeAdapter ?=null


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)
        saveToken = requireActivity().getSharedPreferences("Token", Context.MODE_PRIVATE)
        val token = saveToken!!.getString("Token", "")

        getSeaPort(token!!)

        RV=view.findViewById(R.id.RV)
        RV.layoutManager= StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        return view
    }


    private fun getSeaPort(token: String) {
        viewModel = ViewModelProvider(this).get(PortsViewModel::class.java)

        viewModel.getAllPorts(token) { isSuccess, seaports, message ->
            if (isSuccess) {
                // Populate spinner adapter with names
                seaports?.let {
                    AllPorts.addAll(it.data)
                }
                homeAdapter= HomeAdapter(AllPorts)
                RV.adapter=homeAdapter
            } else {
                Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }


}

/*fun getImageViaUrl(context: Context?, url: String?, imageView: ImageView?, progressBar: View?) {
    progressBar!!.visibility = View.VISIBLE

   /* Animations.animJumpAndFade(context, progressBar)
    Glide.with(context!!).load(url).listener(object : RequestListener<Drawable?> {
        override fun onLoadFailed(
            @Nullable e: GlideException?,
            model: Any?,
            target: Target<Drawable?>?,
            isFirstResource: Boolean
        ): Boolean {
            shortToast(context, context!!.getString(R.string.failed_to_load_image))
            progressBar!!.clearAnimation()
            progressBar!!.visibility = View.INVISIBLE
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar!!.clearAnimation()
            progressBar!!.visibility = View.INVISIBLE
            return false
        }
    }).timeout(30000).into<com.bumptech.glide.request.target.Target<Drawable>>(
        imageView!!
    )*/

}*/