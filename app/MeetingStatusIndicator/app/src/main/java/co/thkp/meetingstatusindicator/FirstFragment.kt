package co.thkp.meetingstatusindicator

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.thkp.meetingstatusindicator.viewmodel.RequestAttemptViewModel
import kotlinx.android.synthetic.main.fragment_first.*

val ENDPOINT_KEY = "co.thkp.meetingstatusindicator.ENDPOINT"

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val requestAttemptViewModel: RequestAttemptViewModel by activityViewModels<RequestAttemptViewModel>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = activity?.getSharedPreferences("prefs", MODE_PRIVATE)
        val endpoint = preferences?.getString(ENDPOINT_KEY, ENDPOINT);
        val endpointField = view.findViewById<EditText>(R.id.endpoint)
        endpointField.setText(endpoint);

        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
          preferences?.edit()?.putString(ENDPOINT_KEY, endpointField.text.toString())?.apply()
        }

        val rv = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RequestAttemptListAdapter(view.context);
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(view.context)

        requestAttemptViewModel.allRequests.observe(viewLifecycleOwner, Observer { requests -> requests?.let { adapter.setRequestAttempts(requests)} })
    }
}
