package org.example.controller;

import org.example.service.SampleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sample-data")
public class SampleDataController {

    @Autowired
    private SampleDataService sampleDataService;

    /**
     * POST /api/sample-data/create - Create sample data
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createSampleData() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (sampleDataService.hasData()) {
                response.put("success", false);
                response.put("message", "🔍 Sample data already exists. Use /clear first if you want to recreate.");
                response.put("totalUsers", sampleDataService.getTotalUsers());
                return ResponseEntity.ok(response);
            }
            
            sampleDataService.createSampleData();
            
            response.put("success", true);
            response.put("message", "✅ Sample data created successfully!");
            response.put("totalUsers", sampleDataService.getTotalUsers());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Error creating sample data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * GET /api/sample-data/summary - Show data summary
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getDataSummary() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            sampleDataService.printDataSummary();
            
            response.put("success", true);
            response.put("message", "📊 Data summary printed to console");
            response.put("totalUsers", sampleDataService.getTotalUsers());
            response.put("hasData", sampleDataService.hasData());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Error getting summary: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * DELETE /api/sample-data/clear - Clear all data
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearAllData() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (!sampleDataService.hasData()) {
                response.put("success", false);
                response.put("message", "🔍 No data to clear. Database is already empty.");
                response.put("totalUsers", 0);
                return ResponseEntity.ok(response);
            }
            
            sampleDataService.clearAllData();
            
            response.put("success", true);
            response.put("message", "🗑️ All data cleared successfully!");
            response.put("totalUsers", sampleDataService.getTotalUsers());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Error clearing data: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * GET /api/sample-data/status - Get current data status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getDataStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            response.put("success", true);
            response.put("hasData", sampleDataService.hasData());
            response.put("totalUsers", sampleDataService.getTotalUsers());
            response.put("message", sampleDataService.hasData() ? 
                "📊 Database contains sample data" : "🔍 Database is empty");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "❌ Error getting status: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
