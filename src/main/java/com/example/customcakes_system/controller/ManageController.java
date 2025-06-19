package com.example.customcakes_system.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j

public class ManageController {

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    @PostMapping("/api/store/update/business-hour")
    public ResponseEntity<?> updateBusinessHours(@RequestBody List<Map<String, String>> hoursList) {
        if (hoursList.isEmpty()) {
            return ResponseEntity.badRequest().body("No data");
        }

        String storeId = hoursList.get(0).get("store_id");

        try {
            // 기존 데이터 삭제
            String deleteSql = "DELETE FROM store_business_hour WHERE store_id = ?";
            Query deleteQuery = entityManager.createNativeQuery(deleteSql);
            deleteQuery.setParameter(1, storeId);
            deleteQuery.executeUpdate();

            // 새 데이터 삽입
            for (Map<String, String> item : hoursList) {
                String insertSql = "INSERT INTO store_business_hour (store_id, day_of_week, open_time, close_time) VALUES (?, ?, ?, ?)";
                Query insertQuery = entityManager.createNativeQuery(insertSql);
                insertQuery.setParameter(1, item.get("store_id"));
                insertQuery.setParameter(2, Integer.parseInt(item.get("day_of_week")));
                insertQuery.setParameter(3, item.get("open_time"));
                insertQuery.setParameter(4, item.get("close_time"));
                insertQuery.executeUpdate();
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("Business Hour Update Error: ", e);
            return ResponseEntity.internalServerError().body("Error");
        }
    }

    @PostMapping("/api/store/update/sheet")
    @Transactional
    public ResponseEntity<?> updateSheet(@RequestBody Map<String, String> params,
                                         HttpServletResponse res) {

        String store_id = params.get("store_id");
        String sheet = params.get("sheet");
        String is_availableStr = params.get("is_available");

        log.info("debug");
        log.info("params: {}", params);

        // 문자열로 받은 is_available -> boolean/int로 변환
        int is_available = is_availableStr.equals("판매 중") ? 1 : 0;

        String sql = "SELECT * FROM cake_sheet WHERE store_id = ? AND sheet = ?";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, store_id);
            query.setParameter(2, sheet);
            List<Object[]> results = query.getResultList();

            if (results.isEmpty()) {
                return ResponseEntity.ok("NOTFOUND");
            } else {
                String sql2 = "UPDATE cake_sheet SET is_available = ? WHERE store_id = ? AND sheet = ?";
                Query query2 = entityManager.createNativeQuery(sql2);
                query2.setParameter(1, is_available);
                query2.setParameter(2, store_id);
                query2.setParameter(3, sheet);

                int result = query2.executeUpdate();

                log.info("store_id: {}", store_id);
                log.info("sheet: {}", sheet);
                log.info("is_available: {}", is_available);

                if (result > 0) {
                    return ResponseEntity.ok("UPDATEOK");
                } else {
                    return ResponseEntity.ok("NOTUPDATE");
                }
            }

        } catch (Exception e) {
            log.error("Update error: ", e);
            return ResponseEntity.internalServerError().body("Update Failed");
        }
    }

    @PostMapping("/api/store/update/size")
    @Transactional
    public ResponseEntity<?> updateSize(@RequestBody Map<String, String> params,
                                         HttpServletResponse res) {

        String store_id = params.get("store_id");
        String size = params.get("size");
        String is_availableStr = params.get("is_available");

        // 문자열로 받은 is_available -> boolean/int로 변환
        int is_available = is_availableStr.equals("판매 중") ? 1 : 0;

        String sql = "SELECT * FROM cake_size WHERE store_id = ? AND size = ?";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, store_id);
            query.setParameter(2, size);
            List<Object[]> results = query.getResultList();

            if (results.isEmpty()) {
                return ResponseEntity.ok("NOTFOUND");
            } else {
                String sql2 = "UPDATE cake_size SET is_available = ? WHERE store_id = ? AND size = ?";
                Query query2 = entityManager.createNativeQuery(sql2);
                query2.setParameter(1, is_available);
                query2.setParameter(2, store_id);
                query2.setParameter(3, size);

                int result = query2.executeUpdate();

                log.info("store_id: {}", store_id);
                log.info("size: {}", size);
                log.info("is_available: {}", is_available);

                if (result > 0) {
                    return ResponseEntity.ok("UPDATEOK");
                } else {
                    return ResponseEntity.ok("NOTUPDATE");
                }
            }

        } catch (Exception e) {
            log.error("Update error: ", e);
            return ResponseEntity.internalServerError().body("Update Failed");
        }
    }

    @PostMapping("/api/store/update/cream")
    @Transactional
    public ResponseEntity<?> updateCream(@RequestBody Map<String, String> params,
                                         HttpServletResponse res) {

        String store_id = params.get("store_id");
        String cream = params.get("cream");
        String is_availableStr = params.get("is_available");

        log.info("debug");
        log.info("params: {}", params);

        // 문자열로 받은 is_available -> boolean/int로 변환
        int is_available = is_availableStr.equals("판매 중") ? 1 : 0;

        String sql = "SELECT * FROM cake_cream WHERE store_id = ? AND cream = ?";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, store_id);
            query.setParameter(2, cream);
            List<Object[]> results = query.getResultList();

            if (results.isEmpty()) {
                return ResponseEntity.ok("NOTFOUND");
            } else {
                String sql2 = "UPDATE cake_cream SET is_available = ? WHERE store_id = ? AND cream = ?";
                Query query2 = entityManager.createNativeQuery(sql2);
                query2.setParameter(1, is_available);
                query2.setParameter(2, store_id);
                query2.setParameter(3, cream);

                int result = query2.executeUpdate();

                log.info("store_id: {}", store_id);
                log.info("cream: {}", cream);
                log.info("is_available: {}", is_available);

                if (result > 0) {
                    return ResponseEntity.ok("UPDATEOK");
                } else {
                    return ResponseEntity.ok("NOTUPDATE");
                }
            }

        } catch (Exception e) {
            log.error("Update error: ", e);
            return ResponseEntity.internalServerError().body("Update Failed");
        }
    }


    @PostMapping("/api/store/insert/sheet")
    @Transactional
    public ResponseEntity<?> addSheet(@RequestBody Map<String, String> params, HttpServletResponse res) {

        String store_id = params.get("store_id");
        int is_available = 1;
        String sheet = params.get("sheet");


        String sql = "INSERT INTO cake_sheet(sheet, is_available, store_id) VALUES (?, ?, ?)";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, sheet);
            query.setParameter(2, is_available);
            query.setParameter(3, store_id);

            int result = query.executeUpdate();

            return (result > 0) ? ResponseEntity.ok("OK") : ResponseEntity.ok("NOT");

        } catch (Exception e) {
            log.error("Insert error: ", e);
            return ResponseEntity.internalServerError().body("Insert Failed");
        }
    }

    @PostMapping("/api/store/insert/size")
    @Transactional
    public ResponseEntity<?> addSize(@RequestBody Map<String, String> params, HttpServletResponse res) {

        String store_id = params.get("store_id");
        int is_available = 1;
        String size = params.get("size");


        String sql = "INSERT INTO cake_size(size, is_available, store_id) VALUES (?, ?, ?)";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, size);
            query.setParameter(2, is_available);
            query.setParameter(3, store_id);

            int result = query.executeUpdate();

            return (result > 0) ? ResponseEntity.ok("OK") : ResponseEntity.ok("NOT");

        } catch (Exception e) {
            log.error("Insert error: ", e);
            return ResponseEntity.internalServerError().body("Insert Failed");
        }
    }

    @PostMapping("/api/store/insert/cream")
    @Transactional
    public ResponseEntity<?> addCream(@RequestBody Map<String, String> params, HttpServletResponse res) {

        String store_id = params.get("store_id");
        int is_available = 1;
        String cream = params.get("cream");


        String sql = "INSERT INTO cake_cream(cream, is_available, store_id) VALUES (?, ?, ?)";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, cream);
            query.setParameter(2, is_available);
            query.setParameter(3, store_id);

            int result = query.executeUpdate();

            return (result > 0) ? ResponseEntity.ok("OK") : ResponseEntity.ok("NOT");

        } catch (Exception e) {
            log.error("Insert error: ", e);
            return ResponseEntity.internalServerError().body("Insert Failed");
        }
    }

}
