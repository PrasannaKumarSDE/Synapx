package com.example.demo.model;



import jakarta.persistence.*;

@Entity
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyNumber;
    private String policyholderName;
    private String dateOfLoss;
    private String location;
    private String assetType;
    private Double estimatedDamage;
    private String claimType;
    private String recommendedRoute;

    @Column(length = 1000)
    private String reasoning;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPolicyholderName() {
		return policyholderName;
	}

	public void setPolicyholderName(String policyholderName) {
		this.policyholderName = policyholderName;
	}

	public String getDateOfLoss() {
		return dateOfLoss;
	}

	public void setDateOfLoss(String dateOfLoss) {
		this.dateOfLoss = dateOfLoss;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Double getEstimatedDamage() {
		return estimatedDamage;
	}

	public void setEstimatedDamage(Double estimatedDamage) {
		this.estimatedDamage = estimatedDamage;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getRecommendedRoute() {
		return recommendedRoute;
	}

	public void setRecommendedRoute(String recommendedRoute) {
		this.recommendedRoute = recommendedRoute;
	}

	public String getReasoning() {
		return reasoning;
	}

	public void setReasoning(String reasoning) {
		this.reasoning = reasoning;
	}

    // getters and setters
}
