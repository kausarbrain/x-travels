package com.xtravels.service;

import com.xtravels.models.Location;
import com.xtravels.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    public List<Location> getAllLocationOrderByNameAsc() {
        return locationRepository.findAllByOrderByNameAsc();
    }


    public Location save(Location location) {
        return locationRepository.save(location);
    }


    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    public  Optional<Location> findLocationByName(String name){
        return locationRepository.findByName(name);
    }

}
