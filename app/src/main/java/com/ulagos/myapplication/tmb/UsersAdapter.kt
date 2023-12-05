package com.ulagos.myapplication.tmb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ulagos.myapplication.R

class UsersAdapter(
    private var users: List<UserData>,
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idTextView: TextView = view.findViewById(R.id.textViewUserId)
        val nameTextView: TextView = view.findViewById(R.id.textViewUserName)
        val descriptionTextView: TextView = view.findViewById(R.id.textViewUserDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val userView = inflater.inflate(R.layout.item_user, parent, false)
        return ViewHolder(userView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.idTextView.text = user.id.toString()  // Ajustar el texto del ID
        holder.nameTextView.text = user.nombre
        holder.descriptionTextView.text = user.descripcion
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateUsers(newUsers: List<UserData>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
