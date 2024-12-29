package com.alexP.socialnetwork.presentation.main.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexP.socialnetwork.App
import com.alexP.socialnetwork.R
import com.alexP.socialnetwork.databinding.FragmentContactsBinding
import com.alexP.socialnetwork.presentation.base.BaseFragment
import com.alexP.socialnetwork.presentation.main.contacts.adapter.ContactsAdapter
import com.alexP.socialnetwork.presentation.main.contacts.adapter.IContactActionListener
import com.alexP.socialnetwork.presentation.main.viewpager.ViewPagerFragmentDirections
import com.alexP.socialnetwork.utils.SpacingItemDecorator
import com.alexP.socialnetwork.utils.applyWindowInsets
import com.alexP.socialnetwork.utils.enableTransitionAnimation
import com.alexp.contactsprovider.Contact
import com.google.android.material.snackbar.Snackbar


class ContactsFragment : BaseFragment<FragmentContactsBinding>() {

    private val viewModel: ContactsViewModel by viewModels {
        ContactsViewModel.createFactory((requireContext().applicationContext as App).contactService)
    }

    private lateinit var adapter: ContactsAdapter
    private var isSelectionMode = false

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                loadContactsFromDevice()
            } else {
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    getString(R.string.permission_not_granted),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentContactsBinding {
        return FragmentContactsBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        enableTransitionAnimation()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            root.applyWindowInsets()
            setRecyclerView()
            tryToLoadContactsFromDevice()
            recyclerView.doOnPreDraw {
                startPostponedEnterTransition()
            }
            setListeners()
        }
    }

    private fun FragmentContactsBinding.setListeners() {
        addContactButton.setOnClickListener {
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToAddContactFragment()
            if (findNavController().currentDestination?.getAction(action.actionId) != null) {
                findNavController().navigate(action)
            }
        }

        deleteContactsButton.setOnClickListener {
            deleteContacts()
        }
    }

    private fun setRecyclerView() {
        val rv = binding.recyclerView
        adapter = ContactsAdapter(object : IContactActionListener {
            override fun onContactDelete(contact: Contact) {
                deleteContact(contact)
            }

            override fun onContactDetails(contact: Contact, imageView: ImageView) {
                val action =
                    ViewPagerFragmentDirections.actionViewPagerFragmentToContactsDetailsFragment(
                        contact.photo,
                        contact.fullName,
                        contact.address,
                        contact.career
                    )
                val extras = FragmentNavigatorExtras(imageView to "contacts_details")
                findNavController().navigate(action, extras)
            }

            override fun onContactSelect(contact: Contact) {
                toggleSelection(contact)
            }
        })

        adapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0)
                    rv.scrollToPosition(positionStart)
            }
        })

        viewModel.contacts.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val contact = viewModel.contacts.value?.get(viewHolder.bindingAdapterPosition)
                contact?.let { deleteContact(contact) }
            }

            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                return if (isSelectionMode) {
                    0
                } else {
                    super.getSwipeDirs(recyclerView, viewHolder)
                }
            }
        }).attachToRecyclerView(rv)

        val layoutManager = LinearLayoutManager(context)

        rv.layoutManager = layoutManager
        rv.adapter = adapter
        rv.addItemDecoration(
            SpacingItemDecorator(
                resources.getDimensionPixelSize(R.dimen.contacts_recyclerView_horizontal_spacing),
                resources.getDimensionPixelSize(R.dimen.contacts_recyclerView_vertical_spacing)
            )
        )
    }

    private fun toggleSelection(contact: Contact) {
        adapter.toggleSelection(contact)
        val selectedCount = adapter.getSelectedContacts().size
        isSelectionMode = selectedCount > 0
        if (isSelectionMode) {
            binding.deleteContactsButton.visibility = View.VISIBLE
        } else {
            binding.deleteContactsButton.visibility = View.GONE
        }
    }

    private fun deleteContact(contact: Contact) {
        viewModel.deleteContact(contact)

        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.contact_deleted),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(getString(R.string.undo)) {
            viewModel.recoverContacts()
        }
        snackbar.show()
    }

    private fun deleteContacts() {
        viewModel.deleteContacts(adapter.getSelectedContacts())
        isSelectionMode = false
        adapter.clearSelection()
        binding.deleteContactsButton.visibility = View.GONE
        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.contacts_deleted),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(getString(R.string.undo)) {
            viewModel.recoverContacts()
        }
        snackbar.show()
    }

    private fun loadContactsFromDevice() {
        viewModel.addContacts(requireContext().contentResolver)
    }

    private fun tryToLoadContactsFromDevice() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ),
                -> {
                loadContactsFromDevice()
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_CONTACTS
                )
            }
        }
    }

}