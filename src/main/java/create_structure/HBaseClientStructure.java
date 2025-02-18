package create_structure;

import com.github.javafaker.Faker;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;


public class HBaseClientStructure {

    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // ==============================================================================================
        // CREATE COLLECTION
        // ==============================================================================================
        
        TableName tableKonsentrasiKeahlian = TableName.valueOf("konsentrasiKeahlians");
        String[] konsentrasiKeahlian = { "main","programKeahlian", "detail" };
        client.deleteTable(tableKonsentrasiKeahlian);
        client.createTable(tableKonsentrasiKeahlian, konsentrasiKeahlian);
        
        TableName tableProgramKeahlian = TableName.valueOf("programKeahlians");
        String[] programKeahlian = { "main","bidangKeahlian", "detail" };
        client.deleteTable(tableProgramKeahlian);
        client.createTable(tableProgramKeahlian, programKeahlian);
        
        // Create Table Bidang Keahlian
        TableName tableBidangKeahlian = TableName.valueOf("bidangKeahlians");
        String[] bidangKeahlian = { "main", "school", "detail" };
        client.deleteTable(tableBidangKeahlian);
        client.createTable(tableBidangKeahlian, bidangKeahlian);
        
        // Create Table Sekolah
        TableName tableSchool = TableName.valueOf("schools");
        String[] school = { "main", "detail" };
        client.deleteTable(tableSchool);
        client.createTable(tableSchool, school);
        
        // Create Table Profil Sekolah
        TableName tableSchoolProfile = TableName.valueOf("school-profiles");
        String[] schoolprofile = { "main", "school", "detail" };
        client.deleteTable(tableSchoolProfile);
        client.createTable(tableSchoolProfile, schoolprofile);

        // Create Tabel Mata Kuliah
        TableName tableSubject = TableName.valueOf("subjects");
        String[] subjects = { "main", "study_program", "subject_group", "detail" };
        client.deleteTable(tableSubject);
        client.createTable(tableSubject, subjects);
        
        // Create Tabel Mata Pelajaran
        TableName tableMapel = TableName.valueOf("mapels");
        String[] mapel = { "main", "detail" };
        client.deleteTable(tableMapel);
        client.createTable(tableMapel, mapel);
        
        TableName tableJadwal = TableName.valueOf("jadwalPelajarans");
        String[] jadwal = { "main", "lecture", "mapel", "detail" };
        client.deleteTable(tableJadwal);
        client.createTable(tableJadwal, jadwal);
        
        // Create Tabel Kelas
        TableName tableKelas = TableName.valueOf("kelas");
        String[] kelas = { "main", "detail" };
        client.deleteTable(tableKelas);
        client.createTable(tableKelas, kelas);
        
        // Create Tabel Tahun Ajaran
        TableName tableTahun = TableName.valueOf("tahunAjaran");
        String[] tahun = { "main", "detail" };
        client.deleteTable(tableTahun);
        client.createTable(tableTahun, tahun);
        
        // Create Tabel Season
        TableName tableSeason = TableName.valueOf("seasons");
        String[] season = { "main", "bidangKeahlian", "programKeahlian", "konsentrasiKeahlian", "kelas", "tahunAjaran", "student", "lecture", "jadwalPelajaran", "detail" };
        client.deleteTable(tableSeason);
        client.createTable(tableSeason, season);

        // Create Tabel Bab
        TableName tableChapter = TableName.valueOf("chapters");
        String[] chapters = { "main", "subject", "detail" };
        client.deleteTable(tableChapter);
     client.createTable(tableChapter, chapters);

        // Create Tabel Rumpun Mata Kuliah
        TableName tableSubjectGroup = TableName.valueOf("subject_groups");
        String[] subjectGroups = { "main", "detail" };
        client.deleteTable(tableSubjectGroup);
     client.createTable(tableSubjectGroup, subjectGroups);

        // Create Tabel Dosen
         TableName tableLecture= TableName.valueOf("lectures");
         String[] lectures = { "main", "bidangKeahlian", "programKeahlian", "konsentrasiKeahlian", "religion", "detail" };
         client.deleteTable(tableLecture);
        client.createTable(tableLecture, lectures);

        // Create Tabel Mahasiswa
         TableName tableStudent = TableName.valueOf("students");
         String[] students = { "main", "bidangKeahlian", "programKeahlian", "konsentrasiKeahlian", "religion", "detail" };
         client.deleteTable(tableStudent);
        client.createTable(tableStudent, students);

        // Create Tabel RPS
        TableName tableRPS = TableName.valueOf("rps");
        String[] RPS = { "main", "learning_media_softwares", "learning_media_hardwares", "requirement_subjects", "study_program", "subject", "dev_lecturers", "teaching_lecturers", "coordinator_lecturers", "ka_study_program", "detail" };
        client.deleteTable(tableRPS);
       client.createTable(tableRPS, RPS);

        // Create Tabel Detail RPS
         TableName tableRPSDetail = TableName.valueOf("rps_details");
         String[] RPSDetails = { "main", "rps", "learning_materials", "form_learning", "learning_methods", "assignments", "estimated_times", "student_learning_experiences", "assessment_criterias", "appraisal_forms", "assessment_indicators", "detail" };
         client.deleteTable(tableRPSDetail);
        client.createTable(tableRPSDetail, RPSDetails);

        // Create Table Pustaka
        TableName tableReference = TableName.valueOf("references");
        String[] references = { "main", "detail" };
        client.deleteTable(tableReference);
       client.createTable(tableReference, references);

        // Create Table Media Pembelajaran
        TableName tableLearningMedia = TableName.valueOf("learning_medias");
        String[] learningMedias = { "main", "detail" };
        client.deleteTable(tableLearningMedia);
       client.createTable(tableLearningMedia, learningMedias);

        // Create Table Agama
        TableName tableReligion = TableName.valueOf("religions");
        String[] religions = { "main", "detail" };
        client.deleteTable(tableReligion);
      client.createTable(tableReligion, religions);

        // Create Table Jurusan
        TableName tableDepartment = TableName.valueOf("departments");
        String[] departments = { "main", "detail" };
        client.deleteTable(tableDepartment);
       client.createTable(tableDepartment, departments);

        // Create Table Prodi
        TableName tableStudyProgram = TableName.valueOf("study_programs");
        String[] studyPrograms = { "main", "department", "detail" };
        client.deleteTable(tableStudyProgram);
      client.createTable(tableStudyProgram, studyPrograms);

         //Create Table Users
         TableName tableUser = TableName.valueOf("users");
         String[] users = { "main", "school", "detail" };
         client.deleteTable(tableUser);
        client.createTable(tableUser, users);

        // Create Table Bentuk Penilaian
        TableName tableAppraisalForm = TableName.valueOf("appraisal_forms");
        String[] appraisalForms = { "main", "detail" };
        client.deleteTable(tableAppraisalForm);
       client.createTable(tableAppraisalForm, appraisalForms);

        // Create Tabel Kriteria Penilaian
        TableName tableAssessmentCriteria = TableName.valueOf("assessment_criterias");
        String[] assessmentCriterias = { "main", "detail" };
        client.deleteTable(tableAssessmentCriteria);
        client.createTable(tableAssessmentCriteria, assessmentCriterias);

        // Create Tabel Bentuk Pembelajaran
        TableName tableFormLearning = TableName.valueOf("form_learnings");
        String[] formLearnings = { "main", "detail" };
        client.deleteTable(tableFormLearning);
      client.createTable(tableFormLearning, formLearnings);

        // Create Tabel Metode Pembelajaran
        TableName tableLearningMethod = TableName.valueOf("learning_methods");
        String[] learningMethods = { "main", "detail" };
        client.deleteTable(tableLearningMethod);
   client.createTable(tableLearningMethod, learningMethods);

