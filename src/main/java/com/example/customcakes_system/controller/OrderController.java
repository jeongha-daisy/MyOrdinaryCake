package com.example.customcakes_system.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// REST API 컨트롤러임을 명시
@RestController
@Slf4j

public class OrderController {

    @PersistenceContext
    private EntityManager entityManager;
    @PostMapping("/api/order/create")
    @Transactional
    public ResponseEntity<?> createOrder(
            @RequestParam("cake_design") String cake_design,
            @RequestParam(value = "cake_image", required = false) MultipartFile cake_image,
            @RequestParam("cake_sheet") String cake_sheet,
            @RequestParam("cake_cream") String cake_cream,
            @RequestParam("cake_size") String cake_size,
            @RequestParam("customer_id") String customer_id,
            @RequestParam("customer_name") String customer_name,
            @RequestParam("customer_number") String customer_number,
            @RequestParam("deposit_name") String deposit_name,
            @RequestParam("order_state") String order_state,
            @RequestParam("order_date") String order_date,
            @RequestParam("pick_up_date") String pick_up_date,
            @RequestParam("pickup_time") String pickup_time,
            @RequestParam("store_id") String store_id,
            @RequestParam("cake_price") String cake_price,
            @RequestParam("bank_account") String bank_account
    ) {
        try {
            String imageUrl = null;

            // 파일이 있을 때만 저장
            if (cake_image != null && !cake_image.isEmpty()) {
                String originalFilename = cake_image.getOriginalFilename();
                String filename = customer_id + "_" + originalFilename;
                String filePath = System.getProperty("user.dir") + "/upload/images";
                Path saveDir = Paths.get(filePath);
                Path savePath = saveDir.resolve(filename);

                if (!Files.exists(saveDir)) {
                    Files.createDirectories(saveDir);
                }

                cake_image.transferTo(savePath.toFile());
                imageUrl = "/images/" + filename;
            }

            String sql = "INSERT INTO orders " +
                    "(cake_design, cake_image, cake_sheet, cake_cream, cake_size, customer_id, customer_name, customer_number, deposit_name, order_state, order_date, pick_up_date, pickup_time, store_id, cake_price, bank_account) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, cake_design);
            query.setParameter(2, imageUrl);  // imageUrl이 null이라도 DB에 null 저장됨
            query.setParameter(3, cake_sheet);
            query.setParameter(4, cake_cream);
            query.setParameter(5, cake_size);
            query.setParameter(6, customer_id);
            query.setParameter(7, customer_name);
            query.setParameter(8, customer_number);
            query.setParameter(9, deposit_name);
            query.setParameter(10, order_state);
            query.setParameter(11, order_date);
            query.setParameter(12, pick_up_date);
            query.setParameter(13, pickup_time);
            query.setParameter(14, store_id);
            query.setParameter(15, cake_price);
            query.setParameter(16, bank_account);

            int result = query.executeUpdate();

            return (result > 0) ? ResponseEntity.ok("OK") : ResponseEntity.ok("NOT");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
        } catch (Exception e) {
            log.error("Insert error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Insert Failed");
        }
    }


    @PostMapping("/api/order/update/user")
    @Transactional
    public ResponseEntity<?> update(@RequestBody Map<String, String> params,
                                    HttpServletResponse res) {

        String order_id = params.get("order_id");

        String sql = "SELECT * FROM orders where id = ?";
        try{
            Query query  = entityManager.createNativeQuery(sql);
            query.setParameter(1, order_id);
            List<Object[]> results = query.getResultList();

            if (results.isEmpty()){
                return ResponseEntity.ok("NOT");
            } else{
                String sql2 = "update orders set order_state = ? where id = ?";
                Query query2  = entityManager.createNativeQuery(sql2);
                query2.setParameter(1, "취소");
                query2.setParameter(2, order_id);

                int result = query2.executeUpdate();


                if (result > 0){
                    return ResponseEntity.ok("UPDATEOK");
                } else {
                    return ResponseEntity.ok("NOTUPDATE");
                }
            }

        } catch (Exception e){
            log.error("Insert error: ", e);
            return ResponseEntity.internalServerError().body("Insert Failed");
        }
    }

    @PostMapping("/api/order/update/store")
    @Transactional
    public ResponseEntity<?> update_store(@RequestBody Map<String, String> params,
                                    HttpServletResponse res) {

        String order_id = params.get("order_id");
        String order_state = params.get("order_state");

        String sql = "SELECT * FROM orders where id = ?";
        try{
            Query query  = entityManager.createNativeQuery(sql);
            query.setParameter(1, order_id);
            List<Object[]> results = query.getResultList();

            if (results.isEmpty()){
                return ResponseEntity.ok("NOT");
            } else{
                // 승인이라면 order_state
                if ("승인".equals(order_state)) {
                    String sql2 = "update orders set order_state = ?, cake_price = ?, bank_account = ? where id = ?";
                    Query query2  = entityManager.createNativeQuery(sql2);
                    String cake_price = params.get("cake_price");
                    String bank_account = params.get("bank_account");
                    query2.setParameter(1, order_state);
                    query2.setParameter(2, cake_price);
                    query2.setParameter(3, bank_account);
                    query2.setParameter(4, order_id);

                    int result = query2.executeUpdate();
                    if (result > 0){
                        return ResponseEntity.ok("UPDATEOK");
                    } else {
                        return ResponseEntity.ok("NOTUPDATE");
                    }
                }
                else {
                    String sql2 = "update orders set order_state = ? where id = ?";
                    Query query2  = entityManager.createNativeQuery(sql2);
                    query2.setParameter(1, order_state);
                    query2.setParameter(2, order_id);

                    int result = query2.executeUpdate();
                    if (result > 0){
                        return ResponseEntity.ok("UPDATEOK");
                    } else {
                        return ResponseEntity.ok("NOTUPDATE");
                    }
                }
            }

        } catch (Exception e){
            log.error("Insert error: ", e);
            return ResponseEntity.internalServerError().body("Insert Failed");
        }
    }
}
