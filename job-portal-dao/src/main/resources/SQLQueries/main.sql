CREATE DATABASE IF NOT EXISTS jobPortal CHARACTER SET utf8 COLLATE utf8_general_ci;

USE jobPortal;


CREATE TABLE basic_account_detail
(
  account_id bigint NOT NULL AUTO_INCREMENT,
  email_id char(200),
  work_email_id char(200),
  password_hash text NOT NULL,
  forgot_password_key text,
  forgot_password_key_generate_time datetime,
  is_jobseeker tinyint NOT NULL DEFAULT 0,
  is_recruiter tinyint NOT NULL DEFAULT 0,
  is_expert tinyint NOT NULL DEFAULT 0,
  jobseeker_verification_key text,
  recruiter_verification_key text,
  expert_verification_key text,
  create_date datetime NOT NULL,
  modeified_date datetime NOT NULL,
  emailid_verification_key text,
  work_emailid_verification_key text,
  is_emailid_verified tinyint NOT NULL DEFAULT 0,
  is_work_emailid_verified tinyint NOT NULL DEFAULT 0,
  new_unverified_email_id char(200),
  new_unverfied_work_email_id char(200),
  CONSTRAINT pk_account_details_account_id PRIMARY KEY (account_id)
);

CREATE TABLE industry_code_detail
(
  industry_code integer NOT NULL,
  industry_description char(200) NOT NULL,
  CONSTRAINT pk_industry_code_details PRIMARY KEY (industry_code)
);

CREATE TABLE job_detail
(
  job_id bigint NOT NULL AUTO_INCREMENT,
  job_processing_state char(200) DEFAULT 'NOTPROCESSED',
  jobseeker_processing_state char(200) DEFAULT 'NOTPROCESSED',
  last_processed_time datetime,
  title text NOT NULL,
  organisation_name text,
  industry_name text,
  min_salary integer,
  max_salary integer,
  salary_currency_code char(10) NOT NULL,
  keep_salay_hidden tinyint NOT NULL DEFAULT 0,
  only_top_tier_institute tinyint NOT NULL DEFAULT 0,
  jd_file_location text,
  job_description text,
  job_status char(200) NOT NULL,
  expire_date datetime NOT NULL,
  recruiter_id bigint NOT NULL,
  create_date datetime NOT NULL,
  modified_date datetime,
  location_detail text,
  employment_type text,
  job_experiance integer,
  job_applicability_type text,
  link_to_site text,
  job_function text,
  job_skills text,
  organisation_tier integer DEFAULT 0,
  CONSTRAINT pk_jobdetail_id PRIMARY KEY (job_id)
);

CREATE TABLE job_relationship_detail
(
  job_relationship_id bigint NOT NULL AUTO_INCREMENT,
  job_id bigint NOT NULL,
  recruiter_id bigint NOT NULL,
  jobseeker_id bigint NOT NULL,
  expert_id bigint,
  job_application_status char(100),
  recruiter_application_status char(100),
  jobSeeker_application_status char(100),
  rejected_date date,
  expert_application_status char(100),
  expert_assessment text,
  is_in_jobseeker_wishlist tinyint NOT NULL DEFAULT 0,
  is_in_recruiter_wishlist tinyint NOT NULL DEFAULT 0,
  create_date datetime NOT NULL,
  modified_date datetime NOT NULL,
  CONSTRAINT pk_job_relationship_detail_id PRIMARY KEY (job_relationship_id)
);

CREATE TABLE jobs_by_user
(
  job_by_user_id bigint NOT NULL AUTO_INCREMENT,
  job_id bigint NOT NULL,
  user_id bigint NOT NULL,
  percentage_match bigint NOT NULL,
  CONSTRAINT pk_jobs_by_user PRIMARY KEY (job_by_user_id)
);

CREATE TABLE organisation_detail
(
  organisation_id bigint NOT NULL AUTO_INCREMENT,
  organisation_name char(200) NOT NULL,
  organisastion_industry_code integer NOT NULL,
  organisation_tier integer DEFAULT 0,
  CONSTRAINT pk_organisation_details PRIMARY KEY (organisation_id)
);

CREATE TABLE request_demo_detail
(
  id bigint NOT NULL AUTO_INCREMENT,
  name char(200) NOT NULL,
  company_name char(200) NOT NULL,
  email_id char(200) NOT NULL,
  mobile_number char(20) NOT NULL,
  CONSTRAINT pk_request_demo_detail PRIMARY KEY (id)
);

