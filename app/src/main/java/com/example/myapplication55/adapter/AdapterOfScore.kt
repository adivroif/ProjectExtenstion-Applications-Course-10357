package com.example.myapplication55.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication55.interfaces.CallbackOfPlayer
import com.example.myapplication55.models.Player
import com.google.android.material.imageview.ShapeableImageView
import com.example.myapplication55.databinding.HorizontalBinding
import com.google.android.material.textview.MaterialTextView


class AdapterOfScore : RecyclerView.Adapter<AdapterOfScore.PlayerViewHolder>() {
    private var context: Context? = null
    private var players: List<Player>? = null
    var callbackOfPlayer: CallbackOfPlayer? = null



    fun PlayerAdapter(context: Context?, players: List<Player>?) {
        this.context = context
        this.players = players
    }

    fun setPlayerCallback(callbackOfPlayer: CallbackOfPlayer?): AdapterOfScore {
        this.callbackOfPlayer = callbackOfPlayer
        return this
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = HorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return players!!.size
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        with(holder) {
            with(players!![position]) {
                binding.playerRank.text = "#" + (position + 1).toString()
                binding.playerName.text = this.name
                binding.playerScore.text = this.score.toString()
            }

        }
    }

    fun getItem(position: Int): Player {
        return players!![position]
    }

    fun updatePlayerList(newPlayers: List<Player>?) {
        this.players = newPlayers
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(val binding: HorizontalBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.playerCARDData.setOnClickListener {
                if (callbackOfPlayer != null)
                    callbackOfPlayer!!.playerClicked(
                        getItem(adapterPosition),
                        adapterPosition
                    )
            }
        }
    }
}
