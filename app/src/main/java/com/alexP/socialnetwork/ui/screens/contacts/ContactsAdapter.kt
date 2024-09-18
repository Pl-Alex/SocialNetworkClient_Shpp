package com.alexP.socialnetwork.ui.screens.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexP.socialnetwork.databinding.ItemContactBinding

class ContactsAdapter(
) : ListAdapter<Contact, ContactsAdapter.ContactsViewHolder>(ContactsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)

        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ContactsViewHolder(
        private val binding: ItemContactBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            with(binding) {
                textViewContactFullName.text = contact.fullName
                textViewContactCareer.text = contact.career
            }
        }
    }
}

class ContactsDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

}