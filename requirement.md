# Resume Parser & Job Match Scorer
## Software Requirements Specification (SRS)
**Version:** 1.0.0 | **Language:** Java 17+ | **Type:** Desktop Application | **Status:** Active Development

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Technology Stack](#2-technology-stack)
3. [Maven Dependencies](#3-maven-dependencies-pomxml)
4. [Project Structure & Packages](#4-project-structure--packages)
5. [Modules & Classes](#5-modules--classes--detailed-breakdown)
   - [5.1 PDF Parser Module](#51-pdf-parser-module)
   - [5.2 DOCX Parser Module](#52-docx-parser-module)
   - [5.3 NLP Text Analyzer Module](#53-nlp-text-analyzer-module)
   - [5.4 Scoring Engine Module](#54-scoring-engine-module)
6. [Database Schema (SQLite)](#6-database-schema-sqlite)
7. [UI Screens (Java Swing)](#7-ui-screens-java-swing)
8. [API Connectivity](#8-api-connectivity)
   - [8.1 LinkedIn Jobs API via RapidAPI](#81-linkedin-jobs-api-via-rapidapi)
   - [8.2 Email via SMTP (JavaMail)](#82-email-via-smtp-javamail)
9. [PDF Report Generation](#9-pdf-report-generation)
10. [File Handling Requirements](#10-file-handling-requirements)
11. [Functional Requirements](#11-functional-requirements)
12. [Non-Functional Requirements](#12-non-functional-requirements)
13. [Development Roadmap](#13-development-roadmap)
14. [Pre-Seeded Skills Dictionary](#14-pre-seeded-skills-dictionary)
15. [Configuration Properties](#15-configuration-properties)
16. [Test Plan](#16-test-plan)

---

## 1. Project Overview

The **Resume Parser & Job Match Scorer** is a fully functional Java-based desktop application that enables users to:

- Upload their resume (PDF or DOCX format)
- Paste or fetch a job description (manually or via LinkedIn Jobs API)
- Receive an intelligent **match score (0–100%)** with detailed breakdown
- View missing skills, matched skills, and actionable recommendations
- Export a professional PDF report and optionally email it

The system extracts structured data from resumes, performs NLP-based analysis, computes weighted scoring, stores history in a local SQLite database, and generates downloadable PDF reports — all without any internet connection required (API features are optional enhancements).

### Core Objectives

- Parse and extract structured content from PDF and DOCX resume files
- Analyze job descriptions to identify required skills, experience, and keywords
- Compute a multi-factor weighted match score (0–100%)
- Store all scan results in a local SQLite database with full history tracking
- Export detailed analysis reports as downloadable PDF files
- Optionally fetch live job descriptions via RapidAPI JSearch (LinkedIn Jobs API)
- Send email reports via JavaMail SMTP integration
- Provide a polished desktop UI with charts and visual score feedback

---

## 2. Technology Stack

| Layer | Technology | Version | Purpose |
|---|---|---|---|
| Language | Java | 17 LTS | Core application language |
| UI Framework | Java Swing | Built-in | Desktop GUI components |
| UI Theme | FlatLaf | 3.4.1 | Modern look & feel for Swing |
| PDF Reading | Apache PDFBox | 3.0.2 | Extract text from PDF resumes |
| DOCX Reading | Apache POI | 5.2.5 | Extract text from Word resumes |
| PDF Export | Apache PDFBox | 3.0.2 | Generate downloadable reports |
| Database | SQLite JDBC | 3.45.1.0 | Local persistent storage |
| Charts | JFreeChart | 1.5.4 | Skill gap & score visualizations |
| HTTP Client | OkHttp | 4.12.0 | LinkedIn Jobs API calls |
| JSON Parser | Gson | 2.10.1 | Parse API JSON responses |
| Email | Jakarta Mail | 2.0.1 | Send reports via SMTP |
| Build Tool | Apache Maven | 3.9+ | Dependency & build management |
| IDE | IntelliJ IDEA | 2024+ | Recommended development IDE |
| Java Version | OpenJDK | 17 LTS | Minimum runtime requirement |

---

## 3. Maven Dependencies (pom.xml)

All dependencies are available on Maven Central — no paid licenses required.

```xml
<dependencies>

    <!-- PDF Reading & Report Generation -->
    <dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>3.0.2</version>
    </dependency>

    <!-- DOCX Resume Reading -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.5</version>
    </dependency>

    <!-- SQLite Database -->
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.45.1.0</version>
    </dependency>

    <!-- Modern Swing UI Theme -->
    <dependency>
        <groupId>com.formdev</groupId>
        <artifactId>flatlaf</artifactId>
        <version>3.4.1</version>
    </dependency>

    <!-- Charts & Graphs -->
    <dependency>
        <groupId>org.jfree</groupId>
        <artifactId>jfreechart</artifactId>
        <version>1.5.4</version>
    </dependency>

    <!-- HTTP Client for API Calls -->
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>4.12.0</version>
    </dependency>

    <!-- JSON Parsing -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>

    <!-- Email (SMTP / JavaMail) -->
    <dependency>
        <groupId>org.eclipse.angus</groupId>
        <artifactId>jakarta.mail</artifactId>
        <version>2.0.3</version>
    </dependency>

    <!-- Logging -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.12</version>
    </dependency>

    <!-- Unit Testing -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>

</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>
        <!-- Fat JAR: bundles all dependencies for single-file distribution -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.6.0</version>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>com.resumematcher.Main</mainClass>
                    </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
            <executions>
                <execution>
                    <id>make-assembly</id>
                    <phase>package</phase>
                    <goals><goal>single</goal></goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

---

## 4. Project Structure & Packages

```
resume-matcher/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/resumematcher/
    │   │   ├── Main.java                          # Application entry point
    │   │   ├── config/
    │   │   │   ├── AppConfig.java                 # App-wide configuration loader
    │   │   │   ├── APIConfig.java                 # API keys and endpoints
    │   │   │   └── DBConfig.java                  # Database path and settings
    │   │   ├── ui/
    │   │   │   ├── MainFrame.java                 # Root JFrame with CardLayout
    │   │   │   ├── HomeScreen.java                # Upload + paste JD screen
    │   │   │   ├── ScanProgressScreen.java        # Animated progress screen
    │   │   │   ├── ResultsScreen.java             # Score + matched/missing skills
    │   │   │   ├── SkillGapScreen.java            # JFreeChart skill gap chart
    │   │   │   ├── HistoryScreen.java             # Past scan history table
    │   │   │   ├── JobSearchScreen.java           # API job search screen
    │   │   │   ├── ReportScreen.java              # PDF export & email screen
    │   │   │   ├── SettingsScreen.java            # API key, SMTP, preferences
    │   │   │   ├── AboutScreen.java               # App info and credits
    │   │   │   └── components/
    │   │   │       ├── ScoreMeterPanel.java       # Custom circular score meter
    │   │   │       ├── SkillTagPanel.java         # Green/red skill tag chips
    │   │   │       ├── SideNavPanel.java          # Left navigation sidebar
    │   │   │       └── LoadingOverlay.java        # Translucent loading overlay
    │   │   ├── parser/
    │   │   │   ├── PDFParser.java                 # Apache PDFBox PDF extractor
    │   │   │   ├── DOCXParser.java                # Apache POI DOCX extractor
    │   │   │   └── TextCleaner.java               # Normalize and clean raw text
    │   │   ├── nlp/
    │   │   │   ├── Tokenizer.java                 # Word/phrase tokenizer + n-grams
    │   │   │   ├── StopWordFilter.java            # Remove common stop words
    │   │   │   ├── SkillExtractor.java            # Match tokens against skills dict
    │   │   │   ├── SectionDetector.java           # Detect resume sections
    │   │   │   ├── ExperienceCalculator.java      # Parse date ranges → years
    │   │   │   └── EducationExtractor.java        # Detect highest degree level
    │   │   ├── scorer/
    │   │   │   ├── ScoringEngine.java             # Orchestrates full scoring pipeline
    │   │   │   ├── TFIDFCalculator.java           # TF-IDF keyword scoring
    │   │   │   ├── JaccardSimilarity.java         # Set overlap calculation
    │   │   │   ├── WeightedScorer.java            # Combines sub-scores with weights
    │   │   │   └── GapAnalyzer.java               # Missing skills + recommendations
    │   │   ├── db/
    │   │   │   ├── DatabaseManager.java           # Connection pool, table creation
    │   │   │   ├── ResumeDAO.java                 # CRUD for resumes table
    │   │   │   ├── JobDAO.java                    # CRUD for job_descriptions table
    │   │   │   └── ScanResultDAO.java             # CRUD for scan_results table
    │   │   ├── api/
    │   │   │   ├── LinkedInAPIClient.java         # OkHttp calls to JSearch API
    │   │   │   ├── APIResponse.java               # Generic API response wrapper
    │   │   │   └── JobListing.java                # Model for API job result
    │   │   ├── email/
    │   │   │   ├── EmailSender.java               # Jakarta Mail SMTP sender
    │   │   │   └── EmailTemplate.java             # HTML email body builder
    │   │   ├── report/
    │   │   │   ├── PDFReportGenerator.java        # PDFBox report builder
    │   │   │   └── ReportData.java                # Data container for report
    │   │   ├── model/
    │   │   │   ├── Resume.java                    # Resume POJO
    │   │   │   ├── JobDescription.java            # Job description POJO
    │   │   │   ├── ScanResult.java                # Full scan result POJO
    │   │   │   ├── Skill.java                     # Skill with name, category, weight
    │   │   │   └── SkillCategory.java             # Enum: PROGRAMMING, FRAMEWORK, etc.
    │   │   └── util/
    │   │       ├── FileUtils.java                 # File copy, path helpers
    │   │       ├── TextUtils.java                 # String manipulation helpers
    │   │       ├── DateUtils.java                 # Date parsing and formatting
    │   │       └── Constants.java                 # App-wide constant values
    │   └── resources/
    │       ├── db/
    │       │   └── skills_seed.sql                # 500+ skills pre-seed SQL
    │       ├── nlp/
    │       │   └── stopwords.txt                  # English stop words list
    │       └── ui/
    │           └── icons/                         # SVG/PNG icons for UI
    └── test/
        └── java/com/resumematcher/
            ├── parser/PDFParserTest.java
            ├── parser/DOCXParserTest.java
            ├── nlp/TokenizerTest.java
            ├── nlp/SkillExtractorTest.java
            ├── scorer/ScoringEngineTest.java
            ├── scorer/TFIDFCalculatorTest.java
            ├── scorer/JaccardSimilarityTest.java
            └── db/DatabaseManagerTest.java
```

---

## 5. Modules & Classes — Detailed Breakdown

### 5.1 PDF Parser Module

Responsible for reading uploaded PDF resume files and converting them to clean plain text.

| Class | Method | Description |
|---|---|---|
| `PDFParser` | `String extractText(File file)` | Opens PDF using PDFBox, extracts all text page by page |
| `PDFParser` | `List<String> extractPages(File file)` | Returns list of strings, one entry per page |
| `PDFParser` | `boolean isValidPDF(File file)` | Validates file is readable, non-empty PDF |
| `PDFParser` | `String extractWithLayout(File file)` | Uses PDFTextStripperByArea for multi-column layouts |
| `TextCleaner` | `String cleanResumeText(String raw)` | Removes special chars, normalizes whitespace, fixes encoding |
| `TextCleaner` | `String normalizeWhitespace(String text)` | Collapses multiple spaces, trims all lines |
| `TextCleaner` | `String removeBulletSymbols(String text)` | Strips unicode bullet/arrow characters from text |

---

### 5.2 DOCX Parser Module

Handles Microsoft Word (.docx) resume files using Apache POI.

| Class | Method | Description |
|---|---|---|
| `DOCXParser` | `String extractText(File file)` | Uses `XWPFDocument` to read all paragraphs and tables |
| `DOCXParser` | `List<String> extractParagraphs(File file)` | Returns paragraph-level text list |
| `DOCXParser` | `String extractTableText(File file)` | Extracts text from tables inside the DOCX |
| `DOCXParser` | `boolean isValidDOCX(File file)` | Validates file format and readability |

---

### 5.3 NLP Text Analyzer Module

Core intelligence layer — breaks down text, removes noise, and extracts meaningful entities.

| Class | Method | Description |
|---|---|---|
| `Tokenizer` | `List<String> tokenize(String text)` | Splits text into words using whitespace and punctuation rules |
| `Tokenizer` | `List<String> tokenizeNGrams(String text, int n)` | Generates bigrams/trigrams for multi-word skill detection |
| `StopWordFilter` | `List<String> filter(List<String> tokens)` | Removes common English stop words from token list |
| `StopWordFilter` | `void loadStopWords()` | Loads stop word list from bundled `stopwords.txt` resource |
| `SkillExtractor` | `List<Skill> extractSkills(String text)` | Matches tokens against skills dictionary, returns found skills |
| `SkillExtractor` | `void loadSkillsDictionary()` | Loads 500+ skills from SQLite `skills_dict` table on startup |
| `SkillExtractor` | `boolean matchesAlias(String token, Skill skill)` | Checks if token matches any alias of a skill |
| `SectionDetector` | `Map<String,String> detectSections(String text)` | Identifies EDUCATION, EXPERIENCE, SKILLS, PROJECTS sections |
| `ExperienceCalculator` | `int calculateYearsExperience(String text)` | Parses date ranges (e.g. `2020–2023`) and sums total years |
| `EducationExtractor` | `String extractHighestDegree(String text)` | Detects B.Tech, M.Tech, MBA, PhD etc. from education section |

---

### 5.4 Scoring Engine Module

Computes the final match score using multiple weighted algorithms.

| Class | Method | Description |
|---|---|---|
| `ScoringEngine` | `ScanResult computeScore(Resume r, JobDescription jd)` | Orchestrates all sub-scorers and returns final `ScanResult` |
| `TFIDFCalculator` | `Map<String,Double> computeTFIDF(List<String> tokens, List<List<String>> corpus)` | Computes TF-IDF scores for keyword importance |
| `JaccardSimilarity` | `double compute(Set<String> set1, Set<String> set2)` | Returns overlap ratio between two token sets (0.0–1.0) |
| `WeightedScorer` | `double computeWeightedScore(Map<String,Double> scores)` | Applies weights: skills 50%, experience 25%, education 15%, keywords 10% |
| `GapAnalyzer` | `List<Skill> findMissingSkills(List<Skill> resumeSkills, List<Skill> jdSkills)` | Returns skills in JD not found in resume |
| `GapAnalyzer` | `List<String> generateRecommendations(List<Skill> gaps)` | Produces actionable improvement suggestions per missing skill |

#### Scoring Formula

```
Final Score = (Skills Score × 0.50)
            + (Experience Score × 0.25)
            + (Education Score × 0.15)
            + (Keyword Score × 0.10)
```

| Factor | Weight | Algorithm Used |
|---|---|---|
| Technical Skills Match | 50% | Jaccard Similarity on skill sets |
| Experience Match | 25% | Date range parsing + year comparison |
| Education Match | 15% | Degree level keyword matching |
| Keyword Density Match | 10% | TF-IDF weighted token overlap |

---

## 6. Database Schema (SQLite)

All data is stored locally in a SQLite database file at:
`~/.resumematcher/resumematcher.db`

No external database server is required. The file and all tables are auto-created on first launch.

---

### Table: `resumes`

```sql
CREATE TABLE IF NOT EXISTS resumes (
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    filename          TEXT    NOT NULL,
    filepath          TEXT    NOT NULL,
    extracted_text    TEXT    NOT NULL,
    upload_date       TEXT    NOT NULL,   -- ISO 8601 timestamp
    skills_json       TEXT,               -- JSON array of extracted skill names
    experience_years  REAL,               -- Total calculated years
    highest_degree    TEXT                -- e.g. "B.Tech", "MBA", "PhD"
);
```

---

### Table: `job_descriptions`

```sql
CREATE TABLE IF NOT EXISTS job_descriptions (
    id                    INTEGER PRIMARY KEY AUTOINCREMENT,
    title                 TEXT    NOT NULL,
    company               TEXT,
    description_text      TEXT    NOT NULL,
    required_skills_json  TEXT,           -- JSON array of required skill names
    source                TEXT,           -- "manual" | "linkedin_api" | "file"
    date_added            TEXT    NOT NULL -- ISO 8601 timestamp
);
```

---

### Table: `scan_results`

```sql
CREATE TABLE IF NOT EXISTS scan_results (
    id                      INTEGER PRIMARY KEY AUTOINCREMENT,
    resume_id               INTEGER NOT NULL REFERENCES resumes(id),
    job_id                  INTEGER NOT NULL REFERENCES job_descriptions(id),
    total_score             REAL    NOT NULL,   -- 0.0 to 100.0
    skills_score            REAL,
    experience_score        REAL,
    education_score         REAL,
    keyword_score           REAL,
    matched_skills_json     TEXT,               -- JSON array
    missing_skills_json     TEXT,               -- JSON array
    recommendations_json    TEXT,               -- JSON array of strings
    scan_date               TEXT    NOT NULL    -- ISO 8601 timestamp
);
```

---

### Table: `skills_dict`

```sql
CREATE TABLE IF NOT EXISTS skills_dict (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    skill_name  TEXT    NOT NULL UNIQUE,
    category    TEXT    NOT NULL,   -- Programming | Framework | Database | Cloud | Soft Skill
    aliases_json TEXT,              -- JSON array e.g. ["J2EE", "Core Java"]
    weight      REAL    DEFAULT 1.0 -- Importance multiplier for scoring
);
```

> **Note:** `skills_dict` is pre-seeded with 500+ technology skills on first launch via `src/main/resources/db/skills_seed.sql`.

---

## 7. UI Screens (Java Swing)

The application uses Java Swing with **FlatLaf** theme (light/dark toggle). All screens are `JPanel` subclasses swapped inside a root `JFrame` using `CardLayout`.

| Screen Class | Screen Name | Key UI Components |
|---|---|---|
| `HomeScreen.java` | Home / Upload | `JFileChooser` for PDF/DOCX, `JTextArea` for job description paste, Scan button, recent scans mini-list |
| `ScanProgressScreen.java` | Scanning Progress | `JProgressBar` with stage labels (Parsing → Extracting → Scoring), animated indicator using `SwingWorker` |
| `ResultsScreen.java` | Results & Score | Custom circular score meter (`ScoreMeterPanel`), matched skills list (green), missing skills list (red), sub-score breakdown |
| `SkillGapScreen.java` | Skill Gap Chart | `JFreeChart` BarChart comparing resume skills vs. required skills by category |
| `HistoryScreen.java` | Scan History | `JTable` of all past scans, sortable columns, click to re-open any result, delete option |
| `JobSearchScreen.java` | Job Search (API) | Search field, results `JList` from LinkedIn/JSearch API, button to use selected JD for scanning |
| `ReportScreen.java` | Export Report | Preview panel, export PDF button, email report button with recipient field |
| `SettingsScreen.java` | Settings | API key fields, SMTP email config, theme toggle (light/dark), DB path display, clear history button |
| `AboutScreen.java` | About | Project info, version, tech stack credits |

### Custom UI Components

| Component Class | Description |
|---|---|
| `ScoreMeterPanel.java` | Custom `JPanel` with `Graphics2D` arc drawing for animated circular score display |
| `SkillTagPanel.java` | Flow-layout panel rendering skill names as colored chip tags (green = matched, red = missing) |
| `SideNavPanel.java` | Left sidebar with icon + label navigation buttons for all screens |
| `LoadingOverlay.java` | Translucent JPanel overlay with spinner shown during background processing |

---

## 8. API Connectivity

### 8.1 LinkedIn Jobs API via RapidAPI

Allows users to search and fetch real live job descriptions directly inside the app. Uses the **JSearch API** on RapidAPI.

| Item | Detail |
|---|---|
| API Provider | RapidAPI — JSearch API |
| API URL | `https://rapidapi.com/letscrape-6bRB4Kjpnw/api/jsearch` |
| HTTP Client | OkHttp 4.12.0 |
| Base URL | `https://jsearch.p.rapidapi.com` |
| Endpoint | `GET /search?query={title}&page=1&num_pages=1` |
| Auth Header 1 | `X-RapidAPI-Key: {user_api_key}` |
| Auth Header 2 | `X-RapidAPI-Host: jsearch.p.rapidapi.com` |
| Response Format | JSON — parsed via Gson into `JobListing` model objects |
| Class | `com.resumematcher.api.LinkedInAPIClient` |
| Method | `List<JobListing> searchJobs(String query, String apiKey)` |
| Timeout | 10 seconds (configured in OkHttp client) |
| Rate Limit | Free tier: 500 requests/month (shown in Settings screen) |
| Fallback | If API key not set, user pastes JD manually — all features still work |

#### LinkedInAPIClient Implementation Outline

```java
// com.resumematcher.api.LinkedInAPIClient.java

public class LinkedInAPIClient {

    private static final String BASE_URL = "https://jsearch.p.rapidapi.com";
    private final OkHttpClient httpClient;
    private final Gson gson;

    public LinkedInAPIClient() {
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();
        this.gson = new Gson();
    }

    public List<JobListing> searchJobs(String query, String apiKey) throws IOException {
        String url = BASE_URL + "/search?query=" 
                   + URLEncoder.encode(query, "UTF-8") 
                   + "&page=1&num_pages=1";

        Request request = new Request.Builder()
            .url(url)
            .get()
            .addHeader("X-RapidAPI-Key", apiKey)
            .addHeader("X-RapidAPI-Host", "jsearch.p.rapidapi.com")
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("API error: " + response.code());
            String body = response.body().string();
            APIResponse apiResponse = gson.fromJson(body, APIResponse.class);
            return apiResponse.getData(); // List<JobListing>
        }
    }
}
```

---

### 8.2 Email via SMTP (JavaMail)

Allows the user to email the generated PDF report to themselves or a recruiter directly from the app.

| Item | Detail |
|---|---|
| Library | Jakarta Mail 2.0.3 (`org.eclipse.angus:jakarta.mail`) |
| Protocol | SMTP with STARTTLS |
| Default SMTP Host | `smtp.gmail.com` |
| Default SMTP Port | `587` |
| Auth | Username + Gmail App Password (not regular password) |
| Class | `com.resumematcher.email.EmailSender` |
| Method | `void sendReport(String toAddress, File pdfAttachment, String candidateName)` |
| Email Body | HTML formatted by `EmailTemplate.java` with score summary |
| Config Storage | `~/.resumematcher/config.properties` — never hardcoded in source |

#### EmailSender Implementation Outline

```java
// com.resumematcher.email.EmailSender.java

public class EmailSender {

    public void sendReport(String toAddress, File pdfFile, String candidateName,
                           String smtpHost, int smtpPort,
                           String username, String appPassword) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
        message.setSubject("Resume Match Report — " + candidateName);

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(EmailTemplate.buildHTML(candidateName), "text/html");

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(pdfFile);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);

        Transport.send(message);
    }
}
```

---

## 9. PDF Report Generation

When the user clicks **Export Report**, the app generates a professional multi-page PDF using Apache PDFBox.

| Class / Section | Description |
|---|---|
| `PDFReportGenerator.generate(ScanResult result, File outputPath)` | Main entry point — assembles all pages and saves PDF to disk |
| **Page 1 — Cover** | Candidate name, job title, scan date, overall score badge (colored: green/yellow/red) |
| **Page 2 — Score Breakdown** | Visual horizontal bars for each sub-score: Skills, Experience, Education, Keywords |
| **Page 3 — Matched Skills** | Green-highlighted table of all skills found in both resume and JD |
| **Page 4 — Skills Gap** | Red-highlighted table of missing skills with their category labels |
| **Page 5 — Recommendations** | Numbered actionable improvement tips, one per missing skill |
| **Page 6 — Raw Resume Text** | Extracted resume text for reference/verification |

---

## 10. File Handling Requirements

| Requirement | Detail |
|---|---|
| Supported Resume Formats | PDF (`.pdf`) and Microsoft Word (`.docx`) |
| Max File Size | 10 MB per uploaded file |
| File Storage Location | Uploaded files copied to `~/.resumematcher/uploads/` for history access |
| File Validation | MIME type check + magic bytes check before parsing (not just extension check) |
| Multi-page PDF Support | All pages parsed; text concatenated with section break markers |
| Multi-column PDF Support | `PDFTextStripperByArea` used for 2-column resume layouts |
| Export Path | Reports saved to `~/.resumematcher/reports/` by default; user can override in Settings |
| Config File Path | `~/.resumematcher/config.properties` — stores API keys, SMTP, preferences |
| Database File Path | `~/.resumematcher/resumematcher.db` — auto-created on first launch |
| Skills Seed File | Bundled at `src/main/resources/db/skills_seed.sql` — runs on first launch only |

---

## 11. Functional Requirements

### 11.1 Resume Upload & Parsing

- **FR-001** — User can upload a PDF or DOCX resume via a file chooser dialog
- **FR-002** — System validates file type and size before processing begins
- **FR-003** — System extracts full text from all pages of the uploaded file
- **FR-004** — System detects and labels resume sections: Education, Experience, Skills, Projects, Summary
- **FR-005** — System extracts years of experience from detected date ranges (e.g. `Jan 2020 – Mar 2023`)
- **FR-006** — System identifies the highest education degree mentioned (B.Tech, MBA, PhD, etc.)
- **FR-007** — System extracts all recognized skill keywords from the resume text
- **FR-008** — Extracted resume data is saved to the `resumes` table in SQLite

### 11.2 Job Description Analysis

- **FR-009** — User can paste a job description manually in a multi-line text area
- **FR-010** — User can search for live job descriptions via the JSearch/LinkedIn API (requires API key)
- **FR-011** — System extracts required skills from the job description text
- **FR-012** — System identifies seniority level (intern / junior / mid / senior) from JD text
- **FR-013** — System identifies required years of experience mentioned in JD
- **FR-014** — Job description data is saved to the `job_descriptions` table

### 11.3 Scoring & Analysis

- **FR-015** — System computes a technical skills match score using Jaccard Similarity
- **FR-016** — System computes an experience match score based on required vs. actual years
- **FR-017** — System computes an education match score based on degree level hierarchy
- **FR-018** — System computes a keyword density score using TF-IDF
- **FR-019** — System combines sub-scores using configurable weights into a final score (0–100)
- **FR-020** — System produces a list of all missing skills (gap analysis)
- **FR-021** — System generates at least one recommendation per missing skill
- **FR-022** — Complete scan result is stored in `scan_results` table with all sub-scores

### 11.4 UI & Navigation

- **FR-023** — App opens to Home screen on launch
- **FR-024** — All screens are navigable via the left sidebar
- **FR-025** — Score is displayed as an animated circular meter on the Results screen
- **FR-026** — Matched skills shown in green chip tags; missing skills shown in red chip tags
- **FR-027** — Skill gap bar chart rendered using JFreeChart on Skill Gap screen
- **FR-028** — History screen lists all past scans with date, job title, and score
- **FR-029** — User can click any history entry to reload its full result
- **FR-030** — Settings screen allows entry of RapidAPI key and SMTP email credentials

### 11.5 Report & Email

- **FR-031** — User can export scan results as a multi-page PDF report
- **FR-032** — PDF report includes score, sub-score breakdown, matched skills, gaps, and recommendations
- **FR-033** — User can email the generated PDF report directly from the app
- **FR-034** — Email credentials configured in Settings and stored in `config.properties`

---

## 12. Non-Functional Requirements

| Category | Requirement |
|---|---|
| **Performance** | Full scan (parse + score) completes within 5 seconds for any resume under 10 MB |
| **Performance** | UI remains responsive during processing — all parsing runs on background `SwingWorker` thread |
| **Reliability** | All database operations wrapped in try-catch with transaction rollback on failure |
| **Reliability** | API calls have 10-second timeout; app works fully offline without an API key |
| **Usability** | All error messages shown in `JOptionPane` dialogs with clear, non-technical language |
| **Usability** | App window minimum size 1024×768; supports window resizing and maximizing |
| **Security** | API keys and email passwords never stored in plain Java source code |
| **Security** | All credentials stored only in `~/.resumematcher/config.properties` |
| **Portability** | Runs on Windows, macOS, and Linux with Java 17+ installed |
| **Portability** | Distributed as a single fat JAR via `maven-assembly-plugin` |
| **Maintainability** | Each module in its own package; DAO pattern used for all database access |
| **Testability** | Core scoring and NLP logic has JUnit unit tests covering at least 80% of methods |

---

## 13. Development Roadmap

| Week | Phase | Deliverables |
|---|---|---|
| Week 1 | Project Setup | Maven project, `pom.xml` with all dependencies, full package structure, `Main.java`, `AppConfig.java`, DB auto-creation on launch, skills seed SQL execution |
| Week 2 | PDF & DOCX Parsing | `PDFParser`, `DOCXParser`, `TextCleaner` fully working; unit tests for all parsers; tested against 5 sample resumes |
| Week 3 | NLP Engine | `Tokenizer`, `StopWordFilter`, `SkillExtractor`, `SectionDetector`, `ExperienceCalculator`, `EducationExtractor`; integration with parser output |
| Week 4 | Scoring Engine | `TFIDFCalculator`, `JaccardSimilarity`, `WeightedScorer`, `GapAnalyzer`; full scoring pipeline working end-to-end in console |
| Week 5 | Database Layer | `DatabaseManager`, all four DAO classes, full CRUD for all tables; integration tests passing |
| Week 6 | Swing UI | All 9 screens built with `CardLayout` navigation; FlatLaf theme applied; file chooser and text area working |
| Week 7 | API & Email | `LinkedInAPIClient` with OkHttp + Gson; `EmailSender` with Jakarta Mail; Settings screen fully functional |
| Week 8 | Report Generation | `PDFReportGenerator` producing all 6 report pages; export and email flow working end-to-end |
| Week 9 | Charts & Polish | JFreeChart skill gap bar chart; `ScoreMeterPanel` custom `Graphics2D` paint; UI polish and window resizing |
| Week 10 | Testing & Demo | Full integration testing; JUnit coverage check; fix edge cases; demo video; complete README |

---

## 14. Pre-Seeded Skills Dictionary

The `skills_dict` table is pre-populated with **500+ skills** on first launch via `src/main/resources/db/skills_seed.sql`.

| Category | Sample Skills |
|---|---|
| Programming Languages | Java, Python, C++, C#, JavaScript, TypeScript, Kotlin, Swift, Go, Rust, PHP, Ruby, Scala, R, MATLAB |
| Web Frameworks | Spring Boot, Spring MVC, Hibernate, React, Angular, Vue.js, Node.js, Django, Flask, Express.js, ASP.NET |
| Databases | MySQL, PostgreSQL, SQLite, MongoDB, Redis, Cassandra, Oracle DB, Microsoft SQL Server, DynamoDB, Firebase |
| Cloud Platforms | AWS, Azure, Google Cloud, Heroku, Docker, Kubernetes, Terraform, Jenkins, GitHub Actions, CircleCI |
| Mobile | Android SDK, iOS/Swift, Flutter, React Native, Xamarin, Jetpack Compose |
| Data & ML | TensorFlow, PyTorch, Scikit-learn, Pandas, NumPy, Tableau, Power BI, Apache Spark, Hadoop, Kafka |
| DevOps & Tools | Git, GitHub, GitLab, Maven, Gradle, IntelliJ IDEA, VS Code, Postman, Jira, Confluence, Linux/Bash |
| Soft Skills | Communication, Leadership, Problem Solving, Teamwork, Agile, Scrum, Project Management, Critical Thinking |

---

## 15. Configuration Properties

All settings stored in `~/.resumematcher/config.properties` — auto-created on first launch with defaults.

| Property Key | Default Value | Description |
|---|---|---|
| `db.path` | `~/.resumematcher/resumematcher.db` | Path to the SQLite database file |
| `uploads.dir` | `~/.resumematcher/uploads` | Directory where uploaded resumes are stored |
| `reports.dir` | `~/.resumematcher/reports` | Directory where exported PDF reports are saved |
| `api.rapidapi.key` | *(empty)* | RapidAPI key for JSearch API — enter in Settings |
| `api.rapidapi.host` | `jsearch.p.rapidapi.com` | RapidAPI host header value |
| `smtp.host` | `smtp.gmail.com` | SMTP server hostname |
| `smtp.port` | `587` | SMTP server port (STARTTLS) |
| `smtp.username` | *(empty)* | Email address used for SMTP authentication |
| `smtp.password` | *(empty)* | Gmail App Password (not your regular password) |
| `ui.theme` | `light` | UI theme: `light` or `dark` (FlatLaf toggle) |
| `scoring.weight.skills` | `0.50` | Weight for technical skills match factor |
| `scoring.weight.experience` | `0.25` | Weight for experience match factor |
| `scoring.weight.education` | `0.15` | Weight for education match factor |
| `scoring.weight.keywords` | `0.10` | Weight for keyword density match factor |

---

## 16. Test Plan

| Test Type | Test Class | What to Test |
|---|---|---|
| Unit | `PDFParserTest` | Valid PDF, password-protected PDF, corrupted file, empty file, multi-column layout |
| Unit | `DOCXParserTest` | Valid DOCX, DOCX with tables, DOCX with embedded images, empty DOCX |
| Unit | `TokenizerTest` | Normal text, unicode characters, numbers-only input, empty string |
| Unit | `SkillExtractorTest` | Known skills present, case-insensitive match, alias match, no skills found |
| Unit | `ScoringEngineTest` | Perfect match (100%), zero match (0%), partial match, all weight combinations |
| Unit | `TFIDFCalculatorTest` | Single document corpus, multi-document corpus, empty token list |
| Unit | `JaccardSimilarityTest` | Identical sets (1.0), completely disjoint sets (0.0), partial overlap |
| Unit | `DatabaseManagerTest` | Table auto-creation, insert/read/delete for all 4 tables, duplicate handling |
| Integration | `FullScanPipelineTest` | End-to-end: PDF upload → parse → NLP → score → DB save → result display |
| Integration | `APIClientTest` | Mock HTTP responses, API key missing fallback, network timeout handling |
| Integration | `EmailSenderTest` | Valid config sends email, missing config shows error dialog gracefully |
| Manual Smoke | Full App Test | Launch → upload resume → paste JD → run scan → view results → export PDF → email |

---

*End of Software Requirements Specification — Resume Parser & Job Match Scorer v1.0*
