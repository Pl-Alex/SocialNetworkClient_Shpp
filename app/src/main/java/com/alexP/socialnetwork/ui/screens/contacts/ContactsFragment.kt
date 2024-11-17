package com.alexP.socialnetwork.ui.screens.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexP.socialnetwork.App
import com.alexP.socialnetwork.R
import com.alexP.socialnetwork.databinding.FragmentContactsBinding
import com.alexP.socialnetwork.ui.base.BaseFragment
import com.alexP.socialnetwork.ui.screens.contacts.adapter.ContactsAdapter
import com.alexP.socialnetwork.ui.screens.contacts.adapter.IContactActionListener
import com.alexP.socialnetwork.utils.SpacingItemDecorator
import com.alexP.socialnetwork.utils.applyWindowInsets
import com.alexp.contactsprovider.Contact
import com.google.android.material.snackbar.Snackbar


class ContactsFragment : BaseFragment<FragmentContactsBinding>() {

    private lateinit var adapter: ContactsAdapter

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

    private val onSaveAction: (Contact) -> Unit = { contact: Contact ->
        vm.addContact(contact)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        supportFragmentManager.fragmentFactory = MyFragmentFactory(onSaveAction)
        binding.root.applyWindowInsets()
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setListeners()
        tryToLoadContactsFromDevice()
    }

    private fun setListeners() {
        binding.addContactButton.setOnClickListener {
            showAddContactDialog()
        }
        binding.topAppBar.setNavigationOnClickListener {
//            startActivity(
//                Intent(this, AuthActivity::class.java),
//                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
//            )
//            finish()
        }
    }

    private fun setRecyclerView() {
        adapter = ContactsAdapter(object : IContactActionListener {
            override fun onContactDelete(contact: Contact) {
                deleteContact(contact)
            }

            override fun onContactDetails(contact: Contact) {
                val bundle = bundleOf(
                    FULL_NAME to contact.fullName,
                    CAREER to contact.career,
                    HOME_ADDRESS to contact.address,
                    PHOTO to contact.photo)
                findNavController().navigate(R.id.action_contactsFragment_to_contactsDetailsFragment, bundle)
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

        val itemAnimator = binding.recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun showAddContactDialog() {
//        val fragmentManager = supportFragmentManager
//        val transaction = fragmentManager.beginTransaction()
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//        transaction
//            .add(android.R.id.content, AddContactFragment::class.java, null)
//            .addToBackStack(null)
//        transaction.commit()
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

    companion object {
        val FULL_NAME = "fullname"
        val CAREER = "career"
        val HOME_ADDRESS = "homeaddress"
        val PHOTO = "photo"
    }

}