package com.aurionpro.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cities")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
	
	@Column
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cityId;
	
	@Column(nullable = false)
	private String cityName;
	
	@ManyToOne
	@JoinColumn(name = "state_id", nullable = false)
	private State state;
	
	@Column(nullable = false)
	private boolean isDeleted = false;

}
