CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE
    );

CREATE TABLE IF NOT EXISTS jobs (
                                    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    requested_by_id UUID,
    job_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    started_at TIMESTAMP WITHOUT TIME ZONE,
    finished_at TIMESTAMP WITHOUT TIME ZONE,
    last_updated TIMESTAMP WITHOUT TIME ZONE,
    git_repository_owner VARCHAR(255),
    git_repository_name VARCHAR(255),
    cursor TEXT,
    worker_id VARCHAR(255),

    FOREIGN KEY (requested_by_id) REFERENCES users(id) ON DELETE SET NULL
    );

CREATE TABLE IF NOT EXISTS mining_results (
                                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_id UUID UNIQUE,
    repository_owner VARCHAR(255),
    repository_name VARCHAR(255),
    started_at TIMESTAMP WITHOUT TIME ZONE,
    last_updated_at TIMESTAMP WITHOUT TIME ZONE,
    total_commits INTEGER,
    status VARCHAR(50) NOT NULL,
    last_cursor TEXT,

    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS git_authors (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    avatar_url TEXT,
    date TIMESTAMP WITHOUT TIME ZONE,
    email VARCHAR(255),
    name VARCHAR(255),
    user_id UUID,
    login VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS mining_commits (
                                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mining_result_id UUID,
    oid VARCHAR(255),
    message TEXT,
    message_body TEXT,
    additions INTEGER,
    deletions INTEGER,

    author_id UUID,
    committer_id UUID,

    authored_by_committer BOOLEAN,
    commit_url VARCHAR(255),
    committed_date TIMESTAMP WITHOUT TIME ZONE,

    FOREIGN KEY (mining_result_id) REFERENCES mining_results(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES git_authors(id) ON DELETE SET NULL,
    FOREIGN KEY (committer_id) REFERENCES git_authors(id) ON DELETE SET NULL
    );