package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.HasilUjian;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HasilUjianRepository {

    Configuration conf = HBaseConfiguration.create();
    String tableName = "hasil_ujian";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<HasilUjian> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableHasilUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.showListTableIndex(tableHasilUjian.toString(), columnMapping, HasilUjian.class, indexedFields,
                size);
    }

    public HasilUjian save(HasilUjian hasilUjian) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        String rowKey = hasilUjian.getIdHasilUjian();
        TableName tableHasilUjian = TableName.valueOf(tableName);

        // Save main info
        saveMainInfo(client, tableHasilUjian, rowKey, hasilUjian);

        // Save score data
        saveScoreData(client, tableHasilUjian, rowKey, hasilUjian);

        // Save analytics data (NEW)
        saveAnalyticsData(client, tableHasilUjian, rowKey, hasilUjian);

        // Save security data (NEW)
        saveSecurityData(client, tableHasilUjian, rowKey, hasilUjian);

        // Save relationships
        saveRelationships(client, tableHasilUjian, rowKey, hasilUjian);

        client.insertRecord(tableHasilUjian, rowKey, "detail", "created_by", "CAT-System");
        return hasilUjian;
    }

    private void saveMainInfo(HBaseCustomClient client, TableName table, String rowKey, HasilUjian hasilUjian) {
        client.insertRecord(table, rowKey, "main", "idHasilUjian", hasilUjian.getIdHasilUjian());
        client.insertRecord(table, rowKey, "main", "idUjian", hasilUjian.getIdUjian());
        client.insertRecord(table, rowKey, "main", "idPeserta", hasilUjian.getIdPeserta());
        client.insertRecord(table, rowKey, "main", "sessionId", hasilUjian.getSessionId());
        client.insertRecord(table, rowKey, "main", "idSchool", hasilUjian.getIdSchool());

        if (hasilUjian.getAttemptNumber() != null) {
            client.insertRecord(table, rowKey, "main", "attemptNumber", hasilUjian.getAttemptNumber().toString());
        }

        client.insertRecord(table, rowKey, "main", "statusPengerjaan", hasilUjian.getStatusPengerjaan());

        if (hasilUjian.getIsAutoSubmit() != null) {
            client.insertRecord(table, rowKey, "main", "isAutoSubmit", hasilUjian.getIsAutoSubmit().toString());
        }

        if (hasilUjian.getWaktuMulai() != null) {
            client.insertRecord(table, rowKey, "main", "waktuMulai", hasilUjian.getWaktuMulai().toString());
        }

        if (hasilUjian.getWaktuSelesai() != null) {
            client.insertRecord(table, rowKey, "main", "waktuSelesai", hasilUjian.getWaktuSelesai().toString());
        }

        if (hasilUjian.getDurasiPengerjaan() != null) {
            client.insertRecord(table, rowKey, "main", "durasiPengerjaan", hasilUjian.getDurasiPengerjaan().toString());
        }

        if (hasilUjian.getSisaWaktu() != null) {
            client.insertRecord(table, rowKey, "main", "sisaWaktu", hasilUjian.getSisaWaktu().toString());
        }

        client.insertRecord(table, rowKey, "main", "createdAt", hasilUjian.getCreatedAt().toString());
        client.insertRecord(table, rowKey, "main", "updatedAt", hasilUjian.getUpdatedAt().toString());
    }

    private void saveScoreData(HBaseCustomClient client, TableName table, String rowKey, HasilUjian hasilUjian) {
        try {
            // Save answers data
            if (hasilUjian.getJawabanPeserta() != null && !hasilUjian.getJawabanPeserta().isEmpty()) {
                String jawabanPesertaJson = objectMapper.writeValueAsString(hasilUjian.getJawabanPeserta());
                client.insertRecord(table, rowKey, "score", "jawabanPeserta", jawabanPesertaJson);
            } else {
                client.insertRecord(table, rowKey, "score", "jawabanPeserta", "{}");
            }

            // Save correct answers mapping
            if (hasilUjian.getJawabanBenar() != null && !hasilUjian.getJawabanBenar().isEmpty()) {
                String jawabanBenarJson = objectMapper.writeValueAsString(hasilUjian.getJawabanBenar());
                client.insertRecord(table, rowKey, "score", "jawabanBenar", jawabanBenarJson);
            } else {
                client.insertRecord(table, rowKey, "score", "jawabanBenar", "{}");
            }

            // Save score per question
            if (hasilUjian.getSkorPerSoal() != null && !hasilUjian.getSkorPerSoal().isEmpty()) {
                String skorPerSoalJson = objectMapper.writeValueAsString(hasilUjian.getSkorPerSoal());
                client.insertRecord(table, rowKey, "score", "skorPerSoal", skorPerSoalJson);
            } else {
                client.insertRecord(table, rowKey, "score", "skorPerSoal", "{}");
            }

            // Save total scores
            if (hasilUjian.getTotalSkor() != null) {
                client.insertRecord(table, rowKey, "score", "totalSkor", hasilUjian.getTotalSkor().toString());
            }

            if (hasilUjian.getSkorMaksimal() != null) {
                client.insertRecord(table, rowKey, "score", "skorMaksimal", hasilUjian.getSkorMaksimal().toString());
            }

            if (hasilUjian.getPersentase() != null) {
                client.insertRecord(table, rowKey, "score", "persentase", hasilUjian.getPersentase().toString());
            }

            client.insertRecord(table, rowKey, "score", "nilaiHuruf", hasilUjian.getNilaiHuruf());

            if (hasilUjian.getLulus() != null) {
                client.insertRecord(table, rowKey, "score", "lulus", hasilUjian.getLulus().toString());
            }

            // Save analysis data
            if (hasilUjian.getJumlahBenar() != null) {
                client.insertRecord(table, rowKey, "analysis", "jumlahBenar", hasilUjian.getJumlahBenar().toString());
            }

            if (hasilUjian.getJumlahSalah() != null) {
                client.insertRecord(table, rowKey, "analysis", "jumlahSalah", hasilUjian.getJumlahSalah().toString());
            }

            if (hasilUjian.getJumlahKosong() != null) {
                client.insertRecord(table, rowKey, "analysis", "jumlahKosong", hasilUjian.getJumlahKosong().toString());
            }

            if (hasilUjian.getTotalSoal() != null) {
                client.insertRecord(table, rowKey, "analysis", "totalSoal", hasilUjian.getTotalSoal().toString());
            }

            // Save metadata
            if (hasilUjian.getMetadata() != null && !hasilUjian.getMetadata().isEmpty()) {
                String metadataJson = objectMapper.writeValueAsString(hasilUjian.getMetadata());
                client.insertRecord(table, rowKey, "main", "metadata", metadataJson);
            } else {
                client.insertRecord(table, rowKey, "main", "metadata", "{}");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize score data", e);
        }
    }

    private void saveAnalyticsData(HBaseCustomClient client, TableName table, String rowKey, HasilUjian hasilUjian) {
        try {
            // Time Analytics
            if (hasilUjian.getTimeSpentPerQuestion() != null && !hasilUjian.getTimeSpentPerQuestion().isEmpty()) {
                String timeSpentJson = objectMapper.writeValueAsString(hasilUjian.getTimeSpentPerQuestion());
                client.insertRecord(table, rowKey, "analytics", "timeSpentPerQuestion", timeSpentJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "timeSpentPerQuestion", "{}");
            }

            if (hasilUjian.getAnswerTimestamps() != null && !hasilUjian.getAnswerTimestamps().isEmpty()) {
                String timestampsJson = objectMapper.writeValueAsString(hasilUjian.getAnswerTimestamps());
                client.insertRecord(table, rowKey, "analytics", "answerTimestamps", timestampsJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "answerTimestamps", "{}");
            }

            if (hasilUjian.getAnswerHistory() != null && !hasilUjian.getAnswerHistory().isEmpty()) {
                String historyJson = objectMapper.writeValueAsString(hasilUjian.getAnswerHistory());
                client.insertRecord(table, rowKey, "analytics", "answerHistory", historyJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "answerHistory", "{}");
            }

            if (hasilUjian.getAttemptCountPerQuestion() != null && !hasilUjian.getAttemptCountPerQuestion().isEmpty()) {
                String attemptCountJson = objectMapper.writeValueAsString(hasilUjian.getAttemptCountPerQuestion());
                client.insertRecord(table, rowKey, "analytics", "attemptCountPerQuestion", attemptCountJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "attemptCountPerQuestion", "{}");
            }

            // Behavioral Analytics
            client.insertRecord(table, rowKey, "analytics", "workingPattern", hasilUjian.getWorkingPattern());

            if (hasilUjian.getConsistencyScore() != null) {
                client.insertRecord(table, rowKey, "analytics", "consistencyScore",
                        hasilUjian.getConsistencyScore().toString());
            }

            if (hasilUjian.getHasSignsOfGuessing() != null) {
                client.insertRecord(table, rowKey, "analytics", "hasSignsOfGuessing",
                        hasilUjian.getHasSignsOfGuessing().toString());
            }

            if (hasilUjian.getHasSignsOfAnxiety() != null) {
                client.insertRecord(table, rowKey, "analytics", "hasSignsOfAnxiety",
                        hasilUjian.getHasSignsOfAnxiety().toString());
            }

            client.insertRecord(table, rowKey, "analytics", "confidenceLevel", hasilUjian.getConfidenceLevel());

            // Performance Insights
            if (hasilUjian.getTopicPerformance() != null && !hasilUjian.getTopicPerformance().isEmpty()) {
                String topicPerformanceJson = objectMapper.writeValueAsString(hasilUjian.getTopicPerformance());
                client.insertRecord(table, rowKey, "analytics", "topicPerformance", topicPerformanceJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "topicPerformance", "{}");
            }

            if (hasilUjian.getConceptMastery() != null && !hasilUjian.getConceptMastery().isEmpty()) {
                String conceptMasteryJson = objectMapper.writeValueAsString(hasilUjian.getConceptMastery());
                client.insertRecord(table, rowKey, "analytics", "conceptMastery", conceptMasteryJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "conceptMastery", "{}");
            }

            if (hasilUjian.getStrengths() != null && !hasilUjian.getStrengths().isEmpty()) {
                String strengthsJson = objectMapper.writeValueAsString(hasilUjian.getStrengths());
                client.insertRecord(table, rowKey, "analytics", "strengths", strengthsJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "strengths", "[]");
            }

            if (hasilUjian.getWeaknesses() != null && !hasilUjian.getWeaknesses().isEmpty()) {
                String weaknessesJson = objectMapper.writeValueAsString(hasilUjian.getWeaknesses());
                client.insertRecord(table, rowKey, "analytics", "weaknesses", weaknessesJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "weaknesses", "[]");
            }

            if (hasilUjian.getRecommendedStudyAreas() != null && !hasilUjian.getRecommendedStudyAreas().isEmpty()) {
                String recommendedAreasJson = objectMapper.writeValueAsString(hasilUjian.getRecommendedStudyAreas());
                client.insertRecord(table, rowKey, "analytics", "recommendedStudyAreas", recommendedAreasJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "recommendedStudyAreas", "[]");
            }

            // Answer Pattern Analysis
            if (hasilUjian.getTotalAnswerChanges() != null) {
                client.insertRecord(table, rowKey, "analytics", "totalAnswerChanges",
                        hasilUjian.getTotalAnswerChanges().toString());
            }

            if (hasilUjian.getAnswerChangeSuccessRate() != null) {
                client.insertRecord(table, rowKey, "analytics", "answerChangeSuccessRate",
                        hasilUjian.getAnswerChangeSuccessRate().toString());
            }

            if (hasilUjian.getFrequentlyChangedQuestions() != null
                    && !hasilUjian.getFrequentlyChangedQuestions().isEmpty()) {
                String frequentlyChangedJson = objectMapper
                        .writeValueAsString(hasilUjian.getFrequentlyChangedQuestions());
                client.insertRecord(table, rowKey, "analytics", "frequentlyChangedQuestions", frequentlyChangedJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "frequentlyChangedQuestions", "[]");
            }

            if (hasilUjian.getChangePatterns() != null && !hasilUjian.getChangePatterns().isEmpty()) {
                String changePatternsJson = objectMapper.writeValueAsString(hasilUjian.getChangePatterns());
                client.insertRecord(table, rowKey, "analytics", "changePatterns", changePatternsJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "changePatterns", "{}");
            }

            // Learning Analytics
            client.insertRecord(table, rowKey, "analytics", "learningStyle", hasilUjian.getLearningStyle());

            if (hasilUjian.getLearningInsights() != null && !hasilUjian.getLearningInsights().isEmpty()) {
                String learningInsightsJson = objectMapper.writeValueAsString(hasilUjian.getLearningInsights());
                client.insertRecord(table, rowKey, "analytics", "learningInsights", learningInsightsJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "learningInsights", "{}");
            }

            if (hasilUjian.getStudyStrategies() != null && !hasilUjian.getStudyStrategies().isEmpty()) {
                String studyStrategiesJson = objectMapper.writeValueAsString(hasilUjian.getStudyStrategies());
                client.insertRecord(table, rowKey, "analytics", "studyStrategies", studyStrategiesJson);
            } else {
                client.insertRecord(table, rowKey, "analytics", "studyStrategies", "[]");
            }

            if (hasilUjian.getAdaptiveDifficultyScore() != null) {
                client.insertRecord(table, rowKey, "analytics", "adaptiveDifficultyScore",
                        hasilUjian.getAdaptiveDifficultyScore().toString());
            }

            // Comparative Analysis
            if (hasilUjian.getRankInClass() != null) {
                client.insertRecord(table, rowKey, "analytics", "rankInClass", hasilUjian.getRankInClass().toString());
            }

            if (hasilUjian.getRankInSchool() != null) {
                client.insertRecord(table, rowKey, "analytics", "rankInSchool",
                        hasilUjian.getRankInSchool().toString());
            }

            if (hasilUjian.getPercentileRank() != null) {
                client.insertRecord(table, rowKey, "analytics", "percentileRank",
                        hasilUjian.getPercentileRank().toString());
            }

            client.insertRecord(table, rowKey, "analytics", "relativePerformance", hasilUjian.getRelativePerformance());

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize analytics data", e);
        }
    }

    private void saveSecurityData(HBaseCustomClient client, TableName table, String rowKey, HasilUjian hasilUjian) {
        try {
            // Security & Integrity
            client.insertRecord(table, rowKey, "security", "resultHash", hasilUjian.getResultHash());

            if (hasilUjian.getIsVerified() != null) {
                client.insertRecord(table, rowKey, "security", "isVerified", hasilUjian.getIsVerified().toString());
            }

            client.insertRecord(table, rowKey, "security", "verifiedBy", hasilUjian.getVerifiedBy());

            if (hasilUjian.getVerificationTime() != null) {
                client.insertRecord(table, rowKey, "security", "verificationTime",
                        hasilUjian.getVerificationTime().toString());
            }

            client.insertRecord(table, rowKey, "security", "securityStatus", hasilUjian.getSecurityStatus());

            if (hasilUjian.getSecurityFlags() != null && !hasilUjian.getSecurityFlags().isEmpty()) {
                String securityFlagsJson = objectMapper.writeValueAsString(hasilUjian.getSecurityFlags());
                client.insertRecord(table, rowKey, "security", "securityFlags", securityFlagsJson);
            } else {
                client.insertRecord(table, rowKey, "security", "securityFlags", "{}");
            }

            // Quality Assurance
            if (hasilUjian.getHasAppeal() != null) {
                client.insertRecord(table, rowKey, "qa", "hasAppeal", hasilUjian.getHasAppeal().toString());
            }

            client.insertRecord(table, rowKey, "qa", "appealReason", hasilUjian.getAppealReason());
            client.insertRecord(table, rowKey, "qa", "appealStatus", hasilUjian.getAppealStatus());

            if (hasilUjian.getAppealSubmittedAt() != null) {
                client.insertRecord(table, rowKey, "qa", "appealSubmittedAt",
                        hasilUjian.getAppealSubmittedAt().toString());
            }

            client.insertRecord(table, rowKey, "qa", "appealReviewedBy", hasilUjian.getAppealReviewedBy());

            if (hasilUjian.getAppealData() != null && !hasilUjian.getAppealData().isEmpty()) {
                String appealDataJson = objectMapper.writeValueAsString(hasilUjian.getAppealData());
                client.insertRecord(table, rowKey, "qa", "appealData", appealDataJson);
            } else {
                client.insertRecord(table, rowKey, "qa", "appealData", "{}");
            }

            if (hasilUjian.getViolationIds() != null) {
                client.insertRecord(table, rowKey, "security", "violationIds",
                        objectMapper.writeValueAsString(hasilUjian.getViolationIds()));
            }
            if (hasilUjian.getCheatDetections() != null) {
                client.insertRecord(table, rowKey, "security", "cheatDetections",
                        objectMapper.writeValueAsString(hasilUjian.getCheatDetections()));
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize security data", e);
        }
    }

    private void saveRelationships(HBaseCustomClient client, TableName table, String rowKey, HasilUjian hasilUjian) {
        // Save Ujian relationship
        if (hasilUjian.getUjian() != null && hasilUjian.getUjian().getIdUjian() != null) {
            client.insertRecord(table, rowKey, "ujian", "idUjian", hasilUjian.getUjian().getIdUjian());
            client.insertRecord(table, rowKey, "ujian", "namaUjian", hasilUjian.getUjian().getNamaUjian());
        }

        // Save Peserta relationship
        if (hasilUjian.getPeserta() != null && hasilUjian.getPeserta().getId() != null) {
            client.insertRecord(table, rowKey, "peserta", "id", hasilUjian.getPeserta().getId());
            client.insertRecord(table, rowKey, "peserta", "name", hasilUjian.getPeserta().getName());
            client.insertRecord(table, rowKey, "peserta", "username", hasilUjian.getPeserta().getUsername());
        }

        // Save School relationship
        if (hasilUjian.getSchool() != null && hasilUjian.getSchool().getIdSchool() != null) {
            client.insertRecord(table, rowKey, "school", "idSchool", hasilUjian.getSchool().getIdSchool());
            client.insertRecord(table, rowKey, "school", "nameSchool", hasilUjian.getSchool().getNameSchool());
        }
    }

    // ==================== ENHANCED QUERY METHODS ====================

    public HasilUjian findById(String hasilUjianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableHasilUjian = TableName.valueOf(tableName);
        Map<String, String> columnMapping = getStandardColumnMapping();

        return client.showDataTable(tableHasilUjian.toString(), columnMapping, hasilUjianId, HasilUjian.class);
    }

    public List<HasilUjian> findByUjianAndPeserta(String idUjian, String idPeserta) throws IOException {
        TableName tableHasilUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        // Get results by ujian first
        List<HasilUjian> results = client.getDataListByColumnIndeks(
                tableHasilUjian.toString(),
                columnMapping,
                "main",
                "idUjian",
                idUjian,
                HasilUjian.class,
                100,
                indexedFields);

        // Filter by peserta
        return results.stream()
                .filter(hasil -> idPeserta.equals(hasil.getIdPeserta()))
                .collect(Collectors.toList());
    }

    public List<HasilUjian> findByUjian(String idUjian) throws IOException {
        TableName tableHasilUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableHasilUjian.toString(),
                columnMapping,
                "main",
                "idUjian",
                idUjian,
                HasilUjian.class,
                1000,
                indexedFields);
    }

    public List<HasilUjian> findByPeserta(String idPeserta) throws IOException {
        TableName tableHasilUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableHasilUjian.toString(),
                columnMapping,
                "main",
                "idPeserta",
                idPeserta,
                HasilUjian.class,
                1000,
                indexedFields);
    }

    public List<HasilUjian> findBySchool(String idSchool) throws IOException {
        TableName tableHasilUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableHasilUjian.toString(),
                columnMapping,
                "main",
                "idSchool",
                idSchool,
                HasilUjian.class,
                1000,
                indexedFields);
    }

    public List<HasilUjian> findBySecurityStatus(String securityStatus, int limit) throws IOException {
        TableName tableHasilUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableHasilUjian.toString(),
                columnMapping,
                "security",
                "securityStatus",
                securityStatus,
                HasilUjian.class,
                limit,
                indexedFields);
    }

    public List<HasilUjian> findByAppealStatus(String appealStatus, int limit) throws IOException {
        TableName tableHasilUjian = TableName.valueOf(tableName);
        HBaseCustomClient client = new HBaseCustomClient(conf);

        Map<String, String> columnMapping = getStandardColumnMapping();
        Map<String, String> indexedFields = getIndexedFields();

        return client.getDataListByColumnIndeks(
                tableHasilUjian.toString(),
                columnMapping,
                "qa",
                "appealStatus",
                appealStatus,
                HasilUjian.class,
                limit,
                indexedFields);
    }

    // ==================== ENHANCED COLUMN MAPPING ====================

    private Map<String, String> getStandardColumnMapping() {
        Map<String, String> columnMapping = new HashMap<>();

        // Main fields
        columnMapping.put("idHasilUjian", "idHasilUjian");
        columnMapping.put("idUjian", "idUjian");
        columnMapping.put("idPeserta", "idPeserta");
        columnMapping.put("sessionId", "sessionId");
        columnMapping.put("idSchool", "idSchool");
        columnMapping.put("attemptNumber", "attemptNumber");
        columnMapping.put("statusPengerjaan", "statusPengerjaan");
        columnMapping.put("isAutoSubmit", "isAutoSubmit");
        columnMapping.put("waktuMulai", "waktuMulai");
        columnMapping.put("waktuSelesai", "waktuSelesai");
        columnMapping.put("durasiPengerjaan", "durasiPengerjaan");
        columnMapping.put("sisaWaktu", "sisaWaktu");
        columnMapping.put("createdAt", "createdAt");
        columnMapping.put("updatedAt", "updatedAt");

        // Score fields
        columnMapping.put("jawabanPeserta", "jawabanPeserta");
        columnMapping.put("jawabanBenar", "jawabanBenar");
        columnMapping.put("skorPerSoal", "skorPerSoal");
        columnMapping.put("totalSkor", "totalSkor");
        columnMapping.put("skorMaksimal", "skorMaksimal");
        columnMapping.put("persentase", "persentase");
        columnMapping.put("nilaiHuruf", "nilaiHuruf");
        columnMapping.put("lulus", "lulus");

        // Analysis fields
        columnMapping.put("jumlahBenar", "jumlahBenar");
        columnMapping.put("jumlahSalah", "jumlahSalah");
        columnMapping.put("jumlahKosong", "jumlahKosong");
        columnMapping.put("totalSoal", "totalSoal");
        columnMapping.put("metadata", "metadata");

        // Analytics fields (NEW)
        columnMapping.put("timeSpentPerQuestion", "timeSpentPerQuestion");
        columnMapping.put("answerTimestamps", "answerTimestamps");
        columnMapping.put("answerHistory", "answerHistory");
        columnMapping.put("attemptCountPerQuestion", "attemptCountPerQuestion");
        columnMapping.put("workingPattern", "workingPattern");
        columnMapping.put("consistencyScore", "consistencyScore");
        columnMapping.put("hasSignsOfGuessing", "hasSignsOfGuessing");
        columnMapping.put("hasSignsOfAnxiety", "hasSignsOfAnxiety");
        columnMapping.put("confidenceLevel", "confidenceLevel");
        columnMapping.put("topicPerformance", "topicPerformance");
        columnMapping.put("conceptMastery", "conceptMastery");
        columnMapping.put("strengths", "strengths");
        columnMapping.put("weaknesses", "weaknesses");
        columnMapping.put("recommendedStudyAreas", "recommendedStudyAreas");
        columnMapping.put("totalAnswerChanges", "totalAnswerChanges");
        columnMapping.put("answerChangeSuccessRate", "answerChangeSuccessRate");
        columnMapping.put("frequentlyChangedQuestions", "frequentlyChangedQuestions");
        columnMapping.put("changePatterns", "changePatterns");
        columnMapping.put("learningStyle", "learningStyle");
        columnMapping.put("learningInsights", "learningInsights");
        columnMapping.put("studyStrategies", "studyStrategies");
        columnMapping.put("adaptiveDifficultyScore", "adaptiveDifficultyScore");
        columnMapping.put("rankInClass", "rankInClass");
        columnMapping.put("rankInSchool", "rankInSchool");
        columnMapping.put("percentileRank", "percentileRank");
        columnMapping.put("relativePerformance", "relativePerformance");

        // Security fields (NEW)
        columnMapping.put("resultHash", "resultHash");
        columnMapping.put("isVerified", "isVerified");
        columnMapping.put("verifiedBy", "verifiedBy");
        columnMapping.put("verificationTime", "verificationTime");
        columnMapping.put("securityStatus", "securityStatus");
        columnMapping.put("securityFlags", "securityFlags");

        // Quality Assurance fields (NEW)
        columnMapping.put("hasAppeal", "hasAppeal");
        columnMapping.put("appealReason", "appealReason");
        columnMapping.put("appealStatus", "appealStatus");
        columnMapping.put("appealSubmittedAt", "appealSubmittedAt");
        columnMapping.put("appealReviewedBy", "appealReviewedBy");
        columnMapping.put("appealData", "appealData");

        // Relationships
        columnMapping.put("ujian", "ujian");
        columnMapping.put("peserta", "peserta");
        columnMapping.put("school", "school");

        return columnMapping;
    }

    private Map<String, String> getIndexedFields() {
        Map<String, String> indexedFields = new HashMap<>();
        indexedFields.put("jawabanPeserta", "MAP");
        indexedFields.put("jawabanBenar", "MAP");
        indexedFields.put("skorPerSoal", "MAP");
        indexedFields.put("metadata", "MAP");

        // Analytics indexed fields
        indexedFields.put("timeSpentPerQuestion", "MAP");
        indexedFields.put("answerTimestamps", "MAP");
        indexedFields.put("answerHistory", "MAP");
        indexedFields.put("attemptCountPerQuestion", "MAP");
        indexedFields.put("topicPerformance", "MAP");
        indexedFields.put("conceptMastery", "MAP");
        indexedFields.put("strengths", "LIST");
        indexedFields.put("weaknesses", "LIST");
        indexedFields.put("recommendedStudyAreas", "LIST");
        indexedFields.put("changePatterns", "MAP");
        indexedFields.put("learningInsights", "MAP");
        indexedFields.put("studyStrategies", "LIST");
        indexedFields.put("securityFlags", "MAP");
        indexedFields.put("appealData", "MAP");

        return indexedFields;
    }

    // ==================== UTILITY METHODS ====================

    public boolean deleteById(String hasilUjianId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, hasilUjianId);
        return true;
    }

    public boolean existsById(String hasilUjianId) throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableHasilUjian = TableName.valueOf(tableName);
            Map<String, String> columnMapping = new HashMap<>();
            columnMapping.put("idHasilUjian", "idHasilUjian");

            HasilUjian hasil = client.getDataByColumn(tableHasilUjian.toString(), columnMapping,
                    "main", "idHasilUjian",
                    hasilUjianId, HasilUjian.class);

            return hasil != null && hasil.getIdHasilUjian() != null;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== OPTIMIZED UPDATE METHODS ====================

    public boolean updateSecurityStatus(String hasilUjianId, String securityStatus, Map<String, Object> securityFlags)
            throws IOException {
        try {
            HBaseCustomClient client = new HBaseCustomClient(conf);
            TableName tableHasilUjian = TableName.valueOf(tableName);

            client.insertRecord(tableHasilUjian, hasilUjianId, "security", "securityStatus", securityStatus);

            if (securityFlags != null && !securityFlags.isEmpty()) {
                String securityFlagsJson = objectMapper.writeValueAsString(securityFlags);
                client.insertRecord(tableHasilUjian, hasilUjianId, "security", "securityFlags", securityFlagsJson);
            }

            client.insertRecord(tableHasilUjian, hasilUjianId, "main", "updatedAt", Instant.now().toString());

            return true;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize security flags", e);
        }
    }

    public boolean updateAppealStatus(String hasilUjianId, String appealStatus, String reviewedBy, String reviewNote)
            throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableHasilUjian = TableName.valueOf(tableName);

        client.insertRecord(tableHasilUjian, hasilUjianId, "qa", "appealStatus", appealStatus);

        if (reviewedBy != null) {
            client.insertRecord(tableHasilUjian, hasilUjianId, "qa", "appealReviewedBy", reviewedBy);
        }

        if (reviewNote != null) {
            try {
                Map<String, Object> appealData = new HashMap<>();
                appealData.put("reviewNote", reviewNote);
                appealData.put("reviewedAt", Instant.now().toString());

                String appealDataJson = objectMapper.writeValueAsString(appealData);
                client.insertRecord(tableHasilUjian, hasilUjianId, "qa", "appealData", appealDataJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize appeal data", e);
            }
        }

        client.insertRecord(tableHasilUjian, hasilUjianId, "main", "updatedAt", Instant.now().toString());

        return true;
    }

    public boolean updateVerificationStatus(String hasilUjianId, boolean isVerified, String verifiedBy)
            throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        TableName tableHasilUjian = TableName.valueOf(tableName);

        client.insertRecord(tableHasilUjian, hasilUjianId, "security", "isVerified", String.valueOf(isVerified));

        if (verifiedBy != null) {
            client.insertRecord(tableHasilUjian, hasilUjianId, "security", "verifiedBy", verifiedBy);
            client.insertRecord(tableHasilUjian, hasilUjianId, "security", "verificationTime",
                    Instant.now().toString());
        }

        client.insertRecord(tableHasilUjian, hasilUjianId, "main", "updatedAt", Instant.now().toString());

        return true;
    }

    // ==================== STATISTICS METHODS ====================

    public long countByUjian(String idUjian) throws IOException {
        return findByUjian(idUjian).size();
    }

    public long countPassedByUjian(String idUjian) throws IOException {
        List<HasilUjian> results = findByUjian(idUjian);
        return results.stream().filter(hasil -> Boolean.TRUE.equals(hasil.getLulus())).count();
    }

    public double getAverageScoreByUjian(String idUjian) throws IOException {
        List<HasilUjian> results = findByUjian(idUjian);
        return results.stream()
                .filter(hasil -> hasil.getPersentase() != null)
                .mapToDouble(HasilUjian::getPersentase)
                .average()
                .orElse(0.0);
    }
}