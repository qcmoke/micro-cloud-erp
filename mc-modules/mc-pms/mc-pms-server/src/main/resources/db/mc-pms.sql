/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-pms

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 11/03/2020 00:17:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_materiel
-- ----------------------------
DROP TABLE IF EXISTS `t_materiel`;
CREATE TABLE `t_materiel`  (
  `materiel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物料编号',
  `materiel_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`materiel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_materiel
-- ----------------------------
INSERT INTO `t_materiel` VALUES (1, '骁龙850', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_materiel` VALUES (2, '麒麟960', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_materiel` VALUES (3, '联发科x30', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_materiel` VALUES (4, '英特尔酷睿i5-4210m', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_materiel` VALUES (5, '夏普2k-3100屏', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_materiel` VALUES (6, '索尼2000万光学后置摄像头', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_materiel_refund
-- ----------------------------
DROP TABLE IF EXISTS `t_materiel_refund`;
CREATE TABLE `t_materiel_refund`  (
  `refund_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购退货单主表编号',
  `purchase_order_master_id` bigint(11) NULL DEFAULT NULL COMMENT '采购订单主表编号',
  `refund_channel` tinyint(4) NULL DEFAULT NULL COMMENT '退款渠道[1-支付宝，2-微信，3-银联，4-汇款]',
  `total_amount` double NULL DEFAULT NULL COMMENT '总金额',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '退货状态（1:退货中；2:退货成功；3:退货失败）',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货原因',
  `return_date` datetime(0) NULL DEFAULT NULL COMMENT '退货日期',
  `finished_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`refund_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_purchase_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_order_detail`;
CREATE TABLE `t_purchase_order_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购订单明细表编号',
  `materiel_id` bigint(20) NULL DEFAULT NULL COMMENT '物料编号',
  `master_id` bigint(20) NULL DEFAULT NULL COMMENT '采购主表编号',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `count` bigint(20) NULL DEFAULT NULL COMMENT '数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_purchase_order_master
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_order_master`;
CREATE TABLE `t_purchase_order_master`  (
  `master_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购订单主表id',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商id',
  `purchase_date` datetime(0) NULL DEFAULT NULL COMMENT '采购时间',
  `pay_type` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】',
  `total_amount` double NULL DEFAULT NULL COMMENT '总金额(元)',
  `status` int(10) NULL DEFAULT NULL COMMENT '入库状态(1:未入库；2:已入库)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`master_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier`  (
  `supplier_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '供应商编号',
  `supplier_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商地址',
  `bank` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户银行',
  `bank_account` bigint(20) NULL DEFAULT NULL COMMENT '银行账号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `link_man` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `link_tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人号码',
  `tel_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `postcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`supplier_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
