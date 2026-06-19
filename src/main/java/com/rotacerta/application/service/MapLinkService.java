package com.rotacerta.application.service;

import com.rotacerta.infrastructure.solver.model.Location;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapLinkService {

    public String generateGoogleMapsUrl(Location origin, List<Location> waypoints) {
        if (waypoints == null || waypoints.isEmpty()) return null;

        StringBuilder url = new StringBuilder("https://www.google.com/maps/dir/?api=1");
        
        // Origem
        url.append("&origin=").append(origin.getLatitude()).append(",").append(origin.getLongitude());
        
        // Destino (último ponto ou volta para a base)
        Location destination = waypoints.get(waypoints.size() - 1);
        url.append("&destination=").append(destination.getLatitude()).append(",").append(destination.getLongitude());

        // Waypoints (todos exceto o último, se houver mais de 1)
        if (waypoints.size() > 1) {
            url.append("&waypoints=");
            String waypointStr = waypoints.subList(0, waypoints.size() - 1).stream()
                    .map(loc -> loc.getLatitude() + "," + loc.getLongitude())
                    .collect(Collectors.joining("|"));
            url.append(waypointStr);
        }

        url.append("&travelmode=driving");
        
        return url.toString();
    }

    public String generateWazeUrl(Location destination) {
        if (destination == null) return null;
        // Waze suporta melhor apenas um destino por link
        return String.format("https://waze.com/ul?ll=%f,%f&navigate=yes", 
                destination.getLatitude(), destination.getLongitude());
    }
}
