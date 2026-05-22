CREATE TABLE outbox (
  id UUID NOT NULL,
  aggregate_type VARCHAR(255) NOT NULL,
  aggregate_id VARCHAR(255) NOT NULL,
  event_type VARCHAR(255) NOT NULL,
  payload TEXT NOT NULL,
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  trace_id VARCHAR(255),
  CONSTRAINT pk_outbox PRIMARY KEY (id)
);

CREATE INDEX idx_outbox_status ON outbox(status);