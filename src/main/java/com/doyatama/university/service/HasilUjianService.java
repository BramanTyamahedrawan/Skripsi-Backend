package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.HasilUjian;
import com.doyatama.university.model.Ujian;
import com.doyatama.university.model.User;
import com.doyatama.university.model.School;
import com.doyatama.university.model.UjianSession;
import com.doyatama.university.model.BankSoal;
import com.doyatama.university.model.BankSoalUjian;
import com.doyatama.university.model.CheatDetection;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.repository.HasilUjianRepository;
import com.doyatama.university.repository.UjianRepository;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.UjianSessionRepository;
import com.doyatama.university.repository.BankSoalRepository;
import com.doyatama.university.repository.CheatDetectionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service untuk mengelola HasilUjian
 * Menangani operasi CRUD, analytics, security, dan reporting
 */
@Service
public class HasilUjianService {

    private static final Logger logger = LoggerFactory.getLogger(HasilUjianService.class);

    @Autowired
    private HasilUjianRepository hasilUjianRepository;

    @Autowired
    private UjianRepository ujianRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UjianSessionRepository ujianSessionRepository;

    @Autowired
    private BankSoalRepository bankSoalRepository;

    @Autowired
    private CheatDetectionRepository cheatDetectionRepository;

    // ==================== OPERASI CRUD UTAMA ====================

    /**
     * Get all hasil ujian dengan PagedResponse - FIXED FORMAT
     */
    public PagedResponse<HasilUjian> getAllHasilUjian(int page, int size, String schoolId) throws IOException {
        logger.debug("Mengambil hasil ujian dengan page: {}, size: {}, school: {}", page, size, schoolId);

        List<HasilUjian> results = hasilUjianRepository.findAll(size * (page + 1));

        // Filter by school if specified
        if (schoolId != null && !schoolId.equals("*")) {
            results = results.stream()
                    .filter(hasil -> schoolId.equals(hasil.getIdSchool()))
                    .collect(Collectors.toList());
        }

        // Enrich data
        for (HasilUjian hasil : results) {
            enrichHasilUjianData(hasil);
        }

        // Apply pagination
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, results.size());

        List<HasilUjian> pagedResults = results.subList(startIndex, endIndex);

