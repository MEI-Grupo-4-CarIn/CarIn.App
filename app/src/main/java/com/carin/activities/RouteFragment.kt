package com.carin.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carin.R
import com.carin.adapter.RouteAdapter

class RouteFragment : Fragment() {

    private lateinit var adapter: RouteAdapter
    private lateinit var route : List<RouteFragment.Route>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.route_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.addItemDecoration(ItemSpacingDecoration(5))

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        route = getRoutes()

        adapter = RouteAdapter(route)
        recyclerView.adapter = adapter
    }


    private fun getRoutes(): List<RouteFragment.Route> {

        val routes = mutableListOf<RouteFragment.Route>()
        routes.add(RouteFragment.Route("Porto","Luxembourg", "Bruno Ferreira", "19H10m", "12 Fevereiro", "INFINITY Vision Qe", "1896km"))
        routes.add(RouteFragment.Route("Porto","Luxembourg", "Bruno Ferreira", "19H10m", "12 Fevereiro", "INFINITY Vision Qe", "1896km"))
        routes.add(RouteFragment.Route("Porto","Luxembourg", "Bruno Ferreira", "19H10m", "12 Fevereiro", "INFINITY Vision Qe", "1896km"))
        routes.add(RouteFragment.Route("Porto","Luxembourg", "Bruno Ferreira", "19H10m", "12 Fevereiro", "INFINITY Vision Qe", "1896km"))
        routes.add(RouteFragment.Route("Porto","Luxembourg", "Bruno Ferreira", "19H10m", "12 Fevereiro", "INFINITY Vision Qe", "1896km"))

        return routes
    }

    data class Route(val origin: String, val destination: String,  val driver: String, val hours: String, val departureDate: String, val vehicle: String, val km: String)
}