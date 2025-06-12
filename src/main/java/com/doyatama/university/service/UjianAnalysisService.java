package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.UjianAnalysis;
import com.doyatama.university.model.Ujian;
import com.doyatama.university.model.HasilUjian;
import com.doyatama.university.model.UjianSession;
import com.doyatama.university.model.CheatDetection;
import com.doyatama.university.model.School;
import com.doyatama.university.model.User;
import com.doyatama.university.model.BankSoalUjian;
import com.doyatama.university.payload.DefaultResponse;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.payload.UjianAnalysisRequest;
import com.doyatama.university.repository.UjianAnalysisRepository;
import com.doyatama.university.repository.UjianRepository;
import com.doyatama.university.repository.HasilUjianRepository;
import com.doyatama.university.repository.UjianSessionRepository;
import com.doyatama.university.repository.CheatDetectionRepository;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.util.AppConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UjianAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(UjianAnalysisService.class);

    @Autowired
    private UjianAnalysisRepository ujianAnalysisRepository;

    @Autowired
    private UjianRepository ujianRepository;

    @Autowired
    private HasilUjianRepository hasilUjianRepository;

    @Autowired
    private UjianSessionRepository ujianSessionRepository;

    @Autowired
    private CheatDetectionRepository cheatDetectionRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private UserRepository userRepository;

    // ==================== CRUD OPERATIONS ====================

    /**
     * Get all analysis with pagination and filtering
     */
    public PagedResponse<UjianAnalysis> getAllAnalysis(int page, int size, String userID, String schoolID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<UjianAnalysis> analysisResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            analysisResponse = ujianAnalysisRepository.findAll(size);
        } else {
            analysisResponse = ujianAnalysisRepository.findBySchoolId(schoolID, size);
        }

        return new PagedResponse<>(analysisResponse, analysisResponse.size(), "Successfully get data", 200);
    }

    /**
     * Get analysis by ujian ID
     */
    public PagedResponse<UjianAnalysis> getAnalysisByUjian(String ujianId, int page, int size, String schoolID)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<UjianAnalysis> analysisResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            analysisResponse = ujianAnalysisRepository.findByUjianId(ujianId, size);
        } else {
            analysisResponse = ujianAnalysisRepository.findByUjianIdAndSchoolId(ujianId, schoolID, size);
        }

        return new PagedResponse<>(analysisResponse, analysisResponse.size(), "Successfully get data", 200);
    }

    /**
     * Get analysis by type
     */
    public PagedResponse<UjianAnalysis> getAnalysisByType(String analysisType, int page, int size, String schoolID)
            throws IOException {
        validatePageNumberAndSize(page, size);
        validateAnalysisType(analysisType);

        List<UjianAnalysis> analysisResponse;

        if (schoolID.equalsIgnoreCase("*")) {
            analysisResponse = ujianAnalysisRepository.findByAnalysisType(analysisType, size);
        } else {
            analysisResponse = ujianAnalysisRepository.findByAnalysisTypeAndSchoolId(analysisType, schoolID, size);
        }

        return new PagedResponse<>(analysisResponse, analysisResponse.size(), "Successfully get data", 200);
    }

    /**
     * Get single analysis by ID
     */
    public DefaultResponse<UjianAnalysis> getAnalysisById(String analysisId) throws IOException {
        UjianAnalysis analysis = ujianAnalysisRepository.findById(analysisId);
        if (analysis == null) {
            throw new ResourceNotFoundException("UjianAnalysis", "id", analysisId);
        }
        return new DefaultResponse<>(analysis, 1, "Successfully get data");
    }

    /**
     * Delete analysis by ID
     */
    public void deleteAnalysisById(String analysisId) throws IOException {
        UjianAnalysis analysis = ujianAnalysisRepository.findById(analysisId);
        if (analysis == null) {
            throw new ResourceNotFoundException("UjianAnalysis", "id", analysisId);
        }

        ujianAnalysisRepository.deleteById(analysisId);
        logger.info("Analysis deleted: {}", analysisId);
    }

    // ==================== ANALYSIS GENERATION ====================

    /**
     * Generate comprehensive analysis
     */
    public UjianAnalysis generateAnalysis(UjianAnalysisRequest.GenerateAnalysisRequest request) throws IOException {
        logger.debug("Generating analysis for ujian: {}", request.getIdUjian());

        // Validate request
        validateGenerateAnalysisRequest(request);

        // Validate ujian exists and is completed
        Ujian ujian = validateUjianForAnalysis(request.getIdUjian());

        // Validate school
        School school = validateSchool(request.getIdSchool());

        // Check if analysis already exists
        List<UjianAnalysis> existingAnalysis = ujianAnalysisRepository.findByUjianIdAndAnalysisType(
                request.getIdUjian(), request.getAnalysisType());

        if (!existingAnalysis.isEmpty()
                && !request.getAnalysisConfiguration().getOrDefault("allowDuplicate", false).equals(true)) {
            throw new BadRequestException(
                    "Analysis sudah ada untuk ujian ini dengan tipe: " + request.getAnalysisType());
        }

        // Create new analysis object
        UjianAnalysis analysis = new UjianAnalysis();
        analysis.setIdAnalysis(UUID.randomUUID().toString());
        analysis.setIdUjian(request.getIdUjian());
        analysis.setIdSchool(request.getIdSchool());
        analysis.setAnalysisType(request.getAnalysisType() != null ? request.getAnalysisType() : "COMPREHENSIVE");
        analysis.setGeneratedBy(getCurrentUserId());
        analysis.setConfigurationUsed(request.getAnalysisConfiguration());

        // Set relational data
        analysis.setUjian(ujian);
        analysis.setSchool(school);
        analysis.setGeneratedByUser(getCurrentUser());

        // FIXED: Use correct method names from the actual repositories
        List<HasilUjian> hasilUjianList = hasilUjianRepository.findByUjian(request.getIdUjian()); // CORRECTED
        List<UjianSession> sessionList = ujianSessionRepository.findSessionsByUjian(request.getIdUjian()); // CORRECTED
        List<CheatDetection> cheatDetectionList = cheatDetectionRepository.findByUjianId(request.getIdUjian()); // CORRECTED

        // Generate analysis components based on request
        if (request.getIncludeDescriptiveStats()) {
            generateDescriptiveStatistics(analysis, hasilUjianList, ujian);
        }

        if (request.getIncludeItemAnalysis()) {
            generateItemAnalysis(analysis, hasilUjianList, ujian);
        }

        if (request.getIncludeDifficultyAnalysis()) {
            generateDifficultyAnalysis(analysis, hasilUjianList, ujian);
        }

        if (request.getIncludeTimeAnalysis()) {
            generateTimeAnalysis(analysis, sessionList, hasilUjianList);
        }

        if (request.getIncludeCheatingAnalysis()) {
            generateCheatingAnalysis(analysis, cheatDetectionList, sessionList);
        }

        if (request.getIncludeComparativeAnalysis()) {
            generateComparativeAnalysis(analysis, request.getIdSchool());
        }

        if (request.getIncludeLearningAnalytics()) {
            generateLearningAnalytics(analysis, hasilUjianList, ujian);
        }

        if (request.getIncludeRecommendations()) {
            generateRecommendations(analysis, ujian);
        }

        // Calculate performance by categories
        generateCategoryPerformance(analysis, hasilUjianList, ujian);

        // Save analysis
        return ujianAnalysisRepository.save(analysis);
    }

    /**
     * Update existing analysis
     */
    public UjianAnalysis updateAnalysis(UjianAnalysisRequest.UpdateAnalysisRequest request) throws IOException {
        logger.debug("Updating analysis: {}", request.getIdAnalysis());

        UjianAnalysis analysis = ujianAnalysisRepository.findById(request.getIdAnalysis());
        if (analysis == null) {
            throw new ResourceNotFoundException("UjianAnalysis", "id", request.getIdAnalysis());
        }

        // Update metadata
        if (request.getUpdatedMetadata() != null) {
            Map<String, Object> currentMetadata = analysis.getAnalysisMetadata();
            if (currentMetadata == null) {
                currentMetadata = new HashMap<>();
            }
            currentMetadata.putAll(request.getUpdatedMetadata());
            analysis.setAnalysisMetadata(currentMetadata);
        }

        // Add additional recommendations
        if (request.getAdditionalRecommendations() != null) {
            for (String recommendation : request.getAdditionalRecommendations()) {
                analysis.addRecommendation(recommendation);
            }
        }

        // Add additional improvements
        if (request.getAdditionalImprovements() != null) {
            for (String improvement : request.getAdditionalImprovements()) {
                analysis.addImprovementSuggestion(improvement);
            }
        }

        // Adjust scores if provided
        if (request.getAdjustedScores() != null) {
            analysis.addMetadata("adjustedScores", request.getAdjustedScores());
            analysis.addMetadata("adjustmentReason", request.getUpdateReason());
        }

        // Set update metadata
        analysis.addMetadata("lastUpdatedBy", request.getUpdatedBy());
        analysis.addMetadata("lastUpdateReason", request.getUpdateReason());
        analysis.updateTimestamp();

        // Recalculate statistics if requested
        if (request.getRecalculateStats() != null && request.getRecalculateStats()) {
            Ujian ujian = ujianRepository.findById(analysis.getIdUjian());
            List<HasilUjian> hasilUjianList = hasilUjianRepository.findByUjian(analysis.getIdUjian()); // CORRECTED
            generateDescriptiveStatistics(analysis, hasilUjianList, ujian);
        }

        return ujianAnalysisRepository.save(analysis);
    }

    // ==================== ANALYSIS COMPONENTS GENERATION ====================

    /**
     * Generate descriptive statistics
     */
    private void generateDescriptiveStatistics(UjianAnalysis analysis, List<HasilUjian> hasilUjianList, Ujian ujian) {
        if (hasilUjianList == null || hasilUjianList.isEmpty()) {
            logger.warn("No hasil ujian data found for analysis");
            return;
        }

        // Basic counts
        analysis.setTotalParticipants(hasilUjianList.size());

        // FIXED: Use correct field name getStatusPengerjaan instead of getStatus
        long completedCount = hasilUjianList.stream()
                .filter(hasil -> hasil.getStatusPengerjaan() != null && hasil.getStatusPengerjaan().equals("SELESAI"))
                .count();

        analysis.setCompletedParticipants((int) completedCount);
        analysis.setIncompleteParticipants(analysis.getTotalParticipants() - analysis.getCompletedParticipants());

        // FIXED: Use correct field name getTotalSkor instead of getTotalScore
        List<Double> scores = hasilUjianList.stream()
                .filter(hasil -> hasil.getTotalSkor() != null)
                .map(HasilUjian::getTotalSkor)
                .collect(Collectors.toList());

        if (!scores.isEmpty()) {
            analysis.setAverageScore(scores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
            analysis.setHighestScore(scores.stream().mapToDouble(Double::doubleValue).max().orElse(0.0));
            analysis.setLowestScore(scores.stream().mapToDouble(Double::doubleValue).min().orElse(0.0));

            // Calculate median
            List<Double> sortedScores = scores.stream().sorted().collect(Collectors.toList());
            int middle = sortedScores.size() / 2;
            if (sortedScores.size() % 2 == 0) {
                analysis.setMedianScore((sortedScores.get(middle - 1) + sortedScores.get(middle)) / 2.0);
            } else {
                analysis.setMedianScore(sortedScores.get(middle));
            }

            // Calculate standard deviation and variance
            double mean = analysis.getAverageScore();
            double variance = scores.stream()
                    .mapToDouble(score -> Math.pow(score - mean, 2))
                    .average()
                    .orElse(0.0);

            analysis.setVariance(variance);
            analysis.setStandardDeviation(Math.sqrt(variance));
        }

        // Pass/Fail analysis
        Double minPassingScore = ujian.getMinPassingScore() != null ? ujian.getMinPassingScore() : 60.0;

        long passedCount = scores.stream()
                .filter(score -> score >= minPassingScore)
                .count();

        analysis.setPassedCount((int) passedCount);
        analysis.setFailedCount(scores.size() - (int) passedCount);
        analysis.setPassRate(scores.isEmpty() ? 0.0 : (double) passedCount / scores.size() * 100.0);
        analysis.setFailRate(100.0 - analysis.getPassRate());

        // Grade distribution
        Map<String, Integer> gradeDistribution = new HashMap<>();
        Map<String, Double> gradePercentages = new HashMap<>();

        for (Double score : scores) {
            String grade = calculateGrade(score);
            gradeDistribution.put(grade, gradeDistribution.getOrDefault(grade, 0) + 1);
        }

        // Calculate percentages
        int totalScores = scores.size();
        for (Map.Entry<String, Integer> entry : gradeDistribution.entrySet()) {
            double percentage = (double) entry.getValue() / totalScores * 100.0;
            gradePercentages.put(entry.getKey(), percentage);
        }

        analysis.setGradeDistribution(gradeDistribution);
        analysis.setGradePercentages(gradePercentages);

        logger.debug("Generated descriptive statistics for {} participants", analysis.getTotalParticipants());
    }

    /**
     * Generate item analysis
     */
    private void generateItemAnalysis(UjianAnalysis analysis, List<HasilUjian> hasilUjianList, Ujian ujian) {
        if (ujian.getBankSoalList() == null || ujian.getBankSoalList().isEmpty()) {
            logger.warn("No bank soal data found for item analysis");
            return;
        }

        Map<String, UjianAnalysis.ItemAnalysisData> itemAnalysis = new HashMap<>();

        for (BankSoalUjian bankSoal : ujian.getBankSoalList()) {
            UjianAnalysis.ItemAnalysisData itemData = new UjianAnalysis.ItemAnalysisData();

            // Basic item information
            itemData.setIdBankSoal(bankSoal.getIdBankSoal());
            itemData.setPertanyaan(bankSoal.getPertanyaan());
            itemData.setJenisSoal(bankSoal.getJenisSoal());

            // FIXED: Use correct field name getJawabanPeserta instead of getJawabanDetails
            List<Object> responses = hasilUjianList.stream()
                    .filter(hasil -> hasil.getJawabanPeserta() != null)
                    .map(hasil -> hasil.getJawabanPeserta().get(bankSoal.getIdBankSoal()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            itemData.setTotalResponses(responses.size());

            // Calculate correct responses
            int correctCount = 0;
            Map<String, Integer> optionFrequency = new HashMap<>();

            for (Object response : responses) {
                boolean isCorrect = evaluateAnswer(response, bankSoal.getJawabanBenar(), bankSoal.getJenisSoal());
                if (isCorrect) {
                    correctCount++;
                }

                // Count option frequency for multiple choice questions
                if ("PG".equals(bankSoal.getJenisSoal()) || "MULTI".equals(bankSoal.getJenisSoal())) {
                    String answer = response.toString();
                    optionFrequency.put(answer, optionFrequency.getOrDefault(answer, 0) + 1);
                }
            }

            itemData.setCorrectResponses(correctCount);
            itemData.setCorrectPercentage(responses.isEmpty() ? 0.0 : (double) correctCount / responses.size() * 100.0);

            // Calculate difficulty index (P-value)
            double difficultyIndex = responses.isEmpty() ? 0.0 : (double) correctCount / responses.size();
            itemData.setDifficultyIndex(difficultyIndex);

            // Set difficulty level
            itemData.setDifficultyLevel(categorizeDifficulty(difficultyIndex));

            // Calculate option percentages
            Map<String, Double> optionPercentages = new HashMap<>();
            for (Map.Entry<String, Integer> entry : optionFrequency.entrySet()) {
                double percentage = (double) entry.getValue() / responses.size() * 100.0;
                optionPercentages.put(entry.getKey(), percentage);
            }

            itemData.setOptionFrequency(optionFrequency);
            itemData.setOptionPercentage(optionPercentages);

            // Generate recommendation
            itemData.setRecommendation(generateItemRecommendation(difficultyIndex, itemData.getDiscriminationIndex()));

            itemAnalysis.put(bankSoal.getIdBankSoal(), itemData);
        }

        analysis.setItemAnalysis(itemAnalysis);
        logger.debug("Generated item analysis for {} items", itemAnalysis.size());
    }

    /**
     * Generate difficulty analysis
     */
    private void generateDifficultyAnalysis(UjianAnalysis analysis, List<HasilUjian> hasilUjianList, Ujian ujian) {
        Map<String, Double> questionDifficulty = new HashMap<>();
        List<String> easiestQuestions = new ArrayList<>();
        List<String> hardestQuestions = new ArrayList<>();

        if (analysis.getItemAnalysis() != null) {
            for (Map.Entry<String, UjianAnalysis.ItemAnalysisData> entry : analysis.getItemAnalysis().entrySet()) {
                String questionId = entry.getKey();
                UjianAnalysis.ItemAnalysisData itemData = entry.getValue();

                questionDifficulty.put(questionId, itemData.getDifficultyIndex());

                // Categorize questions by difficulty
                if (itemData.getDifficultyIndex() > 0.8) {
                    easiestQuestions.add(questionId);
                } else if (itemData.getDifficultyIndex() < 0.3) {
                    hardestQuestions.add(questionId);
                }
            }
        }

        analysis.setQuestionDifficulty(questionDifficulty);
        analysis.setEasiestQuestions(easiestQuestions);
        analysis.setHardestQuestions(hardestQuestions);

        logger.debug("Generated difficulty analysis: {} easy, {} hard questions",
                easiestQuestions.size(), hardestQuestions.size());
    }

    /**
     * Generate time analysis
     */
    private void generateTimeAnalysis(UjianAnalysis analysis, List<UjianSession> sessionList,
            List<HasilUjian> hasilUjianList) {
        if (sessionList == null || sessionList.isEmpty()) {
            logger.warn("No session data found for time analysis");
            return;
        }

        List<Double> completionTimes = sessionList.stream()
                .filter(session -> session.getStartTime() != null && session.getEndTime() != null)
                .map(session -> {
                    long durationMs = ChronoUnit.MILLIS.between(session.getStartTime(), session.getEndTime());
                    return durationMs / 1000.0 / 60.0; // Convert to minutes
                })
                .collect(Collectors.toList());

        if (!completionTimes.isEmpty()) {
            analysis.setAverageCompletionTime(
                    completionTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
            analysis.setShortestCompletionTime(
                    completionTimes.stream().mapToDouble(Double::doubleValue).min().orElse(0.0));
            analysis.setLongestCompletionTime(
                    completionTimes.stream().mapToDouble(Double::doubleValue).max().orElse(0.0));
        }

        logger.debug("Generated time analysis for {} sessions", sessionList.size());
    }

    /**
     * Generate cheating analysis
     */
    private void generateCheatingAnalysis(UjianAnalysis analysis, List<CheatDetection> cheatDetectionList,
            List<UjianSession> sessionList) {
        if (cheatDetectionList == null) {
            cheatDetectionList = new ArrayList<>();
        }

        analysis.setSuspiciousSubmissions(cheatDetectionList.size());

        long flaggedParticipants = cheatDetectionList.stream()
                .map(CheatDetection::getIdPeserta)
                .distinct()
                .count();

        analysis.setFlaggedParticipants((int) flaggedParticipants);

        // Calculate integrity score (higher is better)
        int totalParticipants = analysis.getTotalParticipants() != null ? analysis.getTotalParticipants() : 1;
        double integrityScore = Math.max(0.0, 1.0 - ((double) flaggedParticipants / totalParticipants));
        analysis.setIntegrityScore(integrityScore);

        logger.debug("Generated cheating analysis: {} violations, {} flagged participants",
                cheatDetectionList.size(), flaggedParticipants);
    }

    /**
     * Generate comparative analysis
     */
    private void generateComparativeAnalysis(UjianAnalysis analysis, String schoolId) {
        // This would typically compare with other schools/national averages
        // For now, we'll add placeholder data structure
        Map<String, Double> schoolComparison = new HashMap<>();
        Map<String, Double> nationalAverage = new HashMap<>();

        // Add metadata indicating comparative analysis was requested
        analysis.addMetadata("comparativeAnalysisRequested", true);
        analysis.addMetadata("comparisonScope", "SCHOOL_LEVEL");

        analysis.setSchoolComparison(schoolComparison);
        analysis.setNationalAverage(nationalAverage);

        logger.debug("Generated comparative analysis for school: {}", schoolId);
    }

    /**
     * Generate learning analytics
     */
    private void generateLearningAnalytics(UjianAnalysis analysis, List<HasilUjian> hasilUjianList, Ujian ujian) {
        Map<String, List<String>> learningGaps = new HashMap<>();
        Map<String, String> studyRecommendations = new HashMap<>();
        List<String> curriculumSuggestions = new ArrayList<>();

        // Analyze weak areas based on item analysis
        if (analysis.getItemAnalysis() != null) {
            for (Map.Entry<String, UjianAnalysis.ItemAnalysisData> entry : analysis.getItemAnalysis().entrySet()) {
                UjianAnalysis.ItemAnalysisData itemData = entry.getValue();

                if (itemData.getCorrectPercentage() != null && itemData.getCorrectPercentage() < 50.0) {
                    String topic = extractTopicFromQuestion(itemData.getPertanyaan());
                    learningGaps.computeIfAbsent(topic, k -> new ArrayList<>()).add(itemData.getPertanyaan());
                }
            }
        }

        // FIXED: Use correct field name getTotalSkor instead of getTotalScore
        for (HasilUjian hasil : hasilUjianList) {
            if (hasil.getTotalSkor() != null && hasil.getTotalSkor() < 70.0) {
                studyRecommendations.put(hasil.getIdPeserta(),
                        generateStudyRecommendation(hasil.getTotalSkor(), learningGaps));
            }
        }

        // Generate curriculum suggestions
        if (!learningGaps.isEmpty()) {
            curriculumSuggestions.add("Fokus pada materi yang memiliki tingkat kesalahan tinggi");
            curriculumSuggestions.add("Perkuat pemahaman konsep dasar sebelum lanjut ke materi advanced");
        }

        analysis.setLearningGaps(learningGaps);
        analysis.setStudyRecommendations(studyRecommendations);
        analysis.setCurriculumSuggestions(curriculumSuggestions);

        logger.debug("Generated learning analytics: {} learning gaps identified", learningGaps.size());
    }

    /**
     * Generate recommendations
     */
    private void generateRecommendations(UjianAnalysis analysis, Ujian ujian) {
        List<String> recommendations = new ArrayList<>();
        List<String> improvements = new ArrayList<>();

        // Analyze pass rate
        if (analysis.getPassRate() != null) {
            if (analysis.getPassRate() < 50.0) {
                recommendations.add("Tingkat kelulusan rendah (" + String.format("%.1f", analysis.getPassRate()) +
                        "%). Pertimbangkan untuk meninjau ulang materi atau metode pengajaran.");
                improvements.add("Adakan remedial teaching untuk materi yang sulit dipahami");
            } else if (analysis.getPassRate() > 90.0) {
                recommendations
                        .add("Tingkat kelulusan sangat tinggi (" + String.format("%.1f", analysis.getPassRate()) +
                                "%). Pertimbangkan untuk meningkatkan tingkat kesulitan soal.");
            }
        }

        // Analyze item difficulty
        if (analysis.getHardestQuestions() != null && analysis.getHardestQuestions().size() > 3) {
            recommendations.add("Terdapat " + analysis.getHardestQuestions().size() +
                    " soal dengan tingkat kesulitan sangat tinggi. Pertimbangkan untuk merevisi atau mengganti.");
        }

        // Analyze cheating
        if (analysis.getIntegrityScore() != null && analysis.getIntegrityScore() < 0.8) {
            recommendations.add("Skor integritas ujian rendah (" + String.format("%.2f", analysis.getIntegrityScore()) +
                    "). Perkuat pengawasan dan sistem anti-kecurangan.");
            improvements.add("Implementasikan sistem monitoring yang lebih ketat");
        }

        analysis.setRecommendations(recommendations);
        analysis.setImprovementSuggestions(improvements);

        logger.debug("Generated {} recommendations and {} improvements",
                recommendations.size(), improvements.size());
    }

    /**
     * Generate category performance analysis
     */
    private void generateCategoryPerformance(UjianAnalysis analysis, List<HasilUjian> hasilUjianList, Ujian ujian) {
        // Group by class, subject, etc. - simplified implementation
        Map<String, UjianAnalysis.CategoryPerformance> performanceByKelas = new HashMap<>();

        if (ujian.getKelas() != null) {
            UjianAnalysis.CategoryPerformance kelasPerformance = new UjianAnalysis.CategoryPerformance();
            kelasPerformance.setCategoryName(ujian.getKelas().getNamaKelas());
            kelasPerformance.setCategoryType("KELAS");
            kelasPerformance.setParticipantCount(analysis.getTotalParticipants());
            kelasPerformance.setAverageScore(analysis.getAverageScore());
            kelasPerformance.setHighestScore(analysis.getHighestScore());
            kelasPerformance.setLowestScore(analysis.getLowestScore());
            kelasPerformance.setPassRate(analysis.getPassRate());
            kelasPerformance.setStandardDeviation(analysis.getStandardDeviation());
            kelasPerformance.setGradeDistribution(analysis.getGradeDistribution());

            performanceByKelas.put(ujian.getKelas().getNamaKelas(), kelasPerformance);
        }

        analysis.setPerformanceByKelas(performanceByKelas);

        logger.debug("Generated category performance analysis");
    }

    // ==================== COMPARISON AND TREND ANALYSIS ====================

    /**
     * Compare multiple analyses
     */
    public Map<String, Object> compareAnalyses(UjianAnalysisRequest.CompareAnalysisRequest request) throws IOException {
        logger.debug("Comparing analyses: {}", request.getAnalysisIds());

        List<UjianAnalysis> analyses = new ArrayList<>();

        for (String analysisId : request.getAnalysisIds()) {
            UjianAnalysis analysis = ujianAnalysisRepository.findById(analysisId);
            if (analysis != null) {
                analyses.add(analysis);
            }
        }

        if (analyses.size() < 2) {
            throw new BadRequestException("Minimal 2 analysis diperlukan untuk perbandingan");
        }

        Map<String, Object> comparison = new HashMap<>();

        // Basic comparison
        comparison.put("analysisCount", analyses.size());
        comparison.put("comparisonType", request.getComparisonType());
        comparison.put("comparisonScope", request.getComparisonScope());

        // Score comparison
        Map<String, Double> scoreComparison = new HashMap<>();
        for (UjianAnalysis analysis : analyses) {
            String key = analysis.getUjian() != null ? analysis.getUjian().getNamaUjian() : analysis.getIdUjian();
            scoreComparison.put(key, analysis.getAverageScore());
        }
        comparison.put("averageScoreComparison", scoreComparison);

        // Pass rate comparison
        Map<String, Double> passRateComparison = new HashMap<>();
        for (UjianAnalysis analysis : analyses) {
            String key = analysis.getUjian() != null ? analysis.getUjian().getNamaUjian() : analysis.getIdUjian();
            passRateComparison.put(key, analysis.getPassRate());
        }
        comparison.put("passRateComparison", passRateComparison);

        // Statistical tests if requested
        if (request.getIncludeStatisticalTests() != null && request.getIncludeStatisticalTests()) {
            Map<String, Object> statisticalTests = performStatisticalTests(analyses);
            comparison.put("statisticalTests", statisticalTests);
        }

        return comparison;
    }

    /**
     * Export analysis to various formats
     */
    public Map<String, Object> exportAnalysis(UjianAnalysisRequest.ExportAnalysisRequest request) throws IOException {
        logger.debug("Exporting analysis: {} in format: {}", request.getIdAnalysis(), request.getFormat());

        UjianAnalysis analysis = ujianAnalysisRepository.findById(request.getIdAnalysis());
        if (analysis == null) {
            throw new ResourceNotFoundException("UjianAnalysis", "id", request.getIdAnalysis());
        }

        Map<String, Object> exportResult = new HashMap<>();
        exportResult.put("analysisId", analysis.getIdAnalysis());
        exportResult.put("format", request.getFormat());
        exportResult.put("exportedAt", Instant.now());
        exportResult.put("templateType", request.getTemplateType());

        // Generate export data based on format
        switch (request.getFormat().toUpperCase()) {
            case "JSON":
                exportResult.put("data", analysis);
                break;
            case "PDF":
                exportResult.put("pdfUrl", generatePdfReport(analysis, request));
                break;
            case "EXCEL":
                exportResult.put("excelUrl", generateExcelReport(analysis, request));
                break;
            case "CSV":
                exportResult.put("csvUrl", generateCsvReport(analysis, request));
                break;
            default:
                throw new BadRequestException("Format tidak didukung: " + request.getFormat());
        }

        return exportResult;
    }

    // ==================== HELPER METHODS ====================

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    private void validateAnalysisType(String analysisType) {
        List<String> validTypes = Arrays.asList("DESCRIPTIVE", "ITEM_ANALYSIS", "COMPARATIVE",
                "DIFFICULTY_ANALYSIS", "COMPREHENSIVE");

        if (!validTypes.contains(analysisType)) {
            throw new BadRequestException("Analysis type must be one of: " + String.join(", ", validTypes));
        }
    }

    private void validateGenerateAnalysisRequest(UjianAnalysisRequest.GenerateAnalysisRequest request) {
        if (request.getIdUjian() == null || request.getIdUjian().trim().isEmpty()) {
            throw new BadRequestException("ID Ujian wajib diisi");
        }

        if (request.getIdSchool() == null || request.getIdSchool().trim().isEmpty()) {
            throw new BadRequestException("ID School wajib diisi");
        }

        if (request.getAnalysisConfiguration() == null) {
            request.setAnalysisConfiguration(new HashMap<>());
        }
    }

    private Ujian validateUjianForAnalysis(String ujianId) throws IOException {
        Ujian ujian = ujianRepository.findById(ujianId);
        if (ujian == null) {
            throw new ResourceNotFoundException("Ujian", "id", ujianId);
        }

        if (!ujian.isSelesai()) {
            throw new BadRequestException("Analisis hanya dapat dilakukan pada ujian yang sudah selesai");
        }

        return ujian;
    }

    private School validateSchool(String schoolId) throws IOException {
        School school = schoolRepository.findById(schoolId);
        if (school == null) {
            throw new ResourceNotFoundException("School", "id", schoolId);
        }
        return school;
    }

    private String calculateGrade(Double score) {
        if (score >= 90)
            return "A";
        else if (score >= 80)
            return "B";
        else if (score >= 70)
            return "C";
        else if (score >= 60)
            return "D";
        else
            return "E";
    }

    private boolean evaluateAnswer(Object response, List<String> correctAnswers, String jenisSoal) {
        if (response == null || correctAnswers == null || correctAnswers.isEmpty()) {
            return false;
        }

        switch (jenisSoal) {
            case "PG":
                return correctAnswers.contains(response.toString());
            case "MULTI":
                // Handle multiple correct answers
                return correctAnswers.contains(response.toString());
            case "ISIAN":
                return correctAnswers.stream()
                        .anyMatch(answer -> answer.toLowerCase().equals(response.toString().toLowerCase()));
            case "COCOK":
                // Handle matching type questions
                return correctAnswers.contains(response.toString());
            default:
                return false;
        }
    }

    private String categorizeDifficulty(double difficultyIndex) {
        if (difficultyIndex > 0.8)
            return "EASY";
        else if (difficultyIndex > 0.6)
            return "MEDIUM";
        else if (difficultyIndex > 0.3)
            return "HARD";
        else
            return "VERY_HARD";
    }

    private String generateItemRecommendation(double difficultyIndex, Double discriminationIndex) {
        if (difficultyIndex > 0.8) {
            return "REVIEW - Soal terlalu mudah, pertimbangkan untuk diperbaiki";
        } else if (difficultyIndex < 0.3) {
            return "REVIEW - Soal terlalu sulit, pertimbangkan untuk diperbaiki";
        } else if (discriminationIndex != null && discriminationIndex < 0.3) {
            return "DISCARD - Daya pembeda rendah, pertimbangkan untuk dihapus";
        } else {
            return "GOOD - Soal baik, dapat digunakan kembali";
        }
    }

    private String extractTopicFromQuestion(String question) {
        // Simplified topic extraction - in real implementation,
        // this could use NLP or predefined mapping
        if (question != null && question.length() > 20) {
            return question.substring(0, 20) + "...";
        }
        return "General Topic";
    }

    private String generateStudyRecommendation(Double score, Map<String, List<String>> learningGaps) {
        if (score < 50) {
            return "Butuh perbaikan signifikan. Fokus pada pemahaman konsep dasar.";
        } else if (score < 70) {
            return "Perlu peningkatan. Latihan soal tambahan dan review materi yang sulit.";
        } else {
            return "Pertahankan performa. Fokus pada pendalaman materi.";
        }
    }

    private Map<String, Object> performStatisticalTests(List<UjianAnalysis> analyses) {
        Map<String, Object> tests = new HashMap<>();

        // Simplified statistical test implementation
        List<Double> averageScores = analyses.stream()
                .map(UjianAnalysis::getAverageScore)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (averageScores.size() >= 2) {
            double variance = calculateVariance(averageScores);
            tests.put("variance", variance);
            tests.put("standardDeviation", Math.sqrt(variance));

            // Add more statistical tests as needed
            tests.put("note", "Statistical tests are simplified. Implement proper tests for production use.");
        }

        return tests;
    }

    private double calculateVariance(List<Double> values) {
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return values.stream()
                .mapToDouble(value -> Math.pow(value - mean, 2))
                .average()
                .orElse(0.0);
    }

    private String generatePdfReport(UjianAnalysis analysis, UjianAnalysisRequest.ExportAnalysisRequest request) {
        // Implement PDF generation logic
        return "/exports/analysis_" + analysis.getIdAnalysis() + ".pdf";
    }

    private String generateExcelReport(UjianAnalysis analysis, UjianAnalysisRequest.ExportAnalysisRequest request) {
        // Implement Excel generation logic
        return "/exports/analysis_" + analysis.getIdAnalysis() + ".xlsx";
    }

    private String generateCsvReport(UjianAnalysis analysis, UjianAnalysisRequest.ExportAnalysisRequest request) {
        // Implement CSV generation logic
        return "/exports/analysis_" + analysis.getIdAnalysis() + ".csv";
    }

    private String getCurrentUserId() {
        // Implement authentication context to get current user ID
        return "SYSTEM"; // Placeholder
    }

    private User getCurrentUser() throws IOException {
        // Implement authentication context to get current user
        return userRepository.findById(getCurrentUserId());
    }

    // ==================== STATISTICS METHODS ====================

    /**
     * Get analysis statistics for dashboard
     */
    public Map<String, Object> getAnalysisStatistics(String schoolId) throws IOException {
        Map<String, Object> stats = new HashMap<>();

        List<UjianAnalysis> allAnalyses = schoolId.equals("*") ? ujianAnalysisRepository.findAll(1000)
                : ujianAnalysisRepository.findBySchoolId(schoolId, 1000);

        stats.put("totalAnalyses", allAnalyses.size());

        long descriptiveCount = allAnalyses.stream()
                .filter(a -> "DESCRIPTIVE".equals(a.getAnalysisType()))
                .count();

        long comprehensiveCount = allAnalyses.stream()
                .filter(a -> "COMPREHENSIVE".equals(a.getAnalysisType()))
                .count();

        stats.put("descriptiveAnalyses", descriptiveCount);
        stats.put("comprehensiveAnalyses", comprehensiveCount);

        // Calculate average metrics across all analyses
        OptionalDouble avgScore = allAnalyses.stream()
                .filter(a -> a.getAverageScore() != null)
                .mapToDouble(UjianAnalysis::getAverageScore)
                .average();

        OptionalDouble avgPassRate = allAnalyses.stream()
                .filter(a -> a.getPassRate() != null)
                .mapToDouble(UjianAnalysis::getPassRate)
                .average();

        stats.put("overallAverageScore", avgScore.orElse(0.0));
        stats.put("overallPassRate", avgPassRate.orElse(0.0));

        return stats;
    }
}