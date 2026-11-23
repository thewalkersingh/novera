CREATE TABLE students
(
   id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   first_name     VARCHAR(100) NOT NULL,
   last_name      VARCHAR(100) NOT NULL,
   date_of_birth  DATE         NOT NULL,
   gender         VARCHAR(20)  NOT NULL,
   email          VARCHAR(100) NOT NULL UNIQUE,
   phone          VARCHAR(20),
   grade          VARCHAR(20)  NOT NULL,
   admission_date DATE         NOT NULL,
   status         VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
   created_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
   updated_at     TIMESTAMPTZ  NOT NULL DEFAULT now(),
   CONSTRAINT chk_students_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE INDEX idx_students_name ON students (last_name, first_name);
CREATE INDEX idx_students_grade ON students (grade);
CREATE INDEX idx_students_status ON students (status);