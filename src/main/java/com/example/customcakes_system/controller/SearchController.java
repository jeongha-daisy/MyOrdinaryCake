package com.example.customcakes_system.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// REST API 컨트롤러임을 명시
@RestController
@Slf4j

public class SearchController {

    @PersistenceContext
    private EntityManager entityManager;
    @PostMapping("/api/search/businesshour")
    public ResponseEntity<?> search(@RequestBody Map<String, String> params) {
        String store_id = params.get("store_id");
        log.info("store_id: {}", store_id);

        String sql = "SELECT * FROM store_business_hour where store_id like ?";
        try{
            Query query  = entityManager.createNativeQuery(sql);
            query.setParameter(1, store_id + '%');
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()){
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Object[] row : results){

                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("id", row[0]);
                    resultMap.put("store_id", row[1]);
                    resultMap.put("day_of_week", row[2]);
                    resultMap.put("open_time", row[3]);
                    resultMap.put("close_time", row[4]);
                    resultList.add(resultMap);
                }

                return ResponseEntity.ok(resultList);
            } else{
                return ResponseEntity.ok(Map.of("status","NOT"));
            }

        } catch (Exception e){
            log.error("Login error: ", e);
            return ResponseEntity.internalServerError().body("Login Failed");
        }
    }


    @PostMapping("/api/search/sheet")
    public ResponseEntity<?> search_sheet(@RequestBody Map<String, String> params) {
        String store_id = params.get("store_id");
        log.info("store_id: {}", store_id);

        String sql = "SELECT * FROM cake_sheet where store_id like ?";
        try{
            Query query  = entityManager.createNativeQuery(sql);
            query.setParameter(1, '%' + store_id + '%');
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()){
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Object[] row : results){

                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("id", row[0]);
                    resultMap.put("sheet", row[1]);
                    resultMap.put("is_available", row[2]);
                    resultMap.put("store_id", row[3]);
                    resultList.add(resultMap);
                }

                return ResponseEntity.ok(resultList);
            } else{
                return ResponseEntity.ok(Map.of("status","NOT"));
            }

        } catch (Exception e){
            log.error("Error: ", e);
            return ResponseEntity.internalServerError().body("Failed");
        }
    }

    @PostMapping("/api/search/size")
    public ResponseEntity<?> search_size(@RequestBody Map<String, String> params) {
        String store_id = params.get("store_id");
        log.info("store_id: {}", store_id);

        String sql = "SELECT * FROM cake_size where store_id like ?";
        try{
            Query query  = entityManager.createNativeQuery(sql);
            query.setParameter(1, store_id + '%');
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()){
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Object[] row : results){

                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("id", row[0]);
                    resultMap.put("size", row[1]);
                    resultMap.put("is_available", row[2]);
                    resultMap.put("store_id", row[3]);
                    resultList.add(resultMap);
                }

                return ResponseEntity.ok(resultList);
            } else{
                return ResponseEntity.ok(Map.of("status","NOT"));
            }

        } catch (Exception e){
            log.error("Error: ", e);
            return ResponseEntity.internalServerError().body("Failed");
        }
    }

    @PostMapping("/api/search/cream")
    public ResponseEntity<?> search_cream(@RequestBody Map<String, String> params) {
        String store_id = params.get("store_id");
        log.info("store_id: {}", store_id);

        String sql = "SELECT * FROM cake_cream where store_id like ?";
        try{
            Query query  = entityManager.createNativeQuery(sql);
            query.setParameter(1, store_id + '%');
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()){
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Object[] row : results){

                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("id", row[0]);
                    resultMap.put("cream", row[1]);
                    resultMap.put("is_available", row[2]);
                    resultMap.put("store_id", row[3]);
                    resultList.add(resultMap);
                }

                return ResponseEntity.ok(resultList);
            } else{
                return ResponseEntity.ok(Map.of("status","NOT"));
            }

        } catch (Exception e){
            log.error("error: ", e);
            return ResponseEntity.internalServerError().body("Search Failed");
        }
    }


    @PostMapping("/api/search/store")
    public ResponseEntity<?> search_store(@RequestBody Map<String, String> params) {
        String user_name = params.get("store_name");
        log.info("store_name: {}", user_name);

        String sql = "SELECT * FROM user WHERE user_name LIKE ? AND user_mode = ?";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, '%' + user_name + '%');
            query.setParameter(2, 2);
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()) {
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Object[] row : results) {

                    String userId = row[0].toString();  // user_id 컬럼 (PK)
                    String userName = row[2].toString(); // user_name 컬럼 (이름)

                    // 추가 조회: store 테이블에서 store_address 가져오기
                    String storeSql = "SELECT store_address FROM store WHERE store_id = ?";
                    Query storeQuery = entityManager.createNativeQuery(storeSql);
                    storeQuery.setParameter(1, userId);
                    List<Object> storeResults = storeQuery.getResultList();

                    String storeAddress = "";
                    if (!storeResults.isEmpty()) {
                        storeAddress = storeResults.get(0).toString();
                    }

                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("user_id", userId);
                    resultMap.put("user_name", userName);
                    resultMap.put("store_address", storeAddress);

                    resultList.add(resultMap);
                }

                return ResponseEntity.ok(resultList);
            } else {
                return ResponseEntity.ok(Map.of("status", "NO SEARCH RESULT"));
            }

        } catch (Exception e) {
            log.error("error: ", e);
            return ResponseEntity.internalServerError().body("Search Failed");
        }
    }

    @PostMapping("/api/search/get_store_info")
    public ResponseEntity<?> get_store_info(@RequestBody Map<String, String> params) {
        String store_id = params.get("store_id");
        log.info("store_id: {}", store_id);

        String countSql = "SELECT COUNT(*) FROM orders WHERE store_id = ?";
        String imageSql = "SELECT store_image FROM store WHERE store_id = ?";

        try {
            // 주문 수 가져오기
            Query countQuery = entityManager.createNativeQuery(countSql);
            countQuery.setParameter(1, store_id);
            int store_order = ((Number) countQuery.getSingleResult()).intValue();

            // 이미지 가져오기
            Query imageQuery = entityManager.createNativeQuery(imageSql);
            imageQuery.setParameter(1, store_id);
            String store_image = (String) imageQuery.getSingleResult();

            // 결과 맵핑
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("store_order", store_order);
            resultMap.put("store_image", store_image);

            return ResponseEntity.ok(resultMap);
        } catch (Exception e) {
            log.error("error: ", e);
            return ResponseEntity.internalServerError().body("Search Failed");
        }
    }



    @PostMapping("/api/explore")
    public ResponseEntity<?> explore() {

        String sql = "SELECT * FROM user WHERE user_mode = ?";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, 2);
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()) {
                List<Map<String, Object>> resultList = new ArrayList<>();
                for (Object[] row : results) {
                    String userId = row[0].toString();  // user_id 컬럼 (PK)
                    String userName = row[2].toString(); // user_name 컬럼 (이름)
                    String userNumber = row[3].toString(); // user_name 컬럼 (이름)
                    System.out.println("result"+ userId);

                    // 추가 조회: store 테이블에서 store_address 가져오기
                    String storeSql = "SELECT store_address, store_image, store_desc FROM store WHERE store_id = ?";
                    Query storeQuery = entityManager.createNativeQuery(storeSql);
                    storeQuery.setParameter(1, userId);
                    List<Object[]> storeResults = storeQuery.getResultList();

                    String storeAddress = "";
                    String store_image = "";
                    String store_desc = "";

                    if (!storeResults.isEmpty()) {
                        Object[] storeRow = storeResults.get(0);
                        storeAddress = storeRow[0] != null ? storeRow[0].toString() : "";
                        store_image = storeRow[1] != null ? storeRow[1].toString() : "";
                        store_desc = storeRow[2] != null ? storeRow[2].toString() : "";
                    }

                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("store_id", userId);
                    resultMap.put("store_name", userName);
                    resultMap.put("store_address", storeAddress);
                    resultMap.put("store_image", store_image);
                    resultMap.put("store_desc", store_desc);
                    resultMap.put("store_number", userNumber);

                    resultList.add(resultMap);
                }

                return ResponseEntity.ok(resultList);
            } else {
                return ResponseEntity.ok(Map.of("status", "NO SEARCH RESULT"));
            }

        } catch (Exception e) {
            log.error("error: ", e);
            return ResponseEntity.internalServerError().body("Search Failed");
        }
    }

    @PostMapping("/api/search/order/user")
    public ResponseEntity<?> search_order(@RequestBody Map<String, String> params) {
        String user_id = params.get("user_id");
        log.info("user_id: {}", user_id);

        // JOIN을 사용
        String sql = "SELECT o.*, u.user_name FROM orders o " +
                "LEFT JOIN user u ON o.store_id = u.user_id " +
                "WHERE o.customer_id LIKE ? ORDER BY o.id desc";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, user_id);
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()) {
                log.info("result with user_name: {}", results);
                return ResponseEntity.ok(results);
            } else {
                return ResponseEntity.ok(Map.of("status", "NO SEARCH RESULT"));
            }

        } catch (Exception e) {
            log.error("error: ", e);
            return ResponseEntity.internalServerError().body("Search Failed");
        }
    }
    @PostMapping("/api/search/order/store")
    public ResponseEntity<?> search_order_store(@RequestBody Map<String, String> params) {
        String store_id = params.get("store_id");
        log.info("user_id: {}", store_id);

        String sql = "SELECT * FROM orders WHERE store_id LIKE ? ORDER BY id desc";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, store_id + '%');
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()) {
                return ResponseEntity.ok(results);
            } else {
                return ResponseEntity.ok(Map.of("status", "NO SEARCH RESULT"));
            }

        } catch (Exception e) {
            log.error("error: ", e);
            return ResponseEntity.internalServerError().body("Search Failed");
        }
    }


}
