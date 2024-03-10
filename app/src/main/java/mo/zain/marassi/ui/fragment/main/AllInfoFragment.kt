package mo.zain.marassi.ui.fragment.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mo.zain.marassi.R
import mo.zain.marassi.adapter.LinksAdapter
import mo.zain.marassi.model.DataX
import mo.zain.marassi.model.Link
import mo.zain.marassi.model.SeaPortItems


class AllInfoFragment : Fragment() {



    lateinit var portImage:ImageView
    lateinit var fab:FloatingActionButton
    lateinit var rvLinks:RecyclerView
    private var linksList:List<Link> = ArrayList()
    private lateinit var adapter:LinksAdapter
    lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_all_info, container, false)

        fab=view.findViewById(R.id.fab)
        //val parentLayout: NestedScrollView = view.findViewById(R.id.nested)
        textView= view.findViewById(R.id.description)
        rvLinks=view.findViewById(R.id.rvAll)
        rvLinks.layoutManager=LinearLayoutManager(requireContext())

        portImage=view.findViewById(R.id.portImage)

        val receivedBundle = arguments
        if (receivedBundle != null) {
            val portItems = receivedBundle.getSerializable("amount") as? DataX


            if (portItems != null) {

                textView.text=portItems.description

                Glide.with(requireActivity()).load(portItems.photo)
                .into(portImage)

                linksList=portItems.links
                adapter=LinksAdapter(linksList)
                rvLinks.adapter=adapter

                Log.d("Photo",portItems.photo)
                fab.setOnClickListener {
                    val url = portItems.location
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)

                }
            } else {
                Toast.makeText(requireContext(), "Failed to retrieve port items", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Bundle is null", Toast.LENGTH_SHORT).show()
        }
        return view
    }

}