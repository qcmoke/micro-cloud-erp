/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-fms

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 03/04/2020 13:47:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_account
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account`  (
  `account_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '账户id（支付类型要和账户id一致）',
  `account_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易平台名称(支付宝，微信，银行)',
  `bank_num` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易平台账号或者银行卡号',
  `amount` double(255, 0) NULL DEFAULT NULL COMMENT '余额',
  `is_default` tinyint(4) NULL DEFAULT 2 COMMENT '是否默认(1:是；2:否)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`account_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_account
-- ----------------------------
INSERT INTO `t_account` VALUES (1, '支付宝账户', '支付宝', '666666666111132456000000', 19990697, 1, '2020-03-24 08:58:19', '2020-04-03 04:26:46', 0);
INSERT INTO `t_account` VALUES (2, '微信账户', '微信', '555555555111132456000000', 19977684, 2, '2020-03-24 08:58:21', '2020-04-03 04:38:39', 0);
INSERT INTO `t_account` VALUES (3, '银联账户', '银联', '777777777111132456000000', 19997701, 2, '2020-03-24 08:58:23', '2020-04-03 04:49:55', 0);

-- ----------------------------
-- Table structure for t_bill
-- ----------------------------
DROP TABLE IF EXISTS `t_bill`;
CREATE TABLE `t_bill`  (
  `bill_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账单编号',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '账目类型(1:采购付款；2:采购退货收款；3:销售收款；4:销售退款)',
  `deal_num` bigint(20) NULL DEFAULT NULL COMMENT '交易单据编号(采购单，采购退货单，销售订单，销售退货单等编号)',
  `account_id` bigint(20) NULL DEFAULT NULL COMMENT '账户id',
  `total_amount` double NULL DEFAULT NULL COMMENT '合计金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`bill_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_bill
-- ----------------------------
INSERT INTO `t_bill` VALUES (18, 2, 34, 1, 8757, '2020-04-03 04:26:45', NULL, 0);
INSERT INTO `t_bill` VALUES (19, 2, 35, 2, 6158, '2020-04-03 04:38:39', NULL, 0);
INSERT INTO `t_bill` VALUES (21, 1, 13, 3, 2299, '2020-04-03 04:49:55', NULL, 0);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
