package mo.zain.marassi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mo.zain.marassi.R
import mo.zain.marassi.model.Link

class LinksAdapter(val linkedList: List<Link>): RecyclerView.Adapter<LinksAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.links_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentLink:Link=linkedList.get(position)

        holder.tv_name.text=currentLink.name
        holder.tv_url.text=currentLink.url
    }

    override fun getItemCount(): Int {
        return linkedList.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var tv_url:TextView
        lateinit var tv_name:TextView

        init {
            tv_url=itemView.findViewById(R.id.tv_url)
            tv_name=itemView.findViewById(R.id.tv_name)
        }

    }


}