        return new PagedResponse<>(pagedResults, pagedResults.size(), "Successfully get data", 200);
    }

    /**
     * Get hasil ujian by ID dengan DefaultResponse - FIXED FORMAT
     */
    public DefaultResponse<HasilUjian> getHasilUjianById(String hasilUjianId) throws IOException {
        logger.debug("Mengambil hasil ujian dengan ID: {}", hasilUjianId);

        HasilUjian hasil = hasilUjianRepository.findById(hasilUjianId);
        if (hasil == null) {
            throw new ResourceNotFoundException("Hasil Ujian", "id", hasilUjianId);
        }

        // Enrich dengan data relasi
        enrichHasilUjianData(hasil);

        return new DefaultResponse<>(hasil, 1, "Successfully get data");
    }

    /**
     * Get hasil ujian by ujian dengan PagedResponse - FIXED FORMAT
     */
    public PagedResponse<HasilUjian> getHasilByUjian(String idUjian, int page, int size, Boolean includeAnalytics)
            throws IOException {
        logger.debug("Mengambil hasil ujian untuk ujian: {}", idUjian);

        List<HasilUjian> results = hasilUjianRepository.findByUjian(idUjian);

        if (includeAnalytics != null && includeAnalytics) {
            calculateComparativeAnalytics(results);
        }

        // Enrich data
        for (HasilUjian hasil : results) {
            enrichHasilUjianData(hasil);
        }

        // Apply pagination
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, results.size());

        List<HasilUjian> pagedResults = results.subList(startIndex, endIndex);

        return new PagedResponse<>(pagedResults, results.size(), "Successfully get data", 200);
    }

    /**
     * Get hasil ujian by peserta dengan PagedResponse - FIXED FORMAT
     */
    public PagedResponse<HasilUjian> getHasilByPeserta(String idPeserta, int page, int size) throws IOException {
        logger.debug("Mengambil hasil ujian untuk peserta: {}", idPeserta);

        List<HasilUjian> results = hasilUjianRepository.findByPeserta(idPeserta);

        // Sort by created date descending
        List<HasilUjian> sortedResults = results.stream()
                .sorted(Comparator.comparing(HasilUjian::getCreatedAt).reversed())
                .collect(Collectors.toList());

        // Enrich data
        for (HasilUjian hasil : sortedResults) {
            enrichHasilUjianData(hasil);
        }

        // Apply pagination
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, sortedResults.size());

        List<HasilUjian> pagedResults = sortedResults.subList(startIndex, endIndex);

        return new PagedResponse<>(pagedResults, sortedResults.size(), "Successfully get data", 200);
    }

    // Keep individual method for direct usage
    public HasilUjian getHasilByPesertaAndUjianDirect(String idUjian, String idPeserta,
            Integer attemptNumber, Boolean includeAnswers, Boolean includeSecurityData) throws IOException {

        List<HasilUjian> results = hasilUjianRepository.findByUjianAndPeserta(idUjian, idPeserta);

        if (results.isEmpty()) {
            throw new ResourceNotFoundException("Hasil Ujian",
                    "peserta=" + idPeserta + " ujian=" + idUjian, "");
        }

        // Ambil attempt terbaru atau attempt tertentu
        HasilUjian hasil;
        if (attemptNumber != null) {
            hasil = results.stream()
                    .filter(h -> attemptNumber.equals(h.getAttemptNumber()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Hasil Ujian", "attemptNumber",
                            attemptNumber.toString()));
        } else {
            hasil = results.stream()
                    .max(Comparator.comparing(h -> h.getAttemptNumber() != null ? h.getAttemptNumber() : 0))
                    .get();
        }

        // Filter data berdasarkan flags
        if (includeAnswers != null && !includeAnswers) {
            hasil.setJawabanPeserta(new HashMap<>());
            hasil.setJawabanBenar(new HashMap<>());
        }

        if (includeSecurityData != null && !includeSecurityData) {
            hasil.setSecurityFlags(new HashMap<>());
            hasil.setSecurityStatus("HIDDEN");
        }

        enrichHasilUjianData(hasil);
        return hasil;
    }

    /**
     * Buat hasil ujian baru dari session yang sudah selesai
     */
    public HasilUjian createHasilUjianFromSession(String sessionId, Map<String, Object> finalAnswers,
            boolean isAutoSubmit, String autoSubmitReason) throws IOException {

        logger.info("Membuat hasil ujian dari session: {}", sessionId);

        // Ambil data session ujian
        UjianSession session = ujianSessionRepository.findBySessionId(sessionId);
        if (session == null) {
            throw new ResourceNotFoundException("Ujian Session", "sessionId", sessionId);
        }

        // Validasi session sudah selesai
        if (!"COMPLETED".equals(session.getStatus()) && !isAutoSubmit) {
            throw new BadRequestException("Session belum completed");
        }

        // PERBAIKAN: Gunakan method dari session untuk create hasil ujian
        HasilUjian hasilUjian = session.createHasilUjian();

        // Override answers jika ada final answers
        if (finalAnswers != null && !finalAnswers.isEmpty()) {
            hasilUjian.setJawabanPeserta(finalAnswers);
        }

        // TAMBAHAN: Ambil dan proses cheat detections
        try {
            List<CheatDetection> violations = cheatDetectionRepository.findBySessionId(sessionId);
            for (CheatDetection violation : violations) {
                hasilUjian.addCheatDetection(violation);
                // Update violation dengan hasil ujian
                violation.updateHasilUjian(hasilUjian);
            }
        } catch (Exception e) {
            logger.warn("Error processing cheat detections for session {}: {}", sessionId, e.getMessage());
        }

        // Hitung skor dan lakukan analisis
        calculateScoresAndAnalysis(hasilUjian);

        // Evaluasi status keamanan (sudah include violations)
        evaluateSecurityStatus(hasilUjian);

        // Generate hash untuk integritas data
        hasilUjian.generateSecurityHash();

        // Simpan alasan auto submit jika ada
        if (isAutoSubmit && autoSubmitReason != null) {
            Map<String, Object> metadata = hasilUjian.getMetadata();
            metadata.put("autoSubmitReason", autoSubmitReason);
            hasilUjian.setMetadata(metadata);
        }

        HasilUjian savedResult = hasilUjianRepository.save(hasilUjian);
        logger.info("Hasil ujian berhasil dibuat dengan ID: {}", savedResult.getIdHasilUjian());

        return savedResult;
    }

    // ==================== OPERASI ANALYTICS ====================

    /**
     * Update data analytics untuk hasil ujian
     */
    public HasilUjian updateAnalytics(String idHasilUjian, Map<String, Integer> timeSpentPerQuestion,
            Map<String, Object> answerHistory, Map<String, Integer> attemptCountPerQuestion,
            String workingPattern, Double consistencyScore, Boolean hasSignsOfGuessing,
            Boolean hasSignsOfAnxiety, String confidenceLevel, Map<String, Object> topicPerformance,
            List<String> strengths, List<String> weaknesses, List<String> recommendedStudyAreas) throws IOException {

        HasilUjian hasil = getHasilUjianById(idHasilUjian).getContent();

        // Update field analytics
        if (timeSpentPerQuestion != null) {
            hasil.setTimeSpentPerQuestion(timeSpentPerQuestion);
        }
        if (answerHistory != null) {
            hasil.setAnswerHistory(answerHistory);
        }
        if (attemptCountPerQuestion != null) {
            hasil.setAttemptCountPerQuestion(attemptCountPerQuestion);
        }
        if (workingPattern != null) {
            hasil.setWorkingPattern(workingPattern);
        }
        if (consistencyScore != null) {
            hasil.setConsistencyScore(consistencyScore);
        }
        if (hasSignsOfGuessing != null) {
            hasil.setHasSignsOfGuessing(hasSignsOfGuessing);
        }
        if (hasSignsOfAnxiety != null) {
            hasil.setHasSignsOfAnxiety(hasSignsOfAnxiety);
        }
        if (confidenceLevel != null) {
            hasil.setConfidenceLevel(confidenceLevel);
        }
        if (topicPerformance != null) {
            hasil.setTopicPerformance(topicPerformance);
        }
        if (strengths != null) {
            hasil.setStrengths(strengths);
        }
        if (weaknesses != null) {
            hasil.setWeaknesses(weaknesses);
        }
        if (recommendedStudyAreas != null) {
            hasil.setRecommendedStudyAreas(recommendedStudyAreas);
        }

        // Analisis ulang pola jawaban
        hasil.performCompleteAnalysis();

        return hasilUjianRepository.save(hasil);
    }

    /**
     * Generate analytics lanjutan
     */
    public Map<String, Object> generateAdvancedAnalytics(String analysisScope, String idPeserta, String idUjian,
            Boolean includeComparative, Boolean includePredictive, Boolean includeRecommendations) throws IOException {

        Map<String, Object> analytics = new HashMap<>();

        switch (analysisScope) {
            case "INDIVIDUAL":
                analytics = generateIndividualAnalytics(idPeserta, idUjian);
                break;
            case "CLASS":
                analytics = generateClassAnalytics(idUjian);
                break;
            case "SCHOOL":
                analytics = generateSchoolAnalytics(idPeserta); // gunakan idPeserta sebagai idSchool
                break;
            case "UJIAN":
                analytics = generateUjianAnalytics(idUjian);
                break;
        }

        if (includeComparative != null && includeComparative) {
            analytics.put("comparative", generateComparativeAnalytics(idUjian, idPeserta));
        }

        if (includePredictive != null && includePredictive) {
            analytics.put("predictive", generatePredictiveAnalytics(idPeserta));
        }

        if (includeRecommendations != null && includeRecommendations) {
            analytics.put("recommendations", generateRecommendations(idUjian, idPeserta));
        }

        return analytics;
    }

    // ==================== OPERASI SECURITY ====================

    /**
     * Update status keamanan
     */
    public boolean updateSecurityStatus(String idHasilUjian, String securityStatus,
            Map<String, Object> securityFlags, String updatedBy, String reason) throws IOException {

        HasilUjian hasil = getHasilUjianById(idHasilUjian).getContent();

        // Update status security
        hasil.setSecurityStatus(securityStatus);

        if (securityFlags != null) {
            hasil.getSecurityFlags().putAll(securityFlags);
        }

        // Tambah audit trail
        Map<String, Object> auditData = new HashMap<>();
        auditData.put("updatedBy", updatedBy);
        auditData.put("reason", reason);
        auditData.put("timestamp", Instant.now().toString());

        hasil.getSecurityFlags().put("lastUpdate", auditData);

        return hasilUjianRepository.updateSecurityStatus(idHasilUjian, securityStatus, hasil.getSecurityFlags());
    }

    /**
     * Submit appeal
     */
    public HasilUjian submitAppeal(String idHasilUjian, String appealReason, String appealDescription,
            List<String> attachmentIds, String idPeserta) throws IOException {

        HasilUjian hasil = getHasilUjianById(idHasilUjian).getContent();

        // Validasi bisa banding
        if (Boolean.TRUE.equals(hasil.getHasAppeal()) &&
                !"REJECTED".equals(hasil.getAppealStatus()) &&
                !"WITHDRAWN".equals(hasil.getAppealStatus())) {
            throw new BadRequestException("Appeal sudah ada dan masih dalam proses");
        }

        // Set data appeal
        hasil.setHasAppeal(true);
        hasil.setAppealReason(appealReason);
        hasil.setAppealStatus("PENDING");
        hasil.setAppealSubmittedAt(Instant.now());

        // Tambah detail appeal
        Map<String, Object> appealData = hasil.getAppealData();
        appealData.put("description", appealDescription);
        appealData.put("attachmentIds", attachmentIds);
        appealData.put("submittedBy", idPeserta);
        appealData.put("submittedAt", Instant.now().toString());
        hasil.setAppealData(appealData);

        return hasilUjianRepository.save(hasil);
    }

    /**
     * Review appeal
     */
    public HasilUjian reviewAppeal(String idHasilUjian, String appealStatus, String reviewedBy,
            String reviewNote, String feedback, Boolean adjustScore, Double newScore) throws IOException {

        HasilUjian hasil = getHasilUjianById(idHasilUjian).getContent();

        // Validasi appeal ada
        if (!Boolean.TRUE.equals(hasil.getHasAppeal())) {
            throw new BadRequestException("Tidak ada appeal untuk hasil ujian ini");
        }

        // Update status appeal
        hasil.setAppealStatus(appealStatus);
        hasil.setAppealReviewedBy(reviewedBy);

        // Tambah data review
        Map<String, Object> appealData = hasil.getAppealData();
        appealData.put("reviewNote", reviewNote);
        appealData.put("reviewedBy", reviewedBy);
        appealData.put("reviewedAt", Instant.now().toString());
        appealData.put("feedback", feedback);
        hasil.setAppealData(appealData);

        // Sesuaikan skor jika disetujui
        if ("APPROVED".equals(appealStatus) && Boolean.TRUE.equals(adjustScore)) {
            if (newScore != null) {
                hasil.setTotalSkor(newScore);
                // Hitung ulang field terkait
                if (hasil.getSkorMaksimal() != null && hasil.getSkorMaksimal() > 0) {
                    double newPercentage = (newScore / hasil.getSkorMaksimal()) * 100.0;
                    hasil.setPersentase(newPercentage);
                }
                hasil.calculateGrade();
                hasil.determinePassStatus(60.0); // Asumsi 60% adalah passing

                // Generate ulang hash
                hasil.generateSecurityHash();
            }
        }

        return hasilUjianRepository.save(hasil);
    }

    /**
     * Verifikasi hasil ujian
     */
    public boolean verifyResult(String idHasilUjian, Boolean isVerified, String verifiedBy,
            String verificationNote, Boolean regenerateHash) throws IOException {

        HasilUjian hasil = getHasilUjianById(idHasilUjian).getContent();

        // Verifikasi integritas dulu
        if (!hasil.validateIntegrity()) {
            throw new BadRequestException("Data integrity validation failed");
        }

        // Update status verifikasi
        hasil.setIsVerified(isVerified);
        hasil.setVerifiedBy(verifiedBy);
        hasil.setVerificationTime(Instant.now());

        // Tambah catatan verifikasi
        if (verificationNote != null) {
            Map<String, Object> metadata = hasil.getMetadata();
            metadata.put("verificationNote", verificationNote);
            hasil.setMetadata(metadata);
        }

        // Generate ulang hash jika diminta
        if (Boolean.TRUE.equals(regenerateHash)) {
            hasil.generateSecurityHash();
        }

        return hasilUjianRepository.updateVerificationStatus(idHasilUjian, isVerified, verifiedBy);
    }

    // ==================== OPERASI STATISTIK ====================

    /**
     * Ambil statistik untuk ujian
     */
    public Map<String, Object> getUjianStatistics(String idUjian) throws IOException {
        List<HasilUjian> results = hasilUjianRepository.findByUjian(idUjian);

        Map<String, Object> stats = new HashMap<>();

        if (results.isEmpty()) {
            return stats;
        }

        // Statistik dasar
        stats.put("totalParticipants", results.size());
        stats.put("completedParticipants",
                results.stream().filter(h -> "SELESAI".equals(h.getStatusPengerjaan())).count());
        stats.put("incompleteParticipants",
                results.stream().filter(h -> !"SELESAI".equals(h.getStatusPengerjaan())).count());

        // Statistik skor
        List<Double> scores = results.stream()
                .filter(h -> h.getPersentase() != null)
                .map(HasilUjian::getPersentase)
                .sorted()
                .collect(Collectors.toList());

        if (!scores.isEmpty()) {
            stats.put("averageScore", scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
            stats.put("medianScore", calculateMedian(scores));
            stats.put("highestScore", scores.get(scores.size() - 1));
            stats.put("lowestScore", scores.get(0));
            stats.put("standardDeviation", calculateStandardDeviation(scores));
        }

        // Distribusi nilai
        Map<String, Integer> gradeDistribution = new HashMap<>();
        Map<String, Double> gradePercentages = new HashMap<>();

        results.stream()
                .filter(h -> h.getNilaiHuruf() != null)
                .forEach(h -> gradeDistribution.merge(h.getNilaiHuruf(), 1, Integer::sum));

        gradeDistribution
                .forEach((grade, count) -> gradePercentages.put(grade, (double) count / results.size() * 100.0));

        stats.put("gradeDistribution", gradeDistribution);
        stats.put("gradePercentages", gradePercentages);

        // Analisis lulus/tidak lulus
        long passedCount = results.stream().filter(h -> Boolean.TRUE.equals(h.getLulus())).count();
        stats.put("passedCount", passedCount);
        stats.put("failedCount", results.size() - passedCount);
        stats.put("passRate", results.size() > 0 ? (double) passedCount / results.size() * 100.0 : 0.0);

        return stats;
    }

    // ==================== METODE ANALYTICS PRIVATE ====================

    /**
     * Generate analytics individual untuk peserta tertentu
     */
    private Map<String, Object> generateIndividualAnalytics(String idPeserta, String idUjian) throws IOException {
        Map<String, Object> analytics = new HashMap<>();

        List<HasilUjian> studentResults = hasilUjianRepository.findByUjianAndPeserta(idUjian, idPeserta);
        if (studentResults.isEmpty()) {
            return analytics;
        }

        HasilUjian latestResult = studentResults.get(studentResults.size() - 1);

        // Performa dasar
        analytics.put("totalScore", latestResult.getTotalSkor());
        analytics.put("percentage", latestResult.getPersentase());
        analytics.put("grade", latestResult.getNilaiHuruf());
        analytics.put("passed", latestResult.getLulus());

        // Analisis waktu
        if (latestResult.getTimeSpentPerQuestion() != null) {
            Map<String, Object> timeAnalytics = new HashMap<>();
            timeAnalytics.put("averageTimePerQuestion",
                    latestResult.getTimeSpentPerQuestion().values().stream()
                            .mapToInt(Integer::intValue).average().orElse(0.0));
            timeAnalytics.put("totalTime", latestResult.getDurasiPengerjaan());
            timeAnalytics.put("workingPattern", latestResult.getWorkingPattern());
            analytics.put("timeAnalytics", timeAnalytics);
        }

        // Insight behavioral
        Map<String, Object> behavioral = new HashMap<>();
        behavioral.put("consistencyScore", latestResult.getConsistencyScore());
        behavioral.put("confidenceLevel", latestResult.getConfidenceLevel());
        behavioral.put("hasSignsOfGuessing", latestResult.getHasSignsOfGuessing());
        behavioral.put("hasSignsOfAnxiety", latestResult.getHasSignsOfAnxiety());
        behavioral.put("totalAnswerChanges", latestResult.getTotalAnswerChanges());
        behavioral.put("answerChangeSuccessRate", latestResult.getAnswerChangeSuccessRate());
        analytics.put("behavioralAnalytics", behavioral);

        // Performance insights
        analytics.put("strengths", latestResult.getStrengths());
        analytics.put("weaknesses", latestResult.getWeaknesses());
        analytics.put("recommendedStudyAreas", latestResult.getRecommendedStudyAreas());
        analytics.put("topicPerformance", latestResult.getTopicPerformance());

        return analytics;
    }

    /**
     * Generate analytics kelas untuk ujian
     */
    private Map<String, Object> generateClassAnalytics(String idUjian) throws IOException {
        List<HasilUjian> classResults = hasilUjianRepository.findByUjian(idUjian);
        Map<String, Object> analytics = new HashMap<>();

        if (classResults.isEmpty()) {
            return analytics;
        }

        // Overview performa kelas
        analytics.put("totalStudents", classResults.size());
        analytics.put("completionRate",
                classResults.stream().filter(h -> "SELESAI".equals(h.getStatusPengerjaan())).count()
                        / (double) classResults.size() * 100.0);

        // Distribusi skor
        List<Double> scores = classResults.stream()
                .filter(h -> h.getPersentase() != null)
                .map(HasilUjian::getPersentase)
                .collect(Collectors.toList());

        if (!scores.isEmpty()) {
            analytics.put("averageScore", scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
            analytics.put("scoreDistribution", calculateScoreDistribution(scores));
        }

        // Analisis pola umum
        Map<String, Object> commonPatterns = new HashMap<>();

        // Distribusi pola kerja
        Map<String, Long> workingPatterns = classResults.stream()
                .filter(h -> h.getWorkingPattern() != null)
                .collect(Collectors.groupingBy(HasilUjian::getWorkingPattern, Collectors.counting()));
        commonPatterns.put("workingPatterns", workingPatterns);

        // Distribusi tingkat kepercayaan diri
        Map<String, Long> confidenceLevels = classResults.stream()
                .filter(h -> h.getConfidenceLevel() != null)
                .collect(Collectors.groupingBy(HasilUjian::getConfidenceLevel, Collectors.counting()));
        commonPatterns.put("confidenceLevels", confidenceLevels);

        analytics.put("commonPatterns", commonPatterns);

        // Kekuatan dan kelemahan kelas
        Map<String, Integer> allStrengths = new HashMap<>();
        Map<String, Integer> allWeaknesses = new HashMap<>();

        classResults.forEach(hasil -> {
            if (hasil.getStrengths() != null) {
                hasil.getStrengths().forEach(strength -> allStrengths.merge(strength, 1, Integer::sum));
            }
            if (hasil.getWeaknesses() != null) {
                hasil.getWeaknesses().forEach(weakness -> allWeaknesses.merge(weakness, 1, Integer::sum));
            }
        });

        analytics.put("classStrengths", allStrengths);
        analytics.put("classWeaknesses", allWeaknesses);

        return analytics;
    }

    /**
     * Generate analytics sekolah
     */
    private Map<String, Object> generateSchoolAnalytics(String idSchool) throws IOException {
        List<HasilUjian> schoolResults = hasilUjianRepository.findBySchool(idSchool);
        Map<String, Object> analytics = new HashMap<>();

        if (schoolResults.isEmpty()) {
            return analytics;
        }

        // Metrik performa tingkat sekolah
        analytics.put("totalResults", schoolResults.size());

        // Trend performa berdasarkan ujian
        Map<String, List<HasilUjian>> resultsByUjian = schoolResults.stream()
                .collect(Collectors.groupingBy(HasilUjian::getIdUjian));

        Map<String, Object> ujianPerformance = new HashMap<>();
        resultsByUjian.forEach((ujianId, results) -> {
            Map<String, Object> ujianStats = new HashMap<>();
            ujianStats.put("participantCount", results.size());
            ujianStats.put("averageScore", results.stream()
                    .filter(h -> h.getPersentase() != null)
                    .mapToDouble(HasilUjian::getPersentase)
                    .average().orElse(0.0));
            ujianStats.put("passRate", results.stream()
                    .filter(h -> Boolean.TRUE.equals(h.getLulus()))
                    .count() / (double) results.size() * 100.0);
            ujianPerformance.put(ujianId, ujianStats);
        });

        analytics.put("ujianPerformance", ujianPerformance);

        return analytics;
    }

    /**
     * Generate analytics ujian
     */
    private Map<String, Object> generateUjianAnalytics(String idUjian) throws IOException {
        return getUjianStatistics(idUjian); // Gunakan ulang metode yang ada
    }

    /**
     * Generate analytics komparatif
     */
    private Map<String, Object> generateComparativeAnalytics(String idUjian, String idPeserta) throws IOException {
        Map<String, Object> comparative = new HashMap<>();

        // Ambil hasil dasar
        List<HasilUjian> baseResults = hasilUjianRepository.findByUjianAndPeserta(idUjian, idPeserta);

        if (baseResults.isEmpty()) {
            return comparative;
        }

        HasilUjian baseResult = baseResults.get(baseResults.size() - 1);

        // Bandingkan dengan rata-rata kelas
        List<HasilUjian> classResults = hasilUjianRepository.findByUjian(idUjian);
        if (!classResults.isEmpty()) {
            double classAverage = classResults.stream()
                    .filter(h -> h.getPersentase() != null)
                    .mapToDouble(HasilUjian::getPersentase)
                    .average().orElse(0.0);

            comparative.put("classAverage", classAverage);
            comparative.put("aboveClassAverage", baseResult.getPersentase() > classAverage);
            comparative.put("percentileRank", calculatePercentileRank(baseResult, classResults));
        }

        return comparative;
    }

    /**
     * Generate analytics prediktif
     */
    private Map<String, Object> generatePredictiveAnalytics(String idPeserta) throws IOException {
        Map<String, Object> predictive = new HashMap<>();

        // Ambil hasil historis peserta
        List<HasilUjian> historicalResults = hasilUjianRepository.findByPeserta(idPeserta);

        if (historicalResults.size() < 2) {
            predictive.put("insufficientData", true);
            return predictive;
        }

        // Hitung trend performa
        List<Double> scores = historicalResults.stream()
                .filter(h -> h.getPersentase() != null)
                .map(HasilUjian::getPersentase)
                .collect(Collectors.toList());

        if (scores.size() >= 2) {
            double trend = calculateTrend(scores);
            predictive.put("performanceTrend", trend > 0 ? "IMPROVING" : trend < 0 ? "DECLINING" : "STABLE");
            predictive.put("trendValue", trend);

            // Prediksi sederhana untuk performa berikutnya
            double lastScore = scores.get(scores.size() - 1);
            double predictedScore = Math.max(0, Math.min(100, lastScore + trend));
            predictive.put("predictedNextScore", predictedScore);
        }

        return predictive;
    }

    /**
     * Generate rekomendasi
     */
    private Map<String, Object> generateRecommendations(String idUjian, String idPeserta) throws IOException {
        Map<String, Object> recommendations = new HashMap<>();

        List<HasilUjian> results = hasilUjianRepository.findByUjianAndPeserta(idUjian, idPeserta);

        if (results.isEmpty()) {
            return recommendations;
        }

        HasilUjian result = results.get(results.size() - 1);
        List<String> recommendationsList = new ArrayList<>();

        // Rekomendasi berdasarkan performa
        if (result.getPersentase() != null) {
            if (result.getPersentase() < 60) {
                recommendationsList.add("Fokus pada pemahaman konsep dasar");
                recommendationsList.add("Tambah latihan soal dengan tingkat kesulitan bertahap");
            } else if (result.getPersentase() < 80) {
                recommendationsList.add("Tingkatkan latihan soal dengan variasi yang lebih kompleks");
                recommendationsList.add("Review materi pada area yang masih lemah");
            }
        }

        // Rekomendasi pola kerja
        if ("RUSHED".equals(result.getWorkingPattern())) {
            recommendationsList.add("Latih manajemen waktu dengan simulasi ujian");
            recommendationsList.add("Fokus pada akurasi dibanding kecepatan");
        } else if ("THOROUGH".equals(result.getWorkingPattern())) {
            recommendationsList.add("Latih efisiensi waktu dalam menjawab soal");
        }

        // Rekomendasi berdasarkan confidence
        if ("LOW".equals(result.getConfidenceLevel())) {
            recommendationsList.add("Tingkatkan kepercayaan diri dengan latihan rutin");
            recommendationsList.add("Review jawaban dan pahami alasan jawaban yang benar");
        }

        // Tambah rekomendasi spesifik kelemahan
        if (result.getWeaknesses() != null && !result.getWeaknesses().isEmpty()) {
            result.getWeaknesses()
                    .forEach(weakness -> recommendationsList.add("Pelajari lebih mendalam materi: " + weakness));
        }

        recommendations.put("studyRecommendations", recommendationsList);
        recommendations.put("recommendedStudyTime", calculateRecommendedStudyTime(result));
        recommendations.put("priorityAreas", result.getRecommendedStudyAreas());

        return recommendations;
    }

    // ==================== METODE UTILITY ====================

    private void enrichHasilUjianData(HasilUjian hasil) throws IOException {
        // Muat entitas terkait jika belum dimuat
        if (hasil.getUjian() == null && hasil.getIdUjian() != null) {
            Ujian ujian = ujianRepository.findById(hasil.getIdUjian());
            hasil.setUjian(ujian);
        }

        if (hasil.getPeserta() == null && hasil.getIdPeserta() != null) {
            User peserta = userRepository.findById(hasil.getIdPeserta());
            hasil.setPeserta(peserta);
        }

        if (hasil.getSchool() == null && hasil.getIdSchool() != null) {
            School school = schoolRepository.findById(hasil.getIdSchool());
            hasil.setSchool(school);
        }
    }

    private void calculateComparativeAnalytics(List<HasilUjian> results) {
        if (results.size() < 2)
            return;

        // Urutkan berdasarkan persentase untuk ranking
        List<HasilUjian> sortedResults = results.stream()
                .filter(h -> h.getPersentase() != null)
                .sorted((a, b) -> Double.compare(b.getPersentase(), a.getPersentase()))
                .collect(Collectors.toList());

        // Hitung rank dan percentile
        for (int i = 0; i < sortedResults.size(); i++) {
            HasilUjian hasil = sortedResults.get(i);
            hasil.setRankInClass(i + 1);

            double percentile = ((double) (sortedResults.size() - i) / sortedResults.size()) * 100;
            hasil.setPercentileRank(percentile);

            if (percentile >= 75) {
                hasil.setRelativePerformance("ABOVE_AVERAGE");
            } else if (percentile >= 25) {
                hasil.setRelativePerformance("AVERAGE");
            } else {
                hasil.setRelativePerformance("BELOW_AVERAGE");
            }
        }
    }

    // ==================== FIXED SCORING CALCULATION ====================

    private void calculateScoresAndAnalysis(HasilUjian hasilUjian) throws IOException {
        if (hasilUjian.getJawabanPeserta() == null || hasilUjian.getJawabanPeserta().isEmpty()) {
            return;
        }

        // Ambil data ujian untuk evaluasi
        Ujian ujian = ujianRepository.findById(hasilUjian.getIdUjian());
        if (ujian == null || ujian.getBankSoalList() == null) {
            return;
        }

        Map<String, Boolean> jawabanBenar = new HashMap<>();
        Map<String, Double> skorPerSoal = new HashMap<>();
        double totalSkor = 0.0;
        double skorMaksimal = 0.0;
        int jumlahBenar = 0;
        int jumlahSalah = 0;
        int jumlahKosong = 0;

        // Evaluasi setiap soal berdasarkan jenis
        for (BankSoalUjian soal : ujian.getBankSoalList()) {
            String idBankSoal = soal.getIdBankSoal();
            double bobotSoal = Double.parseDouble(soal.getBobot());
            skorMaksimal += bobotSoal; // FIX: Add to skorMaksimal

            Object jawabanPeserta = hasilUjian.getJawabanPeserta().get(idBankSoal);
            boolean isCorrect = false;
            double skor = 0.0;

            if (jawabanPeserta != null && soal.getJawabanBenar() != null) {
                switch (soal.getJenisSoal()) {
                    case "PG":
                        isCorrect = evaluateSingleChoice(jawabanPeserta, soal.getJawabanBenar());
                        break;
                    case "MULTI":
                        isCorrect = evaluateMultipleChoice(jawabanPeserta, soal.getJawabanBenar());
                        break;
                    case "ISIAN":
                        isCorrect = evaluateEssay(jawabanPeserta, soal.getJawabanBenar(), soal.getToleransiTypo());
                        break;
                    case "COCOK":
                        isCorrect = evaluateMatchingPairs(jawabanPeserta, soal.getJawabanBenar());
                        break;
                }

                if (isCorrect) {
                    skor = bobotSoal;
                    jumlahBenar++;
                } else {
                    jumlahSalah++;
                }
            } else {
                jumlahKosong++;
            }

            jawabanBenar.put(idBankSoal, isCorrect);
            skorPerSoal.put(idBankSoal, skor);
            totalSkor += skor;
        }

        // Set nilai yang dihitung
        hasilUjian.setJawabanBenar(jawabanBenar);
        hasilUjian.setSkorPerSoal(skorPerSoal);
        hasilUjian.setTotalSkor(totalSkor);
        hasilUjian.setSkorMaksimal(skorMaksimal);
        hasilUjian.setJumlahBenar(jumlahBenar);
        hasilUjian.setJumlahSalah(jumlahSalah);
        hasilUjian.setJumlahKosong(jumlahKosong);
        hasilUjian.setTotalSoal(ujian.getJumlahSoal());

        // Hitung persentase
        if (skorMaksimal > 0) {
            double persentase = (totalSkor / skorMaksimal) * 100.0;
            hasilUjian.setPersentase(persentase);
        }

        // Tentukan grade dan status lulus
        hasilUjian.calculateGrade();
        double minPassingScore = ujian.getMinPassingScore() != null ? ujian.getMinPassingScore() : 60.0;

        // Jika minPassingScore dalam bentuk absolut, convert ke persentase
        if (minPassingScore <= skorMaksimal && skorMaksimal > 0) {
            minPassingScore = (minPassingScore / skorMaksimal) * 100.0;
        }

        hasilUjian.determinePassStatus(minPassingScore);

        // Jalankan analytics
        hasilUjian.performCompleteAnalysis();
    }

    // Metode helper perhitungan
    private double calculateMedian(List<Double> sortedScores) {
        int size = sortedScores.size();
        if (size % 2 == 0) {
            return (sortedScores.get(size / 2 - 1) + sortedScores.get(size / 2)) / 2.0;
        } else {
            return sortedScores.get(size / 2);
        }
    }

    private double calculateStandardDeviation(List<Double> scores) {
        double mean = scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = scores.stream()
                .mapToDouble(score -> Math.pow(score - mean, 2))
                .average().orElse(0.0);
        return Math.sqrt(variance);
    }

    private Map<String, Integer> calculateScoreDistribution(List<Double> scores) {
        Map<String, Integer> distribution = new HashMap<>();

        scores.forEach(score -> {
            String range;
            if (score >= 90)
                range = "90-100";
            else if (score >= 80)
                range = "80-89";
            else if (score >= 70)
                range = "70-79";
            else if (score >= 60)
                range = "60-69";
            else
                range = "0-59";

            distribution.merge(range, 1, Integer::sum);
        });

        return distribution;
    }

    private double calculatePercentileRank(HasilUjian baseResult, List<HasilUjian> classResults) {
        if (baseResult.getPersentase() == null)
            return 0.0;

        long betterCount = classResults.stream()
                .filter(h -> h.getPersentase() != null)
                .filter(h -> h.getPersentase() < baseResult.getPersentase())
                .count();

        return ((double) betterCount / classResults.size()) * 100.0;
    }

    private double calculateTrend(List<Double> scores) {
        if (scores.size() < 2)
            return 0.0;

        // Perhitungan trend linear sederhana
        double firstScore = scores.get(0);
        double lastScore = scores.get(scores.size() - 1);

        return (lastScore - firstScore) / scores.size();
    }

    private int calculateRecommendedStudyTime(HasilUjian result) {
        if (result.getPersentase() == null)
            return 120; // Default 2 jam

        double percentage = result.getPersentase();

        if (percentage < 50)
            return 180; // 3 jam
        else if (percentage < 70)
            return 150; // 2.5 jam
        else if (percentage < 80)
            return 120; // 2 jam
        else
            return 90; // 1.5 jam
    }

    private boolean evaluateSingleChoice(Object jawabanPeserta, List<String> jawabanBenar) {
        // For single choice, jawabanBenar should have only one correct answer
        if (jawabanBenar.isEmpty()) {
            return false;
        }

        String correctAnswer = jawabanBenar.get(0);
        String studentAnswer = jawabanPeserta != null ? jawabanPeserta.toString().trim() : "";

        return correctAnswer.equals(studentAnswer);
    }

    private boolean evaluateEssay(Object jawabanPeserta, List<String> jawabanBenar, String toleransiTypo) {
        if (jawabanPeserta == null || jawabanBenar.isEmpty()) {
            return false;
        }

        String studentAnswer = jawabanPeserta.toString().trim().toLowerCase();

        // Check against all possible correct answers
        for (String correctAnswer : jawabanBenar) {
            if (correctAnswer == null)
                continue;

            String normalizedCorrect = correctAnswer.trim().toLowerCase();

            // Exact match
            if (studentAnswer.equals(normalizedCorrect)) {
                return true;
            }

            // If typo tolerance is enabled, check for similarity
            if ("ENABLED".equals(toleransiTypo)) {
                // Simple typo tolerance: allow up to 2 character differences for answers > 3
                // chars
                if (normalizedCorrect.length() > 3
                        && calculateLevenshteinDistance(studentAnswer, normalizedCorrect) <= 2) {
                    return true;
                }
                // For shorter answers, require exact match
                else if (normalizedCorrect.length() <= 3 && studentAnswer.equals(normalizedCorrect)) {
                    return true;
                }
            }
        }

        return false;
    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private boolean evaluateMultipleChoice(Object jawabanPeserta, List<String> jawabanBenar) {
        // Expected: ["B", "C"] from jawabanBenar
        // Input might be List<String> or comma-separated string
        Set<String> correctAnswers = new HashSet<>(jawabanBenar);
        Set<String> studentAnswers = new HashSet<>();

        if (jawabanPeserta instanceof List) {
            studentAnswers.addAll((List<String>) jawabanPeserta);
        } else {
            String[] answers = jawabanPeserta.toString().split(",");
            for (String answer : answers) {
                studentAnswers.add(answer.trim());
            }
        }

        return correctAnswers.equals(studentAnswers);
    }

    private boolean evaluateMatchingPairs(Object jawabanPeserta, List<String> jawabanBenar) {
        // Expected: ["a=f", "b=e", "c=d"] from jawabanBenar
        // Input should be Map<String, String> or similar format
        Set<String> correctPairs = new HashSet<>(jawabanBenar);
        Set<String> studentPairs = new HashSet<>();

        if (jawabanPeserta instanceof Map) {
            Map<String, String> studentMap = (Map<String, String>) jawabanPeserta;
            studentMap.forEach((key, value) -> studentPairs.add(key + "=" + value));
        }

        return correctPairs.equals(studentPairs);
    }

    /**
     * PERBAIKAN: Evaluasi security status dengan data violations
     */
    private void evaluateSecurityStatus(HasilUjian hasilUjian) throws IOException {
        try {
            // Cek pelanggaran keamanan dari session (sudah diproses di
            // createHasilUjianFromSession)
            Map<String, Object> metadata = hasilUjian.getMetadata();
            List<Map<String, Object>> violations = (List<Map<String, Object>>) metadata.get("violations");

            if (violations != null && !violations.isEmpty()) {
                // Security status sudah di-set oleh updateSecurityStatusFromViolations

                // Tambah security flags
                Map<String, Object> securityFlags = hasilUjian.getSecurityFlags();
                securityFlags.put("hasViolations", true);
                securityFlags.put("violationCount", violations.size());
                securityFlags.put("evaluatedAt", Instant.now().toString());

                // Count by severity
                Map<String, Long> severityCounts = violations.stream()
                        .collect(Collectors.groupingBy(
                                v -> (String) v.get("severity"),
                                Collectors.counting()));
                securityFlags.put("severityCounts", severityCounts);

                hasilUjian.setSecurityFlags(securityFlags);
            } else {
                hasilUjian.setSecurityStatus("CLEAN");
            }
        } catch (Exception e) {
            logger.error("Error evaluating security status for session: {}", hasilUjian.getSessionId(), e);
            hasilUjian.setSecurityStatus("UNKNOWN");
        }
    }
}