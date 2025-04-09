package com.aurionpro.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "insurance_plan_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePlanDocument {

	@Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "insurance_plan_id", nullable = false)
    private InsurancePlan insurancePlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;
    
    public InsurancePlanDocument(InsurancePlan insurancePlan, DocumentType documentType) {
        this.insurancePlan = insurancePlan;
        this.documentType = documentType;
    }
}
