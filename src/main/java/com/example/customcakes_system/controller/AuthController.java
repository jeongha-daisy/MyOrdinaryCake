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

public class AuthController {

    @PersistenceContext
    private EntityManager entityManager;
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> params,
                                   HttpServletResponse res) {

        String user_id = params.get("user_id");
        String user_pass = params.get("user_pass");


        log.info("user_id: {}", user_id);
        log.info("user_pass: {}", user_pass);

        String sql = "SELECT * FROM user where user_id = ? AND user_pass = ?";
        try{
            Query query  = entityManager.createNativeQuery(sql);
            query.setParameter(1, user_id);
            query.setParameter(2, user_pass);
            List<Object[]> results = query.getResultList();
            if (!results.isEmpty()){
                Object[] row = results.get(0);
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("user_id", row[0]);
                resultMap.put("user_mode", row[1]);
                resultMap.put("user_name", row[2]);
                resultMap.put("user_number", row[3]);

                log.info("resultMap: {}", resultMap);
                log.info(resultMap.get("user_name").toString());
                int userMode = (int) row[1];

                // user_mode가 2일 때 store 정보도 추가 조회
                if (userMode == 2) {
                    String sqlStore = "SELECT store_address, store_image, store_desc FROM store WHERE store_id = ?";
                    Query storeQuery = entityManager.createNativeQuery(sqlStore);
                    storeQuery.setParameter(1, row[0]); // user_id

                    List<Object[]> storeResults = storeQuery.getResultList();
                    if (!storeResults.isEmpty()) {
                        Object[] storeRow = storeResults.get(0);
                        resultMap.put("store_address", storeRow[0]);
                        resultMap.put("store_image", storeRow[1]);
                        resultMap.put("store_desc", storeRow[2]);
                    }
                }

                log.info("resultMap: {}", resultMap);
                return ResponseEntity.ok(resultMap);
            } else{
                return ResponseEntity.ok("NOT");
            }

        } catch (Exception e){
            log.error("Login error: ", e);
            return ResponseEntity.internalServerError().body("Login Failed");
        }
    }
    @PostMapping("/api/auth/signup/user")
    @Transactional
    public ResponseEntity<?> signup_user(@RequestBody Map<String, String> params, HttpServletResponse res) {

        String user_id = params.get("user_id");
        int user_mode = Integer.parseInt(params.get("user_mode"));
        String user_name = params.get("user_name");
        String user_number = params.get("user_number");
        String user_pass = params.get("user_pass");


        String sql = "SELECT * FROM user WHERE user_id = ?";
        try {
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, user_id);
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()) {
                return ResponseEntity.ok("ALREADY");
            } else {
                // 1. user INSERT
                String sql2 = "INSERT INTO user(user_id, user_mode, user_name, user_number, user_pass) VALUES (?, ?, ?, ?, ?)";
                Query query2 = entityManager.createNativeQuery(sql2);
                query2.setParameter(1, user_id);
                query2.setParameter(2, user_mode);
                query2.setParameter(3, user_name);
                query2.setParameter(4, user_number);
                query2.setParameter(5, user_pass);

                int result = query2.executeUpdate();
                return (result > 0) ? ResponseEntity.ok("OK") : ResponseEntity.ok("NOT");
            }

        } catch (Exception e) {
            log.error("Insert error: ", e);
            return ResponseEntity.internalServerError().body("Insert Failed");
        }
    }


    @PostMapping("/api/auth/signup/store")
    @Transactional
    public ResponseEntity<?> signup_store(
            @RequestParam("store_image") MultipartFile store_image,
            @RequestParam("user_id") String user_id,
            @RequestParam("user_pass") String user_pass,
            @RequestParam("user_name") String user_name,
            @RequestParam("user_mode") String user_mode,
            @RequestParam("user_number") String user_number,
            @RequestParam("store_address") String store_address,
            @RequestParam("store_desc") String store_desc
    ) {
        String sql1 = "SELECT * FROM user WHERE user_id = ?";
        try {
            Query query = entityManager.createNativeQuery(sql1);
            query.setParameter(1, user_id);
            List<Object[]> results = query.getResultList();

            if (!results.isEmpty()) {
                return ResponseEntity.ok("ALREADY");
            }

            // 이미지 저장
            String originalFilename = store_image.getOriginalFilename();
            String filename = user_id + "_" + originalFilename;
            String filePath = System.getProperty("user.dir");
            String uploadDir = filePath + "/upload/images";
            Path saveDir = Paths.get(uploadDir);
            Path savePath = saveDir.resolve(filename);

            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
            }

            store_image.transferTo(savePath.toFile());
            String imageUrl = "/images/" + filename;

            // 1. user 테이블 insert
            String sql2 = "INSERT INTO user(user_id, user_mode, user_name, user_number, user_pass) VALUES (?, ?, ?, ?, ?)";
            Query query2 = entityManager.createNativeQuery(sql2);
            query2.setParameter(1, user_id);
            query2.setParameter(2, Integer.parseInt(user_mode)); // 문자열 -> 숫자
            query2.setParameter(3, user_name);
            query2.setParameter(4, user_number);
            query2.setParameter(5, user_pass);

            int result = query2.executeUpdate();

            // 2. store 테이블 insert
            if (result > 0) {
                String sql3 = "INSERT INTO store(store_id, store_address, store_image, store_desc) VALUES (?, ?, ?, ?)";
                Query query3 = entityManager.createNativeQuery(sql3);
                query3.setParameter(1, user_id);
                query3.setParameter(2, store_address);
                query3.setParameter(3, imageUrl); // 여기서 store_image는 경로 문자열
                query3.setParameter(4, store_desc);

                int result2 = query3.executeUpdate();

                return (result2 > 0) ? ResponseEntity.ok("OK") : ResponseEntity.ok("NOT");
            }

            return ResponseEntity.ok("NOT");

        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("status", "NOT");
            response.put("message", "이미지 업로드에 실패했습니다.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Insert error: ", e);
            return ResponseEntity.internalServerError().body("Insert Failed");
        }
    }

}