         //Create Tabel Pertanyaan
         TableName tableQuestion = TableName.valueOf("questions");
         String[] questions = { "main", "rps_detail", "detail" };
         client.deleteTable(tableQuestion);
       client.createTable(tableQuestion, questions);

         //Create Tabel Jawaban
         TableName tableAnswer = TableName.valueOf("answers");
         String[] answers = { "main", "question", "detail" };
         client.deleteTable(tableAnswer);
        client.createTable(tableAnswer, answers);

        // Create Tabel Ujian
        TableName tableExam = TableName.valueOf("exams");
        String[] exams = { "main", "rps", "questions", "detail" };
        client.deleteTable(tableExam);
    client.createTable(tableExam, exams);

        // Create Tabel Kuis
         TableName tableQuizzes = TableName.valueOf("quizzes");
         String[] quizzes = { "main", "rps", "questions", "detail" };
         client.deleteTable(tableQuizzes);
         client.createTable(tableQuizzes, quizzes);
        // Create Tabel Pengumuman Kuis
        TableName tableQuizzesAnnouncement = TableName.valueOf("quizzes_announcement");
        String[] quizzes_announcement = { "main", "rps", "questions", "detail" };
        client.deleteTable(tableQuizzesAnnouncement);
      client.createTable(tableQuizzesAnnouncement, quizzes_announcement);

        // Create Tabel Latihan
         TableName tableExcercise = TableName.valueOf("exercises");
         String[] exercises = { "main", "rps", "questions", "detail" };
         client.deleteTable(tableExcercise);
       client.createTable(tableExcercise, exercises);

        // Create Tabel Percobaan pengumpulan Ujian
        TableName tableExamAttempts = TableName.valueOf("exam_attempts");
        String[] examAttempts = { "main", "exam", "user", "student", "student_answers", "detail" };
        client.deleteTable(tableExamAttempts);
       client.createTable(tableExamAttempts, examAttempts);

        // Create Tabel Percobaan pengumpulan Kuis
        TableName tableQuizAttempts = TableName.valueOf("quiz_attempts");
        String[] quizAttempts = { "main", "quiz", "user", "student", "student_answers", "detail" };
        client.deleteTable(tableQuizAttempts);
       client.createTable(tableQuizAttempts, quizAttempts);

        // // Create Tabel Percobaan pengumpulan Latihan
         TableName tableExerciseAttempts = TableName.valueOf("exercise_attempts");
         String[] exerciseAttempts = { "main", "exercise", "user", "student", "student_answers", "detail" };
         client.deleteTable(tableExerciseAttempts);
         client.createTable(tableExerciseAttempts, exerciseAttempts);

        // Create Tabel Metode Pembelajaran
        TableName tableExamType = TableName.valueOf("exam_types");
        String[] examTypes = { "main", "detail" };
        client.deleteTable(tableExamType);
       client.createTable(tableExamType, examTypes);

        // Create Tabel Krireria Penilaian Soal

         TableName tableQuestionCriteria = TableName.valueOf("question_criterias");
         String[] questionCriterias = { "main", "detail" };
         client.deleteTable(tableQuestionCriteria);
         client.createTable(tableQuestionCriteria, questionCriterias);

        // Create Tabel Nilai Linguistic

         TableName tableLinguisticValue = TableName.valueOf("linguistic_values");
         String[] linguisticValues = { "main", "detail" };
         client.deleteTable(tableLinguisticValue);
         client.createTable(tableLinguisticValue, linguisticValues);

        // Create Tabel  Team Teaching
        TableName tableTeamTeaching = TableName.valueOf("team_teachings");
        String[] teamTeachings = {"main","detail","lecture","lecture2","lecture3"};
        client.deleteTable(tableTeamTeaching);
        client.createTable(tableTeamTeaching, teamTeachings);

        // Create Tabel Penilaian Soal
        TableName tableCriteriaValue = TableName.valueOf("criteria_values");
        String[] criteriaValues = { "main","detail","team_teaching","question","user","value1","value2","value3","value4","value5","value6","value7","value8","value9"};
        client.deleteTable(tableCriteriaValue);
        client.createTable(tableCriteriaValue, criteriaValues);

        // seeder
        // time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        //insert school 
        client.insertRecord(tableSchool, "RWK001", "main", "id", "RWK001");
        client.insertRecord(tableSchool, "RWK001", "main", "name", "SMK Negeri 01 ROWOKANGKUNG");
        client.insertRecord(tableSchool, "RWK001", "main", "address", "jl. raya rowokangkung");
        client.insertRecord(tableSchool, "RWK001", "detail", "created_by", "Doyatama");
        
        
        client.insertRecord(tableSchool, "TMP001", "main", "id", "TMP001");
        client.insertRecord(tableSchool, "TMP001", "main", "name", "SMK Negeri 01 TEMPEH");
        client.insertRecord(tableSchool, "TMP001", "main", "address", "jl. raya tempeh");
        client.insertRecord(tableSchool, "TMP001", "detail", "created_by", "Doyatama");
        
//        // Insert Jurusan
//        client.insertRecord(tableDepartment, "DP001", "main", "id", "DP001");
//        client.insertRecord(tableDepartment, "DP001", "main", "name", "Jurusan Teknologi Informasi");
//        client.insertRecord(tableDepartment, "DP001", "main", "description", "Ini merupakan jurusan untuk mahasiswa informatika");
//        client.insertRecord(tableDepartment, "DP001", "detail", "created_by", "Doyatama");
//
//        client.insertRecord(tableDepartment, "DP002", "main", "id", "DP002");
//        client.insertRecord(tableDepartment, "DP002", "main", "name", "Jurusan Teknik Mesin");
//        client.insertRecord(tableDepartment, "DP002", "main", "description", "Ini merupakan jurusan untuk mahasiswa teknik mesin");
//        client.insertRecord(tableDepartment, "DP002", "detail", "created_by", "Doyatama");
//
//        // Insert Prodi
//        client.insertRecord(tableStudyProgram, "SP001", "main", "id", "SP001");
//        client.insertRecord(tableStudyProgram, "SP001", "main", "name", "D4 Teknik Informatika");
//        client.insertRecord(tableStudyProgram, "SP001", "main", "description", "Ini merupakan prodi untuk mahasiswa informatika");
//        client.insertRecord(tableStudyProgram, "SP001", "department", "id", "DP001");
//        client.insertRecord(tableStudyProgram, "SP001", "department", "name", "Jurusan Teknologi Informasi");
//        client.insertRecord(tableStudyProgram, "SP001", "detail", "created_by", "Doyatama");
//
//        client.insertRecord(tableStudyProgram, "SP002", "main", "id", "SP002");
//        client.insertRecord(tableStudyProgram, "SP002", "main", "name", "D3 Manajemen Informatika");
//        client.insertRecord(tableStudyProgram, "SP002", "main", "description", "Ini merupakan prodi untuk mahasiswa informatika");
//        client.insertRecord(tableStudyProgram, "SP002", "department", "id", "DP002");
//        client.insertRecord(tableStudyProgram, "SP002", "department", "name", "Jurusan Teknik Mesin");
//        client.insertRecord(tableStudyProgram, "SP002", "detail", "created_by", "Doyatama");

