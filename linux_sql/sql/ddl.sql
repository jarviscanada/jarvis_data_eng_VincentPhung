-- Check if host_info table exists
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'host_info') THEN
    -- Create the host_info table
    CREATE TABLE PUBLIC.host_info
    (
      id               SERIAL NOT NULL,
      hostname         VARCHAR NOT NULL,
      cpu_number       INT2 NOT NULL,
      cpu_architecture VARCHAR NOT NULL,
      cpu_model        VARCHAR NOT NULL,
      cpu_mhz          FLOAT8 NOT NULL,
      l2_cache         INT4 NOT NULL,
      "timestamp"      TIMESTAMP NULL,
      total_mem        INT4 NULL,
      CONSTRAINT host_info_pk PRIMARY KEY (id),
      CONSTRAINT host_info_un UNIQUE (hostname)
    );

    -- Insert three sample data rows
    INSERT INTO host_info (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, "timestamp", total_mem) VALUES(1, 'jrvs-remote-desktop-centos7-6.us-central1-a.c.spry-framework-236416.internal', 1, 'x86_64', 'Intel(R) Xeon(R) CPU @ 2.30GHz', 2300, 256, '2019-05-29 17:49:53.000', 601324);

    INSERT INTO host_info (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, "timestamp", total_mem) VALUES(2, 'noe1', 1, 'x86_64', 'Intel(R) Xeon(R) CPU @ 2.30GHz', 2300, 256, '2019-05-29 17:49:53.000', 601324);

    INSERT INTO host_info (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, "timestamp", total_mem) VALUES(3, 'noe2', 1, 'x86_64', 'Intel(R) Xeon(R) CPU @ 2.30GHz', 2300, 256, '2019-05-29 17:49:53.000', 601324);
  END IF;
END $$;

-- Check if host_usage table exists
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'host_usage') THEN
    -- Create the host_usage table
    CREATE TABLE PUBLIC.host_usage
    (
      "timestamp"    TIMESTAMP NOT NULL,
      host_id        SERIAL NOT NULL,
      memory_free    INT4 NOT NULL,
      cpu_idle       INT2 NOT NULL,
      cpu_kernel     INT2 NOT NULL,
      disk_io        INT4 NOT NULL,
      disk_available INT4 NOT NULL,
      CONSTRAINT host_usage_host_info_fk FOREIGN KEY (host_id) REFERENCES
      host_info(id)
    );

    -- Insert sample data
    INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
    VALUES('2019-05-29 15:00:00.000', 1, 300000, 90, 4, 2, 3);
    INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
    VALUES('2019-05-29 15:01:00.000', 1, 200000, 90, 4, 2, 3);
  END IF;
END $$;

-- Verify data in host_info table
SELECT * FROM host_info;

-- Verify data in host_usage table
SELECT * FROM host_usage;

-- Test