CREATE TABLE role_detail
(
  role_id bigint NOT NULL AUTO_INCREMENT,
  role_name char(200) NOT NULL,
  role_desc text,
  CONSTRAINT pk_role_details_role_id PRIMARY KEY (role_id)
);

CREATE TABLE skill_detail
(
  skill_id bigint NOT NULL AUTO_INCREMENT,
  skill_name char(50) NOT NULL,
  CONSTRAINT pk_skills_details PRIMARY KEY (skill_id)
);

CREATE TABLE user_award_detail
(
  award_id bigint NOT NULL AUTO_INCREMENT,
  title char(200) NOT NULL,
  organisation_name text,
  user_id bigint NOT NULL,
  date date,
  CONSTRAINT pk_award_details PRIMARY KEY (award_id)
);

CREATE TABLE user_by_job
(
  user_by_job_id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  job_id bigint NOT NULL,
  percentage_match bigint NOT NULL,
  CONSTRAINT pk_user_by_job_id PRIMARY KEY (user_by_job_id)
);

CREATE TABLE user_certification_detail
(
  certification_id bigint NOT NULL AUTO_INCREMENT,
  name text,
  organisation_name text,
  url text,
  start_date date,
  end_date date,
  user_id bigint NOT NULL,
  CONSTRAINT pk_user_certification_details PRIMARY KEY (certification_id)
);

CREATE TABLE user_detail
(
  user_id bigint NOT NULL AUTO_INCREMENT,
  first_name char(100),
  last_name char(100),
  profile_image_url text,
  profile_headline text,
  summary text,
  marital_status char(10),
  gender char(10),
  date_of_birth date,
  interests text,
  mobile_number char(10),
  other_contact_numbers text,
  recruiter_type char(20),
  account_id bigint NOT NULL,
  language text,
  address text,
  ctc integer,
  top_institute_tier integer DEFAULT 0,
  past_experience_months integer,
  job_function char(200),
  skills text,
  latest_company_name text,
  processed_state char(200) DEFAULT 'NOTPROCESSED',
  recruiter_processed_state char(200) DEFAULT 'NOTPROCESSED',
  industry_name char(200),
  skills_found_in_work text,
  user_resume_link text,
  hide_ctc tinyint NOT NULL DEFAULT 0,
  is_resume_parsed tinyint NOT NULL DEFAULT 0,
  has_profile_changed tinyint NOT NULL DEFAULT 0,
  CONSTRAINT pk_user_id PRIMARY KEY (user_id)
);

CREATE TABLE user_education_detail
(
  education_id bigint NOT NULL AUTO_INCREMENT,
  educational_org text,
  time_period_start date,
  time_period_end date,
  degree text,
  major_subject text,
  description text,
  user_id bigint NOT NULL,
  organisation_tier integer,
  degree_type char(200),
  CONSTRAINT pk_user_education_id PRIMARY KEY (education_id)
);

CREATE TABLE user_experince_detail
(
  experince_id bigint NOT NULL AUTO_INCREMENT,
  company_name text,
  role_name text,
  time_period_start date,
  time_period_end date,
  description text,
  user_id bigint NOT NULL,
  location_detail text,
  is_current_job tinyint DEFAULT 0,
  organisation_tier integer DEFAULT 0,
  total_exp_month integer DEFAULT 0,
  CONSTRAINT pk_user_experince_detail_id PRIMARY KEY (experince_id)
);

CREATE TABLE user_patent_detail
(
  patent_id bigint NOT NULL AUTO_INCREMENT,
  title char(200) NOT NULL,
  country_code text,
  patent_status text,
  application_number text,
  filing_date date,
  url text,
  description text,
  user_id bigint NOT NULL,
  CONSTRAINT pk_user_patent_id PRIMARY KEY (patent_id)
);

CREATE TABLE user_project_detail
(
  project_id bigint NOT NULL AUTO_INCREMENT,
  project_name text,
  project_date date,
  project_url text,
  project_description text,
  user_id bigint NOT NULL,
  CONSTRAINT pk_user_project_id PRIMARY KEY (project_id)
);

CREATE TABLE user_publication_detail
(
  publication_id bigint NOT NULL AUTO_INCREMENT,
  title text NOT NULL,
  publication_organisation text,
  date date,
  description text,
  user_id bigint NOT NULL,
  CONSTRAINT pk_user_publication_id PRIMARY KEY (publication_id)
);

