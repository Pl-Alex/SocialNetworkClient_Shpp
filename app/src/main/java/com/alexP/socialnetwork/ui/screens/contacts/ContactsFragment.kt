package com.alexP.socialnetwork.ui.screens.contacts

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
import com.alexP.socialnetwork.ui.base.BaseFragment
import com.alexP.socialnetwork.ui.screens.contacts.adapter.ContactsAdapter
import com.alexP.socialnetwork.ui.screens.contacts.adapter.IContactActionListener
import com.alexP.socialnetwork.ui.screens.viewpager.ViewPagerFragmentDirections
import com.alexP.socialnetwork.utils.SpacingItemDecorator
import com.alexP.socialnetwork.utils.applyWindowInsets
import com.alexP.socialnetwork.utils.enableTransitionAnimation
import com.alexp.contactsprovider.Contact
import com.google.android.material.snackbar.Snackbar


class ContactsFragment : BaseFragment<FragmentContactsBinding>() {

    private val vm: ContactsViewModel by viewModels {
        ContactsViewModel.createFactory((requireContext().applicationContext as App).contactService)
    }

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
        savedInstanceState: Bundle?
    ): View {
        enableTransitionAnimation()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)

        binding.root.applyWindowInsets()
        setRecyclerView()
        tryToLoadContactsFromDevice()

        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }

        binding.addContactButton.setOnClickListener {
            findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToAddContactFragment())
        }
    }

    private fun setRecyclerView() {
        val adapter = ContactsAdapter(object : IContactActionListener {
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
        })

        adapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0)
                    binding.recyclerView.scrollToPosition(positionStart)
            }
        })

        vm.contacts.observe(viewLifecycleOwner) {
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
                val contact = vm.contacts.value?.get(viewHolder.adapterPosition)
                contact?.let { deleteContact(contact) }
            }
        }).attachToRecyclerView(binding.recyclerView)

        val layoutManager = LinearLayoutManager(context)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            SpacingItemDecorator(
                resources.getDimensionPixelSize(R.dimen.contacts_recyclerView_horizontal_spacing),
                resources.getDimensionPixelSize(R.dimen.contacts_recyclerView_vertical_spacing)
            )
        )
    }


    private fun deleteContact(contact: Contact) {
        vm.deleteContact(contact)

        val snackbar = Snackbar.make(
            binding.root,
            getString(R.string.contact_deleted),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(getString(R.string.undo)) {
            vm.recoverContacts()
        }
        snackbar.show()
    }

    private fun loadContactsFromDevice() {
        vm.addContacts(requireContext().contentResolver)
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