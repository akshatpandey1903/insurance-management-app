package com.aurionpro.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.CityRequestDTO;
import com.aurionpro.app.dto.CityResponseDTO;
import com.aurionpro.app.entity.City;
import com.aurionpro.app.entity.State;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.CityRepository;
import com.aurionpro.app.repository.StateRepository;

@Service
public class CityServiceImpl implements CityService{
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	private ModelMapper modelMapper;
	
	public CityServiceImpl() {
		this.modelMapper = new ModelMapper();
	}

	@Override
	public CityResponseDTO addCity(CityRequestDTO requestDto) {
		State state = stateRepository.findById(requestDto.getStateId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"State with id:" + requestDto.getStateId() + " not found"));

        City city = new City();
        city.setCityName(requestDto.getCityName());
        city.setState(state);

        City savedCity = cityRepository.save(city);
        return modelMapper.map(savedCity, CityResponseDTO.class);
	}

	@Override
	public List<CityResponseDTO> getCitiesByStateId(int stateId) {
		List<City> cities = cityRepository.findByStateStateIdAndIsDeletedFalse(stateId);
        return cities.stream()
                .map(city -> modelMapper.map(city, CityResponseDTO.class))
                .collect(Collectors.toList());
	}

	@Override
	public void deleteCity(int id) {
		// TODO Auto-generated method stub
		City city = cityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"City with id:" + id + " not found"));
		city.setDeleted(true);
		cityRepository.save(city);
	}

}
