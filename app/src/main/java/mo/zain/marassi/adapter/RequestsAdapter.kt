package mo.zain.marassi.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import mo.zain.marassi.R
import mo.zain.marassi.model.DataXX

class RequestsAdapter(val listRequests:ArrayList<DataXX>): RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestsAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.request_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: RequestsAdapter.ViewHolder, position: Int) {

        val currentData:DataXX =listRequests.get(position)

        val jump = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.jump_and_fade);
        holder.progress_product_iv.startAnimation(jump)

        holder.tv_product_price.text=currentData.seaport_name
        holder.tv_name.text=currentData.msg;
        holder.tv_description.text=currentData.detail
        holder.tv_id.setText(currentData.days.toString()+" D")


        Glide.with(holder.itemView.context).load(currentData.request_file)
            .listener(object : RequestListener<Drawable?> {
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


    }

    override fun getItemCount(): Int {
        return listRequests.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         var progress_product_iv:ImageView
         var iv_product_to_cart:ImageView
         var tv_product_price:TextView
         var tv_name:TextView
         var iv_photo:ImageView
         var tv_description:TextView
         var tv_id:TextView

        init {
            progress_product_iv=itemView.findViewById(R.id.progress_product_iv)
            iv_product_to_cart=itemView.findViewById(R.id.iv_product_to_cart)
            tv_product_price=itemView.findViewById(R.id.tv_product_price)
            tv_name=itemView.findViewById(R.id.tv_name)
            iv_photo=itemView.findViewById(R.id.iv_photo)
            tv_description=itemView.findViewById(R.id.tv_description)
            tv_id=itemView.findViewById(R.id.tv_id)
        }

    }

}