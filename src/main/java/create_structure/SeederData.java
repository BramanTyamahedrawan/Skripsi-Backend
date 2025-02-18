package create_structure;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import com.github.javafaker.Faker;

public class SeederData {
    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        HBaseCustomClient client = new HBaseCustomClient(conf);

        // time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        // define name all table;
        TableName tableSubject = TableName.valueOf("subjects");
        TableName tableChapter = TableName.valueOf("chapters");
        TableName tableSubjectGroup = TableName.valueOf("subject_groups");
        TableName tableLecture = TableName.valueOf("lectures");
        TableName tableStudent = TableName.valueOf("students");
        TableName tableRPS = TableName.valueOf("rps");
        TableName tableRPSDetail = TableName.valueOf("rps_details");
        TableName tableReference = TableName.valueOf("references");
        TableName tableLearningMedia = TableName.valueOf("learning_medias");
        TableName tableReligion = TableName.valueOf("religions");
        TableName tableDepartment = TableName.valueOf("departments");
        TableName tableStudyProgram = TableName.valueOf("study_programs");
        TableName tableUser = TableName.valueOf("users");
        TableName tableAppraisalForm = TableName.valueOf("appraisal_forms");
        TableName tableAssessmentCriteria = TableName.valueOf("assessment_criterias");
        TableName tableFormLearning = TableName.valueOf("form_learnings");
        TableName tableLearningMethod = TableName.valueOf("learning_methods");
        TableName tableQuestion = TableName.valueOf("questions");
        TableName tableAnswer = TableName.valueOf("answers");
        TableName tableExam = TableName.valueOf("exams");
        TableName tableQuizzes = TableName.valueOf("quizzes");
        TableName tableExcercise = TableName.valueOf("exercises");

        // ==============================================================================================
        // INSERT DATA
        // ==============================================================================================

        // for (int i = 1000000; i < 5000000; i++) {
        // Faker faker = new Faker();
        // String[] typeQuestion = {"VIDEO", "AUDIO", "IMAGE", "NORMAL"};
        // String[] typeAnswer = {"MULTIPLE_CHOICE", "BOOLEAN", "COMPLETION",
        // "MATCHING", "ESSAY"};
        // String typeQ = typeQuestion[faker.random().nextInt(typeQuestion.length)];
        // String path = "";
        // client.insertRecord(tableQuestion, "QST"+i, "main", "id", "QST"+i);
        // client.insertRecord(tableQuestion, "QST"+i, "main", "title",
        // faker.lorem().sentence());
        // client.insertRecord(tableQuestion, "QST"+i, "main", "description",
        // faker.lorem().sentence());
        // client.insertRecord(tableQuestion, "QST"+i, "main", "question_type", typeQ);
        // client.insertRecord(tableQuestion, "QST"+i, "main", "answer_type",
        // typeAnswer[faker.random().nextInt(typeAnswer.length)]);
        // switch (typeQ) {
        // case "VIDEO":
        // path = "webhdfs/v1/questions/video_dummy.mp4?op=OPEN";
        // break;
        // case "AUDIO":
        // path = "webhdfs/v1/questions/audio_dummy.mp3?op=OPEN";
        // break;
        // case "IMAGE":
        // path = "webhdfs/v1/questions/image_dummy.png?op=OPEN";
        // break;
        // case "NORMAL":
        // path = "none";
        // break;
        // }
        // client.insertRecord(tableQuestion, "QST"+i, "main", "file_path", path);
        // client.insertRecord(tableQuestion, "QST"+i, "rps_detail", "id", "RPSD001");
        // client.insertRecord(tableQuestion, "QST"+i, "rps_detail", "sub_cp_mk", "Dummy
        // Sub CP MK");
        // client.insertRecord(tableQuestion, "QST"+i, "detail", "rps_id", "RPS001");
        // client.insertRecord(tableQuestion, "QST"+i, "detail", "created_by",
        // "Doyatama");
        // }
    }
}
