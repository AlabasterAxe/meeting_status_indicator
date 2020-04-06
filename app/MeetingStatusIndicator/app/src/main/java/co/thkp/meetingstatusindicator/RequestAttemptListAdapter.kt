package co.thkp.meetingstatusindicator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.thkp.meetingstatusindicator.model.RequestAttempt

class RequestAttemptListAdapter internal constructor(
    context: Context
): RecyclerView.Adapter<RequestAttemptListAdapter.RequestAttemptViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var requestAttempts = emptyList<RequestAttempt>()

    inner class RequestAttemptViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val requestAttemptItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestAttemptViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return RequestAttemptViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestAttemptViewHolder, position: Int) {
        val current = requestAttempts[position]
        holder.requestAttemptItemView.text = "${current.requestDateTime.hours}:${current.requestDateTime.minutes} ${current.url}: ${current.status}"
    }

    internal fun setRequestAttempts(requestAttempts: List<RequestAttempt>) {
        this.requestAttempts = requestAttempts
        notifyDataSetChanged()
    }

    override fun getItemCount() = requestAttempts.size
}