/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-wms

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 24/03/2020 22:44:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_stock`;
CREATE TABLE `t_stock`  (
  `stock_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存编号',
  `item_type` tinyint(4) NOT NULL COMMENT '货物类型（1:物料；2:产品）',
  `item_id` bigint(20) NOT NULL COMMENT '产品编号',
  `item_count` double(11, 0) NOT NULL DEFAULT 0 COMMENT '库存数量',
  `s_max` int(11) NOT NULL DEFAULT 1000000 COMMENT '库存最大值（默认1000000件（件的具体单位由物料的属性确定），具体由仓库管理员确定）',
  `s_min` int(11) NOT NULL DEFAULT 0 COMMENT '库存最小值（默认0，具体为货物的安全库存量）',
  `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存放地点',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`stock_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 225 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_stock
-- ----------------------------
INSERT INTO `t_stock` VALUES (202, 1, 1, 3002006, 1000000, 2000, NULL, '2020-03-21 12:15:01', '2020-03-23 08:50:54', 0);
INSERT INTO `t_stock` VALUES (203, 1, 2, 2009, 1000000, 2000, NULL, '2020-03-21 12:15:01', '2020-03-23 09:00:34', 0);
INSERT INTO `t_stock` VALUES (204, 1, 3, 2007, 1000000, 2000, NULL, '2020-03-21 12:15:01', '2020-03-23 09:00:34', 0);
INSERT INTO `t_stock` VALUES (205, 1, 4, 2010, 1000000, 2000, NULL, '2020-03-21 12:15:01', '2020-03-24 13:00:55', 0);
INSERT INTO `t_stock` VALUES (206, 1, 5, 2002, 1000000, 2000, NULL, '2020-03-21 12:15:01', '2020-03-24 12:40:26', 0);
INSERT INTO `t_stock` VALUES (207, 1, 6, 2003, 1000000, 2000, NULL, '2020-03-21 12:15:01', '2020-03-23 09:04:00', 0);
INSERT INTO `t_stock` VALUES (208, 2, 1, 4656, 1000000, 2300, NULL, '2020-03-21 12:15:01', '2020-03-24 15:27:07', 0);
INSERT INTO `t_stock` VALUES (209, 2, 2, 1988, 1000000, 2300, NULL, '2020-03-21 12:15:01', '2020-03-24 15:27:07', 0);
INSERT INTO `t_stock` VALUES (210, 2, 5, 1985, 1000000, 2300, NULL, '2020-03-21 12:15:01', '2020-03-24 12:51:04', 0);
INSERT INTO `t_stock` VALUES (211, 2, 6, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (212, 2, 7, 1997, 1000000, 2300, NULL, '2020-03-21 12:15:01', '2020-03-24 12:42:43', 0);
INSERT INTO `t_stock` VALUES (214, 2, 9, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (215, 2, 10, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (216, 2, 11, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (217, 2, 12, 2000, 1000000, 2300, '1号仓', '2020-03-21 12:15:01', '2020-03-22 07:55:42', 0);
INSERT INTO `t_stock` VALUES (218, 2, 13, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (219, 2, 14, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (220, 2, 15, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (221, 2, 16, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (222, 2, 17, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (223, 2, 18, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);
INSERT INTO `t_stock` VALUES (224, 2, 19, 2000, 1000000, 2300, NULL, '2020-03-21 12:15:01', NULL, 0);

-- ----------------------------
-- Table structure for t_stock_item
-- ----------------------------
DROP TABLE IF EXISTS `t_stock_item`;
CREATE TABLE `t_stock_item`  (
  `stock_item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出入库单编号',
  `stock_type` tinyint(4) NULL DEFAULT NULL COMMENT '出入库类型(1:采购入库；2:销售出库；3:采购退货出库；4:销售退货入库)',
  `item_type` tinyint(4) NULL DEFAULT NULL COMMENT '货物类型（1:物料；2:产品）',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单编号',
  `admin_id` bigint(20) NULL DEFAULT NULL COMMENT '库存管理员编号',
  `apply_user_id` bigint(20) NULL DEFAULT NULL COMMENT '交接申请用户id',
  `check_status` tinyint(4) NULL DEFAULT 1 COMMENT '库存管理员审核状态（1:未审核；2:审核不通过；3:审核通过）',
  `finish_status` tinyint(4) NULL DEFAULT NULL COMMENT '完成出入库状态（1:未完成；2:已完成）',
  `make_date` datetime(0) NULL DEFAULT NULL COMMENT '出入库时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`stock_item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 125 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_stock_item
-- ----------------------------
INSERT INTO `t_stock_item` VALUES (121, 2, 2, 70, 17, 17, 3, 2, '2020-03-24 15:27:07', NULL, '2020-03-24 15:26:00', '2020-03-24 15:27:07', 0);
INSERT INTO `t_stock_item` VALUES (122, 4, 2, 70, NULL, 17, 1, 1, NULL, NULL, '2020-03-24 15:28:51', NULL, 0);
INSERT INTO `t_stock_item` VALUES (123, 3, 1, 79, NULL, 17, 1, 1, NULL, NULL, '2020-03-24 15:39:57', NULL, 0);
INSERT INTO `t_stock_item` VALUES (124, 3, 1, 80, NULL, 17, 1, 1, NULL, NULL, '2020-03-24 15:59:24', NULL, 0);

-- ----------------------------
-- Table structure for t_stock_item_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_stock_item_detail`;
CREATE TABLE `t_stock_item_detail`  (
  `stock_item_detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出入库明细单编号',
  `stock_item_id` bigint(20) NULL DEFAULT NULL COMMENT '出入库单编号',
  `item_id` bigint(20) NULL DEFAULT NULL COMMENT '货物编号',
  `item_num` double NULL DEFAULT NULL COMMENT '数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`stock_item_detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 211 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_stock_item_detail
-- ----------------------------
INSERT INTO `t_stock_item_detail` VALUES (204, 121, 1, 2, '2020-03-24 15:26:00', NULL, 0);
INSERT INTO `t_stock_item_detail` VALUES (205, 121, 2, 3, '2020-03-24 15:26:00', NULL, 0);
INSERT INTO `t_stock_item_detail` VALUES (206, 122, 1, 2, '2020-03-24 15:28:51', NULL, 0);
INSERT INTO `t_stock_item_detail` VALUES (207, 122, 2, 3, '2020-03-24 15:28:51', NULL, 0);
INSERT INTO `t_stock_item_detail` VALUES (208, 123, 5, 3, '2020-03-24 15:39:57', NULL, 0);
INSERT INTO `t_stock_item_detail` VALUES (209, 123, 4, 3, '2020-03-24 15:39:57', NULL, 0);
INSERT INTO `t_stock_item_detail` VALUES (210, 124, 4, 3, '2020-03-24 15:59:24', NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;
