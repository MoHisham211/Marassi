package mo.zain.marassi.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import mo.zain.marassi.R
import mo.zain.marassi.model.DataX



class HomeAdapter(val list: ArrayList<DataX>): RecyclerView.Adapter<HomeAdapter.ViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        return ViewModel(LayoutInflater.from(parent.context).inflate(R.layout.ports_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {

        val jump = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.jump_and_fade);

        val portItems: DataX =list.get(position)

        holder.progress_product_iv.startAnimation(jump)

        Glide.with(holder.itemView.context).load(portItems.photo)
            .listener(object :RequestListener<Drawable?>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progress_product_iv.clearAnimation()
                    holder.progress_product_iv.visibility = View.INVISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progress_product_iv.clearAnimation()
                    holder.progress_product_iv.visibility = View.INVISIBLE
                    return false
                }

            }).timeout(30000).into(holder.iv_photo!!)

        holder.tv_id.text=portItems.id.toString();
        holder.tv_name.text=portItems.name
        holder.tv_description.text=portItems.description


//        val bundle = Bundle()
//        bundle.putSerializable("amount", portItems)
//
//        holder.itemView.setOnClickListener {
//            //holder.itemView.context
//              holder.itemView.findNavController().navigate(R.id.action_homeFragment_to_allInfoFragment,bundle)
//            //Toast.makeText(holder.itemView.context, ""+portItems.name, Toast.LENGTH_SHORT).show()
//        }


        //val portItemss: DataX =  portItems// Initialize your data here

        // Create a bundle and put data into it
        val bundle = Bundle()
        bundle.putSerializable("amount",portItems)

        // Navigate to AllInfoFragment with the bundle
        holder.itemView.setOnClickListener {
            val navController = holder.itemView.findNavController()
            navController?.let {
                it.navigate(R.id.action_homeFragment_to_allInfoFragment, bundle)
            }
        }





    }
    //        val jump = AnimationUtils.loadAnimation(itemView.context, R.anim.jump_and_fade);
    //        itemView.startAnimation(jump)


    class ViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var progress_product_iv:ImageView
        lateinit var tv_name:TextView
        lateinit var iv_photo:ImageView
        lateinit var tv_description:TextView
        lateinit var tv_id:TextView
        init {
            progress_product_iv=itemView.findViewById(R.id.progress_product_iv)
            tv_name=itemView.findViewById(R.id.tv_name)
            iv_photo=itemView.findViewById(R.id.iv_photo)
            tv_description=itemView.findViewById(R.id.tv_description)
            tv_id=itemView.findViewById(R.id.tv_id)
        }

    }
}