package com.xtravels.service;

import com.xtravels.models.Location;
import com.xtravels.models.PrivacyType;
import com.xtravels.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

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


    public Boolean save(Location location) {
        Optional<Location> existLocation=findLocationByName(location.getName());
        if(existLocation.isEmpty()){
             locationRepository.save(location);
             return  true;
        }else{
            return false;
        }
    }


    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    public  Optional<Location> findLocationByName(String name){
        return locationRepository.findByName(name);
    }


    public Location getLocation(Long id, BindingResult bindingResult){
        Optional<Location> location= findById(id);
        if(location.isEmpty()){
            bindingResult.rejectValue("location","error.location","Provided Location is not available");
            return  null;
        }else{
            return  location.get();
        }

    }


}
