package com.alexP.socialnetwork.ui.screens.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexP.socialnetwork.R
import com.alexP.socialnetwork.databinding.ItemContactBinding
import com.alexP.socialnetwork.utils.loadCircularImage
import com.alexp.contactsprovider.Contact

class ContactsAdapter(
    private val userActionListener: IContactActionListener,
) : ListAdapter<Contact, ContactsAdapter.ContactsViewHolder>(ContactsDiffCallback()) {

    private val selectedContacts = mutableSetOf<Contact>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactBinding.inflate(inflater, parent, false)

        return ContactsViewHolder(binding, userActionListener, selectedContacts)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun toggleSelection(contact: Contact) {
        if (selectedContacts.contains(contact)) {
            selectedContacts.remove(contact)
            if (selectedContacts.isEmpty()) {
                notifyDataSetChanged()
            } else {
                notifyItemChanged(currentList.indexOf(contact))
            }
        } else {
            val wasEmpty = selectedContacts.isEmpty()
            selectedContacts.add(contact)
            if (wasEmpty) {
                notifyDataSetChanged()
            } else {
                notifyItemChanged(currentList.indexOf(contact))
            }
        }
    }

    fun clearSelection() {
        selectedContacts.clear()
        notifyDataSetChanged()
    }

    fun getSelectedContacts(): List<Contact> = selectedContacts.toList()

    class ContactsViewHolder(
        private val binding: ItemContactBinding,
        private val userActionListener: IContactActionListener,
        private val selectedContacts: MutableSet<Contact>,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            with(binding) {
                textViewContactFullName.text = contact.fullName
                textViewContactCareer.text = contact.career
                imageViewContactImage.loadCircularImage(contact.photo)
                imageViewContactImage.transitionName = "imageview_contact" + contact.id.toString()

                if (selectedContacts.isNotEmpty()) {
                    root.background = ContextCompat.getDrawable(
                        root.context,
                        R.drawable.item_contact_multiselect_frame
                    )
                    buttonTrash.visibility = android.view.View.GONE
                    radioButtonContact.visibility = android.view.View.VISIBLE
                    radioButtonContact.isChecked = selectedContacts.contains(contact)
                } else {
                    root.background = ContextCompat.getDrawable(
                        root.context,
                        R.drawable.item_contact_frame
                    )
                    buttonTrash.visibility = android.view.View.VISIBLE
                    radioButtonContact.visibility = android.view.View.GONE
                }
                setListeners(contact)
            }
        }

        private fun setListeners(contact: Contact) {
            binding.root.setOnLongClickListener {
                userActionListener.onContactSelect(contact)
                true
            }
            binding.root.setOnClickListener {
                if (selectedContacts.isNotEmpty()) {
                    userActionListener.onContactSelect(contact)
                } else {
                    userActionListener.onContactDetails(contact, binding.imageViewContactImage)
                }
            }
            binding.buttonTrash.setOnClickListener {
                userActionListener.onContactDelete(contact)
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

}

