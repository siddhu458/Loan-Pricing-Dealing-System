package com.example.demo.services;

import org.springframework.stereotype.Service;

@Service
public class PricingService {

	public double calculateInterestRate(
			double baseRate,
			double requestedAmount,
			int tenureMonths,
			String creditRating
			) {
		double riskPremium = getRiskPremium(creditRating);
		double tenureAdjustment = tenureMonths > 36 ? 0.75 : 0.35;
		double sizeAdjustment = requestedAmount > 5_00_00_000 ? 0.50 : 0.25;
		
		return baseRate + riskPremium + tenureAdjustment + sizeAdjustment;
	}
	
	private double getRiskPremium(String rating) {
		return switch(rating) {
		case "AAA" -> 0.25;
		case "AA" -> 0.50;
		case "A" -> 0.75;
		case "BBB" -> 1.25;
		default -> 1.75;
		};
	}
}
