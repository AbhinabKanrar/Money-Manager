--DROP SCHEMA mm CASCADE;

CREATE SCHEMA IF NOT EXISTS mm;

CREATE TABLE IF NOT EXISTS mm.user_auth_detail(
	user_id BIGINT PRIMARY KEY,
	username VARCHAR(20) NOT NULL,
	password VARCHAR(60) NOT NULL,
	role VARCHAR(20) NOT NULL,
	mail VARCHAR(255),
	phone_number VARCHAR(13),
	user_status VARCHAR(20) NOT NULL,
	UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS mm.customer_detail(
	customer_id BIGINT PRIMARY KEY,
	region VARCHAR(50) NOT NULL,
	building VARCHAR(50),
	address VARCHAR(255) NOT NULL,
	client VARCHAR(50),
	name VARCHAR(50),
	floor VARCHAR(50),
	fee numeric,
	mahal VARCHAR(20),
	telephone VARCHAR(20),
	left_travel VARCHAR(50),
	note VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS mm.customer_collection_detail(
	collection_id BIGINT PRIMARY KEY,
	customer_id BIGINT UNIQUE,
	collector_id BIGINT,
	jan_fee numeric,
	feb_fee numeric,
	mar_fee numeric,
	apr_fee numeric,
	may_fee numeric,
	jun_fee numeric,
	jul_fee numeric,
	aug_fee numeric,
	sep_fee numeric,
	oct_fee numeric,
	nov_fee numeric,
	dec_fee numeric,
	CONSTRAINT customer_payment_details_customer_id_fk_ FOREIGN KEY (customer_id) REFERENCES mm.customer_detail (customer_id),
	CONSTRAINT customer_payment_details_user_id_fk_ FOREIGN KEY (collector_id) REFERENCES mm.user_auth_detail (user_id)
);

CREATE TABLE IF NOT EXISTS mm.customer_collection_detail_audit(
	audit_id BIGINT PRIMARY KEY,
	customer_id BIGINT,
	collector_id BIGINT,
	location varchar(255),
	reason varchar(255),
	collection_ts timestamp without time zone,
	CONSTRAINT customer_payment_details_customer_id_fk_ FOREIGN KEY (customer_id) REFERENCES mm.customer_detail (customer_id),
	CONSTRAINT customer_payment_details_user_id_fk_ FOREIGN KEY (collector_id) REFERENCES mm.user_auth_detail (user_id)
);

CREATE TABLE IF NOT EXISTS mm.collector_collection(
	id BIGINT PRIMARY KEY,
	collector_id BIGINT NOT NULL,
	collector_name VARCHAR(20) NOT NULL,
	amount numeric,
	CONSTRAINT collector_collection_fk_ FOREIGN KEY (collector_id) REFERENCES mm.user_auth_detail (user_id)
);