package iss.nus.edu.sg.appfiles.androidca

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import iss.nus.edu.sg.appfiles.androidca.databinding.FragmentLoadingBinding
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class LoadingFragment : Fragment() {
	private var _binding: FragmentLoadingBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		_binding = FragmentLoadingBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val username = requireActivity().intent.getStringExtra("username") ?: ""
		val score = requireActivity().intent.getIntExtra("score", 0)

		saveUsername(username)
		submitScore(username, score)

		binding.retryButton.setOnClickListener {
			submitScore(username, score)
		}
	}

	private fun saveUsername(username: String) {
		val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
		sharedPref.edit().putString("username", username).apply()
	}

	private fun submitScore(username: String, score: Int) {
		binding.progressBar.visibility = View.VISIBLE
		binding.loadingText.visibility = View.VISIBLE
		binding.errorLayout.visibility = View.GONE

		Thread {
			try {
				val success = postScore(username, score)

				requireActivity().runOnUiThread {
					if (success) {
						findNavController().navigate(R.id.action_loadingFragment_to_leaderboardFragment)
					} else {
						showError("Failed to submit score. Please try again.")
					}
				}
			} catch (e: Exception) {
				requireActivity().runOnUiThread {
					showError("Network error: ${e.message}")
				}
			}
		}.start()
	}


	private fun postScore(username: String, score: Int): Boolean {
		val urlString = "http://10.0.2.2:5119/api/scores/submit"
		val url = URL(urlString)
		val connection = url.openConnection() as HttpURLConnection

		return try {
			connection.requestMethod = "POST"
			connection.setRequestProperty("Content-Type", "application/json")
			connection.doOutput = true

			val json = JSONObject().apply {
				put("username", username)
				put("score", score)
			}.toString()

			connection.outputStream.use {it.write(json.toByteArray())}

			connection.responseCode == HttpURLConnection.HTTP_OK
		} catch (e: Exception) {
			e.printStackTrace()
			false
		}finally {
			connection.disconnect()
		}
	}

	private fun showError(message: String) {
		binding.progressBar.visibility = View.GONE
		binding.loadingText.visibility = View.GONE
		binding.errorLayout.visibility = View.VISIBLE
		binding.errorMessage.text = message
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}