         //Insert Users
         client.insertRecord(tableUser, "USR001", "main", "id", "USR001");
         client.insertRecord(tableUser, "USR001", "main", "email", "admin@gmail.com");
         client.insertRecord(tableUser, "USR001", "main", "name", "Administrator");
         client.insertRecord(tableUser, "USR001", "main", "username", "admin");
         client.insertRecord(tableUser, "USR001", "main", "password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
         client.insertRecord(tableUser, "USR001", "main", "roles", "1");
         client.insertRecord(tableUser, "USR001", "main", "created_at", "2023-05-14T04:56:23.174Z");
         client.insertRecord(tableUser, "USR001", "detail", "created_by", "Doyatama");

         client.insertRecord(tableUser, "USR002", "main", "id", "USR002");
         client.insertRecord(tableUser, "USR002", "main", "email", "operator1@gmail.com");
         client.insertRecord(tableUser, "USR002", "main", "name", "Operator1");
         client.insertRecord(tableUser, "USR002", "main", "username", "operator1");
         client.insertRecord(tableUser, "USR002", "main", "password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
         client.insertRecord(tableUser, "USR002", "school", "id", "RWK001");
         client.insertRecord(tableUser, "USR002", "school", "name", "SMK Negeri 01 ROWOKANGKUNG");
         client.insertRecord(tableUser, "USR002", "main", "roles", "2");
         client.insertRecord(tableUser, "USR002", "main", "created_at", "2023-05-14T04:56:23.174Z");
         client.insertRecord(tableUser, "USR002", "detail", "created_by", "Doyatama");

         
         client.insertRecord(tableUser, "USR003", "main", "id", "USR003");
         client.insertRecord(tableUser, "USR003", "main", "email", "operator2@gmail.com");
         client.insertRecord(tableUser, "USR003", "main", "name", "Operator2");
         client.insertRecord(tableUser, "USR003", "main", "username", "operator2");
         client.insertRecord(tableUser, "USR003", "main", "password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
         client.insertRecord(tableUser, "USR003", "school", "id", "TMP001");
         client.insertRecord(tableUser, "USR003", "school", "name", "SMK Negeri 01 TEMPEH");
         client.insertRecord(tableUser, "USR003", "main", "roles", "2");
         client.insertRecord(tableUser, "USR003", "main", "created_at", "2023-05-14T04:56:23.174Z");
         client.insertRecord(tableUser, "USR003", "detail", "created_by", "Doyatama");


         // Define the data
//       List<Map<String, String>> usersToInsert = Arrays.asList(
//            new HashMap<String, String>() {{
//                put("id", "guru1");
//                put("name", "Imam Fahrur Rozi, ST., MT");
//                put("username", "ImamFahrurRozi");
//                put("email", "ImamFahrurRozi@gmail.com");
//                put("password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2");
//                 put("schoolId", "1");
//                put("roles", "3");
//            }},
//            new HashMap<String, String>() {{
//                put("id", "guru2");
//                put("name", "Frihandhika Permana SPd., MKom.");
//                put("username", "Frihandhika");
//                put("email", "FrihandhikaPermana@gmail.com");
//                put("password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2");
//                 put("schoolId", "1");
//                put("roles", "3");
//            }},
//            new HashMap<String, String>() {{
//                put("id", "guru3");
//                put("name", "Milyun Ni’ma Shoumi, S.Kom., M.Kom.");
//                put("username", "MilyunNima");
//                put("email", "MilyunNi’maShoumi@gmail.com");
//                put("password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2");
//                 put("schoolId", "1");
//                put("roles", "3");
//            }},
//             new HashMap<String, String>() {{
//                put("id", "guru4");
//                put("name", "Putra Prima Arhandi, ST., M.Kom.");
//                put("username", "PutraPrima");
//                put("email", "PutraPrima@gmail.com");
//                put("password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2");
//                 put("schoolId", "1");
//                put("roles", "3");
//                put("createdAt", null);
//            }},
//            new HashMap<String, String>() {{
//                put("id", "guru5");
//                put("name", "Muhammad Shulhan Khairy, S.Kom, M.Kom");
//                put("username", "MuhammadShulha");
//                put("email", "MuhammadShulhanKhairy@gmail.com");
//                put("password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2");
//                 put("schoolId", "1");
//                put("roles", "3");
//                put("createdAt", null);
//            }},
//            new HashMap<String, String>() {{
//                put("id", "guru6");
//                put("name", "Gunawan Budiprasetyo, S.T., M.MT., Ph.D.");
//                put("username", "GunawanBudi");
//                put("email", "GunawanBudi@gmaill.com");
//                put("password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2");
//                 put("schoolId", "2");
//                put("roles", "3");
//                put("createdAt", null);
//            }},
//            new HashMap<String, String>() {{
//                put("id", "guru7");
//                put("name", "Banni Satria Andoko, S. Kom., M.MSI");
//                put("username", "BanniSatria");
//                put("email", "BanniSatriaAndoko@gmail.com");
//                put("password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2");
//                 put("schoolId", "2");
//                put("roles", "3");
//                put("createdAt", null);
//            }},
//            new HashMap<String, String>() {{
//                put("id", "guru8");
//                put("name", "Priska Choirina, S.S.T., M.Tr.T");
//                put("username", "PriskaChoirina");
//                put("email", "PriskaChoirina@gmail.com");
//                put("password", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2");
//                 put("schoolId", "2");
//                put("roles", "3");
//                put("createdAt", null);
//            }},
//            new HashMap<String, String>() {{
//               put("id", "dudi1");
//               put("name", "ranpo123");
//               put("username", "ranpo123");
//               put("email", "ranpo@gmail.com");
//               put("password","$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
//               put("schoolId", "1");
//               put("roles", "4");
//               put("createdAt", null);
//           }}
//
//
//       );
//
//       // Loop over the data and insert each user
//       for (Map<String, String> user : usersToInsert) {
//           client.insertRecord(tableUser, user.get("id"), "main", "id", user.get("id"));
//           client.insertRecord(tableUser, user.get("id"), "main", "name", user.get("name"));
//           client.insertRecord(tableUser, user.get("id"), "main", "username", user.get("username"));
//           client.insertRecord(tableUser, user.get("id"), "main", "email", user.get("email"));
//           client.insertRecord(tableUser, user.get("id"), "main", "password", user.get("password"));
//           client.insertRecord(tableUser, user.get("id"), "main", "schoolId", user.get("schoolId"));
//           client.insertRecord(tableUser, user.get("id"), "main", "roles", user.get("roles"));
//           client.insertRecord(tableUser, user.get("id"), "detail", "created_by", "Doyatama");
//       }
        
        // Insert Religions
        client.insertRecord(tableReligion, "RLG001", "main", "id", "RLG001");
        client.insertRecord(tableReligion, "RLG001", "main", "name", "Islam");
        client.insertRecord(tableReligion, "RLG001", "main", "description", "deskripsi agama islam");
        client.insertRecord(tableReligion, "RLG001", "detail", "created_by", "Doyatama");

        client.insertRecord(tableReligion, "RLG002", "main", "id", "RLG002");
        client.insertRecord(tableReligion, "RLG002", "main", "name", "Kristen");
        client.insertRecord(tableReligion, "RLG002", "main", "description", "deskripsi agama kristen");
        client.insertRecord(tableReligion, "RLG002", "detail", "created_by", "Doyatama");

        client.insertRecord(tableReligion, "RLG003", "main", "id", "RLG003");
        client.insertRecord(tableReligion, "RLG003", "main", "name", "Katolik");
        client.insertRecord(tableReligion, "RLG003", "main", "description", "deskripsi agama katolik");
        client.insertRecord(tableReligion, "RLG003", "detail", "created_by", "Doyatama");

        client.insertRecord(tableReligion, "RLG004", "main", "id", "RLG004");
        client.insertRecord(tableReligion, "RLG004", "main", "name", "Hindu");
        client.insertRecord(tableReligion, "RLG004", "main", "description", "deskripsi agama hindu");
        client.insertRecord(tableReligion, "RLG004", "detail", "created_by", "Doyatama");

        client.insertRecord(tableReligion, "RLG005", "main", "id", "RLG005");
        client.insertRecord(tableReligion, "RLG005", "main", "name", "Buddha");
        client.insertRecord(tableReligion, "RLG005", "main", "description", "deskripsi agama budha");
        client.insertRecord(tableReligion, "RLG005", "detail", "created_by", "Doyatama");

        client.insertRecord(tableReligion, "RLG006", "main", "id", "RLG006");
        client.insertRecord(tableReligion, "RLG006", "main", "name", "Kong Hu Chu");
        client.insertRecord(tableReligion, "RLG006", "main", "description", "deskripsi agama kong hu chu");
        client.insertRecord(tableReligion, "RLG006", "detail", "created_by", "Doyatama");

        // Insert Bentuk Pembelajaran
        client.insertRecord(tableFormLearning, "BP001", "main", "id", "BP001");
        client.insertRecord(tableFormLearning, "BP001", "main", "name", "Daring");
        client.insertRecord(tableFormLearning, "BP001", "main", "description", "Pembelajaran dilakukan secara dalam jaringan / online");
        client.insertRecord(tableFormLearning, "BP001", "detail", "created_by", "Doyatama");

        client.insertRecord(tableFormLearning, "BP002", "main", "id", "BP002");
        client.insertRecord(tableFormLearning, "BP002", "main", "name", "Luring");
        client.insertRecord(tableFormLearning, "BP002", "main", "description", "Pembelajaran dilakukan secara diluar jaringan / offline");
        client.insertRecord(tableFormLearning, "BP002", "detail", "created_by", "Doyatama");

        // Insert Metode Pembelajaran
        client.insertRecord(tableLearningMethod, "MP001", "main", "id", "MP001");
        client.insertRecord(tableLearningMethod, "MP001", "main", "name", "Contextual Teaching and Learning (CTL)");
        client.insertRecord(tableLearningMethod, "MP001", "main", "description", "Pengertian dari CTL");
        client.insertRecord(tableLearningMethod, "MP001", "detail", "created_by", "Doyatama");

        client.insertRecord(tableLearningMethod, "MP002", "main", "id", "MP002");
        client.insertRecord(tableLearningMethod, "MP002", "main", "name", "Problem Based Learning");
        client.insertRecord(tableLearningMethod, "MP002", "main", "description", "Pengertian dari PBL");
        client.insertRecord(tableLearningMethod, "MP002", "detail", "created_by", "Doyatama");

         // Insert Tipe ujian
//        client.insertRecord(tableExamType, "EX001", "main", "id", "EX001");
//        client.insertRecord(tableExamType, "EX001", "main", "name", "EXERCISE");
//        client.insertRecord(tableExamType, "EX001", "main", "description", "Untuk latihan siswa");
//        client.insertRecord(tableExamType, "EX001", "detail", "created_by", "Alfa");
//
//        client.insertRecord(tableExamType, "EX002", "main", "id", "EX002");
//        client.insertRecord(tableExamType, "EX002", "main", "name", "QUIZ");
//        client.insertRecord(tableExamType, "EX002", "main", "description", "Untuk perssiapan ujian siswa");
//        client.insertRecord(tableExamType, "EX002", "detail", "created_by", "Alfa");
//
//        client.insertRecord(tableExamType, "EX003", "main", "id", "EX003");
//        client.insertRecord(tableExamType, "EX003", "main", "name", "EXAM");
//        client.insertRecord(tableExamType, "EX003", "main", "description", "Untuk ujian siswa");
//        client.insertRecord(tableExamType, "EX003", "detail", "created_by", "Alfa");

        // Insert Kriteria Penilaian
//        client.insertRecord(tableAssessmentCriteria, "KP001", "main", "id", "KP001");
//        client.insertRecord(tableAssessmentCriteria, "KP001", "main", "name", "Ketepatan");
//        client.insertRecord(tableAssessmentCriteria, "KP001", "main", "description", "Ketepatan dalam pengumpulan tugas dan kehadiran di kelas");
//        client.insertRecord(tableAssessmentCriteria, "KP001", "detail", "created_by", "Doyatama");
//
//        client.insertRecord(tableAssessmentCriteria, "KP002", "main", "id", "KP002");
//        client.insertRecord(tableAssessmentCriteria, "KP002", "main", "name", "Penugasan");
//        client.insertRecord(tableAssessmentCriteria, "KP002", "main", "description", "Penilaian didasarkan dari penugasan yang diberikan dosen");
//        client.insertRecord(tableAssessmentCriteria, "KP002", "detail", "created_by", "Doyatama");
//
//        // Insert Bentuk Penilaian
//        client.insertRecord(tableAppraisalForm, "AF001", "main", "id", "AF001");
//        client.insertRecord(tableAppraisalForm, "AF001", "main", "name", "Keaktifan diskusi kelompok meliputi bertanya dan menjawab");
//        client.insertRecord(tableAppraisalForm, "AF001", "main", "description", "Keaktifan diskusi kelompok");
//        client.insertRecord(tableAppraisalForm, "AF001", "detail", "created_by", "Doyatama");
//
//        client.insertRecord(tableAppraisalForm, "AF002", "main", "id", "AF002");
//        client.insertRecord(tableAppraisalForm, "AF002", "main", "name", "Ketepatan jawaban tugas");
//        client.insertRecord(tableAppraisalForm, "AF002", "main", "description", "Ketepatan jawaban yang diberikan dari tugas");
//        client.insertRecord(tableAppraisalForm, "AF002", "detail", "created_by", "Doyatama");
//
//        // Insert Subject Group
//        client.insertRecord(tableSubjectGroup, "SG001", "main", "id", "SG001");
//        client.insertRecord(tableSubjectGroup, "SG001", "main", "name", "Big Data");
//        client.insertRecord(tableSubjectGroup, "SG001", "main", "description", "ini kelompok big data");
//        client.insertRecord(tableSubjectGroup, "SG001", "detail", "created_by", "Doyatama");
//
//        client.insertRecord(tableSubjectGroup, "SG002", "main", "id", "SG002");
//        client.insertRecord(tableSubjectGroup, "SG002", "main", "name", "Artificial Inteligent");
//        client.insertRecord(tableSubjectGroup, "SG002", "main", "description", "ini kelompok AI");
//        client.insertRecord(tableSubjectGroup, "SG002", "detail", "created_by", "Doyatama");
//
//        client.insertRecord(tableSubjectGroup, "SG003", "main", "id", "SG003");
//        client.insertRecord(tableSubjectGroup, "SG003", "main", "name", "Sistem Pendukung Keputusan");
//        client.insertRecord(tableSubjectGroup, "SG003", "main", "description", "ini kelompok SPK");
//        client.insertRecord(tableSubjectGroup, "SG003", "detail", "created_by", "Doyatama");
//
//        // Insert Subject
//        client.insertRecord(tableSubject, "SB001", "main", "id", "SB001");
//        client.insertRecord(tableSubject, "SB001", "main", "name", "Pemrograman Berbasis Object");
//        client.insertRecord(tableSubject, "SB001", "main", "description", "ini deskripsi dari mata kuliah");
//        client.insertRecord(tableSubject, "SB001", "main", "credit_point", "4");
//        client.insertRecord(tableSubject, "SB001", "main", "year_commenced", "2023");
//        client.insertRecord(tableSubject, "SB001", "study_program", "id", "SP001");
//        client.insertRecord(tableSubject, "SB001", "study_program", "name", "D4 Teknik Informatika");
//        client.insertRecord(tableSubject, "SB001", "subject_group", "id", "SG001");
//        client.insertRecord(tableSubject, "SB001", "subject_group", "name", "Big Data");
//        client.insertRecord(tableSubject, "SB001", "detail", "created_by", "Doyatama");
//
//        client.insertRecord(tableSubject, "SB002", "main", "id", "SB002");
//        client.insertRecord(tableSubject, "SB002", "main", "name", "Pemrograman Lanjut");
//        client.insertRecord(tableSubject, "SB002", "main", "description", "ini deskripsi dari mata kuliah");
//        client.insertRecord(tableSubject, "SB002", "main", "credit_point", "4");
//        client.insertRecord(tableSubject, "SB002", "main", "year_commenced", "2023");
//        client.insertRecord(tableSubject, "SB002", "study_program", "id", "SP001");
//        client.insertRecord(tableSubject, "SB002", "study_program", "name", "D4 Teknik Informatika");
//        client.insertRecord(tableSubject, "SB002", "subject_group", "id", "SG001");
//        client.insertRecord(tableSubject, "SB002", "subject_group", "name", "Big Data");
//        client.insertRecord(tableSubject, "SB002", "detail", "created_by", "Doyatama");
//
//        // Insert Lectures
//        client.insertRecord(tableLecture, "LEC001", "main", "id", "LEC001");
//        client.insertRecord(tableLecture, "LEC001", "main", "address", "dosen@gmail.com");
//        client.insertRecord(tableLecture, "LEC001", "main", "date_born", "dosen");
//        client.insertRecord(tableLecture, "LEC001", "main", "gender", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
//        client.insertRecord(tableLecture, "LEC001", "main", "name", "2");
//        client.insertRecord(tableLecture, "LEC001", "main", "nidn", "2");
//        client.insertRecord(tableLecture, "LEC001", "main", "phone", "2");
//        client.insertRecord(tableLecture, "LEC001", "main", "place_born", "2");
//        client.insertRecord(tableLecture, "LEC001", "main", "status", "Dosen");
//        client.insertRecord(tableLecture, "LEC001", "religion", "id", "RLG001");
//        client.insertRecord(tableLecture, "LEC001", "religion", "name", "Islam");
//        client.insertRecord(tableLecture, "LEC001", "study_program", "id", "SP001");
//        client.insertRecord(tableLecture, "LEC001", "study_program", "name", "D4 Teknik Informatika");
//        client.insertRecord(tableLecture, "LEC001", "user", "id", "USR001");
//        client.insertRecord(tableLecture, "LEC001", "user", "name", "dosen");
//        client.insertRecord(tableLecture, "LEC001", "user", "email", "dosen@gmail.com");
//        client.insertRecord(tableLecture, "LEC001", "user", "username", "dosen");
//        client.insertRecord(tableLecture, "LEC001", "detail", "created_by", "Doyatama");
//
//        client.insertRecord(tableLecture, "LEC002", "main", "id", "LEC002");
//        client.insertRecord(tableLecture, "LEC002", "main", "address", "dosen2@gmail.com");
//        client.insertRecord(tableLecture, "LEC002", "main", "date_born", "dosen2");
//        client.insertRecord(tableLecture, "LEC002", "main", "gender", "$2a$10$SDRWMUk.2fnli0GTmqodJexjRksTw0En98dU8fdKsw7nTbZzMrj.2"); // password
//        client.insertRecord(tableLecture, "LEC002", "main", "name", "2");
//        client.insertRecord(tableLecture, "LEC002", "main", "nidn", "2");
//        client.insertRecord(tableLecture, "LEC002", "main", "phone", "2");
//        client.insertRecord(tableLecture, "LEC002", "main", "place_born", "2");
//        client.insertRecord(tableLecture, "LEC002", "main", "status", "Dosen");
//        client.insertRecord(tableLecture, "LEC002", "religion", "id", "RLG001");
//        client.insertRecord(tableLecture, "LEC002", "religion", "name", "Islam");
//        client.insertRecord(tableLecture, "LEC002", "study_program", "id", "SP001");
//        client.insertRecord(tableLecture, "LEC002", "study_program", "name", "D4 Teknik Informatika");
//        client.insertRecord(tableLecture, "LEC002", "user", "id", "USR002");
//        client.insertRecord(tableLecture, "LEC002", "user", "name", "dosen");
//        client.insertRecord(tableLecture, "LEC002", "user", "email", "dosen@gmail.com");
//        client.insertRecord(tableLecture, "LEC002", "user", "username", "dosen");
//        client.insertRecord(tableLecture, "LEC002", "detail", "created_by", "Doyatama");
//
//       // Define the data
//       List<Map<String, String>> lecturersToInsert = Arrays.asList (
//            new HashMap<String, String>() {{
//                put("id", "Dosen4");
//                put("nidn", "dummy");
//                put("name", "Putra Prima Arhandi, ST., M.Kom.");
//                put("place_born", "dummy");
//                put("date_born", "2024-06-21T17:10:48.134Z");
//                put("gender", "L");
//                put("status", "dosen");
//                put("address", "dummy");
//                put("phone", "6123123");
//                put("religion_id", "RLG001");
//                put("religion_name", "Islam");
//                put("study_program_id", "SP001");
//                put("study_program_name", "D4 Teknik Informatika");
//                put("user_id", "Dosen4");
//                put("user_name", "Putra Prima Arhandi, ST., M.Kom.");
//                put("created_by", "Doyatama");
//            }},
//            new HashMap<String, String>() {{
//                put("id", "Dosen3");
//                put("nidn", "dummy");
//                put("name", "Milyun Ni’ma Shoumi, S.Kom., M.Kom.");
//                put("place_born", "dummy");
//                put("date_born", "2024-06-21T17:10:48.134Z");
//                put("gender", "L");
//                put("status", "dosen");
//                put("address", "dummy");
//                put("phone", "6123123");
//                put("religion_id", "RLG001");
//                put("religion_name", "Islam");
//                put("study_program_id", "SP001");
//                put("study_program_name", "D4 Teknik Informatika");
//                put("user_id", "Dosen3");
//                put("user_name", "Milyun Ni’ma Shoumi, S.Kom., M.Kom.");
//                put("created_by", "Doyatama");
//        }},
//            new HashMap<String, String>() {{
//                put("id", "Dosen1");
//                put("nidn", "dummy");
//                put("name", "Imam Fahrur Rozi, ST., MT");
//                put("place_born", "dummy");
//                put("date_born", "2024-06-21T17:10:48.134Z");
//                put("gender", "L");
//                put("status", "dosen");
//                put("address", "dummy");
//                put("phone", "6123123");
//                put("religion_id", "RLG001");
//                put("religion_name", "Islam");
//                put("study_program_id", "SP001");
//                put("study_program_name", "D4 Teknik Informatika");
//                put("user_id", "Dosen1");
//                put("user_name", "Imam Fahrur Rozi, ST., MT");
//                put("created_by", "Doyatama");
//        }},
//        new HashMap<String, String>() {{
//                put("id", "Dosen2");
//                put("nidn", "dummy");
//                put("name", "Frihandhika Permana SPd., MKom.");
//                put("place_born", "dummy");
//                put("date_born", "2024-06-21T17:10:48.134Z");
//                put("gender", "L");
//                put("status", "dosen");
//                put("address", "dummy");
//                put("phone", "6123123");
//                put("religion_id", "RLG001");
//                put("religion_name", "Islam");
//                put("study_program_id", "SP001");
//                put("study_program_name", "D4 Teknik Informatika");
//                put("user_id", "Dosen2");
//                put("user_name", "Frihandhika Permana SPd., MKom.");
//                put("created_by", "Doyatama");
//        }},
//        new HashMap<String, String>() {{
//            put("id", "Dosen5");
//            put("nidn", "dummy");
//            put("name", "Muhammad Shulhan Khairy, S.Kom, M.Kom");
//            put("place_born", "dummy");
//            put("date_born", "2024-06-21T17:10:48.134Z");
//            put("gender", "L");
//            put("status", "dosen");
//            put("address", "dummy");
//            put("phone", "6123123");
//            put("religion_id", "RLG001");
//            put("religion_name", "Islam");
//            put("study_program_id", "SP001");
//            put("study_program_name", "D4 Teknik Informatika");
//            put("user_id", "MuhammadShulha");
//            put("user_name", "Dosen5");
//            put("created_by", "Doyatama");
//        }},
//        new HashMap<String, String>() {{
//            put("id", "Dosen6");
//            put("nidn", "dummy");
//            put("name", "Gunawan Budiprasetyo, S.T., M.MT., Ph.D.");
//            put("place_born", "dummy");
//            put("date_born", "2024-06-21T17:10:48.134Z");
//            put("gender", "L");
//            put("status", "dosen");
//            put("address", "dummy");
//            put("phone", "6123123");
//            put("religion_id", "RLG001");
//            put("religion_name", "Islam");
//            put("study_program_id", "SP001");
//            put("study_program_name", "D4 Teknik Informatika");
//            put("user_id", "Dosen6");
//            put("user_name", "Gunawan Budiprasetyo, S.T., M.MT., Ph.D.");
//            put("created_by", "Doyatama");
//        }},
//        new HashMap<String, String>() {{
//            put("id", "Dosen7");
//            put("nidn", "dummy");
//            put("name", "Banni Satria Andoko, S. Kom., M.MSI");
//            put("place_born", "dummy");
//            put("date_born", "2024-06-21T17:10:48.134Z");
//            put("gender", "L");
//            put("status", "dosen");
//            put("address", "dummy");
//            put("phone", "6123123");
//            put("religion_id", "RLG001");
//            put("religion_name", "Islam");
//            put("study_program_id", "SP001");
//            put("study_program_name", "D4 Teknik Informatika");
//            put("user_id", "Dosen7");
//            put("user_name", "Banni Satria Andoko, S. Kom., M.MSI");
//            put("created_by", "Doyatama");
//        }},
//        new HashMap<String, String>() {{
//            put("id", "Dosen8");
//            put("nidn", "dummy");
//            put("name", "Priska Choirina, S.S.T., M.Tr.T");
//            put("place_born", "dummy");
//            put("date_born", "2024-06-21T17:10:48.134Z");
//            put("gender", "L");
//            put("status", "dosen");
//            put("address", "dummy");
//            put("phone", "6123123");
//            put("religion_id", "RLG001");
//            put("religion_name", "Islam");
//            put("study_program_id", "SP001");
//            put("study_program_name", "D4 Teknik Informatika");
//            put("user_id", "Dosen8");
//            put("user_name", "Priska Choirina, S.S.T., M.Tr.T");
//            put("created_by", "Doyatama");
//        }},
//       new HashMap<String, String>() {{
//           put("id", "Dosen10");
//           put("nidn", "dummy");
//           put("name", "ranpo123");
//           put("place_born", "dummy");
//           put("date_born", "2024-06-21T17:10:48.134Z");
//           put("gender", "L");
//           put("status", "dosen");
//           put("address", "dummy");
//           put("phone", "6123123");
//           put("religion_id", "RLG001");
//           put("religion_name", "Islam");
//           put("study_program_id", "SP001");
//           put("study_program_name", "D4 Teknik Informatika");
//           put("user_id", "Dosen10");
//           put("user_name", "ranpo123");
//           put("created_by", "Doyatama");
//
//       }}
//
//       );
//
//       // Loop over the data and insert each lecturer
//       for (Map<String, String> lecturer : lecturersToInsert) {
//           client.insertRecord(tableLecture, lecturer.get("id"), "main", "id", lecturer.get("id"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "main", "nidn", lecturer.get("nidn"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "main", "name", lecturer.get("name"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "main", "place_born", lecturer.get("place_born"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "main", "date_born", lecturer.get("date_born"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "main", "gender", lecturer.get("gender"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "main", "status", lecturer.get("status"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "main", "address", lecturer.get("address"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "main", "phone", lecturer.get("phone"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "religion", "id", lecturer.get("religion_id"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "religion", "name", lecturer.get("religion_name"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "study_program", "id", lecturer.get("study_program_id"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "study_program", "name", lecturer.get("study_program_name"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "user", "id", lecturer.get("user_id"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "user", "name", lecturer.get("user_name"));
//           client.insertRecord(tableLecture, lecturer.get("id"), "detail", "created_by", lecturer.get("created_by"));
//       }
//       
//        // Learning Media
//        // Software
//        client.insertRecord(tableLearningMedia, "LM001", "main", "id", "LM001");
//        client.insertRecord(tableLearningMedia, "LM001", "main", "name", "Netbeans");
//        client.insertRecord(tableLearningMedia, "LM001", "main", "description", "ini deskripsi netbeans");
//        client.insertRecord(tableLearningMedia, "LM001", "main", "type", "1");
//        client.insertRecord(tableLearningMedia, "LM001", "detail", "created_by", "Doyatama");
//
//        // Hardware
//        client.insertRecord(tableLearningMedia, "LM002", "main", "id", "LM002");
//        client.insertRecord(tableLearningMedia, "LM002", "main", "name", "PC / Komputer");
//        client.insertRecord(tableLearningMedia, "LM002", "main", "description", "ini deskripsi pc / komputer");
//        client.insertRecord(tableLearningMedia, "LM002", "main", "type", "2");
//        client.insertRecord(tableLearningMedia, "LM002", "detail", "created_by", "Doyatama");
//
//        // RPS
//        client.insertRecord(tableRPS, "RPS001", "main", "id", "RPS001");
//        client.insertRecord(tableRPS, "RPS001", "main", "name", "Dummy RPS");
//        client.insertRecord(tableRPS, "RPS001", "main", "sks", "3");
//        client.insertRecord(tableRPS, "RPS001", "main", "semester", "3");
//        client.insertRecord(tableRPS, "RPS001", "main", "cpl_prodi", "Dummy CPL Prodi");
//        client.insertRecord(tableRPS, "RPS001", "main", "cpl_mk", "Dummy CPL MK");
//        client.insertRecord(tableRPS, "RPS001", "study_program", "id", "SP001");
//        client.insertRecord(tableRPS, "RPS001", "study_program", "name", "D4 Teknik Informatika");
//        client.insertRecord(tableRPS, "RPS001", "subject", "id", "SB001");
//        client.insertRecord(tableRPS, "RPS001", "subject", "name", "Pemrograman Berbasis Object");
//        client.insertRecord(tableRPS, "RPS001", "ka_study_program", "id", "LEC001");
//        client.insertRecord(tableRPS, "RPS001", "ka_study_program", "name", "2");
//        client.insertRecord(tableRPS, "RPS001", "detail", "created_by", "Doyatama");
//        client.insertRecord(tableRPS, "RPS001", "detail", "created_at", instant.toString());
//
//        // RPS Detail
////        client.insertRecord(tableRPSDetail, "RPSD001", "main", "id", "RPSD001");
////        client.insertRecord(tableRPSDetail, "RPSD001", "main", "week", "2");
////        client.insertRecord(tableRPSDetail, "RPSD001", "rps", "id", "RPS001");
////        client.insertRecord(tableRPSDetail, "RPSD001", "rps", "name", "Dummy RPS");
////        client.insertRecord(tableRPSDetail, "RPSD001", "main", "sub_cp_mk", "Dummy Sub CP MK");
////        client.insertRecord(tableRPSDetail, "RPSD001", "learning_materials", "lm_1", "Dummy Learning Material");
////        client.insertRecord(tableRPSDetail, "RPSD001", "form_learning", "id", "BP001");
////        client.insertRecord(tableRPSDetail, "RPSD001", "form_learning", "name", "Daring");
////        client.insertRecord(tableRPSDetail, "RPSD001", "assignments", "lm_1",  "Dummy Penugasan");
////        client.insertRecord(tableRPSDetail, "RPSD001", "estimated_times", "et_1", "Dummy Estimasi Waktu");
////        client.insertRecord(tableRPSDetail, "RPSD001", "student_learning_experiences", "sle_1", "Dummy Pengalaman Mahasiswa");
////        client.insertRecord(tableRPSDetail, "RPSD001", "assessment_indicators", "ai_1", "Dummy Assessment Indikator");
////        client.insertRecord(tableRPSDetail, "RPSD001", "main", "weight", "3");
////        client.insertRecord(tableRPSDetail, "RPSD001", "detail", "created_by", "Doyatama");
////        client.insertRecord(tableRPSDetail, "RPSD001", "detail", "created_at", instant.toString());
//
//        // RPS
//        String id = "RPS-PBO-001";
//        client.insertRecord(tableRPS, id, "main", "id", id);
//        client.insertRecord(tableRPS, id, "main", "name", "PEMROGRAMAN BERBASIS OBJEK");
//        client.insertRecord(tableRPS, id, "main", "sks", "2");
//        client.insertRecord(tableRPS, id, "main", "semester", "3");
//        client.insertRecord(tableRPS, id, "main", "cpl_prodi", "S8 Menginternalisasi nilai, norma, dan etika akademik.");
//        client.insertRecord(tableRPS, id, "main", "cpl_mk", "Menguasai Konsep OOP, Class dan Object, Enkapsulasi, Inheritance, Abstraksi, Polimorfisme, GUI, database (JDBC), dan Java API; Mampu memahami perbedaan OOP dan struktural; Mampu membuat desain aplikasi menggunakan konsep dan prinsip OOP dengan penuh tanggung jawab serta memperhatikan nilai, norma, dan etika akademik.");
//        client.insertRecord(tableRPS, id, "study_program", "id", "SP001");
//        client.insertRecord(tableRPS, id, "study_program", "name", "D4 Teknik Informatika");
//        client.insertRecord(tableRPS, id, "subject", "id", "SB001");
//        client.insertRecord(tableRPS, id, "subject", "name", "Pemrograman Berbasis Object");
//
//            // Create an ObjectMapper instance
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // Create the lecturer objects
//        HashMap<String, String> lecturer1 = new HashMap<String, String>() {{
//            put("id", "Dosen7");
//             put("name", "Banni Satria Andoko, S. Kom., M.MSI");
//            // ... add the rest of the properties
//        }};
//        HashMap<String, String> lecturer2 = new HashMap<String, String>() {{
//            put("id", "Dosen8");
//            put("name", "Priska Choirina, S.S.T., M.Tr.T");
//            // ... add the rest of the properties
//        }};
//        HashMap<String, String> lecturer3 = new HashMap<String, String>() {{
//            put("id", "Dosen1");
//            put("name", "Imam Fahrur Rozi, ST., MT");
//            // ... add the rest of the properties
//        }};
//        HashMap<String, String> lecturer4 = new HashMap<String, String>() {{
//            put("id", "Dosen10");
//            put("name", "ranpo123");
//            // ... add the rest of the properties
//        }};
//
//
//
//        // Serialize the lecturer objects to JSON strings
//        String lecturerJson1 = objectMapper.writeValueAsString(lecturer1);
//        String lecturerJson2 = objectMapper.writeValueAsString(lecturer2);
//        String lecturerJson3 = objectMapper.writeValueAsString(lecturer3);
//        String lecturerJson4 = objectMapper.writeValueAsString(lecturer4);
//
//
//        // Store the JSON strings in HBase
//        client.insertRecord(tableRPS, id, "dev_lecturers", "Dosen7", lecturerJson1);
//        client.insertRecord(tableRPS, id, "dev_lecturers", "Dosen8", lecturerJson2);
//        client.insertRecord(tableRPS, id, "dev_lecturers", "Dosen1", lecturerJson3);
//        client.insertRecord(tableRPS, id, "dev_lecturers", "Dosen10", lecturerJson4);
//
//        client.insertRecord(tableRPS, id, "detail", "created_by", "Doyatama");
//        client.insertRecord(tableRPS, id, "detail", "created_at", "2024-06-03T17:21:19.853011600Z");
//        
//        // RPS Detail
//         List<Map<String, String>> rpsDetailsToInsert = Arrays.asList(
//             new HashMap<String, String>() {{
//                 put("main_id", "RPS-PBO-001-1");
//                 put("main_week", "1");
//                 put("rps_id", "RPS-PBO-001");
//                 put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                 put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                 put("learning_materials_lm_1", "Penjelasan silabus dan kontrak kuliah; ●Pengantar Konsep Dasar PBO; ●Penjelasan tentang perbedaan paradigma berorientasi objek dengan paradigma struktural");
//                 put("form_learning_id", "BP002");
//                 put("form_learning_name", "Luring");
//                 put("assignments_lm_1", "-");
//                 put("estimated_times_et_1", "150");
//                 put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                 put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                 put("main_weight", "1.0");
//                 put("detail_created_by", "Doyatama");
//                 put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//             }},
//             new HashMap<String, String>() {{
//                 put("main_id", "RPS-PBO-001-2");
//                 put("main_week", "2");
//                 put("rps_id", "RPS-PBO-001");
//                 put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                 put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                 put("learning_materials_lm_1", "Konsep Dasar PBO: •Pengenalan PBO e Perbedaan paradigma berorientasi objek dengan paradigma struktural Konsep dasar PBO: a. Class b. Object c. Enkapsulasi d. Inheritance e. Polimorfisme • Pengenalan pemodelan UML");
//                 put("form_learning_id", "BP002");
//                 put("form_learning_name", "Luring");
//                 put("assignments_lm_1", "-");
//                 put("estimated_times_et_1", "150");
//                 put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                 put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                 put("main_weight", "1.0");
//                 put("detail_created_by", "Doyatama");
//                 put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//             }},
//             new HashMap<String, String>() {{
//                 put("main_id", "RPS-PBO-001-3");
//                 put("main_week", "3");
//                 put("rps_id", "RPS-PBO-001");
//                 put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                 put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                 put("learning_materials_lm_1", "Enkapsulasi: e Konstruktor •Access modifier •Atribut/Method Class •Atribut/Method Instansiasi •Setter dan Getter •UML: Notasi access modifier dan notasi static");
//                 put("form_learning_id", "BP002");
//                 put("form_learning_name", "Luring");
//                 put("assignments_lm_1", "-");
//                 put("estimated_times_et_1", "150");
//                 put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                 put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                 put("main_weight", "1.0");
//                 put("detail_created_by", "Doyatama");
//                 put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//             }},
//             new HashMap<String, String>() {{
//                 put("main_id", "RPS-PBO-001-4");
//                 put("main_week", "4");
//                 put("rps_id", "RPS-PBO-001");
//                 put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                 put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                 put("learning_materials_lm_1", "Relasi Class: •Relasi Has-A (Studi kasus class 1 memiliki hubungan has-a dengan 1 objek dari klass lain) •Penggambaran relasi class dengan diagram class •Relasi Has-A (Studi kasus class 1 memiliki hubungan has-a dengan lebih dari 1 objek dari klass lain)");
//                 put("form_learning_id", "BP002");
//                 put("form_learning_name", "Luring");
//                 put("assignments_lm_1", "-");
//                 put("estimated_times_et_1", "150");
//                 put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                 put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                 put("main_weight", "1.0");
//                 put("detail_created_by", "Doyatama");
//                 put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//             }},
//             new HashMap<String, String>() {{
//                 put("main_id", "RPS-PBO-001-5");
//                 put("main_week", "5");
//                 put("rps_id", "RPS-PBO-001");
//                 put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                 put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                 put("learning_materials_lm_1", "Kuis 1 Materi pertemuan 1- 4");
//                 put("form_learning_id", "BP002");
//                 put("form_learning_name", "Luring");
//                 put("assignments_lm_1", "-");
//                 put("estimated_times_et_1", "150");
//                 put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                 put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                 put("main_weight", "1.0");
//                 put("detail_created_by", "Doyatama");
//                 put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//             }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-6");
//                     put("main_week", "6");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "Inheritance: Pengertian Inheritance 'Single dan Multilevell Inheritance 'Super keyword eUML:relasi inheritance");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-7");
//                     put("main_week", "7");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "Inheritance: Pengertian Inheritance 'Single dan Multilevell Inheritance 'Super keyword eUML:relasi inheritance");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-8");
//                     put("main_week", "8");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "UTS •Materi 6-8 pertemuan •Overriding •Overloading");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-9");
//                     put("main_week", "9");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "Overriding & Overloading");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-10");
//                     put("main_week", "10");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "Abstract Class: Abstract Konsep Class Abstract method UML:notasi abstract");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-11");
//                     put("main_week", "11");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "Interface: • Konsep Interface •Beda Interface dan Abstract Class Pembuatan interface implements interface •UML:notasi yang ke interface dan relasi implements");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-12");
//                     put("main_week", "12");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "Polimorfisme: Konsep polimorfisme oheterogeneous collection polymorphic arguments evirtual method invocation •casting object");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-13");
//                     put("main_week", "13");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "Materi 09-12 pertemuan");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-14");
//                     put("main_week", "14");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "GUI: Fra me, Textfield, Menu, Button, Label, Combobox, Radiobutton, Checkbox Event Handling (actionperformed)");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-15");
//                     put("main_week", "15");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "GUi, Database, dan Java API: 'JDBC MySQL 'CRUD dengan GUI •Java Docs");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-16");
//                     put("main_week", "16");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "Tugas Besar: Perancangan diagram class dari kasus diberikan. yang diberikan");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }},
//                 new HashMap<String, String>() {{
//                     put("main_id", "RPS-PBO-001-17");
//                     put("main_week", "17");
//                     put("rps_id", "RPS-PBO-001");
//                     put("rps_name", "PEMROGRAMAN BERBASIS OBJEK");
//                     put("main_sub_cp_mk", ".Mahasiswa mampu memahami isi silabus\ndan kontrak kuliah;\n•Mahasiswa\nmemahami\ndasar PBO;\n• Mahasiswa\nmembedakan\nmampu\nkonsep\nmampu\nparadigma berorientasi\nobjek\ndengan\nparadigma struktural.");
//                     put("learning_materials_lm_1", "UAS: e Materi mulai pertemuan 1-16");
//                     put("form_learning_id", "BP002");
//                     put("form_learning_name", "Luring");
//                     put("assignments_lm_1", "-");
//                     put("estimated_times_et_1", "150");
//                     put("student_learning_experiences_sle_1", "en engar an materi dari dosen dan berdiskusi; Diskusi menerapkan konsep dasar PBO dipandu materi dosen dengan presentasi pengampu mata kuliah");
//                     put("assessment_indicators_ai_1", "• Ketepatan menjelaskan dasar PBO; Ketepatan menjelaskan konsep dan memberi studi kasus perbedaan tentang paradigma berorientasi objek denga n paradigma struktural;");
//                     put("main_weight", "1.0");
//                     put("detail_created_by", "Doyatama");
//                     put("detail_created_at", "2024-06-03T17:21:19.853011600Z");
//                 }}
//         );
//         for (Map<String, String> rpsDetail : rpsDetailsToInsert) {
//             String RPSDetailid = rpsDetail.get("main_id");
//             client.insertRecord(tableRPSDetail, RPSDetailid, "main", "id", rpsDetail.get("main_id"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "main", "week", rpsDetail.get("main_week"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "rps", "id", rpsDetail.get("rps_id"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "rps", "name", rpsDetail.get("rps_name"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "main", "sub_cp_mk", rpsDetail.get("main_sub_cp_mk"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "learning_materials", "lm_1", rpsDetail.get("learning_materials_lm_1"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "form_learning", "id", rpsDetail.get("form_learning_id"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "form_learning", "name", rpsDetail.get("form_learning_name"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "assignments", "lm_1", rpsDetail.get("assignments_lm_1"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "estimated_times", "et_1", rpsDetail.get("estimated_times_et_1"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "student_learning_experiences", "sle_1", rpsDetail.get("student_learning_experiences_sle_1"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "assessment_indicators", "ai_1", rpsDetail.get("assessment_indicators_ai_1"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "main", "weight", rpsDetail.get("main_weight"));
//             client.insertRecord(tableRPSDetail, RPSDetailid, "detail", "created_by", rpsDetail.get("detail_created_by"));
//         }

//         //insert question
//         for (int i = 0; i <100; i++) {
//             Faker faker = new Faker();
//             String[] typeQuestion = {"VIDEO", "AUDIO", "IMAGE", "NORMAL"};
//             String[] typeAnswer = {"MULTIPLE_CHOICE", "BOOLEAN", "COMPLETION", "MATCHING", "ESSAY"};
//             String[] examTypesQ = {"EXERCISE","QUIZ", "EXAM"};
//             String typeQ = typeQuestion[faker.random().nextInt(typeQuestion.length)];
//              // Create an ObjectMapper instance
//             ObjectMapper objectMapperQ = new ObjectMapper();

//             List<String> examTypeIds = Arrays.asList("EX001", "EX002", "EX003");
//             Collections.shuffle(examTypeIds);

//             // Randomly select the number of exam types to select
//             int n = faker.random().nextInt(examTypeIds.size() + 1);

//             // Select the first n exam types
//             List<String> selectedExamTypes = examTypeIds.subList(0, n);

//             // Convert the selected exam types to a JSON string
//             String selectedExamTypesJson = objectMapperQ.writeValueAsString(selectedExamTypes);

// //            // Insert into tableQuestion
// //            client.insertRecord(tableQuestion, "QST"+i, "exam_types", selectedExamTypesJson, selectedExamTypesJson);
//             String path = "";
//             client.insertRecord(tableQuestion, "QST"+i, "main", "id", "QST"+i);
//             client.insertRecord(tableQuestion, "QST"+i, "main", "title", faker.lorem().sentence());
//             client.insertRecord(tableQuestion, "QST"+i, "main", "description", faker.lorem().sentence());
//             client.insertRecord(tableQuestion, "QST"+i, "main", "question_type", typeQ);
//             client.insertRecord(tableQuestion, "QST"+i, "main", "answer_type", typeAnswer[faker.random().nextInt(typeAnswer.length)]);
//             switch (typeQ) {
//                 case "VIDEO":
//                     path = "webhdfs/v1/questions/video_dummy.mp4?op=OPEN";
//                     break;
//                 case "AUDIO":
//                     path = "webhdfs/v1/questions/audio_dummy.mp3?op=OPEN";
//                     break;
//                 case "IMAGE":
//                     path = "webhdfs/v1/questions/image_dummy.png?op=OPEN";
//                     break;
//                 case "NORMAL":
//                     path = "none";
//                     break;
//             }
//             client.insertRecord(tableQuestion, "QST"+i, "main", "file_path", path);
//             client.insertRecord(tableQuestion, "QST"+i, "rps_detail", "id", "RPSD001");
//             client.insertRecord(tableQuestion, "QST"+i, "rps_detail", "sub_cp_mk", "Dummy Sub CP MK");
//             client.insertRecord(tableQuestion, "QST"+i, "detail", "rps_id", "RPS001");
//             client.insertRecord(tableQuestion, "QST"+i, "detail", "created_by", "Doyatama");
//         }
    }
}