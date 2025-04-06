package com.aurionpro.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.StateRequestDTO;
import com.aurionpro.app.dto.StateResponseDTO;
import com.aurionpro.app.entity.State;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.StateRepository;

@Service
public class StateServiceImpl implements StateService{
	
	@Autowired
	private StateRepository stateRepository;
	
	private ModelMapper modelMapper;
	
	public StateServiceImpl() {
		this.modelMapper = new ModelMapper();
	}

	@Override
	public StateResponseDTO addState(StateRequestDTO requestDto) {
		State state = modelMapper.map(requestDto, State.class);
        State savedState = stateRepository.save(state);
        return modelMapper.map(savedState, StateResponseDTO.class);
	}

	@Override
	public List<StateResponseDTO> getAllStates() {
		List<State> states = stateRepository.findAll();
        return states.stream()
                .map(state -> modelMapper.map(state, StateResponseDTO.class))
                .collect(Collectors.toList());
	}

	@Override
	public void deleteState(int id) {
		State state = stateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"State with id:" + id + " not found"));
		stateRepository.delete(state);
	}

}
