package com.e.smashbrostrivia

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.e.smashbrostrivia.databinding.FragmentGameWonBinding

/**
 * A simple [Fragment] subclass.
 */
class GameWonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentGameWonBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_won, container, false)
        binding.nextMatchButton.setOnClickListener { view ->
            view.findNavController().navigate(GameWonFragmentDirections.actionGameWonFragmentToGameFragment())
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.winning_screen_menu, menu)

        // Check if the intent resolves to an activity
        if (null == activity?.packageManager?.let { makeShareIntent().resolveActivity(it) }) {
            menu.findItem(R.id.sharing_menu).isVisible = false
        }
    }

    private fun makeShareIntent(): Intent {
        val args = arguments?.let { GameWonFragmentArgs.fromBundle(it) }
        val shareIntent = Intent(Intent.ACTION_SEND)
        if (args != null) {
            shareIntent
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.share_message, args.numberCorrect, args.numberOfQuestions))
        }

        return shareIntent
    }

    private fun shareSuccess() {
        return startActivity(makeShareIntent())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sharing_menu -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}
