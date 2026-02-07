package com.example.demo.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ClaimService {

    public Map<String, Object> processClaim(MultipartFile file) throws Exception {

        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        document.close();

        Map<String, String> extracted = new HashMap<>();
        List<String> missing = new ArrayList<>();

        // ---------------- Policy Information ----------------
        extracted.put("policyNumber", extract(text, "POLICY NUMBER"));
        extracted.put("policyholderName", extract(text, "NAME OF INSURED"));

        // ---------------- Incident Information ----------------
        extracted.put("incidentDate", extract(text, "DATE OF LOSS"));
        extracted.put("incidentTime", extract(text, "TIME"));
        extracted.put("incidentLocation", extract(text, "LOCATION OF LOSS"));
        extracted.put("incidentDescription", extract(text, "DESCRIPTION OF ACCIDENT"));

        // ---------------- Asset Details ----------------
        extracted.put("assetId", extract(text, "V.I.N."));
        extracted.put("estimatedDamage", extractAmount(text, "ESTIMATE AMOUNT"));
        extracted.put("assetType", "Vehicle");

        // ---------------- Derived Fields ----------------
        String claimType = text.toUpperCase().contains("INJURED") ? "injury" : "auto";
        extracted.put("claimType", claimType);
        extracted.put("attachments", "no");
        extracted.put("initialEstimate", extracted.get("estimatedDamage"));

        // ---------------- Mandatory Check ----------------
        List<String> mandatory = List.of(
                "policyNumber",
                "policyholderName",
                "incidentDate",
                "incidentLocation",
                "estimatedDamage",
                "claimType"
        );

        for (String field : mandatory) {
            if (extracted.get(field) == null || extracted.get(field).isEmpty()) {
                missing.add(field);
            }
        }

        // ---------------- Routing Rules ----------------
        String route;
        String reasoning;

        if (!missing.isEmpty()) {
            route = "Manual Review";
            reasoning = "Missing mandatory fields: " + missing;

        } else if (containsFraudKeywords(text)) {
            route = "Investigation Flag";
            reasoning = "Description contains fraud-related keywords.";

        } else if ("injury".equalsIgnoreCase(claimType)) {
            route = "Specialist Queue";
            reasoning = "Claim type is injury.";

        } else {
            double damage = Double.parseDouble(extracted.get("estimatedDamage"));
            if (damage < 25000) {
                route = "Fast-track";
                reasoning = "Estimated damage below ₹25,000.";
            } else {
                route = "Manual Review";
                reasoning = "Estimated damage exceeds ₹25,000.";
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("extractedFields", extracted);
        response.put("missingFields", missing);
        response.put("recommendedRoute", route);
        response.put("reasoning", reasoning);

        return response;
    }

    // ================= HELPERS =================

    private String extract(String text, String key) {
        String[] lines = text.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].toUpperCase().contains(key)) {
                for (int j = i + 1; j < lines.length; j++) {
                    String value = lines[j].trim();
                    if (!value.isEmpty()) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    private String extractAmount(String text, String key) {
        String value = extract(text, key);
        if (value == null) return null;
        value = value.replaceAll("[^0-9.]", "");
        return value.isEmpty() ? null : value;
    }

    private boolean containsFraudKeywords(String text) {
        String t = text.toLowerCase();
        return t.contains("fraud") || t.contains("staged") || t.contains("inconsistent");
    }
}
