package com.ecommerce.service_inventaire;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final String FILE_PATH = "stock.json"; 
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Product> database = new HashMap<>();

    // FLecture fichier JSON
    private void chargerBaseDeDonnees() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                database = objectMapper.readValue(file, new TypeReference<Map<String, Product>>(){});
            }
        } catch (IOException e) {
            System.out.println("❌ Erreur de lecture : " + e.getMessage());
        }
    }

    // Sauvegarde du JSON
    private void sauvegarderBaseDeDonnees() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), database);
        } catch (IOException e) {
            System.out.println("❌ Erreur d'écriture : " + e.getMessage());
        }
    }

    //Renvoie le catalogue pour l'afficher
    @GetMapping("/catalog")
    public ResponseEntity<Map<String, Product>> getCatalog() {
        chargerBaseDeDonnees();
        return ResponseEntity.ok(database);
    }

    // Traitement d'un achat
    @GetMapping("/{productId}")
    public ResponseEntity<StockResponse> processPurchase(@PathVariable String productId) {
        chargerBaseDeDonnees(); 
        
        boolean isAvailable = false;
        int currentStock = 0;

        if (database.containsKey(productId)) {
            Product p = database.get(productId);
            currentStock = p.getStock();
            
            // Si en stock, on valide et on retire 1
            if (currentStock > 0) {
                p.setStock(currentStock - 1);
                isAvailable = true;
                sauvegarderBaseDeDonnees(); // On met à jour le fichier
            }
        }
        
        return ResponseEntity.ok(new StockResponse(productId, isAvailable));
    }
}


class Product {
    private String name;
    private double price;
    private int stock;
    private String image; 

    public Product() {}
    
    public Product(String name, double price, int stock, String image) { 
        this.name = name; 
        this.price = price; 
        this.stock = stock; 
        this.image = image;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}

class StockResponse {
    public String productId;
    public boolean isAvailable;
    public StockResponse(String productId, boolean isAvailable) { this.productId = productId; this.isAvailable = isAvailable; }
}