CREATE TABLE user_test_detail
(
  test_id bigint NOT NULL AUTO_INCREMENT,
  test_name char(100) NOT NULL,
  test_score char(10) NOT NULL,
  test_date date,
  test_description text,
  user_id bigint NOT NULL,
  CONSTRAINT pk_user_test_id PRIMARY KEY (test_id)
);

CREATE TABLE user_volunteer_detail
(
  volunteer_id bigint NOT NULL AUTO_INCREMENT,
  role_name text,
  organisation_name text,
  cause_name text,
  start_date date,
  end_date date,
  user_id bigint NOT NULL,
  description text,
  CONSTRAINT pk_user_volunteer_id PRIMARY KEY (volunteer_id)
);

ALTER TABLE job_detail ADD CONSTRAINT fk_job_detail_recruiter_id
  FOREIGN KEY (recruiter_id) REFERENCES user_detail (user_id);

ALTER TABLE job_detail ADD CONSTRAINT fk_job_details_recruiter_id
  FOREIGN KEY (recruiter_id) REFERENCES user_detail (user_id);

ALTER TABLE job_relationship_detail ADD CONSTRAINT fk_job_relationship_detail_recruiter_id
  FOREIGN KEY (recruiter_id) REFERENCES user_detail (user_id);

ALTER TABLE job_relationship_detail ADD CONSTRAINT fk_job_relationship_details_expert_id
  FOREIGN KEY (expert_id) REFERENCES user_detail (user_id);

ALTER TABLE job_relationship_detail ADD CONSTRAINT fk_job_relationship_details_job_id
  FOREIGN KEY (job_id) REFERENCES job_detail (job_id);

ALTER TABLE job_relationship_detail ADD CONSTRAINT fk_job_relationship_details_jobseeker_id
  FOREIGN KEY (jobseeker_id) REFERENCES user_detail (user_id);

ALTER TABLE jobs_by_user ADD CONSTRAINT fk_jobs_by_user_job_id
  FOREIGN KEY (job_id) REFERENCES job_detail (job_id);

ALTER TABLE jobs_by_user ADD CONSTRAINT fk_jobs_by_user_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE organisation_detail ADD CONSTRAINT fk_organisation_details_industry_code
  FOREIGN KEY (organisastion_industry_code) REFERENCES industry_code_detail (industry_code);

ALTER TABLE user_award_detail ADD CONSTRAINT fk_award_details_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE user_by_job ADD CONSTRAINT fk_job_id
  FOREIGN KEY (job_id) REFERENCES job_detail (job_id);

ALTER TABLE user_by_job ADD CONSTRAINT fk_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE user_certification_detail ADD CONSTRAINT fk_user_certification_details_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE user_detail ADD CONSTRAINT fk_user_details_account_id
  FOREIGN KEY (account_id) REFERENCES basic_account_detail (account_id);

ALTER TABLE user_education_detail ADD CONSTRAINT fk_user_education_details_jobseeker_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE user_experince_detail ADD CONSTRAINT fk_user_experiece_details_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE user_patent_detail ADD CONSTRAINT fk_user_patent_details_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE user_project_detail ADD CONSTRAINT fk_user_project_details_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE user_publication_detail ADD CONSTRAINT fk_user_publication_details_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE user_test_detail ADD CONSTRAINT fk_user_test_details_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

ALTER TABLE user_volunteer_detail ADD CONSTRAINT fk_user_volunteer_details_user_id
  FOREIGN KEY (user_id) REFERENCES user_detail (user_id);

CREATE UNIQUE INDEX ix_basic_account_details_email_id
  ON basic_account_detail (email_id);

CREATE UNIQUE INDEX ix_basic_account_details_work_email_id
  ON basic_account_detail (work_email_id);

CREATE UNIQUE INDEX ix_industry_code_details_industry_description
  ON industry_code_detail (industry_description);

CREATE UNIQUE INDEX ix_job_relationship_detail_unique_id
  ON job_relationship_detail (job_id, jobseeker_id);

CREATE UNIQUE INDEX ix_jobs_by_user_job_user_id
  ON jobs_by_user (job_id, user_id);

CREATE UNIQUE INDEX ix_organisation_details_organisation_name
  ON organisation_detail (organisation_name);

CREATE UNIQUE INDEX ix_request_demo_detail_
  ON request_demo_detail (email_id);

CREATE UNIQUE INDEX ix_skill_details_skill_name
  ON skill_detail (skill_name);

CREATE UNIQUE INDEX ix_user_by_job_
  ON user_by_job (user_id, job_id);















