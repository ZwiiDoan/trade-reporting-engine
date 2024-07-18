CREATE TABLE trade_event
(
    id               TEXT PRIMARY KEY,
    buyer_party      TEXT           NOT NULL,
    seller_party     TEXT           NOT NULL,
    premium_amount   NUMERIC(19, 4) NOT NULL,
    premium_currency TEXT           NOT NULL
);