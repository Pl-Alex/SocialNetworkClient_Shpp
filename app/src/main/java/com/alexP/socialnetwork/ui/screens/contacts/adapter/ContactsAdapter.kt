package com.alexP.socialnetwork.ui.screens.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexP.socialnetwork.databinding.ItemContactBinding
import com.alexP.socialnetwork.utils.loadCircularImage
import com.alexp.contactsprovider.Contact

class ContactsAdapter(
    private val userActionListener: IContactActionListener,
) : ListAdapter<Contact, ContactsAdapter.ContactsViewHolder>(ContactsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)

        return ContactsViewHolder(binding, userActionListener)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ContactsViewHolder(
        private val binding: ItemContactBinding,
        private val userActionListener: IContactActionListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            with(binding) {
                textViewContactFullName.text = contact.fullName
                textViewContactCareer.text = contact.career
                imageViewContactImage.loadCircularImage(contact.photo)
                imageViewContactImage.transitionName = IMAGEVIEW_TRANSITION_NAME_PREFIX + contact.id.toString()

                binding.root.setOnClickListener {
                    userActionListener.onContactDetails(contact, binding.imageViewContactImage)
                }
                binding.buttonTrash.setOnClickListener {
                    userActionListener.onContactDelete(contact)
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

    companion object{
        const val IMAGEVIEW_TRANSITION_NAME_PREFIX = "imageview_contact"
    }
}

