package com.example.myapplication55.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication55.R
import com.example.myapplication55.adapter.AdapterOfScore
import com.example.myapplication55.interfaces.CallbackOfPlayer
import com.example.myapplication55.interfaces.Callback_HighScore_Item_Clicked
import com.example.myapplication55.models.Player
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.jetbrains.annotations.Nullable


class HighScore_Fragment : Fragment() {

    private lateinit var high_score_text: MaterialTextView

    private lateinit var main_LST_scores: RecyclerView

    private lateinit var return_to_menu: MaterialButton

    private var list_of_players = ArrayList<Player>()

    private var adapter: AdapterOfScore = AdapterOfScore()

    var callbackHighScoreItemClicked: Callback_HighScore_Item_Clicked? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_high_score_, container, false)
        findViews(view)
        return view
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    fun setCallBackConnectLocationToPlayer(callback_HighScore_Item_Clicked: Callback_HighScore_Item_Clicked) {
        this.callbackHighScoreItemClicked = callback_HighScore_Item_Clicked
    }

    private fun initViews() {
        if (list_of_players.isNullOrEmpty()) {
            if (high_score_text != null)
                high_score_text.visibility = View.VISIBLE
            if (main_LST_scores != null)
                main_LST_scores.visibility = View.GONE
        } else {
            if (high_score_text != null) {
                high_score_text.setVisibility(View.GONE);
            }
            if (main_LST_scores != null) {
                main_LST_scores.setVisibility(View.VISIBLE);
            }

            adapter = AdapterOfScore()
            adapter.PlayerAdapter(context, list_of_players)
            adapter.callbackOfPlayer = object : CallbackOfPlayer {
                override fun playerClicked(player: Player, position: Int) {
                    itemClicked(player.lat, player.lng)
                }
            }
            var linearManager = LinearLayoutManager(this.getContext())
            linearManager.setOrientation(LinearLayoutManager.VERTICAL)
            main_LST_scores.setLayoutManager(linearManager)
            main_LST_scores.setAdapter(adapter)
            adapter.setPlayerCallback(object : CallbackOfPlayer {
                override fun playerClicked(player: Player, position: Int) {
                    itemClicked(player.lat, player.lng)
                }

            })
        }
    }

    private fun itemClicked(lat: Double, lng: Double) {
        callbackHighScoreItemClicked?.highScoreItemClicked(lat, lng)
    }

    private fun findViews(view: View) {
        high_score_text = view.findViewById(R.id.high_score_text)
        main_LST_scores = view.findViewById(R.id.main_LST_scores)
    }

    fun setPlayerList(playerList: List<Player>) {
        this.list_of_players = playerList as ArrayList<Player>
        if (adapter != null) {
            adapter.updatePlayerList(this.list_of_players);
        }
    }
}
