package database.kotlin.flow9.net.kotlindatabasebasic

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_user.*
import java.util.ArrayList

class UserAdapter(private val context: Context?, private var userList: ArrayList<User>?): RecyclerView.Adapter<UserAdapter.Holder>() {

    fun updateData(updatedList: ArrayList<User>?) {
        userList = updatedList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int = userList?.size?:0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(userList?.get(position))
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id = itemView.findViewById(R.id.id) as TextView
        var name = itemView.findViewById(R.id.name) as TextView
        var password = itemView.findViewById(R.id.password) as TextView
        var age = itemView.findViewById(R.id.age) as TextView

        fun bind(user: User?) {
            id.text = user?.id
            name.text = user?.name
            password.text = user?.password
            age.text = user?.age.toString()
        }
    }
}