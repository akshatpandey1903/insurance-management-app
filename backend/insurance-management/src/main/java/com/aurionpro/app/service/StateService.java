package com.aurionpro.app.service;

import java.util.List;

import com.aurionpro.app.dto.StateRequestDTO;
import com.aurionpro.app.dto.StateResponseDTO;

public interface StateService {
	StateResponseDTO addState(StateRequestDTO requestDto);
    List<StateResponseDTO> getAllStates();
    void deleteState(int id);
}
