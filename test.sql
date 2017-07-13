/**
  日常资金统计表
 */
CREATE TABLE everyday_money_count(
  id BIGINT(19) AUTO_INCREMENT COMMENT 'id',
  isDay TINYINT(1) DEFAULT 0 COMMENT '是否是每日统计； 0：每月统计；1：每日统计',
  start_date DATETIME DEFAULT current_timestamp COMMENT '开始时间',
  end_date DATETIME DEFAULT  current_timestamp COMMENT '结束时间',
  recharge_money DECIMAL(10,2) DEFAULT 0.00 COMMENT '充值总额',
  withdraw DECIMAL(10,2) DEFAULT  0.00 COMMENT '提现总额',
  firm_income DECIMAL(10,2) DEFAULT 0.00 COMMENT '公司收入',
  firm_expend DECIMAL(10,2) DEFAULT 0.00 COMMENT '公司支出',
  withdraw_charge DECIMAL(10,2) DEFAULT 0.00 COMMENT '提现手续费',
  transaction_money DECIMAL(10,2) DEFAULT 0.00 COMMENT '成交总额',
  refund_money DECIMAL(10,2) DEFAULT 0.00 COMMENT '日还总额',
  refund_charge DECIMAL(10,2) DEFAULT 0.00 COMMENT '日还利息',

  PRIMARY KEY (id),
  KEY idx_isDay(is_day),
  KEY idx_start_date(start_date),
  KEY idx_end_date(end_date)

)ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '日常资金统计表';
