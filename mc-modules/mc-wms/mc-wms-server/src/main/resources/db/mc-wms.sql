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

 Date: 17/03/2020 23:00:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_stock`;
CREATE TABLE `t_stock`  (
  `stock_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存编号',
  `item_type` tinyint(4) NULL DEFAULT NULL COMMENT '货物类型（1:物料；2:产品）',
  `item_id` bigint(20) NULL DEFAULT NULL COMMENT '产品编号',
  `item_count` double(11, 0) NULL DEFAULT NULL COMMENT '库存数量',
  `s_max` int(11) NULL DEFAULT NULL COMMENT '库存最大值',
  `s_min` int(11) NULL DEFAULT NULL COMMENT '库存最小值',
  `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存放地点',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`stock_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_stock_item
-- ----------------------------
DROP TABLE IF EXISTS `t_stock_item`;
CREATE TABLE `t_stock_item`  (
  `stock_item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '出入库单编号',
  `stock_type` tinyint(4) NULL DEFAULT NULL COMMENT '出入库类型(1:入库；2:出库)',
  `item_type` tinyint(4) NULL DEFAULT NULL COMMENT '货物类型（1:物料；2:产品）',
  `admin_id` bigint(20) NULL DEFAULT NULL COMMENT '管理员编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单编号',
  `make_date` datetime(0) NULL DEFAULT NULL COMMENT '出入库时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`stock_item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
